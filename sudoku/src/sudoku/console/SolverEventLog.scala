/**
 *
 */
package sudoku.console

import scala.collection.mutable.Subscriber
import sudoku.SudokuSolver
import sudoku.SudokuSolverEvent
import sudoku.GuessValueFailedEvent
import sudoku.CicleEvent
import sudoku.GuessValueTryingEvent

/**
 * @author rafael
 *
 */
object SolverEventLog extends SudokuLog with Subscriber[SudokuSolverEvent, SudokuSolver] {

  def notify(pub: SudokuSolver, evt: SudokuSolverEvent) {
    evt match {
      case CicleEvent(puzzle, solvedInCicle) => {
        println("-" * 20 + " : " + solvedInCicle)
        super.log(puzzle.toString())
      }
      case GuessValueTryingEvent(guessCell) => super.log("TRYING " + guessCell)
      case GuessValueFailedEvent(guessCell) => super.log("GUESS CELL " + guessCell + " FAILED!")
    }
  }

}