import scala.collection.mutable
import Main.Expression._

import scala.collection.mutable.ListBuffer
object Main extends App {
  //  (3+2)*(4*7)
  type Environment = scala.collection.mutable.Map[String, Int]
  type EvaluateEnvironmentFunction = Environment ?=> Int

  given env as Environment = scala.collection.mutable.Map("var" -> 4)

  var Scopes: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty
  var scope: String = ""
  var scopesmap: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty
  var scopeslists: scala.collection.mutable.ListBuffer[String] = scala.collection.mutable.ListBuffer()
  var classes: ListBuffer[scala.collection.mutable.Map[String, ListBuffer[Any]]] = ListBuffer.empty
  enum Expression:
    //In function evaluates the expression after In after the first expression is evaluated
    def In(value: Expression) = {
      eval(value)
    }

    case Value(v: Int)

    case Variable(name: String)

    case Add(o1: Expression, o2: Expression)

    case Subtract(o1: Expression, o2: Expression)

    case Multiply(o1: Expression, o2: Expression)

    case Divide(o1: Expression, o2: Expression)

    case Assign(variable: Variable, o1: Expression)

    case Scope(scopename: String, o1: Expression)

    case Let(variable: Int)

  def eval(expression: Expression): EvaluateEnvironmentFunction = expression match {
    case Value(v) => v
    case Variable(someName) => getscope().getOrElse(someName, throw Exception(s"undefined name $Scopes"))
    case Add(o1, o2) => eval(o1) + eval(o2)
    case Subtract(o1, o2) => eval(o1) - eval(o2)
    case Multiply(o1, o2) => eval(o1) * eval(o2)
    case Divide(o1, o2) => if (eval(o2) == 0) {
      0
    } else {
      eval(o1) / eval(o2)
    }
    case Assign(variable, o1) => eval(Assign(variable, o1))
    case Let(variable) => variable
  }

  //gets the scope so assign and Variable() expressions can go inside the scope and change variables
  def getscope(): scala.collection.mutable.Map[String, Int] = {
    val temp: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty
    if (Scopes.isEmpty) {
      //scope is empty so make scope the environment and return it
      Scopes ++= summon[Environment].asInstanceOf[scala.collection.mutable.Map[String, Any]]
      Scopes.asInstanceOf[scala.collection.mutable.Map[String, Int]]
    }
    else if (scope == "") {
      //scope is empty so make scope the environment and return it
      Scopes ++= summon[Environment].asInstanceOf[scala.collection.mutable.Map[String, Any]]
      Scopes.asInstanceOf[scala.collection.mutable.Map[String, Int]]
    }
    else {
      var finished: Boolean = false
      //continue while loop if Scopes's keys contain scope and if finished is false 
      while (Scopes.contains(scope) == false && finished == false) {
        //check what the Scopes is and scope's relation to Scopes
        if (Scopes.values.head.isInstanceOf[Int] == false && Scopes.contains(scopeslists.head) && scopeslists.head != scope) {
          //scope is inside scopes map
          //make Scopes become Scopes(scope) and return scopes then go out of loop
          Scopes = Scopes(scope).asInstanceOf[scala.collection.mutable.Map[String, Any]]
          Scopes.asInstanceOf[scala.collection.mutable.Map[String, Int]]
          finished = true;
        }
        else if (Scopes.values.head.isInstanceOf[Int] == false && Scopes(Scopes.keys.head).asInstanceOf[scala.collection.mutable.Map[String, Any]].contains(scope)) {
          //scope is inside inner scopes map
          //make scopes become inner scopes map and return scopes then go out of loop
          Scopes = Scopes(Scopes.keys.head).asInstanceOf[scala.collection.mutable.Map[String, Any]]
          Scopes = Scopes(scope).asInstanceOf[scala.collection.mutable.Map[String, Any]]
          Scopes.asInstanceOf[scala.collection.mutable.Map[String, Int]]
          finished = true;
        }
        else if (Scopes.values.head.isInstanceOf[Int] == true && Scopes.values.last.isInstanceOf[Int] == true) {
          //scope is at inner most Scopes map
          //just return Scopes and go out of loop
          Scopes.asInstanceOf[scala.collection.mutable.Map[String, Int]]
          finished = true;
          //Scopes = Scopes(scopeslists.head).asInstanceOf[scala.collection.mutable.Map[String,Any]]
        }
      }
      //println(Scopes)
      Scopes.asInstanceOf[scala.collection.mutable.Map[String, Int]]
    }
  }

  def Assign(variable: Variable, o1: Expression): Expression = {
    //call getscope to get the scope correctly then assign the eval(o1) to the variable 
    getscope()(variable.name) = eval(o1)
    o1
  }

  val macros: scala.collection.mutable.Map[String, Expression] = scala.collection.mutable.Map.empty[String, Expression]

  //macro definition
  def Macro(macroname: String, expression: Expression): Expression = {
    //map the macroname to the Expression expression inside macros and return expression
    macros(macroname) = expression
    expression
  }

  //macro call
  def Macro(macroname: String): Expression = {
    //search macros map for macroname key and return expression if found
    macros.getOrElse(macroname, throw Exception("not found"))
  }

  //MacroName function
  def MacroName(macroname: String): String = {
    //returns macroname
    macroname
  }

  //scope definition
  def Scope(scopename: String, o1: Expression): Expression = {
    var temp: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map.empty
    //set scope to scopename
    scope = scopename
    //if scopes is empty then add scopename to scopeslists and map scopename to o1 and return it
    if (Scopes.isEmpty) {
      scopeslists += scopename
      Scopes(scopename) = o1
      o1
    }
    else {
      //check if scopename is duplicate if it is then just return o1
      if (scopeslists.contains(scopename) == true) {
        o1
      }
      else {
        //if scopename is not duplicate then add scopename to scopeslists
        scopeslists += scopename
        //use temp to make Scopes(scopename) = Scopes
        temp = Scopes
        Scopes = scala.collection.mutable.Map.empty
        Scopes(scopename) = temp
        //return o1
        o1
      }
    }
  }

  //Scope call
  def Scope(scopename: String, o1: Int): Int = {
    //set scope to scopename
    scope = scopename
    //return o1
    o1
  }

  //Scope call
  def evalScope(scopename: String, o1: Expression): Int = {
    //set scope to scopename
    scope = scopename
    //evaluate o1 and return it
    eval(o1)
  }



  import util.control.Breaks._
  //dynamic dispatch mechanism - goes though the entire classes data structure and recursively searches for a class named name and 
  //returns it if it can find it. First starts at the first class of the top level classes and checks if it is the name of the class is name, if it is then it
  //returns the class, if it isn't then it checks if that class has a nested class, if it does then it recursively calls getmyclass on 
  //the nested class. If the recursive call of getmyclass on the nested class returns a Map that isn't empty then it found the class named
  //name inside the nested class. If the recursive call returned a empty Map then there is Nested class doesn't contain a class named name.
  //Then it will call break and go back to the loop and continue searching through the top level classes. 
  //Parameters: ListBuffer[Map[String. ListBuffer]] object, String object
  //returns: Map[String, ListBuffer[]]
  def getmyclass(classs: ListBuffer[scala.collection.mutable.Map[String, ListBuffer[Any]]], name: String): scala.collection.mutable.Map[String, ListBuffer[Any]] = {
    var i = 0
    var table: scala.collection.mutable.Map[String, ListBuffer[Any]] = scala.collection.mutable.Map.empty
    //loop through all top level classes
    for (i <- 0 to classs.length-1) {
      breakable {
        //check if classs(i) is the class with name 'name'
        if (classs(i).contains(name)) {
          table = classs(i).asInstanceOf[scala.collection.mutable.Map[String, ListBuffer[Any]]]
          //classs(i) is the class with name name so return it
          return table
        }
        else {
          //classs(i) is not class with name name so check if there are any nested classes in classs(i)
          if (classs(i)(classs(i).keys.head)(2) != scala.collection.mutable.ListBuffer.empty) {
            //there is one or more nested classes in classs(i)
            //recursively call getmyclass on the list of nested classes
            table = getmyclass(classs(i)(classs(i).keys.head)(2).asInstanceOf[ListBuffer[scala.collection.mutable.Map[String, ListBuffer[Any]]]], name).asInstanceOf[scala.collection.mutable.Map[String, ListBuffer[Any]]]
            //check if table is empty map
            if(table==scala.collection.mutable.Map.empty) {
              //table is empty map which means the list of nest classes don't contain a class called name 
              // so break and return to the outer loop
              break
            }
            else{
              //table is not empty map which means table contains the class called name which is inside the list of 
              //nested classes, so return table
              return table
            }
          }
          else {
            //classs(i) doesn't contain any nested classes so break
            break
          }
        }
      }
    }
    //return empty table because getmyclass was not able to find any class named name inside classes
    //end
    return table
  }

  //base class definition - checks if class is already defined, if it is already defined 
  // then don't do anything. If it not defined then add the a class named name to classes
  def Class(name: String): String = {
    //check if classes is empty, if it is then make a new class inside classes called name
    if(!classes.isEmpty) {
      //find class called name inside classes
      var foundclass = getmyclass(classes, name)
      //check if class named name already exists
      if(foundclass != scala.collection.mutable.Map.empty)
      {
        //class named name already exists inside classes so don't do anything
        return name
      }
    }
    //create new class called name
    classes.addOne(collection.mutable.Map(name -> (scala.collection.mutable.ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer.empty, ""))))
    //return name
    return name
  }

  //derived class definition - checks if class is already defined, if it is already defined 
  //then don't do anything. If it not defined then add the a class named name to classes that 
  // extends a base class whose name is inside extend
  def Class(name: String,  extend: ListBuffer[Any]): String = {
    //check if classes is empty, if it is then make a new class inside classes called name
    if(!classes.isEmpty) {
      var foundclass = getmyclass(classes, name)
      //check if class already exists
      if(foundclass != scala.collection.mutable.Map.empty)
      {
        //class named name already exists
        // check if class named name is derived from extend(1)
        if(foundclass(name)(3) == ""){
          //if it isn't then make class named name derived from extend(1)
          foundclass(name)(3) = extend(1)
        }
        //end  
        return name
      }
    }
    //create new class called name that extends extend(1)
    classes.addOne(collection.mutable.Map(name -> (ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer.empty, extend(1)))))
    //end
    return name
  }

  //nested class definition - checks if class is already defined, if it is already defined 
  //then don't do anything. If it not defined then add the a class named name to classes that 
  // extends a base class whose name is inside extend
  def Class(name: String, NestedClass: String): String = {
    //check if classes is empty, if it is then make a new class inside classes called name
    if(!classes.isEmpty) {
      //find class named name
      var foundclass = getmyclass(classes, name)
      //check if the class named name exists if it exists then check if nested class inside it exists
      if(foundclass != scala.collection.mutable.Map.empty) {
        //check if class named name already has a nested class called NestedClass
        for (i <- 0 to foundclass(name)(2).asInstanceOf[ListBuffer[Any]].length-1) {
          if (foundclass(name)(2).asInstanceOf[ListBuffer[scala.collection.mutable.Map[String, ListBuffer[Any]]]](i).contains(NestedClass)) {
            //class named name already has a nested class called NestedClass so finish
            //end
            return name
          }
        }
        //add new nested class to class named name
        foundclass(name)(2) = ListBuffer(scala.collection.mutable.Map(NestedClass -> (ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer.empty, ""))))
        //end
        return name
      }
      else
      {
        //base class doesn't exist
        //add new base class with name name that has a nested class called NestedClass
        classes.addOne(scala.collection.mutable.Map(name -> (ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer(scala.collection.mutable.Map(NestedClass->(ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer.empty, ""), "")))))))
        //end
        return name
      }
    }
    //base class doesn't exist
    //remove extra NestedClass from top level classes
    classes.remove(classes.length-1)
    //add new base class with name name that has a nested class called NestedClass
    classes.addOne(scala.collection.mutable.Map(name -> (ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer(scala.collection.mutable.Map(NestedClass->(ListBuffer(ListBuffer.empty, ListBuffer.empty, ListBuffer.empty, ""), "")))))))
    //end
    return name
  }

  //example declaration of class variables - checks if class is already defined, if it is already defined 
  //then add classvars to class. If it not defined then create a class named name and add classvar to it
  def Class(name: String, classvars: List[Any]): String = {
    //check if classes is empty, if it is then make a new class inside classes called name
    if(!classes.isEmpty) {
      //find class called name inside classes
      var foundclass = getmyclass(classes, name)
      //check if class named name already exists
      if(foundclass != scala.collection.mutable.Map.empty)
      {
        //class named name already exists inside classes so add classvars to the class
        foundclass(name)(0) = classvars
        //end
        return name
      }
    }
    //create new class called name with class variables classvar
    classes.addOne(collection.mutable.Map(name -> (scala.collection.mutable.ListBuffer(classvars, ListBuffer.empty, ListBuffer.empty, ""))))
    //end
    return name
  }

  //first Extends definition - creates Listbuffer for derived class definition
  def Extends(classname: String): ListBuffer[Any] = {
    ListBuffer("extend", classname)
  }

  //second Extends definition - calls derived class definition with parameters derivedclassname and ListBuffer
  def Extends(derivedclassname: String, classname: String): String = {
    Class(derivedclassname, ListBuffer("extend", classname))
  }

  //parameter definition - creates a parameter map with parameter as key and value as a tuple of (paramtype, parameter value)
  def Parameter(parameter:String, paramtype:String): scala.collection.mutable.Map[String, (String, Any)] = {
    var param: scala.collection.mutable.Map[String, (String, Any)] = scala.collection.mutable.Map(parameter -> (paramtype, null))
    param
  }

  //classVar definition - creates a classVar map with classvar as key and value as a tuple of (vartype, var value)
  def ClassVar(classvar:String, vartype:String): scala.collection.mutable.Map[String, (String, Any)] = {
    var classVar: scala.collection.mutable.Map[String, (String, Any)] = scala.collection.mutable.Map(classvar -> (vartype, null))
    classVar
  }

  //MethodName definition - returns methodname
  def MethodName(methodname: String): String = {
    methodname
  }

  //ParamType definition - returns paramtype
  def ParamType(paramtype: String): String = {
    paramtype
  }

  //VarType definition - returns vartype
  def VarType(vartype: String): String = {
    return vartype
  }

  //example definition of class methods - checks if class exists if it does exists then it adds a method named methodname to the list of methods inside the class
  def Method(classname: String, methodname: String, parameterlist: List[Any], function: List[Expression]): String = {
    if(!classes.isEmpty) {
      var foundclass = getmyclass(classes, classname)
      //check if class named classname exists
      if(foundclass != scala.collection.mutable.Map.empty) {
        //add Method as map(methodname -> List(parameterlist, function)) to class method list
        foundclass(classname)(1).asInstanceOf[ListBuffer[scala.collection.mutable.Map[String, List[Any]]]].addOne(scala.collection.mutable.Map(methodname -> (List(parameterlist, function))))
        //end
        return methodname
      }
    }
    //no class named classname exists
    //end
    return methodname
  }

  //class instance - one variable called class name that is set by constructor, one method called InvokeMethod
  class classInstance (name: String) {
    var classname: String = name
    //InvokeMethod definition - it first searches for the class called name then it checks if the class is derived 
    //from any other class if it then it copies the methods from the base class and adds it to the list of methods inside
    //the derived class. Then it searches for the method methodname inside the list of methods and does every expression and
    //only returns the value of the last expression contained in the method.
    def InvokeMethod(methodname: String, parameters: List[(String, Any)]): Any = {
      var returnvar: Any = 0
      var methodlistsize = 0
      var Class: scala.collection.mutable.Map[String, ListBuffer[Any]] = getmyclass(classes, classname)
      //copy methods from base class if class extends base class
      var baseClass: scala.collection.mutable.Map[String, ListBuffer[Any]] = scala.collection.mutable.Map.empty
      if(Class(classname)(3)!="")
      {
        var baseClassname: String = Class(classname)(3).toString
        baseClass = getmyclass(classes, Class(classname)(3).toString)
        //copy methods
        Class(classname)(1) = Class(classname)(1).asInstanceOf[ListBuffer[Any]] ++ baseClass(baseClassname)(1).asInstanceOf[ListBuffer[Any]]
      }
      // find method with methodname inside class
      for (methodlistsize <- 0 to Class(classname)(1).asInstanceOf[ListBuffer[Any]].length-1) {
        //found method inside list of methods
        if (Class(classname)(1).asInstanceOf[ListBuffer[scala.collection.mutable.Map[String, ListBuffer[Any]]]](methodlistsize).contains(methodname)) {
          var i = 0
          //evaluate every expression in list of expressions and return last expression
          for (i <- 0 to Class(classname)(1).asInstanceOf[ListBuffer[scala.collection.mutable.Map[String, List[List[Expression]]]]](methodlistsize)(methodname)(1).length-1) {
            //get to last expression in expression list
            returnvar = eval((Class(classname)(1).asInstanceOf[ListBuffer[scala.collection.mutable.Map[String, List[List[Expression]]]]](methodlistsize)(methodname)(1)(i)).asInstanceOf[Expression])
          }
          //returns the value of the last expression contained in the method
          //end
          return returnvar
        }
      }
      //method methodname can't be found inside classprintln("Can't find method")
      //end
      return returnvar
    }
  }

  //CreateNew Definition - creates and declares new instance of class classInstance and returns it
  def CreateNew(name: String): classInstance = {
    //create new instance of classInstance and return it
    var classinst = new classInstance(name)
    //end
    return classinst
  }

  def Partial(partialExp:Expression): Expression = partialExp match{
    case Value(v) => partialExp
    case Variable(someName) => partialExp
    case Add(o1, o2) =>
      if(o1.isInstanceOf[Value] && o2.isInstanceOf[Value]){
        return Value(eval(partialExp))
      }
      partialExp
    case Multiply(o1, o2) =>
      val left = Partial(o1)
      val right = Partial(o2)
      if(left.isInstanceOf[Value] && right.isInstanceOf[Multiply]){
        val test = right.asInstanceOf[Multiply]
        if(test.o1.isInstanceOf[Value]){
          println("HERE")
          return Multiply(Value(eval(Multiply(left, test.o1))), test.o2)
        }
        else{
          return Multiply(left, right)
        }

      }
      Multiply(left,right)
    case Assign(variable, o1) => partialExp
    case Let(variable) => partialExp
  }


  //example definition of a macro
  Macro(MacroName("someName"), Multiply(Add(Variable("var"), Value(1)), Value(3)))
  //examples of a variable assignment with scope
  Assign(Variable("somevar"), Add(Variable("var"), Macro("someName")))
  //Let construct
  Let(Assign(Variable("var2"), Add(Variable("var"), Macro("someName"))) In Add(Variable("var2"), Value(1)))
  println(Macro("someName"))


  print(Partial(Multiply(Value(3), Multiply(Value(5), Variable("var")))))


  //println(eval(Variable("var2")))
  //examples of scope definition and use
  //Scope("scopename", Scope("othername", Assign(Variable("poop"),Value(5))))
  //examples of scope use
  //println((Scope("scopename", evalScope("othername", Variable("somevar")))))
  //example definitions of a class
  //  Class("Base")
  //  Class("Derived", Extends(Class("Base")))
  //Extends(Class("Derived"), Class("Base"))
  //example declaration of class variables
  //  Class("Derived", List(ClassVar("v1", VarType("int")), ClassVar("v2", VarType("string"))) )
  //Method(Class("Base"), MethodName("m1"), List(Parameter("p1", ParamType("int")), Parameter("p2", ParamType("string"))), List(Assign(Variable("somevar"), Add(Variable("var"), Macro("someName"))), Let(Assign(Variable("var2"), Add(Variable("var"), Macro("someName"))) In Add(Variable("var2"), Value(1)))))
  //var instance = CreateNew(Class("Derived"))
  //val result = instance.InvokeMethod("m1", List(("p1", 1), ("p2", "howdy!")))
  //println(result)
  //println(classes)
  //Class("Base", Class("Nested"))
  //println(classes)
  //Class("Nested", Class("Hi"))
  //Class("Crazy")
  //println(classes)
  //Class("Crazy", Class("Nested"))
  //println(classes)
  //println(getmyclass1(classes, "Nested"))

}
