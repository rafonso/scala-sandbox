/**
 *
 */
package sudoku.swing

import java.awt.Insets

import scala.collection.mutable.Subscriber
import scala.swing.BorderPanel.Position
import scala.swing.Dialog.Message
import scala.swing.GridBagPanel.Anchor
import scala.swing.GridBagPanel.Fill
import scala.swing.event.ButtonClicked
import scala.swing.event.Key
import scala.swing.Dimension
import scala.swing.Alignment
import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.GridBagPanel
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.Orientation
import scala.swing.ScrollPane
import scala.swing.SimpleSwingApplication
import scala.swing.Table

import javax.swing.table.AbstractTableModel
import javax.swing.UIManager
import sudoku.Cell
import sudoku.CellStatus
import sudoku.ChangeAlghoritimEvent
import sudoku.GuessValueFailedEvent
import sudoku.GuessValueTryingEvent
import sudoku.RunningEvent
import sudoku.RunningState
import sudoku.SudokuEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuPuzzleIteractionEvent
import sudoku.SudokuType

/**
 * @author rafael
 *
 */
object SudokuApp extends SimpleSwingApplication with Subscriber[SudokuEvent, SudokuType] {

  // Uses Native L&F
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

  class GuessTableModel extends AbstractTableModel {

    private var guessesCells: Seq[Cell] = Seq.empty

    def getRowCount() = guessesCells.size

    def getColumnCount() = 3

    override def getColumnName(col: Int) = col match {
      case 0 => "Row"
      case 1 => "Column"
      case 2 => "Value"
      case _ => Empty
    }

    def getValueAt(row: Int, col: Int) = {
      if (row < this.getRowCount()) {
        val selectedCell = guessesCells(row)

        col match {
          case 0 => selectedCell.row.toString
          case 1 => selectedCell.col.toString
          case 2 => selectedCell.value.getOrElse(Empty).toString
          case _ => Empty
        }
      } else Empty
    }

    def updateTable(cells: Seq[Cell]) {
      this.guessesCells = cells.reverse
      fireTableDataChanged()
    }

  }

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
  val tblGuesses = new Table {
    model = new GuessTableModel
    preferredViewportSize = new Dimension(120, 100)
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
    this.refreshTableGuess(Seq.empty)
  }

  def refreshTableGuess(guesses: Seq[Cell]) = tblGuesses.model.asInstanceOf[GuessTableModel].updateTable(board.puzzle.guessesCells)

  private def fillBoard(text: String, reNewPuzzle: Boolean) {
    if (reNewPuzzle) {
      this.board.reInitPuzzle
      this.lblAlghoritim.text = Empty
      this.lblIteractions.text = Empty
      this.lblTime.text = Empty
    }
    text.zip(this.board.puzzle.matrix).foreach {
      case (ch, cell) if ((ch == ' ') || (ch == '0')) => {
        cell.status = CellStatus.Empty
        cell.value = None
      }
      case (ch, cell) => {
        cell.status = CellStatus.Original
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
        lblIteractions.xAlignment = Alignment.Right

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
        lblTime.xAlignment = Alignment.Right

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
        lblAlghoritim.xAlignment = Alignment.Left

      }, Position.North)
      add(SudokuApp.this.board, Position.Center)
      add(new GridPanel(1, 3) {
        contents += (SudokuApp.this.btnAction, SudokuApp.this.btnPuzzle, SudokuApp.this.btnClean)
        hGap = 2
      }, Position.South)
      add(new BoxPanel(Orientation.Vertical) {
        contents += (new Label("Guesses Values"), new ScrollPane(SudokuApp.this.tblGuesses))
      }, Position.East)
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
        //        this.btnClean.enabled = true
        this.btnPuzzle.enabled = true
        this.btnPuzzle.text = NewPuzzleTitle
      }
      case (puzzle: SudokuPuzzle, SudokuPuzzleIteractionEvent) => {
        lblIteractions.text = "%,4d".format(puzzle.getIteraction)
        lblTime.text = "%,10d".format((System.currentTimeMillis() - t0))
      }
      case (_, ChangeAlghoritimEvent(description)) => lblAlghoritim.text = description
      case (_, GuessValueTryingEvent(guessCell))   => this.refreshTableGuess(board.puzzle.guessesCells)
      case (_, GuessValueFailedEvent(guessCell))   => this.refreshTableGuess(board.puzzle.guessesCells)
      case (_, _)                                  =>
    }
  }

  this.init
}