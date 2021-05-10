# Ethan_Luong_cs476_hw2

# Project Description
Program for homework 2 of CS 476. 
Language that allows creation of classes.
The classes support integer class members, expression methods, inheritance, and nested classes.

# Usage
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
