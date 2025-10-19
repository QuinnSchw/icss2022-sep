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
stylesheet: icssParts EOF;

//enter: maak ASTNode en zet deze op stack --> exit: haal ASTNode van stack voeg toe aan node op de stack. Als kind A-- B -- C. als je a eruit haalt gaan de kinderen automatisch mee

// stylesheet: stylerule;
//stylerule: id_selector OPEN_BRACE declaration CLOSE_BRACE;
// id_selector: ID_IDENT;
// declaration: property COLON pixel_literal SEMICOLON
// property: LOWER_IDENT;
// pixel_literal: PIXELSIZE;

//==> LOWER_IDENT: [a-z] [a-z0-9]* ('-' [a-z0-9]+)*; potentieel voor in de toekomst TODO

icssParts: (constant | pre OPEN_BRACE data CLOSE_BRACE)*;
data: (color SEMICOLON | measurements SEMICOLON)*;
measurements: LOWER_IDENT COLON measureType;
measureType: PIXELSIZE | PERCENTAGE;
color: colorType COLON COLOR ;
colorType: LOWER_IDENT | LOWER_IDENT MIN LOWER_IDENT;
pre: LOWER_IDENT | ID_IDENT | CLASS_IDENT ;
constant: CAPITAL_IDENT ASSIGNMENT_OPERATOR constType SEMICOLON;
constType: bool | COLOR | measureType;
bool: TRUE | FALSE;

