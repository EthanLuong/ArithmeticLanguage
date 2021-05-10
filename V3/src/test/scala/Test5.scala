import org.junit.Test
import org.junit.Assert._
import Main.Expression._
import Main.{Class, ClassVar, CreateNew, ELSERUN, Extends, GREATEREQUAL, IFTRUE, Partial, THENEXECUTE, VarType, env, eval}
class Test5 {
  @Test def testPartialMethodInvocations(): Unit = {
    Class("Derived", Extends(Class("Base")))
    Extends(Class("Derived"), Class("Base"))
    Class("Derived", List(ClassVar("v1", VarType("int")), ClassVar("v2", VarType("string"))))
    val instance = CreateNew(Class("Derived"))
    val result = instance.InvokePartialMethod("m1", List(("p1", 1), ("p2", "howdy!")))
    assertEquals(Multiply(Value(15),Variable("var")),result)
  }
}