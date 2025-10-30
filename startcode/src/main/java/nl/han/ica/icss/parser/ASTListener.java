package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */

public class ASTListener extends ICSSBaseListener {
    private AST ast;
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
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        VariableAssignment variableAssignment = new VariableAssignment();
        currentContainer.push(variableAssignment);

        VariableReference variableReference = new VariableReference(ctx.getChild(0).getText());
        variableAssignment.name = variableReference;
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        VariableAssignment variableAssignment = (VariableAssignment) currentContainer.pop();
        currentContainer.peek().addChild(variableAssignment);
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        Stylerule stylerule = new Stylerule();
        currentContainer.push(stylerule);
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        VariableReference variableReference = new VariableReference(ctx.getText());
        currentContainer.push(variableReference);
    }

    @Override
    public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
        VariableReference variableReference = (VariableReference) currentContainer.pop();
        currentContainer.peek().addChild(variableReference);
    }

    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        TagSelector tagSelector = new TagSelector(ctx.getText());
        currentContainer.push(tagSelector);
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        IdSelector idSelector = new IdSelector(ctx.getText());
        currentContainer.push(idSelector);
    }

    @Override
    public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
        IdSelector idSelector = (IdSelector) currentContainer.pop();
        currentContainer.peek().addChild(idSelector);
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ClassSelector classSelector = new ClassSelector(ctx.getText());
        currentContainer.push(classSelector);
    }

    @Override
    public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ClassSelector classSelector = (ClassSelector) currentContainer.pop();
        currentContainer.peek().addChild(classSelector);
    }


    @Override
    public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
        TagSelector tagSelector = (TagSelector) currentContainer.pop();
        currentContainer.peek().addChild(tagSelector);
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        IfClause ifClause = new IfClause();
        currentContainer.push(ifClause);
    }

    @Override
    public void exitIfClause(ICSSParser.IfClauseContext ctx) {
        IfClause ifClause = (IfClause) currentContainer.pop();
        currentContainer.peek().addChild(ifClause);
    }

    @Override
    public void enterElseClause(ICSSParser.ElseClauseContext ctx) {
        ElseClause elseClause = new ElseClause();
        currentContainer.push(elseClause);
    }

    @Override
    public void exitElseClause(ICSSParser.ElseClauseContext ctx) {
        ElseClause elseClause = (ElseClause) currentContainer.pop();
        currentContainer.peek().addChild(elseClause);
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
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        BoolLiteral boolLiteral = new BoolLiteral(ctx.getText());
        currentContainer.push(boolLiteral);
    }

    @Override
    public void exitBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        BoolLiteral literal = (BoolLiteral) currentContainer.pop();
        currentContainer.peek().addChild(literal);
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
    public void enterOperation(ICSSParser.OperationContext ctx) { // Checkt of het een plus of min is om zo het juiste te pushen.
        if (ctx.getChild(1).getText().equals("+")) {
            AddOperation operation = new AddOperation();
            currentContainer.push(operation);
        } else if (ctx.getChild(1).getText().equals("-")) {
            SubtractOperation operation = new SubtractOperation();
            currentContainer.push(operation);
        }

    }

    @Override
    public void exitOperation(ICSSParser.OperationContext ctx) {
        if (ctx.getChild(1).getText().equals("+")) {
            AddOperation operation = (AddOperation) currentContainer.pop();
            currentContainer.peek().addChild(operation);
        } else if (ctx.getChild(1).getText().equals("-")) {
            SubtractOperation operation = (SubtractOperation) currentContainer.pop();
            currentContainer.peek().addChild(operation);
        }

    }

    @Override
    public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        MultiplyOperation operation = new MultiplyOperation();
        currentContainer.push(operation);
    }

    @Override
    public void exitMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        MultiplyOperation operation = (MultiplyOperation) currentContainer.pop();
        currentContainer.peek().addChild(operation);
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        ScalarLiteral literal = new ScalarLiteral(ctx.getText());
        currentContainer.push(literal);
    }

    @Override
    public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        ScalarLiteral literal = (ScalarLiteral) currentContainer.pop();
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
}