/**
 *
 */
package sudoku.fx

import javafx.scene.{ layout => jfxLayout }
import javafx.scene.{ text => jfxText }
import javafx.{ scene => jfxScene }
import scalafx.application.JFXApp
import scalafx.beans.value.ObservableValue._
import scalafx.geometry.Insets
import scalafx.scene.control.Button.sfxButton2jfx
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane.sfxGridPane2jfx
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color.sfxColor2jfx
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Font
import scalafx.scene.Scene
import scalafx.stage.Stage.sfxStage2jfx
import scalafx.stage.Stage

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

  // COMPONENTS - BEGIN

  private lazy val btnAction = new Button {
    disable = true
    text = RunTitle
    id = "btnAction"
    // TODO: Mnemonic
  }

  private lazy val btnPuzzle = new Button {
    text = PuzzleTitle
    id = "btnPuzzle"
    // TODO: Mnemonic
  }

  private lazy val btnClean = new Button() {
    text = CleanTitle
    disable = true
    // TODO: Mnemonic
  }

  private lazy val lblIteractions = new Label {
    text = "99999"
    textAlignment = jfxText.TextAlignment.RIGHT
  }

  private lazy val lblTime = new Label {
    text = "99999"
    textAlignment = jfxText.TextAlignment.RIGHT
  }

  private lazy val lblAlghoritim = new Label {
    text = "SOMETHING"
    textAlignment = jfxText.TextAlignment.RIGHT
  }

  private lazy val LabelFont = Font.font(Font.default.getFamily(), jfxText.FontWeight.BOLD, Font.default.getSize())

  private lazy val viewComponent = new GridPane {
    padding = BasicInsets
    hgrow = jfxLayout.Priority.ALWAYS
  }

  private lazy val controlsComponent = new GridPane {
    padding = BasicInsets
    hgrow = jfxLayout.Priority.ALWAYS
  }

  private lazy val puzzleComponent = new GridPane {
    padding = BasicInsets
    hgrow = jfxLayout.Priority.ALWAYS
    vgrow = jfxLayout.Priority.ALWAYS
    gridLinesVisible = true
  }

  private lazy val rectangle = new Rectangle {
    fill = Color.BEIGE
    vgrow = jfxLayout.Priority.ALWAYS
    hgrow = jfxLayout.Priority.ALWAYS
    width = 100
    height = 300
  }

  private lazy val mainContent = new BorderPane {
    right = rectangle
    center = new BorderPane {
      hgrow = jfxLayout.Priority.ALWAYS
      vgrow = jfxLayout.Priority.ALWAYS
      top = viewComponent
      center = puzzleComponent
      bottom = controlsComponent
    }
  }

  stage = new Stage {
    width = 400
    height = 300
    title = "Sudoku"

    scene = new Scene {
      fill = Color.LIGHTGRAY
      content = mainContent
    }
  }

  // COMPONENTS - END

  private def start {

    val jfxscene = stage.scene.get()
    val witdhBinding = jfxscene.widthProperty.subtract(rectangle.width)

    def initViewComponent {
      this.viewComponent.add(new Label {
        text = "Iteractions:"
        textAlignment = jfxText.TextAlignment.RIGHT
        font = LabelFont
      }.delegate.asInstanceOf[jfxScene.Node], 0, 0)

      this.viewComponent.add(lblIteractions.delegate.asInstanceOf[jfxScene.Node], 1, 0)

      this.viewComponent.add(new Label {
        text = "Time (ms):"
        textAlignment = jfxText.TextAlignment.RIGHT
        font = LabelFont
      }.delegate.asInstanceOf[jfxScene.Node], 2, 0)

      this.viewComponent.add(lblTime.delegate.asInstanceOf[jfxScene.Node], 3, 0)

      this.viewComponent.add(new Label {
        text = "Alghortim::"
        textAlignment = jfxText.TextAlignment.RIGHT
        font = LabelFont
      }.delegate.asInstanceOf[jfxScene.Node], 0, 1)

      this.viewComponent.add(lblAlghoritim.delegate.asInstanceOf[jfxScene.Node], 1, 1, 3, 1)

      this.viewComponent.prefWidth.bind(witdhBinding)
    }

    def initControlsComponent {
      this.controlsComponent.add(this.btnAction, 0, 0)
      this.controlsComponent.add(this.btnPuzzle, 1, 0)
      this.controlsComponent.add(this.btnClean, 2, 0)

      this.controlsComponent.prefWidth.bind(witdhBinding)
    }

    def initPuzzleComponent {
      for {
        i <- (0 until 9)
        j <- (0 until 9)
      } this.puzzleComponent.add(new Label {
        text = ((i + j) % 10).toString
      }.delegate.asInstanceOf[jfxScene.Node], i, j)

      this.puzzleComponent.prefWidth.bind(witdhBinding)
    }

    rectangle.height.bind(jfxscene.heightProperty())
    mainContent.prefHeight.bind(jfxscene.heightProperty())
//    mainContent.prefWidth.bind(witdhBinding)

    initViewComponent
    initControlsComponent
    initPuzzleComponent

    stage.sizeToScene()
    stage.centerOnScreen()
  }

  start

}