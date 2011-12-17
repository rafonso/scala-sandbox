package sudoku

/**
 * Represents a Sudoku Cell with its respective Row, Column and value
 * @param row Cell row
 * @param col Cell column
 * @param v Original Value. If 0, it is not solved.  
 */
case class Cell(row: Int, col: Int, var v: Int) {

  /**
   * <code>true</code> if original value is not 0
   */
  val original = this.solved

  /**
   * 
   */
  val sector = (row / 3, col / 3)

  def solved = (value > 0)

  def value = v

  def value_=(newValue: Int) {
    Predef.assume(!this.original, "Pre defined cell: %s".format(this))

    this.v = newValue
  }

  override def hashCode: Int = 41 * (41 + this.row) + this.col

  override def equals(other: Any) = other match {
    case Cell(r, c, _) => (this.row == r) && (this.col == c)
    case _             => false
  }

}
