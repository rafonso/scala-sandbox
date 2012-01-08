package sudoku.alghoritm

import sudoku.SudokuPuzzle

class FindByRow extends SudokuAlghoritim {

  protected def solveCicle(puzzle: SudokuPuzzle) = (0 until 9).map(i => {
    this.solveCells(puzzle, puzzle.getRow(i))
  }).exists(b => b)

  def description = "Row cells"

}
