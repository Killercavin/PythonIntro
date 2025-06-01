"""
In Python, name shadowing refers to the situation where a local module or variable has the same name as a built-in module or variable, which can cause issues when trying to import or access the original module or variable.
"""

# name shadowing example
import math  # Importing the built-in math module
def math():  # Defining a function with the same name as the math module
    """This function shadows the built-in math module."""
    return "This is a custom math function."
# Using the built-in math module
print(math.sqrt(16))  # This will raise an error because the math module is shadowed by the function
# To avoid name shadowing, it's better to use a different name for the function

# Example of avoiding name shadowing
def custom_math():  # Renaming the function to avoid shadowing
    """This function does not shadow the built-in math module."""
    return "This is a custom math function."

'''
In Python, a built-in module is a set of functions, classes, or variables that are made available to the Python interpreter by the Python core team.
'''