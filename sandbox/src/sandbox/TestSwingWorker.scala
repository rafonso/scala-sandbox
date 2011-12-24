package sandbox

import scala.swing._
import swing.event._
import GridBagPanel._
import javax.swing.ImageIcon

object TestSwingWorker extends SimpleSwingApplication {

  val txfValue = new TextField()
  val txfMax = new TextField("1000")
  val btnAction = new Button("Run")
  val btnPause = new Button("Pause")
  var max: Int = _
  var value = 0

  val worker = new SwingWorker {

    def act() {
    	
    }

  }

  private def init {

    val max = this.txfMax.text.toInt

  }

  def top = new MainFrame {
    title = "Swing Worket Test"
    contents = new GridBagPanel {
      val c = new Constraints
      c.weightx = 0.5
      c.insets = new Insets(5, 5, 5, 5)

      val lblValue = new Label("Value")
      c.anchor = Anchor.East
      c.fill = Fill.None
      c.gridx = 0;
      c.gridy = 0;
      layout(lblValue) = c

      txfValue.editable = false
      //      c.anchor = Anchor.West
      c.fill = Fill.Horizontal
      c.gridx = 1;
      c.gridy = 0;
      layout(txfValue) = c

      val lblMax = new Label("Max Value")
      //      c.anchor = Anchor.East
      c.fill = Fill.None
      c.gridx = 0;
      c.gridy = 1;
      layout(lblMax) = c

      c.fill = Fill.Horizontal
      c.gridx = 1;
      c.gridy = 1;
      layout(txfMax) = c

      //      btnAction.icon = new ImageIcon("sandbox/control_play_blue.png")
      //= swing.Icon("control_play_blue.png")
      c.anchor = Anchor.Center
      c.fill = Fill.None
      c.gridx = 0;
      c.gridy = 2;
      layout(btnAction) = c

      btnPause.enabled = false
      c.anchor = Anchor.Center
      c.fill = Fill.None
      c.gridx = 1;
      c.gridy = 2;
      layout(btnPause) = c

    }

    peer.setLocationRelativeTo(null)
    size = new Dimension(250, 150)
    //    resizable = false
  }

  super.listenTo(btnAction, btnPause)
  
  reactions += {
    case ButtonClicked(b) if (b == this.btnAction) => {
      try {
        this.max = this.txfMax.text.toInt
        println(max)
      } catch {
        case e: NumberFormatException => Dialog.showMessage(
          message = "Non numeric value: " + this.txfMax.text,
          messageType = Dialog.Message.Error,
          title = "Conversion Error")
      }
    }
  }
}