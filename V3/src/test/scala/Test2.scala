import org.junit.Test
import org.junit.Assert._
import Main.Expression._
import Main.{Class, ClassVar, CreateNew, ELSERUN, Extends, GREATEREQUAL, LESSEREQUAL,EQUALEQUAL, IFTRUE, Partial, Scopes, THENEXECUTE, VarType, env, eval}
class Test2 {
  @Test def testFALSEConditional(): Unit = {
    eval(Assign(Variable("lhs"), IFTRUE(GREATEREQUAL(Multiply(Value(0), Variable("var")),Add(Value(2), Variable("var2"))))
      THENEXECUTE(
        Assign(Variable("somevar"), Add(Variable("var"), Value(3)))
      )
      ELSERUN (
      RUN(
        ()=>{
          //println("here")
          Class("Derived", Extends(Class("Base")))
          Extends(Class("Derived"), Class("Base"))
          Class("Derived", List(ClassVar("v1", VarType("int")), ClassVar("v2", VarType("string"))))
          val instance = CreateNew(Class("Derived"))
          val result = instance.InvokeMethod("m1", List(("p1", 1), ("p2", "howdy!")))
        }
      )
      )
    ))
    val instance = CreateNew(Class("Derived"))
    val result = instance.InvokeMethod("m1", List(("p1", 1), ("p2", "howdy!")))
    assertEquals(60, result)
    println(Scopes)
    assertEquals(0, eval(Variable("lhs")))
    Partial(Assign(Variable("lhs"), IFTRUE(LESSEREQUAL(Multiply(Value(2), Variable("var")),Add(Value(2), Variable("var2"))))
      THENEXECUTE(
      Assign(Variable("somevar"), Add(Variable("var"), Value(3)))
      )
      ELSERUN (
      RUN(
        ()=>{
          //println("here")
          Class("Derived", Extends(Class("Base")))
          Extends(Class("Derived"), Class("Base"))
          Class("Derived", List(ClassVar("v1", VarType("int")), ClassVar("v2", VarType("string"))))
          val instance = CreateNew(Class("Derived"))
          val result = instance.InvokeMethod("m1", List(("p1", 1), ("p2", "howdy!")))
        }
      )
      )
    ))
    println(Scopes)
    assertEquals(0, eval(Variable("lhs")))


    Partial(Assign(Variable("lhs"), IFTRUE(EQUALEQUAL(Multiply(Value(0), Variable("var")),Add(Value(2), Variable("var2"))))
      THENEXECUTE(
      Assign(Variable("somevar"), Add(Variable("var"), Value(3)))
      )
      ELSERUN (
      RUN(
        ()=>{
          //println("here")
          Class("Derived", Extends(Class("Base")))
          Extends(Class("Derived"), Class("Base"))
          Class("Derived", List(ClassVar("v1", VarType("int")), ClassVar("v2", VarType("string"))))
          val instance = CreateNew(Class("Derived"))
          val result = instance.InvokeMethod("m1", List(("p1", 1), ("p2", "howdy!")))
        }
      )
      )
    ))
    println(Scopes)
    assertEquals(0, eval(Variable("lhs")))
  }
}