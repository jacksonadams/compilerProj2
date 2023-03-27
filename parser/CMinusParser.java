package parser;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import scanner.CMinusScanner;
import scanner.Token.TokenType;

public class CMinusParser implements Parser {
    /* 
        This is a recursive descent parser.
        Given a program written in the C- language,
        Create an Abstract Syntax Tree (AST).

        Use the Scanner from Project #1 to scan tokens.

        Classes
        1. Program
        2. Param
        3. Decl
        4. VarDecl
        5. FunDecl
        6. Statement
        7. ExpressionStmt
        8. CompoundStmt
        9. SelectionStmt
        10. IterationStmt
        11. ReturnStmt
        12. Expression
        13. AssignExpression
        14. BinaryExpression
        15. CallExpression
        16. NumExpression
        17. VarExpression

        Parse Functions
        1. parseProgram
        2. parseDecl
        3. parseDecl2
        4. parseFunDecl
        5. parseParams
        6. parseParamList
        7. parseParam
        8. parseCompoundStmt
        9. parseLocalDecl
        10. parseStatementList
        11. parseStatement
        12. parseExpressionStmt
        13. parseSelectionStmt
        14. parseIterationStmt
        15. parseReturnStmt
        16. parseExpression
        17. parseExpression2
        18. parseExpression3
        19. parseSimpleExpr2
        20. parseAdditiveExpr
        21. parseAdditiveExpr2
        22. parseTerm
        23. parseTerm2
        24. parseFactor
        25. parseVarCall
        26. parseArgs
        27. parseArgList

        Extra Functions
        1. matchToken() - check and advance
        2. advanceToken() - just advance
        3. checkToken() - just check

        What should AST look like?

        Program {
            =
                int x
                10
            
            =  
                int y
                20

            int z

            int myFunction {
                Params {
                    int x1
                    int x2
                }
                =
                    int sum
                    +
                        x1
                        x2
                return
                    sum
            }
        }
     */

    /* Constructor */
    private CMinusScanner scanner;
    public CMinusParser(CMinusScanner inScanner){
        scanner = inScanner;
    }


    /* Helper functions */
    public Boolean checkToken (TokenType token){
        if(scanner.viewNextToken().getType() == token){
            return true;
        }
        return false;
    }
    public void advanceToken (){
        scanner.getNextToken();
    }
    public void matchToken(TokenType token) {
        if(scanner.getNextToken().getType() != token){
            // throw error, otherwise do nothing
        }
    }
    
    /* 17 classes */
    public class Program {
        public List<Decl> decls;
        public Program(List<Decl> decls) {
            this.decls = decls;
        }

        void print () {
            System.out.println("Program {");
            for(int i = 0; i < decls.size(); i ++){
                decls.get(i).print("");
            }
            System.out.println("}");
        }
    }
    public class Param { 
        // example: int x
        public VarExpression name;
        public Param (VarExpression name){
            this.name = name;
        }

        void print(String parentSpace){
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + "int " + name);
        }
    }
    abstract class Decl {
        // abstract, will be one of the other two decls
        abstract void print(String parentSpace);
    }
    public class VarDecl extends Decl { 
        // example: int x;
        // or int x = 10;

        public VarExpression LHS;
        public Expression RHS;
        public VarDecl(VarExpression LHS, Expression RHS){
            this.LHS = LHS;
            this.RHS = RHS;
        }

        void print(String parentSpace){
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + "=");
            LHS.print(mySpace);
            RHS.print(mySpace);
        }
    }
    public class FunDecl extends Decl { 
        // example: int gcd (int x, int y) { }
        // we need return type, function name, params, and compound statement
        String returnType;
        VarExpression name;
        List<Param> params;
        CompoundStmt content;

        public FunDecl (String returnType, VarExpression name, List<Param> params, CompoundStmt content) {
            this.returnType = returnType;
            this.name = name;
            this.params = params;
            this.content = content;
        }
        void print(String parentSpace){
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + this.returnType);
            name.print(mySpace);
            System.out.println(mySpace + "Params {");
            for(int i = 0; i < params.size(); i ++){
                params.get(i).print(mySpace);
            }
            content.print(mySpace);
            System.out.println(mySpace + "}");
        }
    }
    abstract class Statement {
        // abstract, will be one of the other 5 statements
        abstract void print(String parentSpace);
    }
    public class ExpressionStmt extends Statement { 
        // example: a = 3;
        public ExpressionStmt (VarExpression LHS, Expression RHS){
            
        }

        void print(String parentSpace){}
    }
    public class CompoundStmt extends Statement { 
        // a sequence of other statements inside { }
        // example: { x = 3; y = y + 3; }
        public CompoundStmt (Statement[] statements){

        }

        void print(String parentSpace){}
    }
    public class SelectionStmt extends Statement { 
        // example: if (statement) { } else { }
        public SelectionStmt (Expression condition, CompoundStmt ifSequence, CompoundStmt elseSequence){

        }

        void print(String parentSpace){}
    }
    public class IterationStmt extends Statement { 
        // example: while (x > 0) { }
        public IterationStmt (Expression condition, CompoundStmt sequence){

        }

        void print(String parentSpace){}
    }
    public class ReturnStmt extends Statement { 
        // example: return x;
        // could also be blank: return;
        public ReturnStmt (Expression LHS){

        }

        void print(String parentSpace){}
    }

    abstract class Expression { 
        // abstract expression, will be one of the other 5
        abstract void print(String parentSpace);
    }
    public class AssignExpression extends Expression { 
        // example: x = y, x = 3
        // has to be a var on the left
        public AssignExpression (VarExpression LHS, Expression RHS) {

        }

        void print(String parentSpace){}
    }
    public class BinaryExpression extends Expression {
        // example: 3 + 4, a + b
        public BinaryExpression (Expression LHS, TokenType op, Expression RHS){

        }

        void print(String parentSpace){}
    }
    public class CallExpression extends Expression {
        // example: gcd(3, 4)
        public CallExpression (VarExpression LHS, Expression[] args){

        }

        void print(String parentSpace){}
    }
    public class NumExpression extends Expression {
        // example: 3
        public NumExpression (int num){

        }

        void print(String parentSpace){}
    }
    public class VarExpression extends Expression {
        // example: x
        public VarExpression (String var){

        }

        void print(String parentSpace){}
    }

    /* Parse Functions */
    public Program parseProgram (){
        // program -> decl {decl}
        // first(program) = {void, int}
        // follow(program) = {$}

        // Program returnProgram = new Program();

        List<Decl> declList = new ArrayList<Decl>();

        // check if next token is in first set
        while(checkToken(TokenType.INT_TOKEN) || checkToken(TokenType.VOID_TOKEN)){
            Decl nextDecl = parseDecl();
            declList.add(nextDecl);
        }

        // if we're no longer in the first set, check if we're in the follow set - if yes, continue, if not, error
        if(!checkToken(TokenType.EOF_TOKEN)){
            // throw error
        }

        return new Program(declList);
    }

    private Decl parseDecl(){
        // decl -> void ID fun-decl | int ID decl'
        // first(decl) = {void, int}
        // follow(decl) = {$, int, void}

        Decl returnDecl;

        if(checkToken(TokenType.INT_TOKEN)){
            matchToken(TokenType.INT_TOKEN);
            matchToken(TokenType.IDENT_TOKEN);
            //returnDecl = parseDecl2();
        } else if(checkToken(TokenType.VOID_TOKEN)){
            matchToken(TokenType.VOID_TOKEN);
            matchToken(TokenType.IDENT_TOKEN);
            returnDecl = parseFunDecl();
        } 

        return returnDecl;
    }

    private Decl parseDecl2(){ }
    private Decl parseFunDecl(){ }
    private ArrayList<Param> parseParams(){ }
    private ArrayList<Param> parseParamList(){ }
    private Param parseParam(){ }
    private CompoundStmt parseCompoundStmt(){ }
    private ArrayList<Statement> parseStatementList(){ }
    private Statement parseStatement() { }
    private ExpressionStmt parseExpressionStmt(){ }
    private SelectionStmt parseSelectionStmt(){ }
    private IterationStmt parseIterationStmt(){ }
    private ReturnStmt parseReturnStmt(){ }
    private Expression parseExpression(){ }
    private Expression parseExpression2(){ }
    private Expression parseExpression3(){ }
    private Expression parseSimpleExpr2(){ }
    private Expression parseAdditiveExpr(){ }
    private Expression parseAdditiveExpr2(){ }
    private Expression parseTerm(){ }
    private Expression parseTerm2(){ }
    private Expression parseFactor(){ }
    private CallExpression parseVarCall(){ }
    private Expression parseArgs(){ }
    private ArrayList<Expression> parseArgList(){ }

    /* Print AST */
    public void printTree(){

    }
}
