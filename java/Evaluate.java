/*

Calculate The Input String
And Return the result

*/
//package PA1_ANTLR_2020313974;

import java.util.*;
public class Evaluate {
	List<AstNodes> list;
	public Map<String, Double> values;
	public Map<String, Map<AstNodes,AstNodes>> functions; //이름, args, body
	public List<Double> evaluations;
	
	public Evaluate(List<AstNodes> list) {
		this.list=list;
		values=new HashMap<>();
		functions=new HashMap<>();
		evaluations=new ArrayList<>();
	}

	public void fundec(AstNodes e) {
		FunctionDeclaration fdecl=(FunctionDeclaration) e;
		Map<AstNodes,AstNodes> inner=new HashMap<>();
		inner.put(fdecl.args,fdecl.body);
		functions.put(fdecl.name,inner);
		evaluations.add(0.0);
	}

	public List<Double> evaluate() {
		
		for (AstNodes e: list) {
			if (e instanceof Decl_list) {
				Decl_list decl_list=(Decl_list) e;
				for (AstNodes decl : decl_list.declsList()) {fundec(decl);}
			}
			
			else if (e instanceof FunctionDeclaration) {
				fundec(e);
			}

			else if (e instanceof VariableDeclaration) {
				VariableDeclaration decl=(VariableDeclaration) e;
				values.put(decl.id, getEvalResult(decl.value));
				evaluations.add(getEvalResult(decl.right));
			}
			
			else if (e instanceof AstNodes) {
				//String input=e.toString();
				double result = getEvalResult(e);
				//evaluations.add(input +" is "+result);
				evaluations.add(result);
			}
		} 
		return evaluations;
	}
	public double getEvalResult(AstNodes e) {
		double result=0;
		
		if(e instanceof Number) {
			Number num=(Number) e;
			result=num.num;
		}
/*
		else if (e instanceof Call) {
			Call call=(Call) e;
			if (call.exprs!=null){
				if (((Expr_list)call.exprs).size()==1) {
					double arg=getEvalResult(((Expr_list)call.exprs).getExprs().get(0));
					AstNodes body=functions.get(call.name).values().iterator().next(); //함수 body
					if (body instanceof Addition) {
						if (((Addition)body).left instanceof Variable) {result=arg + getEvalResult(((Addition)body).right);}
						else if (((Addition)body).right instanceof Variable) {result=arg + getEvalResult(((Addition)body).left);}
						return result;
					}
					else if (body instanceof Subtraction) {
						if (((Subtraction)body).left instanceof Variable) {result=arg - getEvalResult(((Subtraction)body).right);}
						else if (((Subtraction)body).right instanceof Variable) {result=arg - getEvalResult(((Subtraction)body).left);}
						return result;
					}
					else if (body instanceof Multiplication) {
						if (((Multiplication)body).left instanceof Variable) {result=arg * getEvalResult(((Multiplication)body).right);}
						else if (((Multiplication)body).right instanceof Variable) {result=arg * getEvalResult(((Multiplication)body).left);}
						return result;
					}
					
				}

			double left=getEvalResult(((Expr_list)call.exprs).getExprs().get(0));
			double right=getEvalResult(((Expr_list)call.exprs).getExprs().get(1));
			AstNodes body=functions.get(call.name).values().iterator().next(); //함수 body
			if (body instanceof Addition) {
				result= left+right;
			}
			else if (body instanceof Multiplication) {
				result= left*right;
			}
			else if (body instanceof Subtraction) {
				result= left-right;
			}
		}
		else {
			result=getEvalResult(functions.get(call.name).values().iterator().next());
		}
			
		}
*/
		else if (e instanceof Call) {
			Call call=(Call) e;
			if (call.exprs!=null) {
				List<AstNodes> called_args=((Expr_list)call.exprs).getExprs();
				List<String> defined_args=((Id_list)functions.get(call.name).keySet().iterator().next()).getElements();
				Map<String, Double> funcvalue=new HashMap<>();
				for (int i=defined_args.size()-1;i>=0;i--) { //정의된 함수의 argument들 순회
					values.put((defined_args.get(i)),getEvalResult(called_args.get(i)));
			}
			result=getEvalResult(functions.get(call.name).values().iterator().next());
		}
		else {result=getEvalResult(functions.get(call.name).values().iterator().next());}
	}

			
		else if (e instanceof VariableDeclaration) {
			VariableDeclaration decl=(VariableDeclaration) e;
			values.put(decl.id, getEvalResult(decl.value));
			result=getEvalResult(decl.right);
		}

		else if (e instanceof Variable) {
			Variable var=(Variable) e;
			result=values.get(var.id);
		}
		else if (e instanceof Parenthesis) {
			Parenthesis parenthesis=(Parenthesis)e;
			result=getEvalResult(parenthesis.value);
		}
		else if (e instanceof Addition) {
			Addition add=(Addition) e;
			double left=getEvalResult(add.left);
			double right=getEvalResult(add.right);
			result=left+right;	
		}
		else if (e instanceof Multiplication) {//add는 그냥 변수명임. Addition변수 그대로 쓰고 안 바꾼 것.
			Multiplication add=(Multiplication) e;
			double left=getEvalResult(add.left);
			double right=getEvalResult(add.right);
			result=left*right;
		} 
		else if (e instanceof Subtraction) {
			Subtraction sub=(Subtraction) e;
			double left=getEvalResult(sub.left);
			double right=getEvalResult(sub.right);
			result=left-right;
		}

		
		return result;
		}
	}

