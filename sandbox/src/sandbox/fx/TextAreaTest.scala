package sandbox.fx

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.paint.Color
import scalafx.scene.layout.BorderPane

object TextAreaTest extends JFXApp {

  stage = new Stage {
    width = 600
    height = 450
    title = "TextArea Test"
    scene = new Scene {
      fill = Color.LIGHTGREEN
      content = new BorderPane {
        center = new TextArea {
//          editable = false
        }
      }
    }
  }

}