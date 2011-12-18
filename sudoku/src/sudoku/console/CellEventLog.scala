/**
 *
 */
package sudoku.console

import scala.collection.mutable.Subscriber

import sudoku.Cell
import sudoku.CellEvent
import sudoku.CellValueChanged

/**
 * @author rafael
 *
 */
object CellEventLog extends SudokuLog with Subscriber[CellEvent, Cell] {

  def notify(pub: Cell, evt: CellEvent) {
    evt match {
      case CellValueChanged => super.log(2, pub.toString())
      case _                =>
    }
  }
}