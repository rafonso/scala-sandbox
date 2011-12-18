package sudoku

import scala.collection.mutable.Publisher

trait SudokuPuzzleEvent
case object SudokuPuzzleIteractionEvent extends SudokuPuzzleEvent

case class SudokuPuzzle(values: Seq[Int], val guessCells: List[Cell] = Nil) extends Publisher[SudokuPuzzleEvent] {
  assert(values.size == 81)
  assert(values.forall(x => x >= 0 && x <= 9))

  type Pub <: SudokuPuzzle

  private def copyNumbers: Set[Int] = SudokuPuzzle.originalNumbers.foldLeft(Set.empty[Int])((set, x) => set + x)

  private def cellsToPendentNumbers(cells: Seq[Cell], cell: Cell): Seq[Int] = cells.filter(_.solved).map(_.value)

  val matrix = values.zipWithIndex.map({ case (v, i) => new Cell(i / 9, i % 9, v) }).toList

  def getRow(row: Int) = matrix.filter(_.row == row)

  def getCol(col: Int) = matrix.filter(_.col == col)

  def getSector(sector: (Int, Int)): List[Cell] = matrix.filter(_.sector == sector)

  def getSector(row: Int, col: Int): List[Cell] = this.getSector((row, col))

  def getNotSolved = this.matrix.filter(!_.solved)

  def getPendentsNumbersFromCell(cell: Cell): List[Int] = {
    val pendentsFromRow = cellsToPendentNumbers(this.getRow(cell.row), cell)
    val pendentsFromCol = cellsToPendentNumbers(this.getCol(cell.col), cell)
    val pendentsFromSector = cellsToPendentNumbers(this.getSector(cell.sector), cell)

    (copyNumbers -- pendentsFromRow -- pendentsFromCol -- pendentsFromSector).toList.sorted
  }

  def isSolved = this.matrix.forall(_.solved)

  override def toString = {

    def rowToString(rowNumber: Int) = {
      val row = this.getRow(rowNumber)
      "|%d %d %d|%d %d %d|%d %d %d|%n".format(row(0).value, row(1).value, row(2).value, row(3).value, row(4).value,
        row(5).value, row(6).value, row(7).value, row(8).value)
    }

    val sbPuzzle = new StringBuilder("Iteraction #%,4d%n".format(SudokuPuzzle.iteraction))
    if (!this.guessCells.isEmpty) {
      sbPuzzle.append("Guesses: %s%n".format(this.guessCells.reverse))
    }
    sbPuzzle append SudokuPuzzle.upperBorder
    sbPuzzle append rowToString(0)
    sbPuzzle append rowToString(1)
    sbPuzzle append rowToString(2)
    sbPuzzle append SudokuPuzzle.middleBorder
    sbPuzzle append rowToString(3)
    sbPuzzle append rowToString(4)
    sbPuzzle append rowToString(5)
    sbPuzzle append SudokuPuzzle.middleBorder
    sbPuzzle append rowToString(6)
    sbPuzzle append rowToString(7)
    sbPuzzle append rowToString(8)
    sbPuzzle append SudokuPuzzle.lowerBorder

    sbPuzzle.toString
  }

  def copyWithGuess(guess: Cell) = {
    val copyValues = this.matrix.map(_.value)

    val result = new SudokuPuzzle(copyValues, guess :: this.guessCells)
    
    result.getRow(guess.row).apply(guess.col).value = guess.value

    result
  }

  def nextIteraction {
    SudokuPuzzle.iteraction += 1
    super.publish(SudokuPuzzleIteractionEvent)
  }

}

object SudokuPuzzle {

  private val line = "-" * 5
  private val upperBorder = "/" + line + "+" + line + "+" + line + "\\" + "\n"
  private val middleBorder = "|" + line + "+" + line + "+" + line + "|" + "\n"
  private val lowerBorder = "\\" + line + "+" + line + "+" + line + "/"

  private val originalNumbers = (1 to 9)

  private var iteraction = 0

  def apply(str: String) = new SudokuPuzzle(str.map(_.toInt - '0'))

}

