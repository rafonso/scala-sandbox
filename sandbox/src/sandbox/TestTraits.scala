package sandbox

// Definição dos traits

trait Base {

  println("Criando Base")

  def emBase = "Base.emBase"

  def emFraco = "Base.emFraco";

  def emForte = "Base.emForte";

  def emTodos = "Base.emTodos"

  def abs: String
}

trait Fraco extends Base {

  println("Criando Fraco")

  override def emFraco = "Fraco.emFraco"

  override def emTodos = "Fraco.emTodos"

  def abs = "Fraco.abs"
}

trait Forte extends Base {

  println("Criando Forte")

  override def emForte = "Forte.emForte"

  override def emTodos = "Forte.emTodos"

  override def abs = "Forte.abs"
}

// Definição da Classes

class BaseImpl extends Base {
  def abs = "BaseImpl.abs"
}

class FracoImpl extends Fraco

class ForteImpl extends Forte

class BaseFraco extends Base with Fraco

class FracoBase extends Fraco with Base

class BaseForte extends Base with Forte

class ForteBase extends Forte with Base

class FracoForte extends Fraco with Forte

class ForteFraco extends Forte with Fraco {
  override def abs = "ForteFraco.abs"
}

class BaseFracoForte extends Base with Fraco with Forte

class BaseForteFraco extends Base with Forte with Fraco {
  override def abs = "BaseForteFraco.abs"
}

class FracoBaseForte extends Fraco with Base with Forte

class FracoForteBase extends Fraco with Forte with Base

class ForteBaseFraco extends Forte with Base with Fraco {
  override def abs = "ForteBaseFraco.abs"
}

class ForteFracoBase extends Forte with Fraco with Base {
  override def abs = "ForteFracoBase.abs"
}

// Aplicação

object TestTraits extends App {

  def showClass(obj: Base) {
    println("-" * 20)
    println(obj.getClass().getSimpleName())
    println(obj.emBase)
    println(obj.emFraco)
    println(obj.emForte)
    println(obj.emTodos)
    println(obj.abs)
    println("=" * 20)
  }

  showClass(new BaseImpl)
  showClass(new FracoImpl)
  showClass(new ForteImpl)
  showClass(new BaseFraco)
  showClass(new FracoBase)
  showClass(new BaseForte)
  showClass(new ForteBase)
  showClass(new FracoForte)
  showClass(new ForteFraco)
  showClass(new BaseFracoForte)
  showClass(new BaseForteFraco)
  showClass(new FracoBaseForte)
  showClass(new FracoForteBase)
  showClass(new ForteBaseFraco)
  showClass(new ForteFracoBase)
}