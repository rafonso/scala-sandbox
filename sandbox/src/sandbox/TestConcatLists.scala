package sandbox

object TestConcatLists {
  
  def cons(         i: Int, list: List[Int], max: Int): List[Int] = if(i >= max) list else cons(         i + 1, i :: list,         max)
  	
  def append(       i: Int, list: List[Int], max: Int): List[Int] = if(i >= max) list else append(       i + 1, list ::: List(i),  max)
  
  def appendWithNil(i: Int, list: List[Int], max: Int): List[Int] = if(i >= max) list else appendWithNil(i + 1, list ::: i :: Nil, max)
  
  def test(max: Int, f: (Int, List[Int], Int) => List[Int]): Long = {
    val t0 = System.currentTimeMillis
    
    val l = f(1, Nil, max)
    println(l)
    
    System.currentTimeMillis - t0
  }
  
  def main(args : Array[String]) : Unit = {
    val max = 20
    
    println("Usando ::        -> " + test(max, cons))
    println("Usando :::       -> " + test(max, append))
    println("Usando ::: (Nil) -> " + test(max, appendWithNil))
  }
}
