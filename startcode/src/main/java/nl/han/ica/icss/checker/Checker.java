package nl.han.ica.icss.checker;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import java.util.HashMap;
import java.util.LinkedList;


public class Checker {
    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

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
        } else if (expression instanceof BoolLiteral){
            return ExpressionType.BOOL;
        }else if (expression instanceof ScalarLiteral) {
            return ExpressionType.SCALAR;
        } else if (expression instanceof VariableReference) {
            VariableReference ref = (VariableReference) expression;
            for (HashMap<String, ExpressionType> scope : variableTypes) { //Loopt door hashmap heen en returnt de ExpressionType die bij de gevonden key hoort
                if (scope.containsKey(ref.name)) {
                    return scope.get(ref.name);
                }
            }
            return ExpressionType.UNDEFINED;

        } else if (expression instanceof Operation) {
            Operation op = (Operation) expression;
            ExpressionType left = determineType(op.lhs);
            ExpressionType right = determineType(op.rhs);
            ExpressionType check = checkEval(left, right, expression);

            if (check == ExpressionType.WRONGEVAL) {
                return ExpressionType.WRONGEVAL;
            }
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

    private ExpressionType checkEval(ExpressionType left, ExpressionType right, Expression expression) { //Hierin check ik of een som wel/niet kan. Zo niet dan return ik WRONGEVAL
        if (expression instanceof AddOperation) {
            if (left == ExpressionType.COLOR || right == ExpressionType.COLOR) {
                return ExpressionType.WRONGEVAL;
            }
            if (left == right) {
                return left;
            }else {
                return ExpressionType.WRONGEVAL;
            }

        } else if (expression instanceof SubtractOperation) {
            if (left == ExpressionType.COLOR || right == ExpressionType.COLOR) {
                return ExpressionType.WRONGEVAL;
            }
            if (left == right) {
                return left;
            } else {
                return ExpressionType.WRONGEVAL;
            }
        } else if (expression instanceof MultiplyOperation) {
            if(left == right){
                if(left != ExpressionType.SCALAR){
                    return ExpressionType.WRONGEVAL;
                }

            }
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
            } else if(child instanceof IfClause){
                checkIfClause((IfClause) child);
            }
        }
        variableTypes.pop();
    }

    private void checkIfClause(IfClause child) {
        ExpressionType conditionType = determineType(child.conditionalExpression);
        if (conditionType != ExpressionType.BOOL) {
            child.setError("The condition in an if-clause must be a boolean expression (TRUE/FALSE).");
        }
    }

    private void checkDeclaration(Declaration declaration) {
        ExpressionType type = determineType(declaration.expression);
        String property = declaration.property.name;
        if (property.equals("width") || property.equals("height")) {
            if (type != ExpressionType.PIXEL && type != ExpressionType.PERCENTAGE) {
                declaration.setError("Property width: not allowed");
            }
        } else if (property.equals("color") || property.equals("background-color")) {
            if (type != ExpressionType.COLOR) {
                declaration.setError("Property color: this is not allowed");
            }
        } else {
            declaration.setError("This property is not allowed");
        }
        if (type == ExpressionType.WRONGEVAL ){
            declaration.setError("These types cannot be added,subtracted or multiplied.");
        }
        if (type == ExpressionType.UNDEFINED) {
            declaration.setError("Types are undefined or can not be combined");
        }
    }
}
