
object Main extends App {
//  (3+2)*(4*7)
  
  enum Expression:
    case Value(v:Int)
    case ScopeName(name:String)
    case Scope(o:Expression, name:ScopeName)
    case Variable(name:String)
    case Subtract(o1:Expression, o2:Expression)
    case Add(o1:Expression, o2:Expression)
    case Multiply(o1:Expression, o2:Expression)
    case MacroAssign(name:Macro, o1:Expression)
    case Macro(name:String)
    case AssignVar(o1:Variable, o2:Expression)
    case Let(o1:AssignVar, o2:Expression)
    case Empty()
  
  import Main.Expression._
  import scala.collection.mutable.Map
  import scala.collection.mutable.ListBuffer

  type Environment = Map[String, Int]
  var variables = scala.collection.mutable.Map[String,Map[String,Int]]("default" -> Map[String,Int]())
  var macros = scala.collection.mutable.Map[String,Expression]()
  val r = scala.util.Random
  
  val result:Expression = AssignVar(Variable("Test"), Value(2))
  
  def getName(expression: Expression):String = expression match{
    case Variable(name) => name
    case Macro(name) => name
    case ScopeName(name) => name
  }
  
  def assignExpression(expression: Expression, scopeList:ListBuffer[String], optional:Expression = Empty()):Int = expression match{
    case Macro(name) => 
      if macros.contains(getName(expression)) then{
        macros = macros - getName(expression)
      }
      macros = macros + (getName(expression) -> optional)
      return 1
    case Variable(name) =>
      variables(scopeList.head) = variables(scopeList.head) + (getName(expression) -> eval(optional, scopeList))
      variables(scopeList.head)(getName(expression))
    case AssignVar(o1, o2) => assignExpression(o1, scopeList, o2)
  }
  
  def eval(expression: Expression, scopeList:ListBuffer[String] = ListBuffer("default")):Int = expression match {
    case Value(v) => v
    case Variable(someName) =>  
      var i = 0;
      while((i < scopeList.size) && (!variables.get(scopeList(i)).get.contains(someName)) ){
        i += 1
      }
      if(i >= scopeList.size){
        throw Exception("undefined variable")
      }
      
      variables.getOrElse(scopeList(i), throw Exception("undefined scope")).getOrElse(someName, throw Exception(s"undefined variable $someName"))
    case Add(o1, o2) => eval(o1, scopeList) + eval(o2, scopeList)
    case Subtract(o1, o2) => eval(o1, scopeList) - eval(o2, scopeList)
    case Multiply(o1, o2) => eval(o1, scopeList) * eval(o2, scopeList)
    case Macro(name) => eval(macros.getOrElse(name, throw  Exception(s"undefined macro $name")), scopeList)
    case Let(o1, o2) => assignExpression(o1, scopeList)
      eval(o2)
    case MacroAssign(o1, o2) => assignExpression(o1, scopeList, o2)
    case AssignVar(o1, o2) => assignExpression(o1, scopeList, o2)
    case Scope(o1, name) => 
      if(getName(name) == ""){
        val random = r.nextInt.toString
        scopeList.prepend(random)
        variables = variables + (random -> Map[String,Int]())
      }
      else{
        if(!variables.contains(getName(name))){
          variables = variables + (getName(name) -> Map[String,Int]())
        }
        scopeList.prepend(getName(name)) 
      }
      val x = eval(o1, scopeList)
      scopeList.remove(0)
      return x
    
  }

}
