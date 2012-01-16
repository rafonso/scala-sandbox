package sudoku

import scala.collection.mutable.Publisher

////////////////////
// CELL EVENTS
////////////////////

/**
 * Represents a Event that ocurred in a Cell.
 */
sealed trait CellEvent extends SudokuEvent

/**
 * Indicates when a Cell Value changed.
 */
case object CellValueChanged extends CellEvent

/**
 * Indicates when a Cell Status changes.
 */
case object CellStatusChanged extends CellEvent

/**
 * Indicates when a Cell is being evaluated or not.
 *
 * @param evaluated IF a Cell is evaluated or not.
 */
case class CellEvaluated(evaluated: Boolean) extends CellEvent

/**
 * Represents a Cell Status.
 */
object CellStatus extends Enumeration {

  type CellStatus = Value

  /**
   * Cell is not filled.
   */
  val Empty = Value

  /**
   * Cell was filled before Solver runs.
   */
  val Original = Value

  /**
   * Cell was filled with no Guess Cell related
   */
  val FilledWithNoGuess = Value

  /**
   * Cell was filled with a guess value.
   */
  val Guess = Value

  /**
   * Cell was filled with a Guess Cell related
   */
  val FilledWithGuess = Value

}

import CellStatus._

/**
 * Represents a Sudoku Cell with its respective Row, Column and value
 * @param row Cell row
 * @param col Cell column
 * @param v Original Value. If 0, it is not solved.
 */
case class Cell(row: Int, col: Int, var v: Option[Int], var cellStatus: CellStatus, var guessCell: Option[Cell] = None) extends SudokuType {
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

  def this(row: Int, col: Int, v: Int) = this(row, col, if (v > 0) Some(v) else None, if (v > 0) CellStatus.Original else CellStatus.Empty)

  def this(row: Int, col: Int) = this(row, col, None, CellStatus.Empty)

  private def evaluateNewValue(newValue: Int) {
    assume(!this.original, "Pre defined cell: %s".format(this))
    //    assume((this.status == Normal) || (this.runningState == RunningState.Idle), "Illegal Cell running state: " + this.runningState)
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

  def status = this.cellStatus

  def status_=(newValue: CellStatus) {
    this.cellStatus = newValue

    super.publish(CellStatusChanged)
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
      case Some(g) if (g == this) => this.status = CellStatus.Guess
      case Some(g) if (g != this) => this.status = CellStatus.FilledWithGuess
      case None                   => this.status = CellStatus.FilledWithNoGuess
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
    stringBuilder.append(", " + this.cellStatus)
    this.guessCell match {
      case Some(c) if (c == this)          =>
      case Some(Cell(r, c, Some(v), _, _)) => stringBuilder.append(", guess[%d, %d, %d]".format(r, c, v))
      case _                               =>
    }
    stringBuilder.append("]")

    stringBuilder.toString()
  }
}

