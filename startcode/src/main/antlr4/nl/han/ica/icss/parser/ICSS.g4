grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';




//--- PARSER: ---


//enter: maak ASTNode en zet deze op stack --> exit: haal ASTNode van stack voeg toe aan node op de stack. Als kind A-- B -- C. als je a eruit haalt gaan de kinderen automatisch mee

// stylesheet: stylerule;
//stylerule: id_selector OPEN_BRACE declaration CLOSE_BRACE;
// id_selector: ID_IDENT;
// declaration: property COLON pixel_literal SEMICOLON
// property: LOWER_IDENT;
// pixel_literal: PIXELSIZE;
// expression:
// PIXELSIZE #pixelLiteral |
// COLOR #colorLiteral;

//==> LOWER_IDENT: [a-z] [a-z0-9]* ('-' [a-z0-9]+)*; potentieel voor in de toekomst TODO

//Op het moment dat er een haakje wordt geopend wordt er een hashmap gemaakt via de linkedList in de checker. Het is als het ware een linkedlist van hashmaps.
// Als je in een haakje gaat komt er een hashmap voor. daar kijk je eerst in en daarna ga je naar de volgende in de linkedlist en dat is dus buiten de haakjes.
// stylerule in grammatica zetten, want dan kun je een enter en exit maken. vgm doe je dit nogsteeds zelf.
//
//LES 2
// expression:
//expression PLUS expression #addExpression
//PIXELSIZE #pixelLiteral






stylesheet: (variableAssignment | stylerule )+ EOF;
stylerule: tagSelector OPEN_BRACE (declaration)+ CLOSE_BRACE;
declaration:  propertyName COLON expression SEMICOLON;

 expression:
    expression MUL expression #MultiplyOperation |
    expression PLUS expression #AddOperation |
    expression MIN expression #SubtractOperation |
    bool #BoolLiteral |
    PIXELSIZE #PixelLiteral |
    PERCENTAGE #PercentageLiteral |
    COLOR #ColorLiteral|
    CAPITAL_IDENT #VariableReference |
    SCALAR #ScalarLiteral;


propertyName: ('background-color' | 'color'| 'width'| 'height');
tagSelector: LOWER_IDENT | ID_IDENT | CLASS_IDENT #ClassSelector;
variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression SEMICOLON;
bool: TRUE | FALSE;

