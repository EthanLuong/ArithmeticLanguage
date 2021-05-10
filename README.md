# ArithmeticLanguage
Arithmetic language with support for assigning variables, creating macros and use of scope.
Created for CS 476

## Prerequisites
This program can be compiled and ran with SBT. The main program can be run with sbt clean compile run and the tests can be run with sbt clean compile test.
This program is also compatible with IntelliJ and can be imported and ran.

## Usage
Every keyword in this language is considered an expression so that anywhere there is an Expression it can be replaced by any other keyword.
The main function of this language is eval which will take an expression, evaluate it and then return the result. Order of operations is determined by what expression comes first. Innermost expressions will be evaluated first.

## Keywords
### Value(x:Int)
The most basic expression. Returns x.

### Add(x:Expression, y:Expression)
Returns the result of adding the evaluation of x and the evaluation of y. (x + y)

### Subtract(x:Expression, y:Expression)
Returns the result of subtracting the evaluation of y from the evaluation of x. (x - y)

### Multiply(x:Expression, y:Expression)
Returns the result of multiplying the evaluation of x and the evaluation of y. (x * y)

### Variable(name:String)
Returns the integer corresponding to the variable name in the current scope.

### AssignVar(name:String, y:Expression)
Used to declare and assign the evaluation of y to a variable name in the current scope.

### Let(x:AssignVar, y:Expression)
Used to declare and assign a variable in the local scope to be used in expression y.

### Macro(name:String)
Returns the expression corresponding to the macro name.

### MacroAssign(name:String, y:Expression)
Used to declare and assign the expression y to the macro name.

### Scope(x:Expression, name:ScopeName)
Declares that the expression x will be evaluated in the scope with the name ScopeName. Multiple variables with the same name will be taken with the innermost scope. If there is no variable matching in the current scope it will look to the outermost scopes until it finds the matching variable or throws an exception.

### ScopeName(name:String)
Used to identify a scope. Leaving the string empty will result in an anonymous scope.

## Examples

(2+5)-(4*2)
eval(Subtract(Add(Value(2),Value(5)),Multiply(Value(4),Value(2)))

x = 5
eval(AssignVar(Variable("x"), Value(5)))


# Classes
The initialization of classes, their members, and their methods will be done using the init function.
The init function takes a single parameter of ClassInit() which will have different subclasses.

### Class(name:String, x:ClassInit() = NoClass())
Passing this parameter into init will declare a class with the name provided. 
The specifics are that it will add the name to the list of classes, set the classnumber to 0 and initialize an empty field in the class methods and class member tables.
The second argument is used for the declaration of nested classes. By default the second parameter will be NoClass. The initialization of the class will only change if the second parameter is of type NestedClass()

### Extends(superC:String, subC:String)
This will declare a new class of name subC. It will also add entry to the class super table that maps subC to superC. This allows the sub class to inherit the methods and members of the superclass when an instance of the class is created.

### Method(x:Class, mName:String, params:List[String], body:List[Expression])
This will create a new method for an existing class. The method will be saved in the method table with name mName. Though the homework description had a specific Parameter type that held the name and type of the parameter, since my program only handles computation of integers, it is assumed that the type will be of Int. This allowed me have the parameter list be of type String instead of Parameter.

### Variables(x:Class, vars:List[String])
This will declare the variables for an existing class. Like the method parameter, it takes a list of String as the variables since it is assumed that the variables will be Integers. The list of variables will be saved in the class variables table.

### NestedClass(x:Class, inner:ClassInit = NoClass())
Used to declare a nested class. Like Class, there is a default parameter that is NoClass by default. This allows for declaration of nested classes inside of nesnted classes.

### NoClass()
Used to represent nulltype of ClassInit(). Made because returning None, Null, Nothing did not work for me.

# Design
The bulk of the functionality of this homework comes from the two classes InfoClass and UserClass.
InfoClass uses a shared object to keep track of information regarding the different classes
This includes declared classes, class members, class methods, superclasses, outer classes, and class numbers.
The majority of the methods in InfoClass are getters and setters that interact with this shared object.

When a user calls CreateClass, it creates a new instance of UserClass and returns it.
The creation of the instance is where most of the initialization is.
Every UserClass will first create its own instance of InfoClass to be able to get information about itself.
It will get its superclass, methods, variables, and id using the InfoClass methods.
If the class is a child class, it will inherit the methods and variables of its superclass.
If it is a inner class, it will also inherit the methods. The difference between a child and inner class is that the inner class will not inherit the variables and instead declares them in their own scope.
Next it will create the correct scopes and initialize every variable it has to 0.

The InvokeMethod follows these steps
1. It looks for the method name in the method table and throws an exception if it doesn't exist
2. It gets the parameters and expressions from the method table and checks if the given parameters match the method parameters
3. It creates a local scope using the method name and class ID and initializes the parameters as variables in that scope
4. It prepends the local scope to the scope list. Scopes will be resolved from method scope, to current class scope, to outer class scopes.
5. It calls the Arithmetic.eval function on all expressions in the method and returns the last result

Some notes about how specific things were implemented:
- A child class is allowed to override methods
- If a child class has the same name member as its super, there will only be one variable of that name
- Though outer class variables are initialized, there is currently no way to access them
# Version 3

Created by: Branden Hernandez, Brian Huang, Ethan luong

## Partial Evaluation
In our program, partial evaluation will reduce Add and Multiply expressions to their simplest forms.
In the case of Add, it will convert it into a Value expression if the expression is adding two Values.
In the case of Multiply, if a multiply expression encapsulates another multiply expression, it will use the operator associativity to simplify into one Multiply expression.

Partial evaluation is done by passing an expression to the Partial function.
If the expression is already partially evaluated, it will return the same expression.
The function works recursively, starting the reduction from the inside and ending at the outside expressions.
The base case would be finding an expression that is not able to be partially evaluated (Value, Variable, Macro, etc.)
When the function finds an Add or Multiply expression, it will first call itself on the expressions left and right parameters.
This ensures that the end result will be fully partially evaluated.
Once those calls have returned, it will see if the expression is able to be partially evaluated and if it is, it will partially evaluate it.

    EXAMPLES:

    2+4+5+2 -> 6+7 -> 17
    
    Partial(Add(Add(Value(2), Value(4),Add(Value(5), Value(2)))

    3*((5+1) * var) -> 3 * (6 * var) -> 18 * var
    
    Partial(Multiply(Value(3), Multiply(Add(Value(5), Value(1)), Variable("var"))))
    
    
    OUTPUT: Value(13)
            Multiply(Value(18), Variable("var")

## The Equality Operation
The equality operation determines if one operation is greater than, less than, or equal to another operand.

GREATEREQUAL  greater than or equal to

LESSEREQUAL   less than or equal to

EQUALEQUAL    equal to

    GREATEREQUAL(Multiply(Value(15), Value(10)),Add(Value(2), Value(12)))
    
    LESSEREQUAL(Multiply(Value(15), Value(10)),Add(Value(2), Value(12)))
    
    EQUALEQUAL(Multiply(Value(15), Value(10)),Add(Value(2), Value(12)))
    
    
    
    OUTPUT: TRUE
            FALSE
            FALSE
    

## Control Flow Statement
### Conditional IFTRUE-THENEXECUTE constructs
The IFTRUE-THENEXECUTE statement is the control flow statement that tells your program to execute a certain section of code only if a particular test evaluates to true. For example,

    eval(Assign(Variable("lhs"), IFTRUE(GREATEREQUAL(Multiply(Value(15), Variable("var")),Add(Value(2), Variable("var2"))))
  
        THENEXECUTE(
        
          Assign(Variable("somevar"), Add(Variable("var"), Value(3)))
          
        )

### Conditional IFTRUE-THENEXECUTE-ELSERUN constructs
The IFTRUE-THENEXECUTE-ELSERUN statement provides a secondary path of execution when an IFTRUE clause evaluates to false. Taking the example from above,
    
    eval(Assign(Variable("lhs"), IFTRUE(GREATEREQUAL(Multiply(Value(15), Variable("var")),Add(Value(2), Variable("var2"))))
            THENEXECUTE(
              Assign(Variable("somevar"), Add(Variable("var"), Value(3)))
            )
            ELSERUN (
              Assign(Variable("somevar"), Add(Variable("var"), Value(4)))
              //Class("Derived", Extends(Class("Base")))
              //Extends(Class("Derived"), Class("Base"))
              //Class("Derived", List(ClassVar("v1", VarType("int")), ClassVar("v2", VarType("string"))))
              //val instance = CreateNew(Class("Derived"))
              //val result = instance.InvokeMethod("m1", List(("p1", 1), ("p2", "howdy!")))
            )
          )
        )
## Partial Assign
Partial() with Assign() allows variables to be mapped not only to values but also to expressions as a result of partial evaluation.

    Partial(Assign(Variable("var3"), Partial(Multiply(Value(3), Multiply(Value(5), Variable("var"))))))
    var3 is assigned to Multiply(Value(15), Variable("var")) not 60

### Explanation:
##### Partial(Assign()) calls
##### case Assign(variable, o1) which returns
##### PartialAssignhelper(variable, o1) which calls
##### getscopepartial()(variable.name) = o1 where variable.name is "var3" and o1 = Multiply(Value(15), Variable("var")) and returns o1
##### getscopepartial() returns Scopes which is environment table at scope so Partial(assign()) and Partial(Variable()) expressions can go inside the scope and assign or update variables to expressions and get expressions from variables


## Partial evaluating method calls with dynamic dispatch mechanism
### InvokePartialMethod purpose
InvokePartialMethod() partially evaluates method calls in presence of the dynamic dispatch mechanism by partially evaluating all methods in the subsumption hierarchy.
### InvokePartialMethod functionality
InvokePartialMethod first searches for the class called name then it checks if the class is derived from any other class if it then 
it copies the methods from the base class and adds it to the list of methods inside the derived class. Then it searches for the 
method methodname inside the list of methods and evaluates expression partially and only returns the partially evaluated expression of the 
last expression contained in the method.


#Five Junit Tests
###testPartial test
####tests Partial Evaluation logic and reduction rules
###testFALSEConditional test
####tests if ELSERUN correctly runs when IFTRUE() is false using GREATEREQUAL, LESSEREQUAL, EQUALEQUAL
###testTRUEConditional test
####tests if THENEXECUTE correctly executes when IFTRUE() is true using GREATEREQUAL, LESSEREQUAL, EQUALEQUAL
###testPartialAssign test
####tests if Partial(Assign()) correctly maps variables to expressions as a result of partial evaluation.
###testPartialMethodInvocations test
####tests if InvokePartialMethod correctly partially evaluates every method in method list of class and returns last partially evaluated expression in method list.


