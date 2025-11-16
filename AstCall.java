/*

print the Ast that we build in BuildAstVisitor.java

*/


import java.util.List;
import java.util.function.Function;
public class AstCall {
    List<AstNodes> list;
    int depth=0; //탭 횟수 저장
	String spaces="";
    String base=String.format("%s","\t"); //base==탭 한 번
    
    public AstCall(List<AstNodes> list) {
    	this.list=list;
    }
    
    public void PrintNode() {
    	for (AstNodes e: list) {

    		String result=""; //문자열화 쉽게 하기 위해 빈 스트링 생성

			if (e instanceof Decl_list) {
				Decl_list decl_list=(Decl_list)e;
				List<AstNodes> decls=decl_list.declsList();
				for (int i=0;i<decls.size();i++) {
					result+=CallFunctionDeclarationNode((FunctionDeclaration)decls.get(i));
					depth=0;
				}
				depth=0;
				result=result.stripTrailing();
			}
			/* 
    		else if (e instanceof FunctionDeclaration) {
				result+=CallFunctionDeclarationNode((FunctionDeclaration)e);
			}
			*/
			
			else if (e instanceof VariableDeclaration) {
				
				result+=CallVariableDeclarationNode((VariableDeclaration)e)+"\n";
			}
			
    		else if (e instanceof Number) {
    			result += CallNumNode((Number)e)+"\n";
    		}
    		else if (e instanceof Parenthesis) {
    			result+=CallParenthesisNode((Parenthesis)e)+"\n";
    		}
    		else if (e instanceof Addition ) {
    			result+=CallAdditionNode((Addition)e)+"\n";
    		}
    		else if (e instanceof Subtraction) {
    			result+=CallSubtractionNode((Subtraction)e)+"\n";
    		}
    		else if (e instanceof Multiplication) {
    			result+=CallMultiplicationNode((Multiplication)e)+"\n";
    		}
			else if (e instanceof Call) {
				result+=CallCallNode((Call)e)+"\n";
			}
			
    		else {continue;}
    		System.out.println(result.stripTrailing());
			depth=0;
    	}
    	
    }
    
    private String GetLeftStr(AstNodes e) {
    	AstNodes leftNode=e;
    	String leftStr="";//문자열화 쉽게 하기 위해 빈 스트링 생성
    	//노드가 어떤 인스턴스인지에 따라 다른 함수 적용
    	if (leftNode instanceof Number) leftStr+=CallNumNode((Number)leftNode);
    	else if(leftNode instanceof Variable) leftStr+=CallVariableNode((Variable)leftNode);
    	else if (leftNode instanceof Parenthesis) leftStr+=CallParenthesisNode((Parenthesis)leftNode);
    	else if (leftNode instanceof Addition) leftStr+=CallAdditionNode((Addition)leftNode);
    	else if (leftNode instanceof Multiplication) leftStr+=CallMultiplicationNode((Multiplication)leftNode);
    	else if (leftNode instanceof Subtraction) leftStr+=CallSubtractionNode((Subtraction)leftNode);
		else if (leftNode instanceof Call) leftStr+=CallCallNode((Call)leftNode);
		else if (leftNode instanceof VariableDeclaration) leftStr+=CallVariableDeclarationNode((VariableDeclaration)leftNode);
    	return leftStr;
    }
    
    private String GetRightStr(AstNodes e) {
    	AstNodes rightNode=e;
    	String rightStr="";
		
    	if (rightNode instanceof Number) rightStr+=CallNumNode((Number)rightNode);
    	else if(rightNode instanceof Variable) rightStr+=CallVariableNode((Variable)rightNode);
    	else if (rightNode instanceof Parenthesis) rightStr+=CallParenthesisNode((Parenthesis)rightNode);
    	else if (rightNode instanceof Addition) rightStr+=CallAdditionNode((Addition)rightNode);
    	else if (rightNode instanceof Multiplication) rightStr+=CallMultiplicationNode((Multiplication)rightNode);
    	else if (rightNode instanceof Subtraction) rightStr+=CallSubtractionNode((Subtraction)rightNode);
		else if (rightNode instanceof Call) rightStr+=CallCallNode((Call)rightNode);
		else if (rightNode instanceof VariableDeclaration) rightStr+=CallVariableDeclarationNode((VariableDeclaration)rightNode);
		
    	return rightStr.stripTrailing()+"\n";
    }
    
    private String CallVariableDeclarationNode(VariableDeclaration e) {
    	VariableDeclaration vd=(VariableDeclaration)e;
    	String id=vd.id;
		AstNodes value=vd.value;
		AstNodes scope=vd.right;
		String spaces="";
		String result="";
		
    	for(int i=0;i<depth;i++) {
    		spaces+=base;//깊이 길어질수록 스페이스 (" ") 추가
    	}
		int temp=depth;
    	
		//result+=String.format("%s%s\n%s\t%s\n",spaces,"LETIN",spaces,id);
		result+=String.format("%sLETIN\n",spaces);
		result+=String.format("%s\t%s\n",spaces,id);

		
		String valueString="";
		if (value instanceof Call) {
			depth++;
			valueString+=GetRightStr((Call)value);
			depth--;
		}
		else {
			valueString+=GetRightStr(value);}
		result+=String.format("%s",valueString);

		
		// depth=temp;
		// spaces=base.repeat(depth);
		String scopeString="";
		if (scope instanceof Call || scope instanceof VariableDeclaration || scope instanceof Addition) {
			depth++;
			scopeString=GetRightStr(scope);
			depth--;
		}
		else {scopeString=GetRightStr(scope);}
		result+=String.format("%s",scopeString);
		
		
		return result;
    }

	private String CallCallNode(Call e) {
		Call c = (Call) e;
		String name=c.name;
		AstNodes exprs=c.exprs;
		String spaces="";
		String callResult="";
		
    	for(int i=0;i<depth;i++) {
    		spaces+=base;//깊이 길어질수록 스페이스 (" ") 추가
    	}
		
		callResult+=String.format("%sCall\n",spaces);
		
		callResult+=String.format("%s\t%s\n",spaces,name);

		if (exprs!=null) { //argument 있을 때
			
			for (AstNodes s : ((Expr_list)exprs).getExprs()) {
				//단항 연산, 다항 연산 구분 필요.
				String arg="";
				if (s instanceof Addition) {
					depth++;
					arg=CallAdditionNode((Addition)s);
					depth--;
					callResult+=String.format("%s\n",arg);
				}
				else if (s instanceof Call) {
					depth++;
					arg=CallCallNode((Call)s);
					callResult+=String.format("%s\n",arg);
					depth--;
				}
				else {
					arg=GetRightStr(s);
					callResult+=String.format("%s\t%s\n",spaces,arg.stripTrailing());
				}
			}
		}
		return callResult;
	}

	private String CallFunctionDeclarationNode(FunctionDeclaration e) {
    	FunctionDeclaration fd=(FunctionDeclaration)e;
		String result="";

		if (fd.args==null) {
			String bodyString = GetRightStr(fd.body);
			result=String.format("%s%s\n\t%s\n\t%s\n", spaces,"DECL",fd.name+"",bodyString+"");
		}
    	else {
			result=String.format("%s%s\n\t%s\n", spaces,"DECL",fd.name+"");
			for (String arg : ((Id_list)fd.args).getElements()) {
				result+=("\t"+arg+"\n");
			}
			if (fd.body instanceof Addition || fd.body instanceof Subtraction) {
				depth++;
				result+=GetRightStr(fd.body);
				depth--;
			}
			else {result+=(GetRightStr(fd.body));}
		}
    	return result.stripTrailing()+"\n";
    }
    private String CallVariableNode(Variable e) {
    	Variable v=(Variable)e;
    	return v.id;
    }
    private String CallNumNode(Number e) {
    	Number num=(Number) e;
    	return (num.num)+"";
    }
    private String CallParenthesisNode(Parenthesis e) {
    	Parenthesis parenthesis=(Parenthesis)e;
    	AstNodes value=parenthesis.value;
    	return GetLeftStr(value);
    }
    
    
    
    private String CallMultiplicationNode(Multiplication e) {
    	Multiplication add=(Multiplication) e;
    	AstNodes leftNode=add.left;
    	AstNodes rightNode=add.right;
    	String leftStr="";
    	String rightStr="";
    	String spaces="";
    	
    	depth++;
    	for(int i=0;i<depth;i++) {
    		spaces+=base;
    	}
    	int temp=depth;
    	leftStr=GetLeftStr(leftNode);
    	depth=temp;
    	rightStr=GetRightStr(rightNode);
    	//이항연산이므로 left, right분리 필요. CallParenth...와의 차이점
    	
    	return String.format("%-8s\n%-8s%-8s\n%-8s%-8s","MUL",spaces,leftStr,spaces,rightStr);
    }

	private String CallAdditionNode(Addition e) {
    	Addition add=(Addition) e;
    	AstNodes leftNode=add.left;
    	AstNodes rightNode=add.right;
    	String leftStr="";
    	String rightStr="";
    	String spaces="";
    	
    	//depth++;
    	for(int i=0;i<depth;i++) {
    		spaces+=base;//깊이 길어질수록 스페이스 (" ") 추가
    	}
    	//depth 늘려야 할 경우는 따로 conditional branch 하기
		if (leftNode instanceof Call) {
			depth++;
			leftStr=GetLeftStr(leftNode);
			depth--;
		}
    	else {leftStr="\t"+GetLeftStr(leftNode);}
		
    	// //depth=temp;
		//leftStr=GetLeftStr(leftNode);
		//System.out.println("leftStr : --------\n"+leftStr+"\n-----------------------");
    	rightStr=GetRightStr(rightNode);
			
		
		String result=String.format("%s%s\n%s%s\n\t%s%s",spaces,"ADD",spaces,leftStr,spaces,rightStr);
		
    	return result;
    	//문자열 포맷팅. spaces들은 depth가 늘어날수록 증가해서 문자열들을 오른쪽에 프린트해줌
    }
    
    private String CallSubtractionNode(Subtraction e) {
    	Subtraction sub=(Subtraction) e;
    	AstNodes leftNode=sub.left;
    	AstNodes rightNode=sub.right;
    	String leftStr="";
    	String rightStr="";
    	String spaces="";
    	
    	for(int i=0;i<depth;i++) {
    		spaces+=base;
    	}
    	//leftNode to String
    	if (leftNode instanceof Call) {
			depth++;
			leftStr=GetLeftStr(leftNode);
			depth--;
		}
    	else {leftStr="\t"+GetLeftStr(leftNode);}
    	
		//rightNode to String
		if (rightNode instanceof Call) {
			depth++;
			rightStr=GetRightStr(rightNode);
			depth--;
		}
    	else {rightStr="\t"+GetRightStr(rightNode);}
    	
    	return String.format("%s%s\n%s%s\n%s%s",spaces,"SUB",spaces,leftStr,spaces,rightStr);
    }
    
   
    
}
