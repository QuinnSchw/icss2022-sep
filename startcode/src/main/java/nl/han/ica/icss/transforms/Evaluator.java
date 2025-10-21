package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new LinkedList<>();

        applyStyleSheet(ast.root);

    }

    private void applyStyleSheet(Stylesheet node) {


        applyStylerule((Stylerule) node.getChildren().get(0)); //--> wij moeten dit niet zo doen aangezien wij meer dan 1 kind hebben.
    }

    private void applyStylerule(Stylerule node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Declaration) {
                applyDeclaration((Declaration) child);
            }
        }
    }

    private void applyDeclaration(Declaration node) { //Je wil de expression berekenen
        node.expression = evalExpression(node.expression);
    }

    private Literal evalExpression(Expression expression) { //--> in ons geval niet goed, want het kan vanalles zijn. Elke soort literal.(was eerst PixelLiteral TODO
        if (expression instanceof PixelLiteral) {
            return (PixelLiteral) expression;

        } else if (expression instanceof PercentageLiteral) {
            return (PercentageLiteral) expression;

        } else if (expression instanceof ColorLiteral) {
            return (ColorLiteral) expression;

        } else if (expression instanceof AddOperation) {
            return evalAddOperation((AddOperation) expression);

        } else if (expression instanceof SubtractOperation) {
            return evalSubtractOperation((SubtractOperation) expression);

        } else if (expression instanceof MultiplyOperation) {
            return evalMultiplyOperation((MultiplyOperation) expression);

        }
        return (Literal) expression;
    }
//    private PixelLiteral evalExpression(Expression expression) { //--> in ons geval niet goed, want het kan vanalles zijn. Elke soort literal.(was eerst PixelLiteral TODO
//        if(expression instanceof PixelLiteral){
//            return (PixelLiteral) expression;
//        } else{
//            return evalAddOperation((AddOperation)expression);
//        }
//    }

    private Literal evalAddOperation(AddOperation expression) {
        Literal left = evalExpression(expression.lhs);
        Literal right = evalExpression(expression.rhs);

        if (left instanceof PixelLiteral && right instanceof PixelLiteral) {
            PixelLiteral l = (PixelLiteral) left;
            PixelLiteral r = (PixelLiteral) right;
            return new PixelLiteral(l.value + r.value);
        }

        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral) {
            PercentageLiteral l = (PercentageLiteral) left;
            PercentageLiteral r = (PercentageLiteral) right;
            return new PercentageLiteral(l.value + r.value);
        }

        return left;
    }

    private Literal evalSubtractOperation(SubtractOperation expression) {
        Literal left = evalExpression(expression.lhs);
        Literal right = evalExpression(expression.rhs);

        if (left instanceof PixelLiteral && right instanceof PixelLiteral) {
            PixelLiteral l = (PixelLiteral) left;
            PixelLiteral r = (PixelLiteral) right;
            return new PixelLiteral(l.value - r.value);
        }

        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral) {
            PercentageLiteral l = (PercentageLiteral) left;
            PercentageLiteral r = (PercentageLiteral) right;
            return new PercentageLiteral(l.value - r.value);
        }
        return left;
    }

    private Literal evalMultiplyOperation(MultiplyOperation expression) {
        Literal left = evalExpression(expression.lhs);
        Literal right = evalExpression(expression.rhs);

        if (left instanceof PixelLiteral && right instanceof PixelLiteral) {
            PixelLiteral l = (PixelLiteral) left;
            PixelLiteral r = (PixelLiteral) right;
            return new PixelLiteral(l.value * r.value);
        }

        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral) {
            PercentageLiteral l = (PercentageLiteral) left;
            PercentageLiteral r = (PercentageLiteral) right;
            return new PercentageLiteral(l.value * r.value);
        }
        return left;
    }
}
