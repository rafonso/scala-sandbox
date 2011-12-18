package sudoku.console

import scala.annotation.tailrec
import scala.collection.mutable.Subscriber

import sudoku.CicleEvent
import sudoku.GuessValueFailedEvent
import sudoku.GuessValueTryingEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuSolver
import sudoku.SudokuSolverEvent

object SudokuMain extends App with SudokuLog {

  val source = scala.io.Source.fromFile("puzzle.txt")

  val matrix = source.mkString.trim.filter(ch => (ch >= '0') && (ch <= '9'))

  val puzzle = SudokuPuzzle(matrix)
  puzzle.subscribe(PuzzleEventLog)
  puzzle.matrix.foreach(_.subscribe(CellEventLog))

  val solver = new SudokuSolver(puzzle)
  solver.subscribe(SolverEventLog)

  val result = solver()
  println("=" * 20)
  result match {
    case Some(p) => println(p)
    case None    => println("NOT SOLVED!!!!!")
  }

}