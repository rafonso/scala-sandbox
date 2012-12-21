package sudoku.alghoritm
import scala.annotation.tailrec

import sudoku.Cell
import sudoku.CellGroupEvaluated
import sudoku.PendentCell
import sudoku.SudokuPuzzle
import sudoku.SudokuType

trait SudokuAlghoritim extends SudokuType {

  @tailrec
  private def solve(puzzle: SudokuPuzzle, hadOneCicleSolvedAtLeast: Boolean): Boolean = {
    if (puzzle.isSolved) {
      true
    } else {
      puzzle.nextIteraction

      if (this.solveCicle(puzzle)) solve(puzzle, true)
      else hadOneCicleSolvedAtLeast
    }
  }

  protected def prepareEvaluatedCells(cells: List[Cell], evaluated: Boolean) {
    super.publish(new CellGroupEvaluated(cells, evaluated))
    cells.foreach(_.evaluated = evaluated)
  }

  protected class CellPendenstValues(val cell: Cell, val pendentValues: Seq[Int])

  protected def findCellsMayHaveValue(pendentCells: Seq[CellPendenstValues], value: Int) =
    pendentCells.filter(_.pendentValues.contains(value)).map(_.cell)

  protected def fillCell(cell: Cell, value: Int, guessCell: Option[Cell]) = {
    cell.addValue(value, guessCell)
    true
  }

  private def findCellUniqueCandidade(cellsWithValue: List[PendentCell], value: Int, guessCell: Option[Cell]): Boolean = cellsWithValue match {
    case Nil                                 => false
    case PendentCell(cell, List(value)) :: _ => this.fillCell(cell, value, guessCell)
    case pc :: others                        => findCellUniqueCandidade(others, value, guessCell)
  }

  private def solveCellsForValue(pendentCells: List[PendentCell], value: Int, guessCell: Option[Cell]): Boolean = {
    pendentCells.filter(pc => pc.candidates.contains(value)) match {
      case Nil                         => false
      case PendentCell(cell, _) :: Nil => this.fillCell(cell, value, guessCell)
      case cellsWithValue              => this.findCellUniqueCandidade(cellsWithValue, value, guessCell)
    }
  }

  protected def solveCells(puzzle: SudokuPuzzle, cells: List[Cell]): Boolean = {
    this.prepareEvaluatedCells(cells, true)

    val pendentCells = cells
      .filter(!_.solved)
      .map(c => PendentCell(c, puzzle.getPendentsNumbersFromCell(c)))
      .filter(p => !p.candidates.isEmpty)

    val result = if (pendentCells.isEmpty) false else (1 to 9).map(v => solveCellsForValue(pendentCells, v, puzzle.lastGuess)).exists(b => b)

    this.prepareEvaluatedCells(cells, false)
    result
  }

  protected def solveCicle(puzzle: SudokuPuzzle): Boolean

  def description: String

  def solvePuzzle(puzzle: SudokuPuzzle): Boolean = {
    if (puzzle.isSolved) false
    else this.solve(puzzle, false)
  }

}
