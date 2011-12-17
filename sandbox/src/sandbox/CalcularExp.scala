package sandbox
import java.math.MathContext

object CalcularExp extends App {

//  val maximo = args(0).toInt
  val termos = args(1).toInt

  def exp(x: Int): BigDecimal = {

    def calculate(n: Int, factorial: Long, soma: BigDecimal): BigDecimal = {
      if (n > termos) {
        soma
      } else {
        val termo = BigDecimal(scala.math.pow(x, n)) / factorial
        println(n + " - " + termo)
        val next = n + 1
        calculate(next,  next * factorial, soma + termo)
      }
    }

    calculate(1, 1, BigDecimal(1, MathContext.UNLIMITED))
  }
  
  println(exp(args(0).toInt))

}