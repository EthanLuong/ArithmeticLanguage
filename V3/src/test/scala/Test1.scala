
import org.junit.Test
import org.junit.Assert._
import Main.Expression._
import Main.{Class, ClassVar, CreateNew, Extends, GREATEREQUAL, IFTRUE, THENEXECUTE, ELSERUN, Partial, VarType, env, eval}
class Test1 {
  @Test def testPartial(): Unit = {
    assertEquals(Multiply(Value(15), Variable("var")), Partial(Multiply(Value(3), Multiply(Value(5), Variable("var")))))
    assertEquals(Multiply(Value(18), Variable("var")), Partial(Multiply(Value(3), Multiply(Add(Value(5), Value(1)), Variable("var")))))
    assertEquals(Value(13), Partial(Add(Add(Value(2), Value(4)), Add(Value(5), Value(2)))))
  }
}