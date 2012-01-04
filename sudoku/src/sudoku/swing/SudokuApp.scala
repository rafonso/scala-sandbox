/**
 *
 */
package sudoku.swing

import scala.collection.mutable.Subscriber
import scala.swing.BorderPanel.Position
import scala.swing.Dialog.Message
import scala.swing.event.ButtonClicked
import scala.swing.Dimension
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication
import javax.swing.UIManager
import sudoku.CellType
import sudoku.RunningEvent
import sudoku.RunningState
import sudoku.SudokuEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuType
import scala.swing.event.Key

/**
 * @author rafael
 *
 */
object SudokuApp extends SimpleSwingApplication with Subscriber[SudokuEvent, SudokuType] {
  // Uses Native L&F
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

  val RunTitle = "Run"
  val PuzzleTitle = "Puzzle"
  val NewPuzzleTitle = "New Puzzle"
  val CleanTitle = "Clean Puzzle"

  val btnAction = new Button(RunTitle) {
    enabled = false
    mnemonic = Key.R
  }
  val btnPuzzle = new Button(PuzzleTitle) {
    enabled = true
    mnemonic = Key.P
  }
  val btnClean = new Button(CleanTitle) {
    enabled = false
    mnemonic = Key.C
  }
  val board = new Board

  private def init {
    super.listenTo(this.btnAction, this.btnPuzzle, this.btnClean)

    reactions += {
      case ButtonClicked(`btnAction`)     => if (this.board.isEmpty) Dialog.showMessage(null, "Values not defined", "Sudoku", Message.Error) else this.run
      case ButtonClicked(`btnPuzzle`)     => this.openPuzzleDialog
      case ButtonClicked(`btnClean`)      => this.cleanBoard
      //      case PuzzleDialogClosed(None)       => this.cleanBoard
      case PuzzleDialogClosed(Some(text)) => this.fillBoard(text.trim, (btnPuzzle.text == NewPuzzleTitle))
    }
  }

  private def run {
    val puzzle = this.board.puzzle
    puzzle.subscribe(this)
    val worker = new SudokuWorker(puzzle)

    worker.start()
  }

  private def openPuzzleDialog {
    val d = new PuzzleDialog(this.board.puzzle)
    this.listenTo(d)
    d.open
  }

  private def cleanBoard {
    this.board.puzzle.matrix.foreach(_.value = None)

    this.btnAction.enabled = false
    this.btnClean.enabled = false
  }

  private def fillBoard(text: String, reNewPuzzle: Boolean) {
    if(reNewPuzzle) {
      this.board.reInitPuzzle
    }
    text.zip(this.board.puzzle.matrix).foreach {
      case (ch, cell) if ((ch == ' ') || (ch == '0')) => {
        cell.cellType = CellType.Normal
        cell.value = None
      }
      case (ch, cell) => {
        cell.cellType = CellType.Original
        cell.value = (ch - '0').toInt
      }
    }

    this.btnAction.enabled = true
    if(reNewPuzzle) {
      this.btnPuzzle.text = PuzzleTitle
    }
    this.btnClean.enabled = true
  }

  def top = new MainFrame {
    contents = new BorderPanel {
      add(SudokuApp.this.board, Position.Center)
      add(new FlowPanel(SudokuApp.this.btnAction, SudokuApp.this.btnPuzzle, SudokuApp.this.btnClean), Position.South)
    }
    preferredSize = new Dimension(300, 400)
    resizable = false
    title = "Sudoku"
    super.centerOnScreen()
  }

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (_: SudokuPuzzle, RunningEvent(RunningState.Runnning)) => {
        this.btnAction.enabled = false
        this.btnClean.enabled = false
        this.btnPuzzle.enabled = false
      }
      case (_: SudokuPuzzle, RunningEvent(RunningState.Solved)) => {
        this.btnAction.enabled = false
        this.btnPuzzle.enabled = true
        this.btnPuzzle.text = NewPuzzleTitle
      }
      case (_, _) =>
    }
  }

  this.init
}