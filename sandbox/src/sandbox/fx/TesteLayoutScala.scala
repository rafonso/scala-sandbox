/**
 *
 */
package sandbox.fx

import scalafx.application.JFXApp
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.stage.Stage
import scalafx.scene._
import scalafx.scene.paint.Color
import scalafx.beans.property._
import javafx.scene.layout.{ BorderPane => jfxBorderPane }
import javafx.geometry.{ Insets => jfxgInsets }
import javafx.beans.{ value => jfxbv }

/**
 * @author rafael
 *
 */
object TesteLayoutScala extends JFXApp {

  private val buttonTop = new Button {
    text = "Top"
  }
  private val buttonCenter = new Button {
    text = "Center"
  }
  private val buttonBottom = new Button {
    text = "Bottom"
  }
  private val buttonLeft = new Button {
    text = "Left"
  }
  private val buttonRight = new Button {
    text = "Right"
  }

  private def getBorderPane = {
    val pane = new BorderPane()

    pane.top = this.buttonTop
//    jfxBorderPane.setMargin(this.buttonTop, new jfxgInsets(2.5, 1, 2.5, 2.5))

    pane.center = this.buttonCenter
//    jfxBorderPane.setMargin(buttonCenter, new jfxgInsets(2.5, 2.5, 2.5, 2.5));

    pane.bottom = this.buttonBottom
//    jfxBorderPane.setMargin(buttonBottom, new jfxgInsets(2.5, 1, 1, 1));

    pane.left = this.buttonLeft
//    jfxBorderPane.setMargin(buttonLeft, new jfxgInsets(2.5, 2.5, 2.5, 1));

    pane.right = this.buttonRight
//    jfxBorderPane.setMargin(buttonRight, new jfxgInsets(2.5, 1, 2.5, 2.5));

    pane
  }

  private def start {
    stage = new Stage {
      title = "Teste Layout Scala FX"
      height = 200
      width = 300

      scene = new Scene {
        fill = Color.AQUAMARINE
        content = getBorderPane
      }
    }

    stage.scene.get().heightProperty.addListener(new jfxbv.ChangeListener[Number] {
      def changed(observable: jfxbv.ObservableValue[_ <: Number],
        oldValue: Number, newValue: Number) {
        val newHeight = newValue.doubleValue() - (
          buttonTop.height.get() + // buttonTop.margin.getBottom + buttonTop.margin.top +
          buttonBottom.height.get() // buttonBottom.margin.top + buttonBottom.margin.bottom
          )
        buttonCenter.prefHeight = newHeight
        buttonLeft.prefHeight = newHeight
        buttonRight.prefHeight = newHeight
      }
    })

    stage.scene.get().widthProperty.addListener(new jfxbv.ChangeListener[Number] {
      def changed(observable: jfxbv.ObservableValue[_ <: Number],
        oldValue: Number, newValue: Number) {
        val newWidth = newValue.doubleValue() - (buttonLeft.width.get() + buttonRight.width.get())
        buttonCenter.prefWidth = newWidth
      }
    })

    this.buttonTop.prefWidth.bind(stage.scene.get().widthProperty())
    this.buttonBottom.prefWidth.bind(stage.scene.get().widthProperty())

    stage.sizeToScene()
    stage.centerOnScreen()
  }

  start

}