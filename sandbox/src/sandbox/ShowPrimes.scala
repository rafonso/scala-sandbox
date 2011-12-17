package sandbox

object ShowPrimes {
  
  def isPrime(i: Int, priorPrimes: List[Int]): Boolean = priorPrimes match {
    case Nil => true	// Chegou ao final da lista
    case prime :: otherPrimes if(Math.sqrt(prime) > i) => true	// O primo corrente é maior que a raiz de i.
    case prime :: otherPrimes if(i % prime == 0) => false	// É divisível por um número primo
    case _  => isPrime(i, priorPrimes.tail) // Compara com o próximo Primo
  }
  
  def getPrimes(i: Int, priorPrimes: List[Int], max: Int): List[Int] = {
    if(i >= max) {
      priorPrimes
    } else if(isPrime(i, priorPrimes)) {
      println(i)
      getPrimes(i + 2, priorPrimes ::: List(i), max)
    } else {
      getPrimes(i + 2, priorPrimes, max)
    }
  } 
  
  
  def sumPrimes(i: Int, priorPrimes: List[Int], max: Int): Int = {
    if(i >= max) {
      0
    } else if(isPrime(i, priorPrimes)) {
      //println(i) 
      i + sumPrimes(i + 2, priorPrimes ::: List(i), max)
    } else {
      sumPrimes(i + 2, priorPrimes, max)
    }
  }
  
  def sumPrimes(i: Int, priorPrimes: List[Int], accumuledSum: Long, max: Int): Long = {
    if(i >= max) {
      accumuledSum
    } else {
      val prime = isPrime(i, priorPrimes)
      val item = if(prime) List(i) else Nil
      val parcel = if(prime) i else 0
      if(prime) {
       // println(i)
      }
      sumPrimes(i + 2, priorPrimes ::: item, accumuledSum + parcel, max)
    }
  }

  def main(args : Array[String]) : Unit = {
    val max: Int = Integer.parseInt(args(0))
    
    //val primes = getPrimes(3, 2 :: Nil, max)
    
    //println(primes)
    val t0 = System.currentTimeMillis
    val sum = sumPrimes(3, 2 :: Nil, 2, max)
    val deltaT = System.currentTimeMillis - t0
      //primes.foldLeft(0)(_ + _)
//    println("========================================================")
    println("SUM = " + sum)
    println("Total Time: " + deltaT + " ms")
    
  }
}
