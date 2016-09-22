package atlc;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.Location;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.Type;
import java.io.StringReader;

%%

%class Scanner
%cup
%line
%column
%char

%{
    private ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
    StringBuffer string = new StringBuffer();
    int currentLineIndent = 0;
    int indentLevel = 0;

    public Scanner(java.io.Reader reader, ComplexSymbolFactory symbolFactory) {
		this(reader);
        this.symbolFactory = symbolFactory;
    }

    public Symbol createSymbol(String plaintext, int code) {
        return symbolFactory.newSymbol(plaintext, code,
            new Location("", yyline + 1, yycolumn + 1, yychar),
            new Location("", yyline + 1, yycolumn + yylength(), yychar));
    }

    public Symbol createSymbol(String plaintext, int code, Object object) {
        return symbolFactory.newSymbol(plaintext, code,
            new Location("", yyline + 1, yycolumn + 1, yychar),
            new Location("", yyline + 1, yycolumn + yylength(), yychar),
            object);
    }

    public Symbol createSymbol(String plaintext, int code, Object object, int buffLength) {
        return symbolFactory.newSymbol(plaintext, code,
            new Location("", yyline + 1, yycolumn + yylength() - buffLength, yychar + yylength() - buffLength),
            new Location("", yyline + 1, yycolumn + yylength(), yychar + yylength()),
            object);
    }

    public Boolean createBoolean(String str) {
        if (!str.equals("YES") && !str.equals("NO")) {
            throw new IllegalArgumentException();
        }
        return str.equals("YES");
    }

    public void printError(String symbol) {
        System.err.println("Invalid symbol <" + string + ">");
    }

%}
%eofval{
    return symbolFactory.newSymbol("EOF",sym.EOF);
%eofval}

// Macros
// For Unix and Windows files
EndOfLine = \n | \r\n | \r
// Tab character
Tab = \t
// We only accept ' ' as a white space
WhiteSpace = " "
// A comment starts with the '~' character and continues with its content
Comment = "~" [ ][^\n]*

// Integer can be a 0 or an infinite amount of any digit preceded by any digit from 1 to 9
IntegerLiteral = 0 | [1-9][0-9]*
BooleanLiteral = "YES" | "NO"

VarName = [_a-z][_a-z0-9]*

%state STRING
%state NORMAL
%state FINAL

%%

<NORMAL> {
	/* Comments */
	^{Comment}{EndOfLine}			{ /* Ignore Comments */ }
	^[\t ]*{Comment}{EndOfLine}     { /* Ignore Comments */ }
	[\t ]*{Comment}                 { /* Ignore Comments */ }
	^{EndOfLine}					{ /* Ignore empty line */ }

    /* Functions */
    "fn"                { return createSymbol("Function", sym.FUNC); }
    "ret"               { return createSymbol("Return", sym.RET); }
    "exit"               { return createSymbol("Return", sym.EXIT); }
    "("					{ return createSymbol("LParen", sym.LPAREN); }
    ")"					{ return createSymbol("RParen", sym.RPAREN); }
    ","					{ return createSymbol("RParen", sym.COMMA); }
	/* Code Structure */
	{EndOfLine}		{ currentLineIndent = 0; yybegin(YYINITIAL); return createSymbol("End of Line", sym.EOL); }
	[ ]					{ return createSymbol("Space", sym.SP); }

	/* Data types */
	"int"				{ return createSymbol("IntType", sym.TYPE, Type.getType(Integer.class)); }
	"bln"				{ return createSymbol("BoolType", sym.TYPE, Type.getType(Boolean.class)); }
	"str"				{ return createSymbol("StrType", sym.TYPE, Type.getType(String.class)); }
	"=" 				{ return createSymbol("Assign", sym.ASSIGN); }
	":" 				{ return createSymbol("TypeAssign", sym.ASSIGN_TYPE); }

	/* Commands */
	"rl"				{ return createSymbol("Read", sym.READ_LINE); }
	"wl"				{ return createSymbol("Write", sym.WRITE_LINE); }
	"w"					{ return createSymbol("Write", sym.WRITE); }

	/* Decision and repetition structures */
	"if"				{ return createSymbol("If", sym.IF); }
	"ls"				{ return createSymbol("Else", sym.ELSE); }
	"do"				{ return createSymbol("Do", sym.DO); }
	"whl"				{ return createSymbol("While", sym.WHILE); }

	/* Arithmetic Operators */
	"+" 				{ return createSymbol("Plus",sym.MATH_BINOP, GeneratorAdapter.ADD); }
	"-" 				{ return createSymbol("Minus", sym.MATH_BINOP, GeneratorAdapter.SUB); }
	"*" 				{ return createSymbol("Times",sym.MATH_BINOP, GeneratorAdapter.MUL); }
	"/" 				{ return createSymbol("Divide", sym.MATH_BINOP, GeneratorAdapter.DIV); }
	"%" 				{ return createSymbol("Divide", sym.MATH_BINOP, GeneratorAdapter.REM); }

	/* Logic Operators */
	"!" 				{ return createSymbol("Not", sym.LOGIC_UNOP_NOT); }
	"&&" 				{ return createSymbol("And", sym.LOGIC_BINOP, GeneratorAdapter.AND); }
	"||" 				{ return createSymbol("Or", sym.LOGIC_BINOP, GeneratorAdapter.OR); }

	/* Boolean operators */
	"<" 				{ return createSymbol("Lower Than", sym.BOOL_BINOP, GeneratorAdapter.LT); }
	">" 				{ return createSymbol("Greater than", sym.BOOL_BINOP, GeneratorAdapter.GT); }
	"<=" 				{ return createSymbol("Lower than or equal to", sym.BOOL_BINOP, GeneratorAdapter.LE); }
	">=" 				{ return createSymbol("greater than or equal to", sym.BOOL_BINOP, GeneratorAdapter.GE); }
	"==" 				{ return createSymbol("Equal to", sym.BOOL_BINOP, GeneratorAdapter.EQ); }

	/* Identifiers */
	{VarName} 			{ return createSymbol("Var Name", sym.VAR_NAME, yytext()); }

	/* Literal */
	{IntegerLiteral} 	{ return createSymbol("Integer", sym.LIT_INT, new Integer(yytext())); }
	{BooleanLiteral} 	{ return createSymbol("Boolean", sym.LIT_BOOL, createBoolean(yytext())); }
	\"                  { string.setLength(0); yybegin(STRING); }
}

<STRING> {
  \"                             { yybegin(NORMAL); 
                                   return createSymbol("String", sym.LIT_STR, string.toString(), string.length()); }
  [^\n\r\"\\]+                   { string.append(yytext()); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

<FINAL> \n { currentLineIndent = 0; yybegin(YYINITIAL); }

<YYINITIAL> {
\t { currentLineIndent++; }
^[\t ]*{EndOfLine}	{/* Ignore blank lines. */}
^[\t ]*{Comment}{EndOfLine}			{ /* Ignore Comments */ }
<<EOF>> {
			if (currentLineIndent < indentLevel) {
				indentLevel--;
				yyreset(new StringReader("\n"));
				yybegin(FINAL);
				return createSymbol("Dedent", sym.DEDENT);
			} else {
				return symbolFactory.newSymbol("EOF",sym.EOF);
			}
		}
.  {
		yypushback(1);
		if (currentLineIndent > indentLevel) {
			indentLevel++;
			return createSymbol("Indent", sym.INDENT);
		} else if (currentLineIndent < indentLevel) {
			indentLevel--;
			return createSymbol("Dedent", sym.DEDENT);
		} else {
			yybegin(NORMAL);
		}
	}
}

[^]		{ throw new Error("Illegal character <" + yytext() + ">"); }
