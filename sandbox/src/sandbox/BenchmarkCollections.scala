package sandbox

import scala.testing.Benchmark
import scala.collection.GenSeq

abstract class BenchmarkCollections extends Benchmark {

  private val size = 1000000

  protected val range = (1 to size)
  
  def getSeq: GenSeq[Int]

  def run = getSeq.partition(_ % 2 == 0)
}

object BenchmarkSeq extends BenchmarkCollections {

  def getSeq = range

}

object BenchmarkPar extends BenchmarkCollections {

  def getSeq = range.par

}