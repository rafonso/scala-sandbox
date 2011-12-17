package sandbox

import scala.collection.mutable._

object TestPrimesScala {
  def main(args : Array[String]) : Unit = {
    val args = Array("1", "3", "10", "30", "100", "300", "1000", "3000", "10000", "30000", "100000", "300000", "1000000")
    val scalaTimes = new ListBuffer[Long]
    val javaTimes = new ListBuffer[Long]
    
    var i = 0
    for(arg <- args) {
      println("-------------------------------------------")
      println(arg)
      println("SCALA")
      val t0Scala = System.currentTimeMillis
      ShowPrimes.main(Array(arg))
      scalaTimes += (System.currentTimeMillis - t0Scala)
      
      println("JAVA")
      val t0Java = System.currentTimeMillis
      Primes.main(Array(arg))
      javaTimes += (System.currentTimeMillis - t0Java)
      
      i = i + 1
    }
    
    
    println("")
    println("")
    println("=================================================================")
    println("It\t\tScala\tJava")
    i = 0
    
    for{arg <- args} {
        printf("%6s\t%6d\t%6d%n", arg, scalaTimes.apply(i), javaTimes.apply(i))
        i = i + 1
      //println(arg + "\t\t" + scalaTimes(i) + "\t" + javaTimes(i))
    }
  }
}
