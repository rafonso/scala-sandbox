package sandbox

object TestaJuntaArrays {
// -----------------------------------------------------------------------------
  def juntar(letras: List[Char]): List[String] = {
    
    def execute(chars: List[Char], string: String): List[String] = chars match {
      case Nil => List(string)
      case letra :: outrasLetras => execute(outrasLetras, string + letra) ::: execute(outrasLetras, string)
    }
    
    execute(letras, "")
  }
  
  def main(args : Array[String]) : Unit = {
    println(juntar(List('0', '1')))
  }
}
