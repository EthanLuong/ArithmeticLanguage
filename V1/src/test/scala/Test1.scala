
import org.junit.Test
import org.junit.Assert._
import Main.Expression._

class Test1 {
  //Basic Operation Testing
  @Test def t1(): Unit = {
    // 1+2 = 3
    val result = Main.eval(Add(Value(1), Value(2)))
    // 5-2 = 3
    val result2 = Main.eval(Subtract(Value(5), Value(2)))
    // 2-5 = -3
    val result3 = Main.eval(Subtract(Value(2), Value(5)))
    // 3*3 = 9
    val result4 = Main.eval(Multiply(Value(3), Value(3)))
    //(5-2)+(2*3) = 9
    val result5 = Main.eval(Add(Subtract(Value(5), Value(2)), Multiply(Value(2), Value(3))))
    assertEquals(result, 3)
    assertEquals(result2, 3)
    assertEquals(result3, -3)
    assertEquals(result4, 9)
    assertEquals(result5, 9)
    
  }
  
  //Assigning Variable and Using Variable Testing
  @Test def t2(): Unit = {
    // v1 = 2
    Main.eval(AssignVar(Variable("v1"), Value(2)))
    // v1 + 2 = 4
    val result = Main.eval(Add(Variable("v1"), Value(2)))
    //Ressigning - v1 = 4
    Main.eval(AssignVar(Variable("v1"), Value(4)))
    // v1 + 2 = 6
    val result2 = Main.eval(Add(Variable("v1"), Value(2)))
    // v2 = v1 * 4 - 16
    Main.eval(AssignVar(Variable("v2"), Multiply(Variable("v1"), Value(4))))
    // 5 + v2 = 21
    val result3 = (Main.eval(Add(Value(5), Variable("v2"))))
    // let levV = 8 in letv + 2 -> 10
    val result4 = (Main.eval(Let(AssignVar(Variable("letV"), Value(8)), Add(Variable("letV"), Value(2)))))
    
    assertEquals(result, 4)
    assertEquals(result2, 6)
    assertEquals(result3, 21)
    assertEquals(result4, 10)
    
  }
  
  //Macro Testing
  @Test def t3(): Unit = {
    //Simple expression
    // 2 + 4
    Main.eval(MacroAssign(Macro("m1"), Add(Value(2), Value(4))))
    val result1 = Main.eval(Macro("m1"))
    
    //Macro in an expression
    val result2 = Main.eval(Multiply(Macro("m1"), Value(5)))
    assertEquals(result2, 30)
    
    
  }
  
  //Scope Test
  @Test def t4(): Unit = {
    /*
    t4v1 = 2
    class testScope1{
      t4v1 = 4
      result = t4v1 + 5
    }
     */
    Main.eval(AssignVar(Variable("t4v1"), Value(2)))
    Main.eval(Scope(AssignVar(Variable("t4v1"), Value(4)), ScopeName("testScope1")))
    val result = Main.eval(Scope(Add(Variable("t4v1"), Value(5)), ScopeName("testScope1")))
    assertEquals(result, 9)
    


    
    
  }
  //Integration Test
  @Test def t5(): Unit = {
    //Increment Variable Macro
    Main.eval(AssignVar(Variable("t5v1"), Value(0)))
    Main.eval(MacroAssign(Macro("t5m1"), AssignVar(Variable("t5v1"), Add(Variable("t5v1"), Value(1)))))
    assertEquals(0, Main.eval(Variable("t5v1")))
    Main.eval(Macro("t5m1"))
    assertEquals(1, Main.eval(Variable("t5v1")))
    
    //Scopes with macros
    Main.eval(AssignVar(Variable("t5v2"), Value(4)))
    Main.eval(Scope(AssignVar(Variable("t5v2"), Value(1)), ScopeName("test5Scope1")))
    Main.eval(MacroAssign(Macro("t5m2"), AssignVar(Variable("t5v2"), Add(Variable("t5v2"), Value(1)))))
    Main.eval(Scope(Macro("t5m2"), ScopeName("test5Scope1")))
    
    
    assertEquals(4, Main.eval(Variable("t5v2")))
    assertEquals(2, Main.eval(Scope(Variable("t5v2"), ScopeName("test5Scope1"))))
    
    
    
    

  }
}