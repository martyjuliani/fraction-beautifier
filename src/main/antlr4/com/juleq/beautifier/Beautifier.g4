/* simple expression grammar with ASS output */
grammar Beautifier;

/* parser rules, set of supported operations */
expr
    : (PLUS|MINUS) expr                           #signExpr
    | LPAREN expr RPAREN                          #parenthesisExpr
    | left=expr operator=(MULT|DIV) right=expr    #multDivExpr
    | left=expr operator=(PLUS|MINUS) right=expr  #plusMinusExpr
    | INT                                         #numericExpr
    ;

/* lexer rules, atomic integers */
INT   : ('0'..'9')+ ;

/* arithmetic operators */
PLUS          : '+'   ;
MINUS         : '-'   ;
MULT          : '*'   ;
DIV           : '/'   ;
LPAREN        : '('   ;
RPAREN 	      :	')'   ;

/* ignored whitespaces */
WS    : [ \r\n\t] + -> skip;