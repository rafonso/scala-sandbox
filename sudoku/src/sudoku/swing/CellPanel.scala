package sudoku.swing

import java.awt.Color
import java.awt.Font

import scala.collection.mutable.Subscriber
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.Swing

import sudoku.Cell
import sudoku.CellEvaluated
import sudoku.CellType
import sudoku.CellTypeChanged
import sudoku.CellValueChanged
import sudoku.GuessValueFailedEvent
import sudoku.GuessValueTryingEvent
import sudoku.SudokuEvent
import sudoku.SudokuType

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

    this.cell.subscribe(this)
  }

  def value: Option[Int] = this.cell.value

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (`cell`, CellEvaluated(true))  => this.background = FundoNormal
      case (`cell`, CellEvaluated(false)) => this.background = FundoAvaliado
      case (`cell`, CellTypeChanged) => {
        this.label.font = this.cell.cellType match {
          case CellType.Guess    => FonteGuess
          case CellType.Normal   => FonteNormal
          case CellType.Original => FonteOriginal
        }
      }
      case (`cell`, CellValueChanged) => {
        this.label.text = this.cell.value match {
          case Some(v) => v.toString()
          case None    => Empty
        }
        println("Cell (%d, %d) changed to %s".format(this.row, this.col, this.cell.value))
      }
      case (_, GuessValueTryingEvent(`cell`)) => this.label.font = FonteGuess
      case (_, GuessValueFailedEvent(`cell`)) => this.label.font = FonteNormal
      case (_, _)                             =>
    }
  }

  init()
}