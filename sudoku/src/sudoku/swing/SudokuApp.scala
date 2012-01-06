/**
 *
 */
package sudoku.swing

import scala.collection.mutable.Subscriber
import scala.swing.BorderPanel.Position
import scala.swing.Dialog.Message
import scala.swing.GridBagPanel._
import scala.swing.event.ButtonClicked
import scala.swing.event.Key
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.Dimension
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication
import javax.swing.UIManager
import java.awt.Insets
import sudoku.SudokuPuzzle._
import sudoku.CellType
import sudoku.RunningEvent
import sudoku.RunningState
import sudoku.SudokuEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuSolver
import sudoku.SudokuPuzzleIteractionEvent
import sudoku.SudokuType
import sudoku.ChangeAlghoritimEvent

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
  val lblIteractions = new Label
  val lblTime = new Label
  val lblAlghoritim = new Label

  val board = new Board

  var t0: Long = _

  private def init {
    super.listenTo(this.btnAction, this.btnPuzzle, this.btnClean)

    reactions += {
      case ButtonClicked(`btnAction`) if (this.board.isEmpty)  => Dialog.showMessage(null, "Values not defined", "Sudoku", Message.Error)
      case ButtonClicked(`btnAction`) if (!this.board.isEmpty) => this.run
      case ButtonClicked(`btnPuzzle`)                          => this.openPuzzleDialog
      case ButtonClicked(`btnClean`)                           => this.cleanBoard
      //      case PuzzleDialogClosed(None)       => this.cleanBoard
      case PuzzleDialogClosed(Some(text))                      => this.fillBoard(text.trim, (btnPuzzle.text == NewPuzzleTitle))
    }
  }

  private def run {
    val puzzle = this.board.puzzle
    puzzle.subscribe(this)

    val worker = new SudokuWorker(puzzle)
    worker.solver.subscribe(this)

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
    this.lblIteractions.text = ""
    this.lblTime.text = ""
  }

  private def fillBoard(text: String, reNewPuzzle: Boolean) {
    if (reNewPuzzle) {
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
    if (reNewPuzzle) {
      this.btnPuzzle.text = PuzzleTitle
    }
    this.btnClean.enabled = true
  }

  def top = new MainFrame {
    contents = new BorderPanel {
      add(SudokuApp.this.board, Position.Center)
      add(new FlowPanel(SudokuApp.this.btnAction, SudokuApp.this.btnPuzzle, SudokuApp.this.btnClean), Position.South)
      add(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)

        c.anchor = Anchor.East
        c.fill = Fill.None
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0
        layout(new Label("Iteractions:")) = c

        c.anchor = Anchor.West
        c.fill = Fill.Horizontal
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1
        layout(lblIteractions) = c

        c.anchor = Anchor.East
        c.fill = Fill.None
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0
        layout(new Label("Time (ms):")) = c

        c.anchor = Anchor.West
        c.fill = Fill.Horizontal
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 1
        layout(lblTime) = c

        c.anchor = Anchor.East
        c.fill = Fill.None
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0
        layout(new Label("Alghortim:")) = c

        c.anchor = Anchor.West
        c.fill = Fill.Horizontal
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3
        c.weightx = 1
        layout(lblAlghoritim) = c
      }, Position.North)
    }
    preferredSize = new Dimension(300, 400)
    resizable = true
    title = "Sudoku"
    super.centerOnScreen()
  }

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (_: SudokuPuzzle, RunningEvent(RunningState.Runnning)) => {
        this.btnAction.enabled = false
        this.btnClean.enabled = false
        this.btnPuzzle.enabled = false
        this.t0 = System.currentTimeMillis()
      }
      case (_: SudokuPuzzle, RunningEvent(RunningState.Solved)) => {
        this.btnAction.enabled = false
        this.btnPuzzle.enabled = true
        this.btnPuzzle.text = NewPuzzleTitle
      }
      case (puzzle: SudokuPuzzle, SudokuPuzzleIteractionEvent) => {
        lblIteractions.text = "%,4d".format(puzzle.getIteraction)
        lblTime.text = "%,10d".format((System.currentTimeMillis() - t0))
      }
      case (_, ChangeAlghoritimEvent(description)) => lblAlghoritim.text = description
      case (_, _)                                  =>
    }
  }

  this.init
}