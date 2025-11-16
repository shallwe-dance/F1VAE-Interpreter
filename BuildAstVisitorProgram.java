//package PA1_ANTLR_2020313974;
//맨 처음 AST 노드 생성할 때 필요.
//custom file 1개 생성 가능하다는 조건 하에 만든 파일입니다.
//import ExprBaseVisitor;
//import ExprParser.ProgContext;
import java.util.ArrayList;
import java.util.List;
public class BuildAstVisitorProgram extends ExprBaseVisitor<ProgramNode> {

	public List<String> semanticErrors=new ArrayList<>();

	@Override
	public ProgramNode visitProg(ExprParser.ProgContext ctx) {
		ProgramNode prog= new ProgramNode();
		semanticErrors=new ArrayList<>();
		BuildAstVisitor exprVisitor=new BuildAstVisitor(semanticErrors);
		for (int i=0;i<ctx.getChildCount();i++) {
			if (i==ctx.getChildCount()-1) {}
			else {prog.addExpressions(exprVisitor.visit(ctx.getChild(i)));}
		}
		return prog;
	}
	
}
