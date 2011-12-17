package sandbox

import scala.testing.Benchmark

object BenchmarkSequential extends Benchmark {

  var iter = 1

  val size = 1000000

  val range = (1 to size)

  def run {
    range.partition(_ % 2 == 0)
    println("Iteração " + iter)
    iter += 1
  }
  
}