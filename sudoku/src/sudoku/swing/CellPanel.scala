package sudoku.swing

import java.awt.Color
import java.awt.Font
import scala.collection.mutable.Subscriber
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.Swing
import scala.swing.Dialog._
import sudoku.Cell
import sudoku.CellEvaluated
import sudoku.CellEvent
import sudoku.CellValueChanged
import scala.swing.event.MouseClicked
import scala.swing.Dialog
import scala.swing.event.KeyPressed
import scala.swing.event.MouseWheelMoved
import scala.swing.event.Key._

class CellPanel(row: Int, col: Int) extends FlowPanel with Subscriber[CellEvent, Cell] {

  private val Empty = " "

  val label = new Label
  this.label.text = Empty

  private val tamanho = 14
  private val FonteNormal = new Font(this.label.font.getName(), Font.PLAIN, tamanho)
  private val FonteOriginal = new Font(this.label.font.getName(), Font.BOLD, tamanho)
  private val FundoNormal = this.background
  private val FundoAvaliado = Color.BLUE

  private def getValue = Dialog.showInput(parent = this,
    "Choice a Value for Cell " + this.row + ", " + this.col,
    "Sudoku",
    Message.Question,
    Swing.EmptyIcon,
    Empty :: (1 to 9).toList,
    0)

  this.contents += this.label

  this.border = Swing.MatteBorder(
    if (row % 3 == 0) 2 else 1, // Borda superior
    if (col % 3 == 0) 2 else 1, // Borda esquerda
    if ((row + 1) % 3 == 0) 2 else 1, // Borda inferior
    if ((col + 1) % 3 == 0) 2 else 1, // Borda direita
    Color.BLACK)

  super.listenTo(this.mouse.clicks, this.keys, this.mouse.wheel)

  this.reactions += {
    case MouseClicked(source, _, modifiers, 2, _) if ((source == this)) => {
      println(modifiers)
      getValue match {
        case Some(value) => {
          this.label.text = value.toString()
          this.label.font = FonteOriginal
        }
        case _ => {
          this.label.text = Empty
          this.label.font = FonteNormal
        }
      }
    }
//    case MouseWheelMoved(source, point, modifiers, rotation) if (source == this ) => println(rotation + " - -" + modifiers)
//    case KeyPressed(source, key, _, _) if (source == this) => println(key)
//
  }

  def value: Option[Int] = if (this.label.text == Empty) None else Some(this.label.text.toInt)

  def notify(pub: Cell, evt: CellEvent) {
    evt match {
      case CellEvaluated(true)  => this.background = FundoNormal
      case CellEvaluated(false) => this.background = FundoAvaliado
      case CellValueChanged => this.label.text = pub.value match {
        case Some(v) => v.toString()
        case None    => Empty
      }
    }
  }
}