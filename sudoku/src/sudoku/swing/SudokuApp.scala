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

/**
 * @author rafael
 *
 */
object SudokuApp extends SimpleSwingApplication with Subscriber[SudokuEvent, SudokuType] {
  // Uses Native L&F
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

  val btnAction = new Button("Run")
  val btnPuzzle = new Button("Puzzle")
  val board = new Board

  private def init {
    super.listenTo(btnAction, btnPuzzle)

    reactions += {
      case ButtonClicked(btn) if (btn == btnAction) => {
        if (this.board.isEmpty) {
          Dialog.showMessage(null, "Values not defined", "Sudoku", Message.Error)
        } else {
          Dialog.showMessage(null, "OK", "Sudoku", Message.Info)
          //          this.run
        }
      }
      case ButtonClicked(btn) if (btn == btnPuzzle) => {
        val d = new PuzzleDialog(null)
        this.listenTo(d)
        d.open
      }
      case PuzzleDialogClosed(None)       => this.cleanBoard
      case PuzzleDialogClosed(Some(text)) => this.fillBoard(text.trim)
    }
  }

  private def run {
    val puzzle = this.board.puzzle
    puzzle.subscribe(this)
    val worker = new SudokuWorker(puzzle)

    worker.start()
  }

  private def cleanBoard {
    this.board.puzzle.matrix.foreach(_.value = None)
  }

  private def fillBoard(text: String) {
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
  }

  def top = new MainFrame {
    contents = new BorderPanel {
      add(SudokuApp.this.board, Position.Center)
      add(new FlowPanel(SudokuApp.this.btnAction, SudokuApp.this.btnPuzzle), Position.South)
    }
    preferredSize = new Dimension(300, 400)
    resizable = false
    title = "Sudoku"
    super.centerOnScreen()
  }

  def notify(pub: SudokuType, evt: SudokuEvent) {
    ((pub, evt): @unchecked) match {
      case (_: SudokuPuzzle, RunningEvent(RunningState.Runnning)) => this.btnAction.enabled = false
      case (_, _) =>
    }
  }

  this.init
}