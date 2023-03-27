package parser;

import java.util.ArrayList;
import java.util.List;

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
        public Program(List<Decl> declList) {

        }

        public void print (String parentSpace) {
            String mySpace = parentSpace + "    ";
        }
    }
    public class Param { 
        // example: int x
        public Param (VarExpression paramName){

        }
    }
    public class Decl { 
        // abstract, will be either varDecl or funDecl
    }
    public class VarDecl { 
        // example: int x;
        // or int x = 10;
        public VarDecl(VarExpression LHS, Expression RHS){
            
        }
    }
    public class FunDecl { 
        // example: int gcd (int x, int y) { }
        public FunDecl (VarExpression funcName, Param[] params) {

        }
    }
    public class Statement { 
        // abstract, will be one of the other 5 statements
    }
    public class ExpressionStmt { 
        // example: a = 3;
        public ExpressionStmt (VarExpression LHS, Expression RHS){

        }
    }
    public class CompoundStmt { 
        // a sequence of other statements inside { }
        // example: { x = 3; y = y + 3; }
        public CompoundStmt (Statement[] statements){

        }
    }
    public class SelectionStmt { 
        // example: if (statement) { } else { }
        public SelectionStmt (Expression condition, CompoundStmt ifSequence, CompoundStmt elseSequence){

        }
    }
    public class IterationStmt { 
        // example: while (x > 0) { }
        public IterationStmt (Expression condition, CompoundStmt sequence){

        }
    }
    public class ReturnStmt { 
        // example: return x;
        // could also be blank: return;
        public ReturnStmt (Expression LHS){

        }
    }
    public class Expression { 
        // abstract expression, will be one of the other 5
    }
    public class AssignExpression { 
        // example: x = y, x = 3
        // has to be a var on the left
        public AssignExpression (VarExpression LHS, Expression RHS) {

        }
    }
    public class BinaryExpression {
        // example: 3 + 4, a + b
        public BinaryExpression (Expression LHS, TokenType op, Expression RHS){

        }
    }
    public class CallExpression {
        // example: gcd(3, 4)
        public CallExpression (VarExpression LHS, Expression[] args){

        }
    }
    public class NumExpression {
        // example: 3
        public NumExpression (int num){

        }

    }
    public class VarExpression {
        // example: x
        public VarExpression (String var){

        }
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

        Decl decl = null;

        if(checkToken(TokenType.INT_TOKEN)){
            matchToken(TokenType.INT_TOKEN);
            matchToken(TokenType.IDENT_TOKEN);
            //returnDecl = parseDecl2();
        } else if(checkToken(TokenType.VOID_TOKEN)){
            matchToken(TokenType.VOID_TOKEN);
            matchToken(TokenType.IDENT_TOKEN);
            decl = parseFunDecl();
        } 

        return decl;
    }

    private Decl parseDecl2(){
        /* decl' → ; | [NUM] | fun-decl
         * First(decl') → { ;, [, ( }
         * Follow(decl') → { $, void, int }
         */ 
        Decl decl2 = null;

        return decl2;
    }

    private Decl parseVarDecl(){ 
        /* var-decl → ; | [NUM]
         * First(var-decl) → { ;, [ }
         * Follow(var-decl) → { int, “}”, ;, ID, NUM, (, *, /, +, -, ;, {, if, while, return }
         */
        Decl varDecl = null;
        
        return varDecl;
    }

    private Decl parseFunDecl(){ 
        /* fun-decl → “(” params-list “)” compound-stmt
         * First(fun-decl) → { ( }
         * Follow(fun-decl) → { $, void, int, }
         */
        Decl funDecl = null;
        
        return funDecl;
    }
    
    private ArrayList<Param> parseParams(){ 
        /* params → param-list | void
         * First(params) → { int, void }
         * Follow(params) → { ) }
         */
        ArrayList<Param> params = null;

        return params;
    }

    private ArrayList<Param> parseParamList(){ 
        /* param-list → param {, param}
         * First(params-list) → { int }
         * Follow(param-list) → { ) }
         */
        ArrayList<Param> paramList = null;

        return paramList;
    }

    private Param parseParam(){ 
        /* param → int ID [“[“ “]”]
         * First(param) → { int }
         * Follow(param) → { “,”, ) }
         */
        Param param = null;

        return param;
    }

    private CompoundStmt parseCompoundStmt(){ 
        /* compound-stmt → “{“ local-declarations statement-list “}”
         * First(compound-stmt) → { { }
         * Follow(compound-stmt) → { $, void, int, “}”, ID, NUM, (, *, /, +, -, ;, {, if, while, return, else }
         */
        CompoundStmt CS = null;

        return CS;
    }

    private ArrayList<Decl> parseLocalDeclarations(){ 
        /* local-declarations → {int ID var-decl}
         * First(local-declarations) → { ε, int }
         * Follow(local-declarations) → { “}”, ID, NUM, (, *, /, +, -, ;, {, if, while, return }
         */
        ArrayList<Decl> localDecls = null;

        return localDecls;
    }
    
    private ArrayList<Statement> parseStatementList(){ 
        /* statement-list → {statement}
         * First(statement-list) → { ε. ID, NUM, (, *, /, +, -, ;, {, if, while, return }
         * Follow(statement-list) → { “}” }
         */
        ArrayList<Statement> SL = null;

        return SL;
    }
    
    private Statement parseStatement() { 
        /* statement → expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
         * First(statement) → { ID, NUM, (, ε, *, /, +, -, ;, {, if, while, return }
         * Follow(statement) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        Statement S = null;

        return S;
    }
    
    private ExpressionStmt parseExpressionStmt(){ 
        /* expression-stmt → [expression] ;
         * First(expression-stmt) → { ID, NUM, (, ε, *, /, +, -, ; }
         * Follow(expression-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        ExpressionStmt ES = null;

        return ES;
    }
    
    private SelectionStmt parseSelectionStmt(){ 
        /* selection-stmt → if “(“ expression “)” statement [else statement]
         * First(selection-stmt) → { if }
         * Follow(selection-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        SelectionStmt SS = null;

        return SS;
    }
    
    private IterationStmt parseIterationStmt(){ 
        /* iteration-stmt → while “(” expression “)” statement
         * First(iteration-stmt) → { while }
         * Follow(iteration-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        IterationStmt IS = null;

        return IS;
    }
    
    private ReturnStmt parseReturnStmt(){ 
        /* return-stmt → return [expression] ;
         * First(return-stmt) → { return }
         * Follow(return-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        ReturnStmt RS = null;

        return RS;
    }
    
    private Expression parseExpression(){ 
        /* expression → ID expression’ | NUM simple-expression’ | (expression) simple-expression’
         * First(expression) → { ID, NUM, (, ε, *, /, +, - }
         * Follow(expression) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression E = null;

        return E;
    }
    
    private Expression parseExpression2(){ 
        /* expression’ → = expression | [expression] expression’’ | (args) simple-expression’ | simple-expression’
         * First(expression’) → { =, ID, NUM, (, ε, *, /, +, - }
         * Follow(expression’) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression E2 = null;

        return E2;
    }
    
    private Expression parseExpression3(){ 
        /* expression’’ → = expression | simple-expression’
         * First(expression’’) → { ID, NUM, (, ε, *, /, +, - }
         * Follow(expression’’) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression E3 = null;

        return E3;
    }
    
    private Expression parseSimpleExpr2(){ 
        /* simple-expression’ → additive-expression’ [relop additive expression]
         * First(simple-expression’) → { ε, *, /, +, - }
         * Follow(simple-expression’) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression SE2 = null;

        return SE2;
    }
    
    private Expression parseAdditiveExpr(){ 
        /* additive-expression → term {addop term}
         * First(additive-expression) → { (, ID, NUM }
         * Follow(additive-expression) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression AE = null;

        return AE;
    }
    
    private Expression parseAdditiveExpr2(){ 
        /* additive-expression’ → term’ {addop term}
         * First(additive-expression’) → { ε, *, /, +, - }
         * Follow(additive-expression’) → { <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression AE2 = null;

        return AE2;
    }
    
    private Expression parseTerm(){ 
        /* term → factor {mulop factor}
         * First(term) → { (, ID, NUM }
         * Follow(term) → { +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression T = null;

        return T;
    }
    
    private Expression parseTerm2(){ 
        /* term’ → {mulop factor}
         * First(term’) → { ε. *, / }
         * Follow(term’) → { +, - }
         */
        Expression T2 = null;

        return T2;
    }
    
    private Expression parseFactor(){ 
        /* factor → “(” expression “)” | ID varcall | NUM
         * First(factor) → { (, ID, NUM }
         * Follow(factor) → { *, /, +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression F = null;

        return F;
    }
    
    private CallExpression parseVarCall(){ 
        /* varcall → “(“ args “)” | “[“ expression “]” | ε
         * First(varcall) → { (, [, ε }
         * Follow(varcall) → { *, /, +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        CallExpression varcall = null;

        return varcall;
    }
    
    private Expression parseArgs(){ 
        /* args → arg-list | ε
         * First(args) → { ID, NUM, (, ε, *, / }
         * Follow(args) → { ) }
         */
        Expression args = null;

        return args;
    }
    
    private ArrayList<Expression> parseArgList(){ 
        /* arg-list → expression {, expression}
         * First(arg-list) → { ID, NUM, (, ε, *, / }
         * Follow(arg-list) → { ) }
         */
        ArrayList<Expression> argList = null;

        return argList;
    }

    /* Print AST */
    public void printTree(){

    }
}
