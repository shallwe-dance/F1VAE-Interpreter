//package PA1_ANTLR_2020313974;

import java.util.*;

//AntlrExampleVisitor에서 Expression과 대응하는 java파일.
public abstract class AstNodes {

}

class ProgramNode extends AstNodes {
	public List<AstNodes> astnodes;
	
	public ProgramNode() {
		this.astnodes=new ArrayList<>();
	}
	public void addExpressions(AstNodes e) {
		astnodes.add(e);
	}
}



//---------------done------------------------------
class Addition extends AstNodes {
	AstNodes left;
	AstNodes right;
	public Addition(AstNodes left, AstNodes right) {
		this.left=left;
		this.right=right;
	}	
}

class Multiplication extends AstNodes {
	AstNodes left;
	AstNodes right;
	public Multiplication(AstNodes left, AstNodes right) {
		this.left=left;
		this.right=right;
	}
}

class Subtraction extends AstNodes {
	AstNodes left;
	AstNodes right;
	public Subtraction(AstNodes left, AstNodes right) {
		this.left=left;
		this.right=right;
	}	
}

class Number extends AstNodes {
	double num;
	public Number(double num) {
		this.num=num;
	}
	public String getText() {
		return this.num+"";
	}
}


class Variable extends AstNodes {
	String id;
	public Variable(String id) {
		this.id=id;
	}
	public String getText() {
		return this.id;
	}
}

class Parenthesis extends AstNodes {
	AstNodes value;
	public Parenthesis(AstNodes value) {
		this.value=value;
	}	
}

class VariableDeclaration extends AstNodes {
	public String id;
	public AstNodes value;
	AstNodes right;
	public VariableDeclaration(String id, AstNodes value, AstNodes right) {
		this.id=id;
		this.value=value;
		this.right=right;
	}
}

class FunctionDeclaration extends AstNodes {
	String name;
	AstNodes args;
	AstNodes body;
	public FunctionDeclaration(String name, AstNodes args, AstNodes body) {
		this.name=name;
		this.args=args;
		this.body=body;
	}
}

class Id_list extends AstNodes {
	List<String> args;
	public Id_list(List<String> args) {
		this.args=args;
	}

	public int size() {
		return args.size();
	}

	public List<String> getElements() {
		List<String> lst=new ArrayList<>();
		for (String arg : args) {
			lst.add(arg);
		}
		return lst;
	}
}

class Expr_list extends AstNodes {
	List<AstNodes> exprs;
	public Expr_list(List<AstNodes> exprs) {
		this.exprs=exprs;
	}
	public int size() {
		return exprs.size();
	}
	public List<AstNodes> getExprs() {
		return this.exprs;
	}
}

class Call extends AstNodes{
	String name;
	AstNodes exprs;
	public Call(String name, AstNodes exprs) {
		this.name=name;
		this.exprs=exprs;
	}
	public String getText() {
		String txt="";
		txt+="Call\n";
		txt+="\t"+this.name+"\n";
		for (AstNodes node :((Expr_list)exprs).getExprs()) {
			if (node instanceof Call) {txt=txt+"\n\t"+((Call)node).getText();}
			else if (node instanceof Number) {txt+="\n"+((Number)node).getText();}
		}
		return txt;
	}
}

class Decl_list extends AstNodes {
	List<AstNodes> decls;
	public Decl_list(List<AstNodes> decls) {
		this.decls=decls;
	}
	public List<AstNodes> declsList() {
		return this.decls;
	}
}
//-------------------done---------------------------















