package sudoku

import scala.annotation.tailrec
import scala.collection.mutable.Publisher

trait SudokuSolverEvent extends SudokuEvent
case class CicleEvent(puzzle: SudokuPuzzle, cellsSolvedInCicle: Boolean) extends SudokuSolverEvent
case class GuessValueTryingEvent(guessCell: Cell) extends SudokuSolverEvent
case class GuessValueFailedEvent(guessCell: Cell) extends SudokuSolverEvent

class SudokuSolver(puzzle: SudokuPuzzle) extends SudokuType with SudokuPublisher[SudokuSolverEvent] {

  type Pub <: SudokuSolver

  type OptPuzzle = Option[SudokuPuzzle]

  import alghoritm._

  val byCellSolver = new FindByPendentNumbersCells
  val byRowSolver = new FindByRow
  val byColSolver = new FindByCol
  val bySectorSolver = new FindBySector

  @tailrec
  private def tryGuessValue(pendentCell: Cell, guessValues: List[Int]): OptPuzzle = guessValues match {
    case Nil => None
    case x :: xs => {
      val guessCell = pendentCell.copy(v = x, cellType = CellType.Guess)
      val guessPuzzle = puzzle.copyWithGuess(guessCell)

      assert(!this.puzzle.eq(guessPuzzle))
      super.publish(GuessValueTryingEvent(guessCell))
      new SudokuSolver(guessPuzzle).apply() match {
        case Some(p) => Some(p)
        case None => {
          super.publish(GuessValueFailedEvent(guessCell))
          tryGuessValue(pendentCell, xs)
        }
      }
    }
  }

  @tailrec
  private def tryGuessCells(pendentCells: List[PendentCell]): OptPuzzle = pendentCells match {
    case Nil => this.puzzle.guessCells match {
      case Nil => throw new SudokuException("It is not possible solve Sudoku", puzzle)
      case _   => None
    }
    case PendentCell(cell, pendentValues) :: others => tryGuessValue(cell, pendentValues) match {
      case Some(p) => Some(p)
      case None    => this.tryGuessCells(others)
    }
  }

  @tailrec
  final def apply(): OptPuzzle = {
    this.puzzle.getNotSolved match {
      case Nil => Some(this.puzzle)
      case pendents => {
        val solvedByCell = this.byCellSolver.solvePuzzle(this.puzzle)
        val solvedByRow = this.byRowSolver.solvePuzzle(this.puzzle)
        val solvedByCol = this.byColSolver.solvePuzzle(this.puzzle)
        val solvedBySector = this.bySectorSolver.solvePuzzle(this.puzzle)

        val solved = solvedByCell || solvedByRow || solvedByCol || solvedBySector
        super.publish(CicleEvent(this.puzzle, solved))
        if (solved) apply()
        else this.tryGuessCells(pendents
          .map(c => new PendentCell(c, this.puzzle))
          .sortBy(p => (p.candidates.size, p.cell.row, p.cell.col)))
      }
    }
  }
}

