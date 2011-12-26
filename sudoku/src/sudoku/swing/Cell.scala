package sudoku.swing

import java.awt.Color
import java.awt.Font

import scala.swing._
import scala.swing.Dialog._
import scala.swing.event._

class Cell(row: Int, col: Int) extends FlowPanel {

  private val Empty = " "
  private val tamanho = 14
  private val FonteOriginal = new Font(Font.DIALOG, Font.BOLD, tamanho)
  private val FonteNormal = new Font(Font.DIALOG, Font.PLAIN, tamanho)

  val label = new Label
  this.label.text = Empty

  this.contents += this.label

  this.border = Swing.MatteBorder(
    if (row % 3 == 0) 2 else 1, // Borda superior
    if (col % 3 == 0) 2 else 1, // Borda esquerda
    if ((row + 1) % 3 == 0) 2 else 1, // Borda inferior
    if ((col + 1) % 3 == 0) 2 else 1, // Borda direita
    Color.BLACK)

  super.listenTo(this.mouse.clicks)

  this.reactions += {
    case MouseClicked(_, _, _, clicks, _) => {
      if (clicks == 2) {
        val value = Dialog.showInput(parent = this,
          "Choice a Value for Cell " + this.row + ", " + this.col,
          "Sudoku",
          Message.Question,
          Swing.EmptyIcon,
          Empty :: (1 to 9).toList,
          0)
        value match {
          case Some(v) => {
            this.label.text = v.toString()
            this.label.font = FonteOriginal
          }
          case _ => {
            this.label.text = Empty
            this.label.font = FonteNormal
          }
        }
      }
    }
  }

  def value: Option[Int] = if (this.label.text == Empty) None else Some(this.label.text.toInt)
}