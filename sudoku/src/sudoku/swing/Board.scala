/**
 *
 */
package sudoku.swing

import scala.swing._
import javax.swing.border._
import java.awt.Color
import scala.swing.event.MouseEntered
import scala.swing.event.MouseClicked
import scala.swing.event.MouseExited

/**
 * @author rafael
 *
 */
class Board extends GridPanel(9, 9) {

  val cells = for (row <- (0 until 9); col <- (0 until 9)) yield new Cell(row, col)

  border = new LineBorder(Color.BLACK)
  contents ++ cells.toBuffer
  preferredSize = new Dimension(300, 300)

  def isEmpty = cells.forall(_.value.isEmpty)
  
}