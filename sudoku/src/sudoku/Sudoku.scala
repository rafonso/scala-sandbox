package sudoku

import scala.annotation.tailrec

class SudokuSolver(puzzle: SudokuPuzzle) extends SudokuLog {

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
      val copyCell = pendentCell.copy(v = x)
      val puzzleCopy = puzzle.copyWithGuess(copyCell)

      assert(!this.puzzle.eq(puzzleCopy))
      log('\n' + "==== tryGuessValue: %s ====".format(copyCell))
      log("puzzle.hashCode = " + puzzle.hashCode())
      log("%s.hashCode = %d".format(copyCell, copyCell.hashCode()))

      new SudokuSolver(puzzleCopy).apply() match {
        case Some(p) => Some(p)
        case None    => tryGuessValue(pendentCell, xs)
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
        if (solved) apply()
        else this.tryGuessCells(pendents
          .map(c => new PendentCell(c, this.puzzle))
          .sortBy(p => (p.candidates.size, p.cell.row, p.cell.col)))
      }
    }
  }
}

