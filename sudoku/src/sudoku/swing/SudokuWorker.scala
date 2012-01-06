package sudoku.swing

import scala.collection.mutable.Subscriber
import scala.swing.SwingWorker

import sudoku.console.SudokuLog
import sudoku.Cell
import sudoku.CellValueChanged
import sudoku.ChangeAlghoritimEvent
import sudoku.CicleEvent
import sudoku.GuessValueFailedEvent
import sudoku.GuessValueTryingEvent
import sudoku.RunningEvent
import sudoku.SudokuEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuPuzzleIteractionEvent
import sudoku.SudokuSolver
import sudoku.SudokuType

class SudokuWorker(puzzle: SudokuPuzzle) extends SwingWorker {
  
  val solver = new SudokuSolver(puzzle)

//  def notify(pub: SudokuType, evt: SudokuEvent) {
//    ((pub, evt): @unchecked) match {
//      case (Cell(row, col, Some(value), _), CellValueChanged)    => super.log(2, "FILLING CELL (%d, %d) = %d".format(row, col, value))
//      case (_: SudokuPuzzle, RunningEvent(state))                => super.log("STATE: " + state)
//      case (_: SudokuSolver, GuessValueTryingEvent(guessCell))   => super.log("TRYING " + guessCell)
//      case (_: SudokuSolver, GuessValueFailedEvent(guessCell))   => super.log("GUESS CELL " + guessCell + " FAILED!")
//      case (_: SudokuSolver, ChangeAlghoritimEvent(description)) => super.log("ALGHORITIM: " + description)
//      case (puzzle: SudokuPuzzle, SudokuPuzzleIteractionEvent) => {
//        println("-" * 20)
//        super.log(puzzle.toString())
//      }
//      case (_: SudokuSolver, CicleEvent(puzzle, solvedInCicle)) => {
//        println("-" * 20 + " : " + solvedInCicle)
//        super.log(puzzle.toString())
//      }
//      case (_, _) =>
//    }
//  }

  def act() {
    
//    puzzle.subscribe(this)
//    puzzle.matrix.foreach(_.subscribe(this))
//    solver.subscribe(this)

    solver()
  }

}