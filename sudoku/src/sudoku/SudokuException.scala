package sudoku

class SudokuException(msg: String, puzzle: SudokuPuzzle, cell: Option[Cell] = None) extends Exception(msg) {

  override def toString = {
    cell match {
      case Some(c) =>
        """%s
%s
%s""".format(super.getMessage(), this.cell, this.puzzle)
      case None =>
        """%s
%s""".format(super.getMessage(), this.puzzle)
    }
  }

}

