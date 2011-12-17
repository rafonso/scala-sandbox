package sudoku

import scala.annotation.tailrec

case class PendentCell(val cell: Cell, val candidates: List[Int]) extends Ordered[PendentCell] {

  def this(cell: Cell, puzzle: SudokuPuzzle) = this(cell, puzzle.getPendentsNumbersFromCell(cell))

  def compare(that: PendentCell): Int = {
    if (this.candidates.size != that.candidates.size) {
      this.candidates.size - that.candidates.size
    } else if (this.cell.row != that.cell.row) {
      this.cell.row - that.cell.row
    } else {
      this.cell.col - that.cell.col
    }
  }

}
