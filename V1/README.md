# Ethan Luong - CS 476 HW 1
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


