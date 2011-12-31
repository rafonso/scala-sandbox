package sudoku.swing

import java.awt.Dimension
import java.awt.Font

import scala.swing.Dialog.Message
import scala.swing.event.ButtonClicked
import scala.swing.event.Event
import scala.swing.event.Key
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.TextArea
import scala.swing.Window

case class PuzzleDialogClosed(strPuzzle: Option[String]) extends Event

class PuzzleDialog(owner: Window) extends Dialog(owner) {
  // default button
  val btnOk = new Button("Ok")
  val btnCancel = new Button("Cancel")
  val txaPuzzle = new TextArea(9, 9)

  def init {
    super.title = "Enter puzzle Values"
    super.preferredSize = new Dimension(300, 400)
    super.modal = true
    btnOk.mnemonic = Key.O
    btnCancel.mnemonic = Key.C
    txaPuzzle.font = new Font(this.txaPuzzle.font.getName(), this.txaPuzzle.font.getStyle(), this.txaPuzzle.font.getSize + 13)

    super.contents = new BorderPanel {
      add(txaPuzzle, BorderPanel.Position.Center)
      add(new FlowPanel {
        contents += btnOk
        contents += btnCancel
      }, BorderPanel.Position.South)
    }

    super.listenTo(btnOk, btnCancel)
    reactions += {
      case ButtonClicked(`btnOk`) => {
        val concat = """([0-9 ]{9})""".r.findAllIn(txaPuzzle.text).mkString
        if(concat.size == 81) {
          publish(PuzzleDialogClosed(Some(concat)))
          this.close()
        } else {
          Dialog.showMessage(null, "Puzzle invalid", "Sudoku", Message.Error)
        }
      }
      case ButtonClicked(`btnCancel`) => {
        publish(PuzzleDialogClosed(None))
        this.close()
      }
    }

    super.centerOnScreen()
  }

  this.init
}