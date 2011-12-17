package sudoku

object SudokuMain extends App {

  val source = scala.io.Source.fromFile("puzzle.txt")

  val matrix = source.mkString.trim.filter(ch => (ch >= '0') && (ch <= '9'))

  val puzzle = SudokuPuzzle(matrix)

  val solver = new SudokuSolver(puzzle)
  val result = solver()
  println("=" * 20)
  result match {
    case Some(p) => println(p)
    case None    => println("NOT SOLVED!!!!!")
  }

}