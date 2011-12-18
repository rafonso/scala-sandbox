/**
 *
 */
package sudoku.alghoritm

import sudoku.SudokuPuzzle

class FindByCol extends SudokuAlghoritim {

  protected def solveCicle(puzzle: SudokuPuzzle) = (0 until 9).map(i => {
    this.solveCells(puzzle, puzzle.getCol(i))
  }).exists(b => b)

}

