package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

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
		currentContainer = new HANStack<>();
	}
    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet stylesheet = new Stylesheet();
        currentContainer.push(stylesheet);
    }


    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String childText = ctx.getChild(i).getText();
            System.out.println("Child " + i + ": " + childText);
        }
        Stylerule stylerule = new Stylerule();
        currentContainer.push(stylerule);
    }
    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        Declaration declaration = new Declaration();
        currentContainer.push(declaration);
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        PropertyName propertyName = new PropertyName(ctx.getText());
        currentContainer.push(propertyName);
    }

    @Override
    public void exitPropertyName(ICSSParser.PropertyNameContext ctx) {
        PropertyName propertyName = (PropertyName) currentContainer.pop();
        currentContainer.peek().addChild(propertyName);

    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        ColorLiteral colorLiteral = new ColorLiteral(ctx.getText());
        currentContainer.push(colorLiteral);
    }

    @Override
    public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        ColorLiteral literal = (ColorLiteral) currentContainer.pop();
        currentContainer.peek().addChild(literal);
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        PercentageLiteral percentageLiteral = new PercentageLiteral(ctx.getText());
        currentContainer.push(percentageLiteral);
    }
    @Override
    public void exitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        PercentageLiteral literal = (PercentageLiteral) currentContainer.pop();
        currentContainer.peek().addChild(literal);
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        PixelLiteral pixelLiteral = new PixelLiteral(ctx.getText());
        currentContainer.push(pixelLiteral);
    }
    @Override
    public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        PixelLiteral literal = (PixelLiteral) currentContainer.pop();
        currentContainer.peek().addChild(literal);
    }

    @Override
    public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
        Declaration declaration = (Declaration) currentContainer.pop();
        currentContainer.peek().addChild(declaration);
    }

    @Override
    public void exitStylerule(ICSSParser.StyleruleContext ctx) {
        Stylerule stylerule = (Stylerule) currentContainer.pop();
        currentContainer.peek().addChild(stylerule);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet stylesheet = (Stylesheet) currentContainer.pop();
        ast.setRoot(stylesheet);
    }



    //
//    //idSelector exit and enter etc



    //enterselector entertagselector als je een tag gebruikt in de grammatica.
    // selector: LOWERIDENT #tagselector | CLASSIDENT #classident | IDIDENT #idSelector

//public void enterTagSelector(ICSSParser.TagSelectorContext ctx) { --> dit doe je ook voor class en id

//        Stylerule stylerule = new Stylerule();
//        currentContainer.push(stylerule);
//    }
}