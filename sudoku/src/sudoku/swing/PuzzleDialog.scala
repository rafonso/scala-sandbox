package sudoku.swing

import java.awt.Dimension
import java.awt.Font

import scala.swing._
import scala.swing.Dialog.Message
import scala.swing.event._

import sudoku.SudokuPuzzle

case class PuzzleDialogClosed(strPuzzle: Option[String]) extends Event

class PuzzleDialog(puzzle: SudokuPuzzle) extends Dialog {
  // default button
  val btnOk = new Button("Ok")
  val btnCancel = new Button("Cancel")
  val txaPuzzle = new TextArea(9, 9)

  private def fillFromPuzzle {

  }

  private def init {
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

    super.listenTo(btnOk, btnCancel, this.txaPuzzle)
    reactions += {
      case ButtonClicked(`btnOk`) => {
        val concat = """([0-9 ]{9})""".r.findAllIn(txaPuzzle.text).mkString
        if (concat.size == 81) {
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
      //      case KeyEvent(e) => println(e)
      case KeyPressed(this.txaPuzzle, char, modifiers, location) => {
        println(char)
      }
    }

    super.centerOnScreen()

    if (!puzzle.matrix.forall(_.value.isEmpty)) {
      for (row <- (0 until 9)) {
        this.txaPuzzle.append(puzzle.getRow(row).map(_.value.getOrElse(0)).mkString + "\n")
      }
    }
  }

  this.init
}