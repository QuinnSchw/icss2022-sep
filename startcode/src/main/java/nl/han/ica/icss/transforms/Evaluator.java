package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

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

    private void applyStylerule(Stylerule node) {
        variableValues.push(new HashMap<>());

        ArrayList<ASTNode> evaluatedBody = new ArrayList<>();

        for (ASTNode child : node.getChildren()) {
            if (child instanceof VariableAssignment) {
                applyVariableAssignment((VariableAssignment) child);
            } else if (child instanceof Declaration) {
                applyDeclaration((Declaration) child);
                evaluatedBody.add(child);
            } else if (child instanceof IfClause) {
                evaluatedBody.addAll(applyIfClause((IfClause) child));
            }
        }

        node.body = evaluatedBody;
        variableValues.pop();
    }

    private LinkedList<ASTNode> applyIfClause(IfClause node) {
        LinkedList<ASTNode> result = new LinkedList<>();

        Literal condition = evalExpression(node.conditionalExpression);

        if (condition instanceof BoolLiteral) {
            BoolLiteral boolCondition = (BoolLiteral) condition;
            boolean conditionValue = boolCondition.value;

            if (conditionValue) {
                variableValues.push(new HashMap<>());
                for (ASTNode child : node.body) {
                    if (child instanceof Declaration) {
                        applyDeclaration((Declaration) child);
                        result.add(child);
                    } else if (child instanceof VariableAssignment) {
                        applyVariableAssignment((VariableAssignment) child);
                    } else if (child instanceof IfClause) {
                        result.addAll(applyIfClause((IfClause) child));
                    }
                }
                variableValues.pop();
            } else if (node.elseClause != null) {
                variableValues.push(new HashMap<>());
                for (ASTNode child : node.elseClause.body) {
                    if (child instanceof Declaration) {
                        applyDeclaration((Declaration) child);
                        result.add(child);
                    } else if (child instanceof VariableAssignment) {
                        applyVariableAssignment((VariableAssignment) child);
                    } else if (child instanceof IfClause) {
                        result.addAll(applyIfClause((IfClause) child));
                    }
                }
                variableValues.pop();
            }
        }

        return result;
    }

    private void applyDeclaration(Declaration node) {
        node.expression = evalExpression(node.expression);
    }


    private void applyVariableAssignment(VariableAssignment node) {

        Literal value = evalExpression(node.expression);
        String varName = node.name.name;


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
            System.out.println(expression);

            if (expression instanceof AddOperation) {
                System.out.println("Add");
                return new PixelLiteral(l.value + r.value);
            }
            if (expression instanceof SubtractOperation) {
                System.out.println("Subtract");
                return new PixelLiteral(l.value - r.value);
            }
            if (expression instanceof Operation) {
                System.out.println("Operation");
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
        if (left instanceof ScalarLiteral && right instanceof ScalarLiteral) {

            ScalarLiteral l = (ScalarLiteral) left;
            ScalarLiteral r = (ScalarLiteral) right;
            System.out.println("expressie" + expression);

            if (expression instanceof MultiplyOperation) {
                return new ScalarLiteral(l.value * r.value);
            }
            if (expression instanceof AddOperation) {
                return new PercentageLiteral(l.value + r.value);
            }
            if (expression instanceof SubtractOperation) {
                return new PercentageLiteral(l.value - r.value);
            }

        }
        return null;

    }
}


