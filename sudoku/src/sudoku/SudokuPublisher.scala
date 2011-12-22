/**
 *
 */
package sudoku

import scala.collection.mutable._

/**
 * @author rafael
 *
 */
trait SudokuPublisher[Evt] extends Publisher[Evt] {

  private val interceptFilters = new HashMap[Sub, Set[Filter]] with MultiMap[Sub, Filter]
  private val interceptSuspended = new HashSet[Sub]

  def getFilters = this.interceptFilters

  def getSuspendeds = this.interceptSuspended

  def addFilters(filters: HashMap[Sub, Set[Filter]] with MultiMap[Sub, Filter]) {
    filters.foreach({ case (sub, filters) => filters.foreach(super.subscribe(sub, _)) })
  }

  def addSuspendeds(suspendeds: HashSet[Sub]) {
    suspendeds.foreach(super.suspendSubscription(_))
  }

  override def subscribe(sub: Sub, filter: Filter) {
    this.interceptFilters.addBinding(sub, filter)
    super.subscribe(sub, filter)
  }

  override def suspendSubscription(sub: Sub) {
    this.interceptSuspended += sub
    super.suspendSubscription(sub)
  }

  override def activateSubscription(sub: Sub) {
    this.interceptSuspended -= sub
    super.activateSubscription(sub)
  }

  override def removeSubscription(sub: Sub) {
    this.interceptFilters -= sub
    super.removeSubscription(sub)
  }

  override def removeSubscriptions() {
    this.interceptFilters.clear
    super.removeSubscriptions();
  }

}
/*
[C](f: (SudokuPublisher.this.Sub, scala.collection.immutable.Set[Evt => Boolean]) => C)Unit <and>   
[U](f: (SudokuPublisher.this.Sub, scala.collection.mutable.Set[Evt => Boolean]) with (SudokuPublisher.this.Sub, scala.collection.immutable.Set[Evt => Boolean]) => U)Unit  cannot be applied to (() => Unit)	SudokuPublisher.scala	/sudoku/src/sudoku	line 23	Scala Problem

*/