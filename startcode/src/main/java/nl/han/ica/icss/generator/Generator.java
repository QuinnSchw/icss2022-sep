package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;

public class Generator {

	public String generate(AST ast) {
        return "/* (c)Quinn Schwachofer*/\n\n" + generateStylesheet(ast.root);


	}

    private String generateStylesheet(Stylesheet node) {
        return generateStylerule((Stylerule)node.getChildren().get(0));
    }

    private String generateStylerule(Stylerule node) { //--> geeft string stylerule en een stylerule is een functie zegmaar (tagselecter + declaration)
       return node.selectors.get(0).toString() + "{\n" + generateDeclaration((Declaration)node.body.get(0)) + "}"; //--> wij moeten dit met meer doen(lijst bvb). hij heeft er nu maar 1.
    }

    private String generateDeclaration(Declaration declaration) {
        return "declaration\n"; //--> hier nog de declaration ontleden door de property en expression te pakken
    }


}
