/**
 *
 */
package sudoku.swing

import java.awt.Color

import scala.swing.Dimension
import scala.swing.GridPanel

import javax.swing.border.LineBorder
import sudoku.SudokuPuzzle

/**
 * @author rafael
 *
 */
class Board extends GridPanel(9, 9) {

  private val cells = for (row <- (0 until 9); col <- (0 until 9)) yield new CellPanel(row, col)

  private var puzz: SudokuPuzzle = _

  def puzzle = this.puzz

  private def init() {
    this.reInitPuzzle

    contents ++ cells.toBuffer
    preferredSize = new Dimension(300, 300)

    cells.foreach(_.requestFocus())
  }

  def isEmpty = cells.forall(_.value.isEmpty)

  def reInitPuzzle {
    this.cells.foreach(_.reInitCell)
    this.puzz = SudokuPuzzle(cells.map(_.cell).toList)
  }
  
  def cleanCellsColor {
    this.cells.foreach(_.cleanColor)
  }
  
  this.init()
}