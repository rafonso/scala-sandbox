package sudoku.swing

import scala.collection.mutable.Subscriber
import scala.swing.SwingWorker

import sudoku.CellGroupEvaluated
import sudoku.SudokuEvent
import sudoku.SudokuPuzzle
import sudoku.SudokuSolver
import sudoku.SudokuType

class SudokuWorker(puzzle: SudokuPuzzle, var pauseTime: Int) extends SwingWorker with Subscriber[SudokuEvent, SudokuType] {

  val solver = new SudokuSolver(puzzle)

  def act() {
    solver.subscribe(this)
    solver()
  }

  def notify(pub: SudokuType, evt: SudokuEvent) {
    (pub, evt) match {
      case (_, CellGroupEvaluated(cells, false)) if (this.pauseTime > 0) => Thread.sleep(this.pauseTime)
      case (_, _) =>
    }
  }

}