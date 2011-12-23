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

  def addFilters(filters: SudokuPublisher.Mapa[Sub, Evt]) {
    filters.foreach({ case (sub, filters) => filters.foreach(this.subscribe(sub, _)) })
  }

  def addSuspendeds(suspendeds: HashSet[Sub]) {
    suspendeds.foreach(this.suspendSubscription(_))
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

object SudokuPublisher {

  type Mapa[Sub, Evt] = HashMap[Sub, Set[Evt => Boolean]] with MultiMap[Sub, Evt => Boolean]

}
