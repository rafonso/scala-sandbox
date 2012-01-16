package sudoku

import scala.collection.mutable.Publisher
import scala.collection.mutable.HashMap
import scala.collection.mutable.MultiMap
import scala.collection.mutable.Set
import scala.collection.mutable.Stack
import scala.deprecated

////////////////////
// PUZZLE EVENTS
////////////////////

/**
 * Represents a Event that ocurred in a Puzzle.
 */
sealed trait SudokuPuzzleEvent extends SudokuEvent

/**
 * Indicates a new puzzle itteraction.
 */
case object SudokuPuzzleIteractionEvent extends SudokuPuzzleEvent

case class SudokuPuzzle(val matrix: List[Cell], var iteraction: Int = 0, val guessesCells: Stack[Cell] = new Stack[Cell]) extends SudokuType {
  assert(matrix.size == 81)

  def this(values: Seq[Int]) = this(values.zipWithIndex.map({ case (v, i) => new Cell(i / 9, i % 9, v) }).toList)

  type Pub <: SudokuPuzzle

  private def copyNumbers: Set[Int] = SudokuPuzzle.originalNumbers.foldLeft(Set.empty[Int])((set, x) => set + x)

  private def cellsToPendentNumbers(cells: Seq[Cell], cell: Cell): Seq[Int] = cells.filter(_.solved).map(_.value.get)

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

  def addGuessCell(guess: Cell) = this.guessesCells.push(guess)

  def removeLastGuess = {
    lastGuess match {
      case Some(guess) => {
        this.matrix
          .filter(cell =>
            cell.getGuessCell match {
              case Some(`guess`) => true
              case _             => false
            })
          .foreach(_.cleanValue)
        this.guessesCells.pop()
      }
      case None => throw new SudokuException("There is not guess values to remove", this)
    }
  }

  def lastGuess: Option[Cell] = if (this.guessesCells.isEmpty) None else Some(this.guessesCells.top)

  override def toString = {

    def cellToString(row: List[Cell], col: Int): Any = row(col).value.getOrElse(".")

    def rowToString(rowNumber: Int) = {
      val row = this.getRow(rowNumber)
      "|%s %s %s|%s %s %s|%s %s %s|%n".format(
        cellToString(row, 0), cellToString(row, 1), cellToString(row, 2),
        cellToString(row, 3), cellToString(row, 4), cellToString(row, 5),
        cellToString(row, 6), cellToString(row, 7), cellToString(row, 8))
    }

    val sbPuzzle = new StringBuilder("Iteraction #%,4d%n".format(this.iteraction))
    if (!this.guessesCells.isEmpty) {
      sbPuzzle.append("Guesses: %s%n".format(this.guessesCells))
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

  def nextIteraction {
    this.iteraction += 1
    super.publish(SudokuPuzzleIteractionEvent)
  }

  def getIteraction = this.iteraction

}

object SudokuPuzzle {

  private val line = "-" * 5
  private val upperBorder = "/" + line + "+" + line + "+" + line + "\\" + "\n"
  private val middleBorder = "|" + line + "+" + line + "+" + line + "|" + "\n"
  private val lowerBorder = "\\" + line + "+" + line + "+" + line + "/"

  private val originalNumbers = (1 to 9)

  //  private var iteraction = 0

  def apply(str: String) = new SudokuPuzzle(str.map(_.toInt - '0'))

}