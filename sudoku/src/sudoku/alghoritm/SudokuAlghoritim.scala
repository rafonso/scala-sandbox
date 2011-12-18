package sudoku.alghoritm

import scala.annotation.tailrec

import sudoku.Cell
import sudoku.PendentCell
import sudoku.SudokuLog
import sudoku.SudokuPuzzle

trait SudokuAlghoritim {

  @tailrec
  private def solve(puzzle: SudokuPuzzle, hadOneCicleSolvedAtLeast: Boolean): Boolean = {
    if (puzzle.isSolved) {
      true
    } else {
      puzzle.nextIteraction
      this.solveCicle(puzzle) match {
        case true  => solve(puzzle, true)
        case false => hadOneCicleSolvedAtLeast
      }
    }
  }

  private def prepareEvaluatedCells(cells: List[Cell], evaluated: Boolean) = cells.foreach(_.evaluated = evaluated)

  protected class CellPendenstValues(val cell: Cell, val pendentValues: Seq[Int])

  protected def findCellsMayHaveValue(pendentCells: Seq[CellPendenstValues], value: Int) =
    pendentCells.filter(_.pendentValues.contains(value)).map(_.cell)

  protected def fillCell(cell: Cell, value: Int) = {
    cell.value = value
    true
  }

  private def findCellUniqueCandidade(cellsWithValue: List[PendentCell], value: Int): Boolean = cellsWithValue match {
    case Nil                                 => false
    case PendentCell(cell, List(value)) :: _ => this.fillCell(cell, value)
    case pc :: others                        => findCellUniqueCandidade(others, value)
  }

  private def solveCellsForValue(pendentCells: List[PendentCell], value: Int): Boolean = {
    pendentCells.filter(pc => pc.candidates.contains(value)) match {
      case Nil                         => false
      case PendentCell(cell, _) :: Nil => this.fillCell(cell, value)
      case cellsWithValue              => this.findCellUniqueCandidade(cellsWithValue, value)
    }
  }

  protected def solveCells(puzzle: SudokuPuzzle, cells: List[Cell]): Boolean = {
    this.prepareEvaluatedCells(cells, true)

    val pendentCells = cells
      .filter(!_.solved)
      .map(c => PendentCell(c, puzzle.getPendentsNumbersFromCell(c)))
      .filter(p => !p.candidates.isEmpty)

    val result = if (pendentCells.isEmpty) false else (1 to 9).map(v => solveCellsForValue(pendentCells, v)).exists(b => b)

    this.prepareEvaluatedCells(cells, false)
    result
  }

  protected def solveCicle(puzzle: SudokuPuzzle): Boolean

  def solvePuzzle(puzzle: SudokuPuzzle): Boolean = {
    if (puzzle.isSolved) false
    else this.solve(puzzle, false)
  }

}
