module F = Format
open Store

let rec interp_expr (e: Ast.expr) (g: FStore.t) (s: Store.t) : Value.t = 
 (* Implement this function *)
 match e with
 |Num n-> NumV n 
 |Add (e1, e2) -> let left=interp_expr e1 g s in let right=interp_expr e2 g s in (
  match (left, right) with
  |(NumV n1, NumV n2) ->NumV(n1+n2)
 )
 |Sub (e1, e2) -> let left=interp_expr e1 g s in let right=interp_expr e2 g s in (
  match (left, right) with
  |(NumV n1, NumV n2) ->NumV(n1-n2)
 )
 |Id x->(try find x s with
 |Not_found ->failwith ("Free identifier: "^x))
 |LetIn (x, e1, e2)-> interp_expr e2 g (add x (interp_expr e1 g s) s)
 |Call (f, args) -> 
  let (params,body)=
  try
    Option.get (M.find_opt f g) with
    |Invalid_argument _->failwith("Undefined function: "^f)
  in
    let calculated_args=List.map(fun arg -> interp_expr arg g s) args in
    let new_s=
    try
      List.fold_left2 (fun acc_s param arg_val -> add param arg_val acc_s) s params calculated_args 
    with
    |Invalid_argument _ ->failwith(Printf.sprintf "The number of arguments of %s mismatched: Required: %d, Actual: %d" f (List.length params ) (List.length calculated_args))
  in interp_expr body g new_s
  (*
     Call of string * expr list
     *)


let interp_fundef (d: Ast.fundef) (g: FStore.t) : FStore.t = 
 (* Implement this function *)
 match d with
 |FunDef (name, args, body) -> FStore.add name (args, body) g

 
let interp (p: Ast.prog) : Value.t = 
 (* Implement this function *)
 match p with
 |Prog (dl,e) ->
  let g=List.fold_left (fun acc x -> interp_fundef x acc) M.empty dl in interp_expr e g M.empty
 

