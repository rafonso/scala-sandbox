package sudoku

import scala.collection.mutable.Publisher

////////////////////
// RUNNING EVENTS
////////////////////

/**
 * Indicates a change in Running State in a SudokuType.
 * 
 * @param runningState New Running State.
 */
case class RunningEvent(runningState: RunningState.Value) extends SudokuEvent


object RunningState extends Enumeration {

  /**
   * Puzzle is waiting for excution
   */
  val Idle = Value

  /**
   * Puzzle is in execution
   */
  val Runnning = Value

  /**
   * Puzzle was solved
   */
  val Solved = Value

  /**
   * Puzzle was not solved
   */
  val NotSolved = Value

  /**
   * There was a Problem with Puzzle and was throwed a Exception
   */
  val Problem = Value

  /**
   * Puzzle execution was cancelled
   */
  val Cancelled = Value
}

/**
 * Basic traits Sudoku Types.
 *
 * @author rafael
 */
trait SudokuType extends Publisher[SudokuEvent] {

  private var runState = RunningState.Idle

  def runningState = runState

  def runningState_=(newState: RunningState.Value) {
    this.runState = newState

    super.publish(RunningEvent(this.runState))
  }
} 