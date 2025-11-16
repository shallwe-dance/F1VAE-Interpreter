//package PA1_ANTLR_2020313974;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class program {

    public static void main(String[] args) throws IOException {
                
        // Get Lexer
        ExprLexer lexer = new ExprLexer(CharStreams.fromStream(System.in));
        
        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Pass tokens to parser
        ExprParser parser = new ExprParser(tokens);
		ParseTree antlrAst = parser.prog();
		
		// Build AST
		// Build the AST with BuildAstVisitor.java
		BuildAstVisitorProgram progVisitor=new BuildAstVisitorProgram();
		ProgramNode prog=(ProgramNode) progVisitor.visit(antlrAst);
		if (!progVisitor.semanticErrors.isEmpty()) {
			String lastError = progVisitor.semanticErrors.get(progVisitor.semanticErrors.size()-1);
			System.out.println(lastError);
			System.exit(0);
		}


		// Print AST
		// Print built AST with AstCall.java
		AstCall caller=new AstCall(prog.astnodes);
		
		
		caller.PrintNode();
		
		// Evaluate AST
		// Evaluate AST with traversing AST.
		//System.out.println("\b");
		Evaluate ep=new Evaluate(prog.astnodes);
		for (Double evaluation: ep.evaluate()) {
			System.out.println(evaluation+"");
		}	
	}
}
