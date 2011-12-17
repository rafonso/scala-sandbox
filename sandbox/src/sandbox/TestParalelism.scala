package sandbox

import scala.collection.GenSeq

object TestParalelism extends App {

  /*
   * GenSeq é a classe comum entre Seq e ParSeq.
   */
  def run(seq: GenSeq[Int]) {
    val t0 = System.currentTimeMillis
    val (even, odd) = seq.partition(_ % 2 == 0)
    val deltaT = System.currentTimeMillis - t0

    println("%10s: %,8d ms".format(seq.getClass.getSimpleName, deltaT))
  }

  val size = args(0).toInt

  println("Gerando coleção original")
  val range = (1 to size)
  println("Coleção original gerada: %,d items".format(range.size))
  run(range)
  run(range.par)
}