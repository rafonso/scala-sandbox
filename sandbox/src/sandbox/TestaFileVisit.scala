package sandbox

import java.io._
import java.nio.file._
import java.nio.file.attribute._
import scala.collection.mutable.ListMap

class FileVisitor extends SimpleFileVisitor[Path] {

  val filesList = ListMap.empty[String, Long]

  override def visitFile(p: Path, attrs: BasicFileAttributes): FileVisitResult = {
    if (attrs.isRegularFile()) {
      val file = p.toFile
      this.filesList += (file.getName -> file.length)
    }

    FileVisitResult.CONTINUE
  }

}

object TestaFileVisit extends App {

  val path = Paths.get(args(0))
  val visitor = new FileVisitor

  Files.walkFileTree(path, visitor)

  visitor.filesList.foreach(nameSize => println("%30s \t %,d".format(nameSize._1, nameSize._2)))
  
  println(visitor.filesList.map(_._2).sum)

}