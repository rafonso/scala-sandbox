package sudoku.swing

import java.awt.Color
import java.awt.Font
import scala.collection.mutable.Subscriber
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.Swing
import sudoku.Cell
import sudoku.CellEvaluated
import sudoku.CellStatus
import sudoku.CellStatusChanged
import sudoku.CellValueChanged
import sudoku.GuessValueFailedEvent
import sudoku.GuessValueTryingEvent
import sudoku.SudokuEvent
import sudoku.SudokuType
import sudoku.RunningState
import sudoku.console.SudokuLog
import sudoku.CellGroupEvaluated
import sudoku.CicleEvent

class CellPanel(row: Int, col: Int) extends FlowPanel with Subscriber[SudokuEvent, SudokuType] with SudokuLog {

  val label = new Label

  val cell = new Cell(row, col)

  private val tamanho = 14
  private val NormalFont = new Font(this.label.font.getName(), Font.PLAIN, tamanho)
  private val GuessFont = new Font(this.label.font.getName(), Font.ITALIC, tamanho)
  private val OriginalFont = new Font(this.label.font.getName(), Font.BOLD, tamanho)

  private val FundoNormal = this.background
  private val FundoAvaliado = Color.CYAN

  private val normalColor = this.label.foreground
  private val changedColor = Color.BLUE

  private def init() {
    this.label.text = Empty
    this.label.font = NormalFont

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
      case (Cell(`row`, `col`, _, _, _), CellEvaluated(true))  => this.background = FundoAvaliado
      case (Cell(`row`, `col`, _, _, _), CellEvaluated(false)) => this.background = FundoNormal
      case (Cell(`row`, `col`, Some(x), _, _), CellValueChanged) => {
        this.label.text = x.toString()
        this.label.foreground =
          if (cell.runningState == RunningState.Runnning) changedColor else normalColor
      }
      case (Cell(`row`, `col`, None, _, _), CellValueChanged) => this.label.text = Empty
      case (Cell(`row`, `col`, _, CellStatus.Empty, _), CellStatusChanged) => this.label.font = NormalFont
      case (Cell(`row`, `col`, _, CellStatus.Original, _), CellStatusChanged) => this.label.font = OriginalFont
      case (Cell(`row`, `col`, _, CellStatus.FilledWithNoGuess, _), CellStatusChanged) => this.label.font = NormalFont
      case (Cell(`row`, `col`, _, CellStatus.Guess, _), CellStatusChanged) => this.label.font = GuessFont
      case (Cell(`row`, `col`, _, CellStatus.FilledWithGuess, _), CellStatusChanged) => this.label.font = NormalFont
      case (_, GuessValueTryingEvent(`cell`)) => this.label.font = GuessFont
      case (_, GuessValueFailedEvent(`cell`)) => this.label.font = NormalFont
      case (_, _) =>
    }
  }

  def reInitCell {
    this.cell.runningState = RunningState.Idle
    this.cell.status = CellStatus.Empty
    this.cell.value = None
    this.cell.guessCell = None
    this.label.foreground = normalColor
  }

  def cleanColor {
    this.label.foreground = normalColor
  }

  init()
}