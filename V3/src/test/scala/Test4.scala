import org.junit.Test
import org.junit.Assert._
import Main.Expression._
import Main.{Class, ClassVar, CreateNew, ELSERUN, Extends, GREATEREQUAL, IFTRUE, Partial, THENEXECUTE, VarType, env, eval}
class Test4 {
  @Test def testPartialAssign(): Unit = {
    Partial(Assign(Variable("var3"), Partial(Multiply(Value(3), Multiply(Value(5), Variable("var"))))))
    //check actual variable
    assertEquals(60,eval(Variable("var3")))
    //check partial variable
    assertEquals(Multiply(Value(15),Variable("var")),Partial(Variable("var3")))
  }
}