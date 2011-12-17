package sandbox

object Liker {

  private val LIKE = '%'

  implicit def stringToLiker(str: String) = {
    new {

      def toLike(before: Boolean, after: Boolean): String = {
        val sbLike = new StringBuilder

        if (before) sbLike append LIKE
        sbLike append str
        if (after) sbLike append LIKE

        sbLike toString
      }

      def toLike: String = toLike(true, true)

    }
  }

}

object Liker2 {

  private val LIKE = '%'

  class InternalLiker(str: String) {

    def toLike(before: Boolean, after: Boolean): String = {
      val sbLike = new StringBuilder

      if (before) sbLike append LIKE
      sbLike append str
      if (after) sbLike append LIKE

      sbLike toString
    }

    def toLike: String = toLike(true, true)

  }

  implicit def stringToLiker(str: String) = new InternalLiker(str)

}

object TesteLiker {

  def run(str: String, metodo: String => Unit, times: Int): Long = {
    val t0 = System.currentTimeMillis()

    (1 to times).foreach(_ => metodo(str))

    System.currentTimeMillis() - t0
  }

  def testarLiker1(str: String) {
    import Liker._

    (str.toLike)
  }

  def testarLiker2(str: String) {
    import Liker2._

    (str.toLike)
  }

  def main(args: Array[String]) {
    val str = "teste"
    val times = args(0).toInt
   

    println(run(str, testarLiker1, times))
    println(run(str, testarLiker2, times))
  }

}