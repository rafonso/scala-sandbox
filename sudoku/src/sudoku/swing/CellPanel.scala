package sudoku.swing

import java.awt.Color
import java.awt.Font

import scala.collection.mutable.Subscriber
import scala.swing.Dialog.Message
import scala.swing.event.MouseClicked
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.Swing

import sudoku.Cell
import sudoku.CellEvaluated
import sudoku.CellEvent
import sudoku.CellType
import sudoku.CellValueChanged
import sudoku.SudokuEvent
import sudoku.SudokuType
import sudoku.GuessValueTryingEvent
import sudoku.GuessValueFailedEvent

class CellPanel(row: Int, col: Int) extends FlowPanel with Subscriber[SudokuEvent, SudokuType] {

  private val Empty = " "

  val label = new Label

  val cell = new Cell(row, col)

  private val tamanho = 14
  private val FonteNormal = new Font(this.label.font.getName(), Font.PLAIN, tamanho)
  private val FonteGuess = new Font(this.label.font.getName(), Font.ITALIC, tamanho)
  private val FonteOriginal = new Font(this.label.font.getName(), Font.BOLD, tamanho)

  private val FundoNormal = this.background
  private val FundoAvaliado = Color.BLUE

  private def init() {
    this.label.text = Empty
    this.label.font = FonteNormal

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
        getValue match {
          case Some(value) => {
            this.label.text = value.toString
            this.label.font = FonteOriginal
            this.cell.value = try {
              Some(value.toString.toInt)
            } catch {
              case t: NumberFormatException => None
            }
            this.cell.cellType = CellType.Original
          }
          case _ => {
            this.label.text = Empty
            this.label.font = FonteNormal
            this.cell.cellType = CellType.Normal
            this.cell.value = None
          }
        }
      }
      //    case MouseWheelMoved(source, point, modifiers, rotation) if (source == this ) => println(rotation + " - -" + modifiers)
      //    case KeyPressed(source, key, _, _) if (source == this) => println(key)
      //
    }

    this.cell.subscribe(this)
  }

  private def getValue = Dialog.showInput(parent = this,
    "Choice a Value for Cell " + this.row + ", " + this.col,
    "Sudoku",
    Message.Question,
    Swing.EmptyIcon,
    Empty :: (1 to 9).toList,
    this.cell.value.getOrElse(0))

  def value: Option[Int] = this.cell.value

  def notify(pub: SudokuType, evt: SudokuEvent) {
    ((pub, evt): @unchecked) match {
      case (`cell`, CellEvaluated(true))  => this.background = FundoNormal
      case (`cell`, CellEvaluated(false)) => this.background = FundoAvaliado
      case (`cell`, CellValueChanged) => {
        this.label.text = this.cell.value match {
          case Some(v) => v.toString()
          case None    => Empty
        }
        println("Cell (%d, %d) changed to %s".format(this.row, this.col, this.cell.value))
      }
      case (_, GuessValueTryingEvent(`cell`)) => this.label.font = FonteGuess
      case (_, GuessValueFailedEvent(`cell`)) => this.label.font = FonteNormal
    }
  }

  init()
}