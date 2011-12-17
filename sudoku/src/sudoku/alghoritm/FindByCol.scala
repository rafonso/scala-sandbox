/**
 *
 */
package sudoku.alghoritm

import sudoku.SudokuPuzzle

class FindByCol extends SudokuAlghoritim {

  protected def solveCicle(puzzle: SudokuPuzzle) = (0 until 9).map(i => {
    log(2, "col " + i)
    this.solveCells(puzzle, puzzle.getCol(i))
  }).exists(b => b)

}

