package sandbox

object Sieve {
  
  def ints(n: Int): Stream[Int] = Stream.cons(n, ints(n+1))
  
  def primes(nums: Stream[Int]): Stream[Int] = Stream.cons(nums.head, primes ((nums tail) filter (x => x % nums.head != 0)) )
  
  def main(args : Array[String]) : Unit = {
    val n = Integer.parseInt(args(0))
    println(primes(ints(2)) take n toList)
    
  }
}
