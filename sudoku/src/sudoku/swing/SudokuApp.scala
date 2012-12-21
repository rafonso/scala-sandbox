/**
 *
 */
package sudoku.swing

import java.awt.Font
import java.awt.Insets

import scala.collection.mutable.Subscriber
import scala.swing.BorderPanel.Position
import scala.swing.Dialog.Message
import scala.swing.GridBagPanel.Anchor
import scala.swing.GridBagPanel.Fill
import scala.swing.ScrollPane.BarPolicy
import scala.swing.event.ButtonClicked
import scala.swing.event.Key
import scala.swing.event.SelectionChanged
import scala.swing.Dimension
import scala.swing.Alignment
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.ComboBox
import scala.swing.Dialog
import scala.swing.GridBagPanel
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.ScrollPane
import scala.swing.SimpleSwingApplication
import scala.swing.Swing
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
object SudokuApp extends SimpleSwingApplication with Subscriber[SudokuEvent, SudokuType] { app =>

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
  val lblInterval = new Label("Interval (ms)") {
    xAlignment = Alignment.Right
    font = font.deriveFont(Font.BOLD)
  }
  val cmbInterval = new ComboBox(List(0, 1, 5, 10, 50, 100))
  val tblGuesses = new Table {
    model = new GuessTableModel
    preferredViewportSize = new Dimension(120, 100)
  }
  val lblIteractions = new Label
  val lblTime = new Label
  val lblAlghoritim = new Label

  val board = new Board {
    minimumSize = preferredSize
    maximumSize = preferredSize
  }

  var t0: Long = _

  var worker: SudokuWorker = _

  private def init {
    super.listenTo(this.btnAction, this.btnPuzzle, this.btnClean, this.cmbInterval.selection)

    reactions += {
      case ButtonClicked(`btnAction`) if (this.board.isEmpty)                 => Dialog.showMessage(null, "Values not defined", "Sudoku", Message.Error)
      case ButtonClicked(`btnAction`) if (!this.board.isEmpty)                => this.run
      case ButtonClicked(`btnPuzzle`) if (this.btnPuzzle.text == PuzzleTitle) => this.openPuzzleDialog
      case ButtonClicked(`btnPuzzle`) if (this.btnPuzzle.text == NewPuzzleTitle) => {
        this.cleanBoard
        this.openPuzzleDialog
      }
      case ButtonClicked(`btnClean`)                                => this.cleanBoard
      case PuzzleDialogClosed(Some(text))                           => this.fillBoard(text.trim, (btnPuzzle.text == NewPuzzleTitle))
      case SelectionChanged(`cmbInterval`) if (this.worker != null) => this.worker.pauseTime = this.cmbInterval.selection.item
    }
  }

  private def run {
    val puzzle = this.board.puzzle
    puzzle.subscribe(this)

    this.worker = new SudokuWorker(puzzle, this.cmbInterval.selection.item)
    this.worker.solver.subscribe(this)

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

  def refreshTableGuess(guesses: Seq[Cell]) = tblGuesses.model.asInstanceOf[GuessTableModel].updateTable(guesses)

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

    val guessesComponent = new ScrollPane(app.tblGuesses) {
      border = Swing.TitledBorder(Swing.EtchedBorder, "Guesses Values")
      preferredSize.width = 120
      minimumSize.width = 120
      verticalScrollBarPolicy = BarPolicy.Never
    }

    val viewComponent = new GridBagPanel {
      val c = new Constraints
      c.insets = new Insets(5, 5, 5, 5)

      c.anchor = Anchor.NorthEast
      c.fill = Fill.None
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 0
      layout(new Label("Iteractions:") {
        font = font.deriveFont(Font.BOLD)
      }) = c

      c.anchor = Anchor.NorthWest
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
      layout(new Label("Time (ms):") {
        font = font.deriveFont(Font.BOLD)
      }) = c

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
      layout(new Label("Alghortim:") {
        font = font.deriveFont(Font.BOLD)
      }) = c

      c.anchor = Anchor.West
      c.fill = Fill.Horizontal
      c.gridx = 1;
      c.gridy = 1;
      c.weightx = 1
      c.gridwidth = 3
      layout(lblAlghoritim) = c
      lblAlghoritim.xAlignment = Alignment.Left

    }

    val controlComponent = new GridPanel(2, 3) {
      contents += (app.btnAction, app.btnPuzzle, app.btnClean, app.lblInterval, app.cmbInterval)
      hGap = 2
      vGap = 2
    }

    contents = new BorderPanel {
      add(new BorderPanel {
        add(viewComponent, Position.North)
        add(app.board, Position.Center)
        add(controlComponent, Position.South)
      }, Position.Center)
      add(guessesComponent, Position.East)
    }
    preferredSize = new Dimension(300, 400)
    resizable = true
    title = "Sudoku"
    super.centerOnScreen()
  }

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (_: SudokuPuzzle, RunningEvent(RunningState.Runnning)) => {
        this.t0 = System.currentTimeMillis()
        this.btnAction.enabled = false
        this.btnClean.enabled = false
        this.btnPuzzle.enabled = false
        this.board.cleanCellsColor
      }
      case (_: SudokuPuzzle, RunningEvent(RunningState.Solved)) => {
        this.btnAction.enabled = false
        this.btnClean.enabled = true
        this.btnPuzzle.enabled = true
        this.btnPuzzle.text = NewPuzzleTitle
        this.board.cleanCellsColor
      }
      case (puzzle: SudokuPuzzle, SudokuPuzzleIteractionEvent) => {
        this.lblIteractions.text = "%,4d".format(puzzle.getIteraction)
        this.lblTime.text = "%,10d".format((System.currentTimeMillis() - t0))
        this.board.cleanCellsColor
      }
      case (_, ChangeAlghoritimEvent(description)) => this.lblAlghoritim.text = description
      case (_, GuessValueTryingEvent(guessCell))   => this.refreshTableGuess(board.puzzle.guessesCells)
      case (_, GuessValueFailedEvent(guessCell))   => this.refreshTableGuess(board.puzzle.guessesCells)
      case (_, _)                                  =>
    }
  }

  this.init
}