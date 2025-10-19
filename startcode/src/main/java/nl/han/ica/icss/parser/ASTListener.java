package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
// mvn compile exec java
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		//currentContainer = new HANStack<>();
	}
    public AST getAST() {
        return ast;
    }

//    @Override
//    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
//        Stylesheet stylesheet = new Stylesheet();
//        currentContainer.push(stylesheet);
//    }


//    @Override
//    public void enterStyleRule(ICSSParser.StyleruleContext ctx) {
//        Stylerule stylerule = new Stylerule();
//        currentContainer.push(stylerule);
//    }
//@Override
//    public void exitStylerule(ICSSParser.StyleruleContext ctx) {
//        Stylerule stylerule = (Stylerule) currentContainer.pop();
//        currentContainer.peek().addChild(stylerule);
//    }
//
//    @Override
//    public void exitStysheet(ICSSParser.StyleruleContext ctx) {
//        Stylesheet stylesheet = (Stylesheet) currentContainer.pop();
//        ast.setRoot(stylesheet);
//    }
////
//    //idSelector exit and enter etc

    
}