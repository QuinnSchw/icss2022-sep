package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.LinkedList;


public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes; // Hashmap houdt bij welke types variabelen zijn. --> handig ook voor het constant probleem

    public void check(AST ast) {
        checkStylesheet(ast.root);
        // variableTypes = new HANLinkedList<>();

    }

    private void checkStylesheet(Stylesheet sheet) {
        checkStylrule((Stylerule)sheet.getChildren().get(0));
    }

    private void checkStylrule(Stylerule stylerule) {
        for(ASTNode child: stylerule.getChildren()){
            if(child instanceof Declaration){
                checkDeclaration((Declaration) child);
            }
        }
    }

    private void checkDeclaration(Declaration declaration) {
if(declaration.property.name.equals("width")){
    if(declaration.expression instanceof ColorLiteral){
        declaration.setError("Property width: color not allowed"); //--> hierdoor wordt declaration dan rood in de tree
    }
}
    }


}
