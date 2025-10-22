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
        variableValues.push(new HashMap<>());
        applyStyleSheet(ast.root);
        variableValues.pop();


    }

    private void applyStyleSheet(Stylesheet node) {

        for (ASTNode child : node.getChildren()) {
            if (child instanceof VariableAssignment) {
                applyVariableAssignment((VariableAssignment) child);
            } else if (child instanceof Stylerule) {
                    applyStylerule((Stylerule) child);
                }
            }

        }

        private void applyStylerule (Stylerule node){
            variableValues.push(new HashMap<>());

            for (ASTNode child : node.getChildren()) {
                if (child instanceof Declaration) {
                    applyDeclaration((Declaration) child);
                } else if (child instanceof VariableAssignment) {
                    applyVariableAssignment((VariableAssignment) child);
                }
            }

            variableValues.pop();
        }

        private void applyDeclaration (Declaration node){
            node.expression = evalExpression(node.expression);
        }


       private void applyVariableAssignment (VariableAssignment node){
            String varName = node.name.name;

           Literal value = evalExpression(node.expression);

            variableValues.peek().put(varName, value);
       }


    private Literal evalExpression(Expression expression) {
        if (expression instanceof Literal) {
            return (Literal) expression;
        } else if (expression instanceof Operation) {
            return evalOperation((Operation) expression);
        } else if (expression instanceof VariableReference) {
                VariableReference ref = (VariableReference) expression;
                for (HashMap<String, Literal> scope : variableValues) {
                    if (scope.containsKey(ref.name)) {
                        return scope.get(ref.name);
                    }
                }
        }
        return (Literal) expression;
    }

    private Literal evalOperation(Operation expression) {
        Literal left = evalExpression(expression.lhs);
        Literal right = evalExpression(expression.rhs);


        if (left instanceof PixelLiteral && right instanceof PixelLiteral) {
            PixelLiteral l = (PixelLiteral) left;
            PixelLiteral r = (PixelLiteral) right;

            if (expression instanceof AddOperation) {
                return new PixelLiteral(l.value + r.value);
            }
            if (expression instanceof SubtractOperation) {
                return new PixelLiteral(l.value - r.value);
            }

        }

        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral) {

            PercentageLiteral l = (PercentageLiteral) left;
            PercentageLiteral r = (PercentageLiteral) right;

            if (expression instanceof AddOperation) {
                return new PercentageLiteral(l.value + r.value);
            }
            if (expression instanceof SubtractOperation) {
                return new PercentageLiteral(l.value - r.value);
            }
        }
        if (left instanceof ScalarLiteral && right instanceof PercentageLiteral) {

            ScalarLiteral l = (ScalarLiteral) left;
            PercentageLiteral r = (PercentageLiteral) right;

            if (expression instanceof MultiplyOperation) {
                return new PercentageLiteral(l.value * r.value);
            }

        }
        if (left instanceof PercentageLiteral && right instanceof ScalarLiteral) {

            PercentageLiteral l = (PercentageLiteral) left;
            ScalarLiteral r = (ScalarLiteral) right;

            if (expression instanceof MultiplyOperation) {
                return new PercentageLiteral(l.value * r.value);
            }

        }
        if (left instanceof ScalarLiteral && right instanceof PixelLiteral) {

            ScalarLiteral l = (ScalarLiteral) left;
            PixelLiteral r = (PixelLiteral) right;

            if (expression instanceof MultiplyOperation) {
                return new PixelLiteral(l.value * r.value);
            }

        }
        if (left instanceof PixelLiteral && right instanceof ScalarLiteral) {

            PixelLiteral l = (PixelLiteral) left;
            ScalarLiteral r = (ScalarLiteral) right;

            if (expression instanceof MultiplyOperation) {
                return new PixelLiteral(l.value * r.value);
            }

        }
        return null;

    }
}


