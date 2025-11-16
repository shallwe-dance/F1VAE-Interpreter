grammar Expr;
//****************ONLY FOR REAL*****************************

// parser rules
prog : ((decl_list expr | expr) ';' NEWLINE?)*;

decl_list : decl decl_list
	|decl
	;

decl : 'def' ID id_list '=' expr 'endef'
	| 'def' ID '=' expr 'endef'
	;

id_list : ID id_list
	| ID
	;

expr : 'let' ID '=' expr 'in' expr #assignExpr
	| ID '()' #callExpr
	| ID '(' expr_list ')' #callExpr
	| expr '*' expr  # multiplicationExpr
     	| expr '+' expr  # additionExpr
	| expr '-' expr # subtractionExpr
	| '(' expr ')'         # parensExpr
     	| NUM              # numberExpr
     	| ID                  # idExpr
     ;
expr_list : expr ',' expr_list
	| expr
	;

// lexer rules
NEWLINE: [\r\n]+;
NUM :'0' | '-'?[1-9][0-9]*;          // should handle negatives
ID: [a-z][a-zA-Z0-9_]* ;
WS: [ \t\r\n]+ -> skip ;
//****************ONLY FOR REAL*****************************