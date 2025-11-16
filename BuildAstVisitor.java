import java.util.ArrayList;
import java.util.List;

public class BuildAstVisitor extends ExprBaseVisitor<AstNodes> {

	private List<String> vars; //variables storage
	//(name (string list, expr))
	private List<String> fstore=new ArrayList<>();
	private List<Integer> paramCount=new ArrayList<>();
	private List<String> semanticErrors=new ArrayList<>();
	
	public BuildAstVisitor(List<String> semanticErrors) {
		vars= new ArrayList<>();
		fstore=new ArrayList<>();
		paramCount=new ArrayList<>();
		this.semanticErrors=semanticErrors;
	}

	@Override
	public AstNodes visitNumberExpr(ExprParser.NumberExprContext ctx) {
		String numText=ctx.getChild(0).getText();
		double num=Double.parseDouble(numText);
		return new Number(num);
	}

	@Override
	public AstNodes visitAdditionExpr(ExprParser.AdditionExprContext ctx) {
		AstNodes left=visit(ctx.getChild(0));
		AstNodes right=visit(ctx.getChild(2));
		return new Addition(left, right);
	}

	@Override
	public AstNodes visitMultiplicationExpr(ExprParser.MultiplicationExprContext ctx) {
		AstNodes left=visit(ctx.getChild(0));
		AstNodes right=visit(ctx.getChild(2));
		return new Multiplication(left, right);
	}

	@Override
	public AstNodes visitSubtractionExpr(ExprParser.SubtractionExprContext ctx) {
		AstNodes left=visit(ctx.getChild(0));
		AstNodes right=visit(ctx.getChild(2));
		return new Subtraction(left, right);
	}

	@Override
	public AstNodes visitParensExpr(ExprParser.ParensExprContext ctx) {
		AstNodes value=visit(ctx.getChild(1));
		return new Parenthesis(value);
	}

	@Override
	public AstNodes visitIdExpr(ExprParser.IdExprContext ctx) {
		String id=ctx.getChild(0).getText();
		if (!vars.contains(id)) {
			semanticErrors.add("Error: Free Identifier "+id+" detected");
		}
		return new Variable(id);
	}

	@Override
	public AstNodes visitAssignExpr(ExprParser.AssignExprContext ctx) {
		String id=ctx.getChild(1).getText();
		vars.add(id);
		AstNodes value = visit(ctx.getChild(3));
		AstNodes right=visit(ctx.getChild(5));
		return new VariableDeclaration(id,value,right);
	}


//-----------------------done -------------------------------------
	//args들 모아둔 리스트
	@Override
	public AstNodes visitId_list(ExprParser.Id_listContext ctx) {
		List<String> args = new ArrayList<>();
		while (ctx != null) {
			String id=ctx.ID().getText();
			args.add(id);
			vars.add(id);//변수 스토리지에 저장
			ctx = ctx.id_list();
		}
		return new Id_list(args);
	}

//함수 정의
	@Override
	public AstNodes visitDecl(ExprParser.DeclContext ctx) {
		
		AstNodes args = null;

		String name=ctx.getChild(1).getText();
		fstore.add(name);

		AstNodes body;
		//System.out.println(ctx.getChild(4).getClass().getName());
		if (ctx.getChildCount()==6) {
			args=visitId_list(ctx.id_list());
			body=visit(ctx.getChild(4));
		}
		else {
			body=visit(ctx.getChild(3));
		}
		if (args==null) {paramCount.add(0);}
		else {paramCount.add(((Id_list)args).size());}
		return new FunctionDeclaration(name, args, body);
	}

	@Override
	public AstNodes visitExpr_list(ExprParser.Expr_listContext ctx) {
		List<AstNodes> exprs=new ArrayList<>();
		while (ctx!=null) {
			AstNodes expr=visit(ctx.expr());
			exprs.add(expr);
			ctx=ctx.expr_list();
		}
		return new Expr_list(exprs);
	}

	@Override
	public AstNodes visitCallExpr(ExprParser.CallExprContext ctx) {
		//args 없는 함수
		String name=ctx.getChild(0).getText();
		AstNodes expr_list=null;
		if (!fstore.contains(name)) {
			System.out.println("Error: Undefined function "+name+" detected");
			System.exit(0);
		}
		if (ctx.getChildCount()==4) {
			expr_list=visitExpr_list(ctx.expr_list());
		}
		int required=paramCount.get(fstore.indexOf(name));
		int actual=0;
		if (expr_list==null) {actual=0;}
		else {actual=((Expr_list)expr_list).size();}
		if (required!=actual) {
			semanticErrors.add("Error: The number of arguments of "+name+" mismatched, Required: "+required+", Actual: "+actual);
		}
		//if (expr_list==null) {System.out.println("expr_list is null! this message should be shown only once!!!");}
		return new Call(name,expr_list);
	}
	
	@Override
	public AstNodes visitDecl_list(ExprParser.Decl_listContext ctx) {
		List <AstNodes> decls=new ArrayList<>();
		while (ctx.getChildCount()==2) {
			AstNodes decl=visitDecl(ctx.decl());
			decls.add(decl);
			ctx=ctx.decl_list();
		}
		AstNodes decl=visitDecl(ctx.decl());
		decls.add(decl);
		return new Decl_list(decls);
	}

	

	

}