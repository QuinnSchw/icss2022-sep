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


stylesheet: (variableAssignment | stylerule )+ EOF;
stylerule: (tagSelector | idSelector | classSelector) OPEN_BRACE (variableAssignment | declaration | ifClause)+  CLOSE_BRACE;
declaration:  propertyName COLON expression SEMICOLON;

 expression:
    expression MUL expression #MultiplyOperation |
    expression (add | subtract) expression #Operation |
    bool #BoolLiteral |
    PIXELSIZE #PixelLiteral |
    PERCENTAGE #PercentageLiteral |
    COLOR #ColorLiteral|
    CAPITAL_IDENT #VariableReference |
    SCALAR #ScalarLiteral;

subtract: MIN #SubtractOperation;
add: PLUS #AddOperation;
ifClause : IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE (variableAssignment | declaration | ifClause)* CLOSE_BRACE (elseClause)?;
elseClause  : ELSE OPEN_BRACE (variableAssignment | declaration | ifClause)* CLOSE_BRACE;
propertyName: ('background-color' | 'color'| 'width'| 'height');
tagSelector: LOWER_IDENT;
idSelector   : ID_IDENT;
classSelector: CLASS_IDENT;

variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression SEMICOLON;
bool: TRUE | FALSE;

