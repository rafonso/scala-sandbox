/**
 *
 */
package sudoku

/**
 * Basic traits for Events.
 * 
 * @author rafael
 *
 */
sealed trait SudokuEvent

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

////////////////////
// SOLVER EVENTS
////////////////////

/**
 * Represents a Event that ocurred while solving a Puzzle.
 */
sealed trait SudokuSolverEvent extends SudokuEvent

/**
 * Indicates a event ocurred in 
 */
case class CicleEvent(puzzle: SudokuPuzzle, cellsSolvedInCicle: Boolean) extends SudokuSolverEvent

/**
 * 
 */
case class GuessValueTryingEvent(guessCell: Cell) extends SudokuSolverEvent
case class GuessValueFailedEvent(guessCell: Cell) extends SudokuSolverEvent

/**
 * Indicates a change in Running State in a SudokuTYpe.
 */
case class ChangeAlghoritimEvent(alghoritimDescription: String) extends SudokuSolverEvent

////////////////////
// PUZZLE EVENTS
////////////////////

/**
 * Represents a Event that ocurred in a Puzzle.
 */
sealed trait SudokuPuzzleEvent extends SudokuEvent

/**
 * 
 */
case object SudokuPuzzleIteractionEvent extends SudokuPuzzleEvent

////////////////////
// RUNNING EVENTS
////////////////////

/**
 * Indicates a change in Running State in a SudokuTYpe.
 */
case class RunningEvent(runningState: RunningState.Value) extends SudokuEvent
