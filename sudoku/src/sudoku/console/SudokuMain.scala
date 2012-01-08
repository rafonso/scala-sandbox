package sudoku.console

import scala.collection.mutable.Subscriber

import sudoku.Cell
import sudoku.CellEvaluated
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

object SudokuMain extends App with SudokuLog with Subscriber[SudokuEvent, SudokuType] {

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (Cell(row, col, Some(value), _, _), CellValueChanged) => super.log(2, "FILLING CELL (%d, %d) = %d".format(row, col, value))
      case (_: SudokuPuzzle, RunningEvent(state))                => super.log("STATE: " + state)
      case (_: SudokuSolver, GuessValueTryingEvent(guessCell))   => super.log("TRYING " + guessCell)
      case (_: SudokuSolver, GuessValueFailedEvent(guessCell))   => super.log("GUESS CELL " + guessCell + " FAILED!")
      case (_: SudokuSolver, ChangeAlghoritimEvent(description)) => super.log("ALGHORITIM: " + description)
      case (_: SudokuType, SudokuPuzzleIteractionEvent) => {
        println("-" * 20)
        super.log(puzzle.toString())
      }
      case (_: SudokuSolver, CicleEvent(puzzle, solvedInCicle)) => {
        println("-" * 20 + " : " + solvedInCicle)
        super.log(puzzle.toString())
      }
      case (_, _) =>
    }
  }

  val source = scala.io.Source.fromFile("puzzle.txt")

  val matrix = source.mkString.trim.filter(ch => (ch >= '0') && (ch <= '9'))

  val puzzle = SudokuPuzzle(matrix)
  puzzle.subscribe(this)
  puzzle.matrix.foreach(_.subscribe(this))

  val solver = new SudokuSolver(puzzle)
  solver.subscribe(this)

  val result = solver()
  println("=" * 20)
  result match {
    case Some(p) => println(p)
    case None    => println("NOT SOLVED!!!!!")
  }

}