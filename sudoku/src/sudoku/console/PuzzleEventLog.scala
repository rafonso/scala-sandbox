/**
 *
 */
package sudoku.console

import scala.collection.mutable.Subscriber
import sudoku.SudokuPuzzleEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuPuzzleIteractionEvent

/**
 * @author rafael
 *
 */
object PuzzleEventLog extends SudokuLog with Subscriber[SudokuPuzzleEvent, SudokuPuzzle] {

  def notify(puzzle: SudokuPuzzle, evt: SudokuPuzzleEvent) {
    evt match {
      case SudokuPuzzleIteractionEvent =>
        println("-" * 20)
        super.log(puzzle.toString())
    }
  }
}