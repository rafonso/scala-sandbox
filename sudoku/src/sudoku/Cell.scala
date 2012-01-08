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
case class Cell(row: Int, col: Int, var v: Option[Int], var cType: CellType, var guessCell: Option[Cell] = None) extends SudokuType {
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
//    assume((this.cellType == Normal) || (this.runningState == RunningState.Idle), "Illegal Cell running state: " + this.runningState)
    assume(newValue > 0 && newValue <= 9, "Illegal Value: " + newValue)
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
//      assume(this.runningState == RunningState.Idle, "Illegal State: %s. It must be %s".format(this.runningState, RunningState.Idle))
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

  def addValue(value: Int, guess: Option[Cell]) {
    this.guessCell = guess
    this.value = value
    guess match {
      case Some(g) if (g == this) => this.cellType = CellType.Guess
      case _                      =>
    }
  }

  def cleanValue {
    this.guessCell = None
    this.value = None
  }

  def getGuessCell = this.guessCell

  override def hashCode: Int = 41 * (41 + this.row) + this.col

  override def equals(other: Any) = other match {
    case Cell(r, c, _, _, _) => (this.row == r) && (this.col == c)
    case _                   => false
  }

  override def toString = {
    val stringBuilder = new StringBuilder("Cell[%d, %d".format(this.row, this.col))
    if (this.value.isDefined) stringBuilder.append(", " + this.value.get)
    stringBuilder.append(", " + this.cType)
    this.guessCell match {
      case Some(c) if (c == this)          =>
      case Some(Cell(r, c, Some(v), _, _)) => stringBuilder.append(", guess[%d, %d, %d]".format(r, c, v))
      case _                               =>
    }
    stringBuilder.append("]")

    stringBuilder.toString()
  }
}

