package sudoku.alghoritm

import sudoku.SudokuPuzzle

class FindBySector extends SudokuAlghoritim {

  val sectors = for (r <- (0 until 3); c <- (0 until 3)) yield (r, c)

  protected def solveCicle(puzzle: SudokuPuzzle) = sectors.map(sector => {
    this.solveCells(puzzle, puzzle.getSector(sector))
  }).exists(b => b)

  def description = "Solving analizing sector cells"

}