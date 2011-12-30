/**
 *
 */
package sudoku.swing

import java.awt.Color

import scala.swing.Dimension
import scala.swing.GridPanel

import javax.swing.border.LineBorder

/**
 * @author rafael
 *
 */
class Board extends GridPanel(9, 9) {

  private val cells = for (row <- (0 until 9); col <- (0 until 9)) yield new CellPanel(row, col)

  private def init {
    border = new LineBorder(Color.BLACK)
    contents ++ cells.toBuffer
    preferredSize = new Dimension(300, 300)

    cells.foreach(_.requestFocus())
  }

  def isEmpty = cells.forall(_.value.isEmpty)

  this.init
}