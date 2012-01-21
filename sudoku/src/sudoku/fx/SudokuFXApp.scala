/**
 *
 */
package sudoku.fx

import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.stage.Stage
import scalafx.scene.paint.Color
import scalafx.scene.layout.BorderPane
import scalafx.scene.control._
import scalafx.scene.layout._
import shape.Rectangle
import javafx.scene.text.TextAlignment
import scalafx.scene.layout.GridPane
import scalafx.geometry.Insets
import text.Font
import javafx.scene.text.FontWeight
import javafx.scene.{ Node => JfxNode }
import control.Label
import javafx.scene.layout.Priority

/**
 * @author rafael
 *
 */
object SudokuFXApp extends JFXApp {

  private val BasicInsets = Insets(5, 5, 5, 5)

  private val RunTitle = "Run"
  private val PuzzleTitle = "Puzzle"
  private val NewPuzzleTitle = "New Puzzle"
  private val CleanTitle = "Clean Puzzle"

  private val btnAction = new Button {
    disable = true
    text = RunTitle
    id = "btnAction"
    // TODO: Mnemonic
  }

  private val btnPuzzle = new Button {
    text = PuzzleTitle
    id = "btnPuzzle"
    // TODO: Mnemonic
  }

  private val btnClean = new Button() {
    text = CleanTitle
    disable = true
    // TODO: Mnemonic
  }

  private val lblIteractions = new Label {
    text = "99999"
    textAlignment = TextAlignment.RIGHT
  }
  private val lblTime = new Label {
    text = "99999"
    textAlignment = TextAlignment.RIGHT
  }
  private val lblAlghoritim = new Label {
    text = "SOMETHING"
    textAlignment = TextAlignment.RIGHT
  }
  private val LabelFont = Font.font(Font.default.getFamily(), FontWeight.BOLD, Font.default.getSize())

  def viewComponent = {
    val grid = new GridPane {
      padding = BasicInsets
      hgrow = Priority.ALWAYS
    }

    grid.add(new Label {
      text = "Iteractions:"
      textAlignment = TextAlignment.RIGHT
      font = LabelFont
    }.delegate.asInstanceOf[JfxNode], 0, 0)
    grid.add(lblIteractions.delegate.asInstanceOf[JfxNode], 1, 0)
    grid.add(new Label {
      text = "Time (ms):"
      textAlignment = TextAlignment.RIGHT
      font = LabelFont
    }.delegate.asInstanceOf[JfxNode], 2, 0)
    grid.add(lblTime.delegate.asInstanceOf[JfxNode], 3, 0)
    grid.add(new Label {
      text = "Alghortim::"
      textAlignment = TextAlignment.RIGHT
      font = LabelFont
    }.delegate.asInstanceOf[JfxNode], 0, 1)
    grid.add(lblAlghoritim.delegate.asInstanceOf[JfxNode], 1, 1, 3, 1)

    grid
  }

  def controlsComponent = {
    val grid = new GridPane {
      padding = BasicInsets
      hgrow = Priority.ALWAYS
    }

    grid.add(this.btnAction, 0, 0)
    grid.add(this.btnPuzzle, 1, 0)
    grid.add(this.btnClean, 2, 0)

    grid
  }

  def puzzleComponent = {
    val grid = new GridPane {
      padding = BasicInsets
      hgrow = Priority.ALWAYS
      vgrow = Priority.ALWAYS
      gridLinesVisible = true
    }

    for {
      i <- (0 until 9)
      j <- (0 until 9)
    } grid.add(new Label {
      text = ((i + j) % 10).toString
    }.delegate.asInstanceOf[JfxNode], i, j)

    grid
  }

  def guesesComponents = {
    new Rectangle {
      fill = Color.BEIGE
      vgrow = Priority.ALWAYS
      width = 50
      height =300
    }
  }

  stage = new Stage {
    width = 400
    height = 300
    title = "Sudoku"
    scene = new Scene {
      fill = Color.LIGHTGRAY
      content = new BorderPane {
        right = guesesComponents
        center = new BorderPane {
          resizable = true
          hgrow = Priority.ALWAYS
          vgrow = Priority.ALWAYS
          top = viewComponent
          center = puzzleComponent
          bottom = controlsComponent
        }
      }
    }
  }
}