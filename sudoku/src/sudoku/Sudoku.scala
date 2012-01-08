package sudoku

import scala.annotation.tailrec
import scala.collection.mutable.Publisher

class SudokuSolver(originalPuzzle: SudokuPuzzle) extends SudokuType {

  type Pub <: SudokuSolver

  type OptPuzzle = Option[SudokuPuzzle]

  import alghoritm._

  val byCellSolver = new FindByPendentNumbersCells
  val byRowSolver = new FindByRow
  val byColSolver = new FindByCol
  val bySectorSolver = new FindBySector

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

}

