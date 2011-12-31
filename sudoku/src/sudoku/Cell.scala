package sudoku

import scala.collection.mutable.Publisher

object CellType extends Enumeration {

  type CellType = Value

  val Normal, Original, Guess = Value
}

import CellType._

/**
 * Represents a Sudoku Cell with its respective Row, Column and value
 * @param row Cell row
 * @param col Cell column
 * @param v Original Value. If 0, it is not solved.
 */
case class Cell(row: Int, col: Int, var v: Option[Int], var cType: CellType) extends SudokuType {
  if (v.isDefined) assume(v.get >= 0 && v.get <= 9)

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

  def this(row: Int, col: Int) = this(row, col, None, CellType.Normal)

  private def evaluateNewValue(newValue: Int) {
    assume(!this.original, "Pre defined cell: %s".format(this))
    assume(this.cellType == Normal || this.runningState == RunningState.Idle)
    assume(newValue > 0 && newValue <= 9)
  }

  private def fillValue(newValue: Option[Int]) {
    this.v = newValue
    publish(CellValueChanged)
  }

  def solved = value.isDefined

  /**
   * Returns Cell's Value
   */
  def value = v

  /**
   * Fill Cell's Value with a Int
   *
   * @param newValue Int Value
   */
  def value_=(newValue: Int) {
    this.evaluateNewValue(newValue)

    this.fillValue(Some(newValue))
  }

  def cellType = this.cType

  def cellType_=(newValue: CellType) {
    this.cType = newValue

    super.publish(CellTypeChanged)
  }

  /**
   * Fill Cell's Value with a Option[Int]
   *
   * @param newValue Option[Int] Value
   */
  def value_=(newValue: Option[Int]) {
    if (newValue != this.value) {
      assume(this.runningState == RunningState.Idle, "Illegal State: %s. It must be %s".format(this.runningState, RunningState.Idle))
      newValue match {
        case Some(x) => this.evaluateNewValue(x)
        case None    =>
      }

      this.fillValue(newValue)
    }
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

