package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

public class Generator {

    public String generate(AST ast) {
        return "/* (c)Quinn Schwachofer*/\n\n" + generateStylesheet(ast.root);
    }

    private String generateStylesheet(Stylesheet node) {
        StringBuilder result = new StringBuilder(); //Maak gebruik van een Stringbuilder om de gegenereerde stylerules makkelijk toe te kunnen voegen.
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Stylerule) {
                result.append(generateStylerule((Stylerule) child) + "\n");
            }
        }
        return result.toString();
    }

    private String generate(Expression expression) {
        if (expression instanceof PixelLiteral) {
            PixelLiteral pixel = (PixelLiteral) expression;
            int value = pixel.value;
            return value + "px";
        } else if (expression instanceof PercentageLiteral) {
            PercentageLiteral percentage = (PercentageLiteral) expression;
            int value = percentage.value;
            return value + "%";
        } else if (expression instanceof ColorLiteral) {
            ColorLiteral color = (ColorLiteral) expression;
            return color.value;
        } else if (expression instanceof BoolLiteral) {
            BoolLiteral bool = (BoolLiteral) expression;
            if (bool.value) {
                return "true";
            } else {
                return "false";
            }
        }
        return expression.toString();
    }

    private String generateStylerule(Stylerule node) {
        StringBuilder decl = new StringBuilder();
        for (ASTNode child : node.body) {
            if (child instanceof Declaration) {
                decl.append(generateDeclaration((Declaration) child));
            }
        }
        return "\n" + node.selectors.get(0).toString() + "{\n" + decl + "}";
    }

    private String generateDeclaration(Declaration declaration) {
        for (ASTNode child : declaration.getChildren()) {
            if (child instanceof PropertyName) {
                PropertyName name = (PropertyName) child;
                String type = name.name;
                if (type.equals("width") || type.equals("height") || type.equals("color") || type.equals("background-color")) {
                    String body = generate(declaration.expression);
                    return " " + " " + type + ": " + body + ";" + "\n";
                }
            }
        }
        return "declaration\n";
    }
}
