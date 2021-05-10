# Group3_cs476_Project

Branden Hernandez, Brian Huang, Ethan luong

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