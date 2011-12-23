package sudoku.console

import scala.annotation.tailrec
import scala.collection.mutable.Subscriber

import sudoku.CellEvaluated
import sudoku.CellEvent
import sudoku.CellValueChanged
import sudoku.CicleEvent
import sudoku.SudokuEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuPuzzleIteractionEvent
import sudoku.SudokuSolver
import sudoku.SudokuSolverEvent
import sudoku.SudokuType
import sudoku.GuessValueFailedEvent
import sudoku.GuessValueTryingEvent
import sudoku.SudokuPublisher

object SudokuMain extends App with SudokuLog with Subscriber[SudokuEvent, SudokuType] {

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (cell, CellValueChanged)                   => super.log(2, cell.toString())
      case (cell, CellEvaluated(_))                   => //super.log(4, "Evaluating    " + cell.toString())
      //      case (cell, CellEvaluated(false))          => super.log(4, "Desevaluating " + cell.toString())
      case (solver, GuessValueTryingEvent(guessCell)) => super.log("TRYING " + guessCell)
      case (solver, GuessValueFailedEvent(guessCell)) => super.log("GUESS CELL " + guessCell + " FAILED!")
      case (puzzle, SudokuPuzzleIteractionEvent) => {
        println("-" * 20)
        super.log(puzzle.toString())
      }
      case (solver, CicleEvent(puzzle, solvedInCicle)) => {
        println("-" * 20 + " : " + solvedInCicle)
        super.log(puzzle.toString())
      }
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