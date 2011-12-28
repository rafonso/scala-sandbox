/**
 *
 */
package sudoku.swing

import scala.swing.BorderPanel._
import scala.swing.Dialog._
import scala.swing._
import scala.swing.event._
import javax.swing.UIManager

/**
 * @author rafael
 *
 */
object SudokuApp extends SimpleSwingApplication {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

  val btnAction = new Button("Run")
  val board = new Board

  super.listenTo(btnAction)

  def top = new MainFrame {
    contents = new BorderPanel {
      add(SudokuApp.this.board, Position.Center)
      add(new FlowPanel(SudokuApp.this.btnAction), Position.South)
    }
    preferredSize = new Dimension(300, 400)
    resizable = false
    title = "Sudoku"
    peer.setLocationRelativeTo(null)
  }

  reactions += {
    case ButtonClicked(btnAction) => {
      this.board.isEmpty match {
        case false => {
          Dialog.showMessage(null, "OK", "Sudoku", Message.Info)
        }
        case true => Dialog.showMessage(null, "Values not defined", "Sudoku", Message.Error)
      }
    }
  }


}