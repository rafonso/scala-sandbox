package sudoku

import javafx.beans.{ value => jfxValue }

package object fx {

  def numberBinding(action: (Number) => Unit) = new jfxValue.ChangeListener[Number] {
    def changed(observable: jfxValue.ObservableValue[_ <: Number], oldValue: Number, newValue: Number) {
      action(newValue)
    }
  }

}