package sudoku

import scala.annotation.tailrec
import scala.collection.mutable.Publisher
import scala.collection.mutable.Subscriber

////////////////////
// SOLVER EVENTS
////////////////////

/**
 * Represents a Event that ocurred while solving a Puzzle.
 */
sealed trait SudokuSolverEvent extends SudokuEvent

/**
 * Indicates a event ocurred in
 */
case class CicleEvent(puzzle: SudokuPuzzle, cellsSolvedInCicle: Boolean) extends SudokuSolverEvent

/**
 * Indicates that a Cell group is being evaluated or not more evaluated.
 *
 * @param evaluatedCells Evaluated Cells.
 * @param evaluated If these cells is being evaluated or not more evaluated.
 */
case class CellGroupEvaluated(evaluatedCells: List[Cell], evaluated: Boolean) extends SudokuEvent

/**
 * Indicates that it is trying a guess value for a Cell.
 *
 * @param guessCell Cell with guess value filled.
 */
case class GuessValueTryingEvent(guessCell: Cell) extends SudokuSolverEvent

/**
 * Indicates that it is a guess value for a Cell was failed.
 *
 * @param guessCell Cell with guess value filled.
 */
case class GuessValueFailedEvent(guessCell: Cell) extends SudokuSolverEvent

/**
 * Indicates a solver alghoritm change.
 *
 * @param alghoritimDescription self explicative.
 */
case class ChangeAlghoritimEvent(alghoritimDescription: String) extends SudokuSolverEvent

class SudokuSolver(originalPuzzle: SudokuPuzzle) extends SudokuType {

  type Pub <: SudokuSolver

  type OptPuzzle = Option[SudokuPuzzle]

  import alghoritm._

  val byCellSolver = new FindByPendentNumbersCells
  val byRowSolver = new FindByRow
  val byColSolver = new FindByCol
  val bySectorSolver = new FindBySector

  private val alghoritmSubscriber = new Subscriber[SudokuEvent, SudokuType] {
    def notify(pub: SudokuType, evt: SudokuEvent) {
      evt match {
        case e: CellGroupEvaluated => SudokuSolver.this.publish(e);
        case _                     =>
      }
    }
  }

  private def init() {
    byCellSolver.subscribe(this.alghoritmSubscriber.asInstanceOf[byCellSolver.Sub])
    byRowSolver.subscribe(this.alghoritmSubscriber.asInstanceOf[byRowSolver.Sub])
    byColSolver.subscribe(this.alghoritmSubscriber.asInstanceOf[byColSolver.Sub])
    bySectorSolver.subscribe(this.alghoritmSubscriber.asInstanceOf[bySectorSolver.Sub])
  }

  private def changeRunningState(newState: RunningState.Value, puzzle: SudokuPuzzle) {
    super.runningState = newState
    puzzle.runningState = newState
    puzzle.matrix.foreach(_.runningState = newState)
  }

  @tailrec
  private def tryGuessValue(puzzle: SudokuPuzzle, pendentCell: Cell, guessValues: List[Int]): OptPuzzle = guessValues match {
    case Nil => None
    case x :: xs => {
      puzzle.addGuessCell(pendentCell)
      pendentCell.addValue(x, puzzle.lastGuess)
      super.publish(GuessValueTryingEvent(pendentCell))

      val puzzleResult = this.solve(puzzle)

      if (puzzleResult.isDefined) puzzleResult
      else {
        super.publish(GuessValueFailedEvent(pendentCell))
        puzzle.removeLastGuess
        tryGuessValue(puzzle, pendentCell, xs)
      }
    }
  }

  @tailrec
  private def tryGuessCells(puzzle: SudokuPuzzle, pendentCells: List[PendentCell]): OptPuzzle = pendentCells match {
    case Nil => {
      if (puzzle.guessesCells.isEmpty) throw new SudokuException("It is not possible solve Sudoku", puzzle)
      else None
    }
    case PendentCell(cell, pendentValues) :: others => tryGuessValue(puzzle, cell, pendentValues) match {
      case Some(p) => Some(p)
      case None    => this.tryGuessCells(puzzle, others)
    }
  }

  private def solveByAlghoritm(puzzle: SudokuPuzzle, alghortim: SudokuAlghoritim): Boolean = {
    if (puzzle.isSolved) {
      true
    } else {
      this.publish(ChangeAlghoritimEvent(alghortim.description))
      alghortim.solvePuzzle(puzzle)
    }
  }

  @tailrec
  private def solve(puzzle: SudokuPuzzle): OptPuzzle = {
    puzzle.getNotSolved match {
      case Nil => Some(puzzle)
      case pendents => {
        val solvedByCell = solveByAlghoritm(puzzle, this.byCellSolver)
        val solvedByRow = solveByAlghoritm(puzzle, this.byRowSolver)
        val solvedByCol = solveByAlghoritm(puzzle, this.byColSolver)
        val solvedBySector = solveByAlghoritm(puzzle, this.bySectorSolver)

        val solved = solvedByCell || solvedByRow || solvedByCol || solvedBySector
        super.publish(CicleEvent(puzzle, solved))
        if (solved) solve(puzzle)
        else {
          val pendentCells = pendents
            .map(c => new PendentCell(c, puzzle))
            .sortBy(p => (p.candidates.size, p.cell.row, p.cell.col))
          this.tryGuessCells(puzzle, pendentCells)
        }
      }
    }
  }

  def apply(): OptPuzzle = {
    try {
      this.changeRunningState(RunningState.Runnning, this.originalPuzzle)

      val result = this.solve(this.originalPuzzle)

      result match {
        case Some(puzzle) => this.changeRunningState(RunningState.Solved, puzzle)
        case None         => this.changeRunningState(RunningState.NotSolved, this.originalPuzzle)
      }

      result
    } catch {
      case t =>
        this.changeRunningState(RunningState.Problem, this.originalPuzzle)
        throw t
    }
  }

  init()
}

