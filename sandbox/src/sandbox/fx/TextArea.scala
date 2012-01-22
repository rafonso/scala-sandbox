/**
 *
 */
package sandbox.fx

import javafx.scene.{control => jfxsc}
import scalafx.Includes.jfxBooleanProperty2sfx
import scalafx.Includes.jfxDoubleProperty2sfx
import scalafx.Includes.jfxIntegerProperty2sfx
import scalafx.scene.control.Control
import scalafx.util.SFXDelegate

object TextArea {
  implicit def sfxTextArea2jfx(v: TextArea) = v.delegate
}

/**
 * @author rafael
 *
 */
class TextArea(override val delegate: jfxsc.TextArea = new jfxsc.TextArea)
  extends Control(delegate) with SFXDelegate[jfxsc.TextArea] {

  def prefColumnCount = delegate.prefColumnCountProperty()
  def prefColumnCount_=(v: Int) {
    prefColumnCount() = v
  }

  def prefRowCount = delegate.prefRowCountProperty()
  def prefRowCount_=(v: Int) {
    prefRowCount() = v
  }

  def scrollLeft = delegate.scrollLeftProperty()
  def scrollLeft_=(v: Double) {
    scrollLeft() = v
  }

  def scrollTop = delegate.scrollTopProperty()
  def scrollTop_=(v: Double) {
    scrollTop() = v
  }

  def wrapText = delegate.wrapTextProperty()
  def wrapText_=(v: Boolean) {
    wrapText() = v
  }

}