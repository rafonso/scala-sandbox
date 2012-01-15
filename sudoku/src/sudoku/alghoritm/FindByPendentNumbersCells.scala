package sudoku.alghoritm

import sudoku.Cell
import sudoku.SudokuException
import sudoku.SudokuPuzzle

class FindByPendentNumbersCells extends SudokuAlghoritim {

  private def solveCell(puzzle: SudokuPuzzle, cell: Cell): Boolean = {
    super.prepareEvaluatedCells(List(cell), true)

    val result = puzzle.getPendentsNumbersFromCell(cell) match {
      case x :: Nil => this.fillCell(cell, x, puzzle.lastGuess)
      case Nil => {
        if (puzzle.guessesCells.isEmpty) throw new SudokuException("There is no disponible number for cell " + cell, puzzle)
        else false
      }
      case _ => false
    }

    super.prepareEvaluatedCells(List(cell), false)
    result
  }

  protected def solveCicle(puzzle: SudokuPuzzle) = puzzle.getNotSolved.map(this.solveCell(puzzle, _)).exists(b => b)

  def description = "Pendents values for cell"

}
