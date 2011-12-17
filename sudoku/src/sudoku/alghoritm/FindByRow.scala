package sudoku.alghoritm

import sudoku.SudokuPuzzle

class FindByRow extends SudokuAlghoritim {

  protected def solveCicle(puzzle: SudokuPuzzle) = (0 until 9).map(i => {
    log(2, "row " + i)
    this.solveCells(puzzle, puzzle.getRow(i))
  }).exists(b => b)

}
