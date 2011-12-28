package sudoku

import scala.collection.mutable.Publisher

object CellType extends Enumeration {

  type CellType = Value

  val Normal, Original, Guess = Value
}

import CellType._

/**
 *
 */
trait CellEvent extends SudokuEvent
case object CellValueChanged extends CellEvent
case class CellEvaluated(evaluated: Boolean) extends CellEvent

/**
 * Represents a Sudoku Cell with its respective Row, Column and value
 * @param row Cell row
 * @param col Cell column
 * @param v Original Value. If 0, it is not solved.
 */
case class Cell(row: Int, col: Int, var v: Option[Int], val cellType: CellType) extends SudokuType with SudokuPublisher[CellEvent] {
    if(v.isDefined) assume(v.get >= 0 && v.get <= 9)

  type Pub <: Cell

  private var inEvaluation: Boolean = false

  /**
   * <code>true</code> if original value is not 0
   */
  val original = this.solved

  /**
   * Cell Sector in Sudoku Board.
   */
  val sector = (row / 3, col / 3)

  def this(row: Int, col: Int, v: Int) = this(row, col, if (v > 0) Some(v) else None, if (v > 0) CellType.Original else CellType.Normal)

  def solved = value.isDefined

  def value = v

  def value_=(newValue: Int) {
    assume(!this.original, "Pre defined cell: %s".format(this))
    assume(this.cellType == Normal)
    assume(newValue > 0 && newValue <= 9)

    this.v = Some(newValue)
    publish(CellValueChanged)
  }

  def evaluated = this.inEvaluation

  def evaluated_=(newValue: Boolean) {
    if (this.inEvaluation != newValue) {
      this.inEvaluation = newValue
      publish(CellEvaluated(newValue))
    }
  }

  override def hashCode: Int = 41 * (41 + this.row) + this.col

  override def equals(other: Any) = other match {
    case Cell(r, c, _, _) => (this.row == r) && (this.col == c)
    case _                => false
  }

}

