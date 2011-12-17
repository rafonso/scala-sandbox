package sudoku.alghoritm

import sudoku.Cell
import sudoku.SudokuException
import sudoku.SudokuPuzzle

class FindByPendentNumbersCells extends SudokuAlghoritim {

  private def solveCell(puzzle: SudokuPuzzle, cell: Cell): Boolean = {
    puzzle.getPendentsNumbersFromCell(cell) match {
      case x :: Nil => this.fillCell(cell, x)
      case Nil => puzzle.guessCells match {
        case Nil     => throw new SudokuException("There is no disponible number for cell " + cell, puzzle)
        case c :: cs => false
      }
      case _ => false
    }
  }

  protected def solveCicle(puzzle: SudokuPuzzle) = puzzle.getNotSolved.map(this.solveCell(puzzle, _)).exists(b => b)

}
