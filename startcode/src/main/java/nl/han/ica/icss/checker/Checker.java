package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.LinkedList;


public class Checker {
    // --> was eerst IHANLinkedList. misschien toevoegen
    private LinkedList<HashMap<String, ExpressionType>> variableTypes; // Hashmap houdt bij welke types variabelen zijn. --> handig ook voor het constant probleem

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        variableTypes.push(new HashMap<>());
        checkStylesheet(ast.root);
        variableTypes.pop();


    }

    private void checkStylesheet(Stylesheet sheet) {
        for (ASTNode child : sheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                variableAssignmentApplier((VariableAssignment) child);
            } else if (child instanceof Stylerule) {
                checkStylerule((Stylerule) child);
            }
        }
    }

    private void variableAssignmentApplier(VariableAssignment child) {
        String varName = child.name.name;
        ExpressionType type = determineType(child.expression);

        variableTypes.peek().put(varName, type);
    }

    private ExpressionType determineType(Expression expression) {
        if (expression instanceof ColorLiteral) {
            return ExpressionType.COLOR;
        } else if (expression instanceof PercentageLiteral) {
            return ExpressionType.PERCENTAGE;
        } else if (expression instanceof PixelLiteral) {
            return ExpressionType.PIXEL;
        } else if (expression instanceof ScalarLiteral) {
            return ExpressionType.SCALAR;
        } else if (expression instanceof VariableReference) {
            VariableReference ref = (VariableReference) expression;
            for (HashMap<String, ExpressionType> scope : variableTypes) {
                if (scope.containsKey(ref.name)) {
                    return scope.get(ref.name);
                }
            }
            return ExpressionType.UNDEFINED;

        } else if (expression instanceof Operation) {
            Operation op = (Operation) expression;

            ExpressionType left = determineType(op.lhs);
            ExpressionType right = determineType(op.rhs);

            if (left == ExpressionType.UNDEFINED || right == ExpressionType.UNDEFINED) {
                return ExpressionType.UNDEFINED;
            }

            if (left == right) {
                return left;
            }

            if ((left == ExpressionType.SCALAR && right == ExpressionType.PIXEL) ||
                    (right == ExpressionType.SCALAR && left == ExpressionType.PIXEL)) {
                return ExpressionType.PIXEL;
            }

            if ((left == ExpressionType.SCALAR && right == ExpressionType.PERCENTAGE) ||
                    (right == ExpressionType.SCALAR && left == ExpressionType.PERCENTAGE)) {
                return ExpressionType.PERCENTAGE;
            }

            return ExpressionType.UNDEFINED;
        }

        return ExpressionType.UNDEFINED;
    }


    private void checkStylerule(Stylerule stylerule) {
        variableTypes.push(new HashMap<>());

        for (ASTNode child : stylerule.getChildren()) {
            if (child instanceof Declaration) {
                checkDeclaration((Declaration) child);
            } else if (child instanceof VariableAssignment) {
                variableAssignmentApplier((VariableAssignment) child);
            }
        }

        variableTypes.pop();
    }


    private void checkDeclaration(Declaration declaration) {
        ExpressionType type = determineType(declaration.expression);
        String property = declaration.property.name;
        if (property.equals("width") || property.equals("height")) {
            if (type != ExpressionType.PIXEL && type != ExpressionType.PERCENTAGE) {
                declaration.setError("Property width: color not allowed"); //--> hierdoor wordt declaration dan rood in de tree
            }
        } else if (property.equals("color") || property.equals("background-color")) {
            if (type != ExpressionType.COLOR) {
                declaration.setError("Property color: this is not allowed"); //--> je moet nakijken of iets hetgeen is wat je verwacht. Anders foutmelding.
            }
        } else {
            declaration.setError("This property is not allowed");
        }




    }
}
