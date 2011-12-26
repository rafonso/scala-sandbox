package sandbox

import scala.swing._
import swing.event._
import GridBagPanel._
import javax.swing.ImageIcon

abstract class CountWorker(max: Int, actionBeforeCounting: () => Unit, actionAfterIncrement: (Int) => Unit, actionAfterCounting: () => Unit) extends SwingWorker {

  private var t0: Long = 0L
  protected var run = true
  protected var count: Int = 0

  protected def prepareCounting {
    this.actionBeforeCounting()

    println("EXECUÇÃO INICIADA!")
    t0 = System.currentTimeMillis()
  }

  protected def increment {
    this.count += 1

    this.actionAfterIncrement(this.count)
  }

  protected def countStoped {
    val deltaT = System.currentTimeMillis() - t0
    if (this.run) println("EXECUÇÃO CONCLUÍDA: %,7d ms".format(deltaT))
    else println("EXECUÇÃO INTERROMPIDA EM %,7d: %,7d ms".format(this.count, deltaT))

    this.actionAfterCounting()
  }

}

class SimpleCountWorker(max: Int, actionBeforeCounting: () => Unit, actionAfterIncrement: (Int) => Unit, actionAfterCounting: () => Unit)
  extends CountWorker(max, actionBeforeCounting, actionAfterIncrement, actionAfterCounting) {

  def act() {
    this.prepareCounting
    loopWhile(count < max && run) {

    } andThen {
      this.countStoped
    }
  }
}

class ReactCountWorker(max: Int, actionBeforeCounting: () => Unit, actionAfterIncrement: (Int) => Unit, actionAfterCounting: () => Unit)
  extends CountWorker(max, actionBeforeCounting, actionAfterIncrement, actionAfterCounting) {

  def act() {
    this.prepareCounting
    loopWhile(count < max && run) {
      this.increment
      receive {
        case 'stop => run = false
        case _     =>
      }
    } andThen {
      this.countStoped
    }
  }
}

object TestSwingWorker extends SimpleSwingApplication {

  private val RunTitle = "Run"
  private val StopTitle = "Stop"

  val txfValue = new TextField()
  val txfMax = new TextField("1000")
  val btnAction = new Button(RunTitle)
  val btnPause = new Button("Pause")

  var worker: CountWorker = _

  private def actionBeforeCounting() {
    btnAction.text = StopTitle
    //      btnPause.enabled = true
    txfMax.editable = false
  }

  private def actionAfterIncrement(count: Int) {
    txfValue.text = count.toString()
  }

  private def actionAfterCounting() {
    btnAction.text = RunTitle
    //      btnPause.enabled = false
    txfMax.editable = true
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
      txfValue.horizontalAlignment = Alignment.Right
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

      txfMax.horizontalAlignment = Alignment.Right
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
    case ButtonClicked(this.btnAction) => {
      this.btnAction.text match {
        case RunTitle => {
          try {
            // Determino qual worker será utilizad: SimpleCountWorker ou ReactCountWorker. 
            this.worker = new ReactCountWorker(this.txfMax.text.toInt, this.actionBeforeCounting, this.actionAfterIncrement, this.actionAfterCounting)
            // Inicia o Loop
            this.worker.start()
          } catch {
            case e: NumberFormatException => Dialog.showMessage(
              message = "Non numeric value: " + this.txfMax.text,
              messageType = Dialog.Message.Error,
              title = "Conversion Error")
          }
        }
        case StopTitle => {
          // Interrompe o Loop.
          this.worker ! 'stop
        }
      }
    }
  }
}