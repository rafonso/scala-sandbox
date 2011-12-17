package sudoku

import scala.util.logging.Logged

trait SudokuLog extends Logged {

  val TAB = "\t"

  override def log(msg: String): Unit = {
    val now = new java.util.Date
    println("[%tT.%tL] %s".format(now, now, msg))
  }

  def log(tabs: Int, msg: String) {
    this.log((TAB * tabs) + msg)
  }

  def logIteraction(puzzle: SudokuPuzzle) {
    println("-" * 20)
    this.log(puzzle.toString())
  }

}

