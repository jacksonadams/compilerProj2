package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.io.FileWriter;

import javax.security.auth.x500.X500Principal;

import scanner.CMinusScanner;
import scanner.Token;
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
        Program program = parseProgram();

        // For debugging purposes
        program.print();
    }


    /* Helper functions */
    public Boolean checkToken (TokenType token){
        if(scanner.viewNextToken().getType() == token){
            return true;
        }
        return false;
    }
    public Token advanceToken (){
        return scanner.getNextToken();
    }
    public Token matchToken(TokenType token) {
        Token nextToken = scanner.getNextToken();

        if(nextToken.getType() != token){
            // throw error
        }

        return nextToken;
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
                if(decls.get(i) != null){
                    decls.get(i).print("");
                }
            }
            System.out.println("}");
        }
    }

    public class Param {
        // example: int x
        public VarExpression name;

        public Param(VarExpression name) {
            this.name = name;
        }

        void print(String parentSpace) {
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + "int " + name);
        }
    }

    abstract class Decl {
        // abstract, will be one of the other two decls
        abstract void print(String parentSpace);
    }

    public class VarDecl extends Decl {
        // example: int x; or int x[10];

        public VarExpression name;

        public VarDecl(VarExpression name) {
            this.name = name;
        }

        void print(String parentSpace) {
            String mySpace = parentSpace + "  ";
            this.name.print(mySpace);
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
        // example: a + 3;

        public Expression statement;
        public ExpressionStmt(Expression statement) {
            this.statement = statement;
        }

        void print(String parentSpace) {
            String mySpace = parentSpace + "  ";
            this.statement.print(mySpace);
        }
    }

    public class CompoundStmt extends Statement {
        // a sequence of other statements inside { }
        // example: { x = 3; y = y + 3; }
        ArrayList<Decl> localDecls = new ArrayList<Decl>();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        public CompoundStmt(ArrayList<Decl> localDecls, ArrayList<Statement> statements) {
            this.localDecls = localDecls;
            this.statements = statements;
        }

        void print(String parentSpace) {
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + "{");
            for (int i = 0; i < localDecls.size(); i++) {
                localDecls.get(i).print(mySpace + "  ");
            }
            for (int i = 0; i < statements.size(); i++) {
                statements.get(i).print(mySpace + "  ");
            }
            System.out.println(mySpace + "}");
        }
    }

    public class SelectionStmt extends Statement {
        // example: if (statement) { } else { }
        public Expression condition;
        public Statement ifSequence;
        public Statement elseSequence;

        public SelectionStmt(Expression condition, Statement ifSequence, Statement elseSequence) {
            this.condition = condition;
            this.ifSequence = ifSequence;
            this.elseSequence = elseSequence;
        }
        public SelectionStmt(Expression condition, Statement ifSequence) {
            this.condition = condition;
            this.ifSequence = ifSequence;
        }

        void print(String parentSpace) {

        }
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
        VarExpression LHS;
        Expression RHS;

        public AssignExpression(VarExpression LHS, Expression RHS) {
            this.LHS = LHS;
            this.RHS = RHS;
        }

        void print(String parentSpace) {
            String mySpace = "  " + parentSpace;
            System.out.println(mySpace + "=");
            this.LHS.print(mySpace);
            this.RHS.print(mySpace);
        }
    }

    public class BinaryExpression extends Expression {
        // example: 3 + 4, a + b
        public BinaryExpression (Expression LHS, TokenType op, Expression RHS){

        }

        void print(String parentSpace){}
    }

    public class CallExpression extends Expression {
        // example: gcd(3, 4)
        public CallExpression (VarExpression LHS, ArrayList<Expression> args){

        }

        void print(String parentSpace){}
    }

    public class NumExpression extends Expression {
        // example: 3
        int num;
        public NumExpression(int num) {
            this.num = num;
        }

        void print(String parentSpace) {
            System.out.println("  " + parentSpace + this.num);
        }
    }

    public class VarExpression extends Expression {
        // example: x or x[10]
        String var;
        int num = -1;
        public VarExpression(String var) {
            this.var = var;
        }
        public VarExpression(String var, int num) {
            this.var = var;
            this.num = num;
        }

        void print(String parentSpace) {
            if(this.num == -1){
                System.out.println("  " + parentSpace + this.var);
            } else {
                System.out.println("  " + parentSpace + this.var + "[" + this.num + "]");
            }
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
        
        if(checkToken(TokenType.VOID_TOKEN)){
            Token temp;
            
            matchToken(TokenType.VOID_TOKEN);
            String returnType = "void";
            
            temp = matchToken(TokenType.IDENT_TOKEN);
            VarExpression name = new VarExpression((String)temp.getData());

            decl = parseFunDecl(returnType, name);
        } else if(checkToken(TokenType.INT_TOKEN)){
            Token temp;

            matchToken(TokenType.INT_TOKEN);
            String returnType = "int";

            temp = matchToken(TokenType.IDENT_TOKEN);
            String name = (String)temp.getData();
            
            decl = parseDecl2(returnType, name);

        }

        return decl;
    }

    private Decl parseFunDecl(String returnType, VarExpression name){ 
        /* fun-decl → “(” params “)” compound-stmt
         * First(fun-decl) → { ( }
         * Follow(fun-decl) → { $, void, int, }
         */
        Decl funDecl = null;

        matchToken(TokenType.LEFT_PAREN_TOKEN);

        List<Param> paramList = parseParams();
        
        matchToken(TokenType.RIGHT_PAREN_TOKEN);

        CompoundStmt CS = parseCompoundStmt();

        funDecl = new FunDecl(returnType, name, paramList, CS);

        return funDecl;
    }

    private Decl parseDecl2(String returnType, String name){
        /* decl’ → var-decl | fun-decl
         * First(decl') → { ;, [, ( }
         * Follow(decl') → { $, void, int }
         */ 
        Decl decl2 = null;
        Token temp;

        if(checkToken(TokenType.SEMI_TOKEN)){
            matchToken(TokenType.SEMI_TOKEN);
            
            VarExpression var = new VarExpression(name);
            decl2 = new VarDecl(var);
        }
        else if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);
            
            temp = matchToken(TokenType.NUM_TOKEN);
            int value = (int)temp.getData();
            VarExpression var = new VarExpression(name, value);
            decl2 = new VarDecl(var);

            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
        }
        else if(checkToken(TokenType.LEFT_PAREN_TOKEN)){
            VarExpression var = new VarExpression(name);
            decl2 = parseFunDecl(returnType, var);
        }

        return decl2;
    }

    private Decl parseVarDecl(String name){ 
        /* var-decl → [ “[“ NUM “]” ] ;
         * First(var-decl) → { ;, [ }
         * Follow(var-decl) → { int, “}”, ;, ID, NUM, (, *, /, +, -, ;, {, if, while, return }
         */
        Decl varDecl = null;
        Token temp;

        if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);

            temp = matchToken(TokenType.NUM_TOKEN);
            int value = (int)temp.getData();
            VarExpression var = new VarExpression(name, value);
            varDecl = new VarDecl(var);

            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
        } 
        else{
            VarExpression var = new VarExpression(name);
            varDecl = new VarDecl(var);
        }

        matchToken(TokenType.SEMI_TOKEN);
        
        return varDecl;
    }
    
    private ArrayList<Param> parseParams(){ 
        /* params → param-list | void
         * First(params) → { int, void }
         * Follow(params) → { ) }
         */
        ArrayList<Param> params = null;

        if(checkToken(TokenType.INT_TOKEN)){
            params = parseParamList();
        }
        else if(checkToken(TokenType.VOID_TOKEN)){
            matchToken(TokenType.VOID_TOKEN);
        }

        return params;
    }

    private ArrayList<Param> parseParamList(){ 
        /* param-list → param {, param}
         * First(params-list) → { int }
         * Follow(param-list) → { ) }
         */
        ArrayList<Param> paramList = new ArrayList<Param>();

        Param param = parseParam();
        paramList.add(param);

        while(checkToken(TokenType.COMMA_TOKEN)){
            matchToken(TokenType.COMMA_TOKEN);
            param = parseParam();
            paramList.add(param);
        }

        return paramList;
    }

    private Param parseParam(){ 
        /* param → int ID [“[“ “]”]
         * First(param) → { int }
         * Follow(param) → { “,”, ) }
         */
        Param param = null;
        Token temp;

        matchToken(TokenType.INT_TOKEN);

        temp = matchToken(TokenType.IDENT_TOKEN);
        String name = (String)temp.getData();

        if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);
            matchToken(TokenType.RIGHT_BRACKET_TOKEN);

            // Replace this once params can capture array variables
            VarExpression var = new VarExpression(name);
            param = new Param(var);
        }
        else{
            VarExpression var = new VarExpression(name);
            param = new Param(var);
        }

        return param;
    }

    private CompoundStmt parseCompoundStmt(){ 
        /* compound-stmt → “{“ local-declarations statement-list “}”
         * First(compound-stmt) → { { }
         * Follow(compound-stmt) → { $, void, int, “}”, ID, NUM, (, *, /, +, -, ;, {, if, while, return, else }
         */
        CompoundStmt CS = null;

        matchToken(TokenType.LEFT_BRACE_TOKEN);

        ArrayList<Decl> localDecls = parseLocalDecls();
        ArrayList<Statement> stmtList = parseStmtList();
        CS = new CompoundStmt(localDecls, stmtList);

        matchToken(TokenType.RIGHT_BRACE_TOKEN);

        return CS;
    }

    private ArrayList<Decl> parseLocalDecls(){ 
        /* local-declarations → {int ID var-decl}
         * First(local-declarations) → { ε, int }
         * Follow(local-declarations) → { “}”, ID, NUM, (, ;, {, if, while, return }
         */
        ArrayList<Decl> localDecls = new ArrayList<Decl>();
        Token temp;

        while(checkToken(TokenType.INT_TOKEN)){
            matchToken(TokenType.INT_TOKEN);

            temp = matchToken(TokenType.IDENT_TOKEN);
            String name = (String)temp.getData();
            Decl decl = parseVarDecl(name);
            localDecls.add(decl);
        }

        return localDecls;
    }
    
    private ArrayList<Statement> parseStmtList(){ 
        /* statement-list → {statement}
         * First(statement-list) → { ε. ID, NUM, (, ;, {, if, while, return }
         * Follow(statement-list) → { “}” }
         */
        ArrayList<Statement> SL = new ArrayList<Statement>();

        while(checkToken(TokenType.IDENT_TOKEN)
        || checkToken(TokenType.NUM_TOKEN)
        || checkToken(TokenType.LEFT_PAREN_TOKEN)
        || checkToken(TokenType.SEMI_TOKEN)
        || checkToken(TokenType.IF_TOKEN)
        || checkToken(TokenType.WHILE_TOKEN)
        || checkToken(TokenType.RETURN_TOKEN)){
            Statement S = parseStatement();
            SL.add(S);
        }

        return SL;
    }
    
    private Statement parseStatement() { 
        /* statement → expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
         * First(statement) → { ID, NUM, (, ;, {, if, while, return }
         * Follow(statement) → { }, ID, NUM, (, ;, {, if, while, return, else }
         */
        Statement S = null;

        if(checkToken(TokenType.IDENT_TOKEN)
        || checkToken(TokenType.NUM_TOKEN)
        || checkToken(TokenType.LEFT_PAREN_TOKEN)
        || checkToken(TokenType.SEMI_TOKEN)){
            S = parseExpressionStmt();
        }
        else if(checkToken(TokenType.LEFT_BRACE_TOKEN)){
            S = parseCompoundStmt();
        }
        else if(checkToken(TokenType.IF_TOKEN)){
            S = parseSelectionStmt();
        }
        else if(checkToken(TokenType.WHILE_TOKEN)){
            S = parseIterationStmt();
        }
        else if(checkToken(TokenType.RETURN_TOKEN)){
            S = parseReturnStmt();
        }

        return S;
    }
    
    private ExpressionStmt parseExpressionStmt(){ 
        /* expression-stmt → [expression] ;
         * First(expression-stmt) → { ID, NUM, (, ; }
         * Follow(expression-stmt) → { }, ID, NUM, (, ;, {, if, while, return, else }
         */
        ExpressionStmt ES = null;

        if(!checkToken(TokenType.SEMI_TOKEN)){
            Expression E = parseExpression();
            ES = new ExpressionStmt(E);
        }

        matchToken(TokenType.SEMI_TOKEN);

        return ES;
    }
    
    private SelectionStmt parseSelectionStmt(){ 
        /* selection-stmt → if “(“ expression “)” statement [else statement]
         * First(selection-stmt) → { if }
         * Follow(selection-stmt) → { }, ID, NUM, (, {, if, while, return, else }
         */
        SelectionStmt SS = null;

        matchToken(TokenType.IF_TOKEN);
        matchToken(TokenType.LEFT_PAREN_TOKEN);

        Expression condition = parseExpression();

        matchToken(TokenType.RIGHT_PAREN_TOKEN);

        Statement ifSequence = parseStatement();

        if(checkToken(TokenType.ELSE_TOKEN)){
            Statement elseSequence = parseStatement();
            SS = new SelectionStmt(condition, ifSequence, elseSequence);
        }
        else{
            SS = new SelectionStmt(condition, ifSequence);
        }

        return SS;
    }
    
    private IterationStmt parseIterationStmt(){ 
        /* iteration-stmt → while “(” expression “)” statement
         * First(iteration-stmt) → { while }
         * Follow(iteration-stmt) → { }, ID, NUM, (, {, if, while, return, else }
         */
        IterationStmt IS = null;

        return IS;
    }
    
    private ReturnStmt parseReturnStmt(){ 
        /* return-stmt → return [expression] ;
         * First(return-stmt) → { return }
         * Follow(return-stmt) → { }, ID, NUM, (, ;, {, if, while, return, else }
         */
        ReturnStmt RS = null;

        return RS;
    }
    
    private Expression parseExpression(){ 
        /* expression → ID expression’ | NUM simple-expression’ | (expression) simple-expression’
         * First(expression) → { ID, NUM, ( }
         * Follow(expression) → { ;, ), ], “,” }
         */
        Expression E = null;
        Token temp;

        if(checkToken(TokenType.IDENT_TOKEN)){
            temp = matchToken(TokenType.IDENT_TOKEN);
            String ID = (String)temp.getData();
            VarExpression var = new VarExpression(ID);

            Expression E2 = parseExpression2(var);

            // Do something with E2
        }
        else if(checkToken(TokenType.NUM_TOKEN)){
            temp = matchToken(TokenType.NUM_TOKEN);
            int value = (int)temp.getData();

            NumExpression NE = new NumExpression(value);
            Expression SE2 = parseSimpleExpr2();

            // Do something with NE and SE2
        }
        else if(checkToken(TokenType.LEFT_PAREN_TOKEN)){

        }

        return E;
    }
    
    private Expression parseExpression2(VarExpression LHS){ 
        /* expression’ → = expression | "["expression"]" expression’’ | (args) simple-expression’ | simple-expression’
         * First(expression’) → { =, [, (, ε, *, /, +, -, <, <=, >, >=, ==, != }
         * Follow(expression’) → { ;, ), ], “,” }
         */
        Expression E2 = null;

        if(checkToken(TokenType.ASSIGN_TOKEN)){
            matchToken(TokenType.ASSIGN_TOKEN);

            Expression RHS = parseExpression();
            E2 = new AssignExpression(LHS, RHS);
        }
        else if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);

            Expression E = parseExpression();

            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
;
            Expression E3 = parseExpression3(LHS);

            /*
             * Do something with all these expressions
             */
        }
        else if(checkToken(TokenType.LEFT_PAREN_TOKEN)){
            matchToken(TokenType.LEFT_PAREN_TOKEN);

            ArrayList<Expression> args = parseArgs();
            // Do a varcall with the args

            matchToken(TokenType.RIGHT_PAREN_TOKEN);

            Expression SE2 = parseSimpleExpr2();

            // Do something with the args and simple expression
        }
        else if(checkToken(TokenType.MULT_TOKEN)
        || checkToken(TokenType.DIVIDE_TOKEN)
        || checkToken(TokenType.PLUS_TOKEN)
        || checkToken(TokenType.MINUS_TOKEN)
        || checkToken(TokenType.LESS_TOKEN)
        || checkToken(TokenType.LESS_EQUAL_TOKEN)
        || checkToken(TokenType.GREATER_TOKEN)
        || checkToken(TokenType.GREATER_EQUAL_TOKEN)
        || checkToken(TokenType.EQUAL_TOKEN)
        || checkToken(TokenType.NOT_EQUAL_TOKEN)){
            Expression SE2 = parseSimpleExpr2();

            // Do something with the simple expression
        }

        return E2;
    }
    
    private Expression parseExpression3(VarExpression LHS){ 
        /* expression’’ → = expression | simple-expression’
         * First(expression’’) → {  =, ε, *, /, +, -, <, <=, >, >=, ==, !=  }
         * Follow(expression’’) → { ;, ), ], “,” }
         */
        Expression E3 = null;

        if(checkToken(TokenType.ASSIGN_TOKEN)){
            matchToken(TokenType.EQUAL_TOKEN);

            Expression RHS = parseExpression();
            E3 = new AssignExpression(LHS, RHS);
        }
        else if(checkToken(TokenType.MULT_TOKEN)
        || checkToken(TokenType.DIVIDE_TOKEN)
        || checkToken(TokenType.PLUS_TOKEN)
        || checkToken(TokenType.MINUS_TOKEN)
        || checkToken(TokenType.LESS_TOKEN)
        || checkToken(TokenType.LESS_EQUAL_TOKEN)
        || checkToken(TokenType.GREATER_TOKEN)
        || checkToken(TokenType.GREATER_EQUAL_TOKEN)
        || checkToken(TokenType.EQUAL_TOKEN)
        || checkToken(TokenType.NOT_EQUAL_TOKEN)){
            E3 = parseSimpleExpr2();
        }

        return E3;
    }
    
    private Expression parseSimpleExpr2(){ 
        /* simple-expression’ → additive-expression’ [relop additive-expression]
         * First(simple-expression’) → { ε, *, /, +, -, <, <=, >, >=, ==, != }
         * Follow(simple-expression’) → { ;, ), ], “,” }
         */
        Expression SE2 = null;
        Token temp;

        Expression AE2 = parseAdditiveExpr2();

        if(checkToken(TokenType.LESS_TOKEN)
        || checkToken(TokenType.LESS_EQUAL_TOKEN)
        || checkToken(TokenType.GREATER_TOKEN)
        || checkToken(TokenType.GREATER_EQUAL_TOKEN)
        || checkToken(TokenType.EQUAL_TOKEN)
        || checkToken(TokenType.NOT_EQUAL_TOKEN)){
            temp = advanceToken();
            String relop = (String)temp.getData();
            
            // Create a binary expression
        }

        Expression AE = parseAdditiveExpr();

        // Create a binary expression

        return SE2;
    }
    
    private Expression parseAdditiveExpr(){ 
        /* additive-expression → term {addop term}
         * First(additive-expression) → { (, ID, NUM }
         * Follow(additive-expression) → { ;, ), ], “,” }
         */
        Expression AE = null;

        return AE;
    }
    
    private Expression parseAdditiveExpr2(){ 
        /* additive-expression’ → term’ {addop term}
         * First(additive-expression’) → { ε, *, /, +, - }
         * Follow(additive-expression’) → { <, >, =, !, ;, ), ], “,” }
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
        Token temp;

        Expression factor = parseFactor();

        // Make this into an expression that T can be

        while(checkToken(TokenType.MULT_TOKEN)
        || checkToken(TokenType.DIVIDE_TOKEN)){
            temp = advanceToken();
            String mulop = (String)temp.getData();

            factor = parseFactor();

            // Make this into an expression that T can be
        }

        return T;
    }
    
    private Expression parseTerm2(){ 
        /* term’ → {mulop factor}
         * First(term’) → { ε. *, / }
         * Follow(term’) → { +, - }
         */
        Expression T2 = null;
        Token temp;

        while(checkToken(TokenType.MULT_TOKEN)
        || checkToken(TokenType.DIVIDE_TOKEN)){
            temp = advanceToken();
            String mulop = (String)temp.getData();

            Expression factor = parseFactor();

            // Make this into an expression that T can be
        }

        return T2;
    }
    
    private Expression parseFactor(){ 
        /* factor → “(” expression “)” | ID varcall | NUM
         * First(factor) → { (, ID, NUM }
         * Follow(factor) → { *, /, +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression F = null;
        Token temp;

        if(checkToken(TokenType.LEFT_PAREN_TOKEN)){
            matchToken(TokenType.LEFT_PAREN_TOKEN);

            F = parseExpression();

            matchToken(TokenType.RIGHT_PAREN_TOKEN);
        }
        else if(checkToken(TokenType.IDENT_TOKEN)){
            temp = matchToken(TokenType.IDENT_TOKEN);
            String name = (String)temp.getData();
            VarExpression ID = new VarExpression(name);

            F = parseVarCall(ID);
        }
        else if(checkToken(TokenType.NUM_TOKEN)){
            temp = matchToken(TokenType.NUM_TOKEN);
            int value = (int)temp.getData();
            F = new NumExpression(value);
        }

        return F;
    }
    
    private Expression parseVarCall(VarExpression ID){ 
        /* varcall → “(“ args “)” | “[“ expression “]” | ε
         * First(varcall) → { (, [, ε }
         * Follow(varcall) → { *, /, +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression varCall = null;

        if(checkToken(TokenType.LEFT_PAREN_TOKEN)){
            matchToken(TokenType.LEFT_PAREN_TOKEN);
    
            ArrayList<Expression> args = parseArgs();
    
            matchToken(TokenType.RIGHT_PAREN_TOKEN);

            varCall = new CallExpression(ID, args);
        }
        else if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);

            Expression E = parseExpression();

            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
        }


        return varCall;
    }
    
    private ArrayList<Expression> parseArgs(){ 
        /* args → arg-list | ε
         * First(args) → { ID, NUM, (, ε }
         * Follow(args) → { ) }
         */
        ArrayList<Expression> args = null;

        if(checkToken(TokenType.IDENT_TOKEN)
        || checkToken(TokenType.NUM_TOKEN)
        || checkToken(TokenType.LEFT_PAREN_TOKEN)){
            args = parseArgList();
        }

        return args;
    }
    
    private ArrayList<Expression> parseArgList(){ 
        /* arg-list → expression {, expression}
         * First(arg-list) → { ID, NUM, ( }
         * Follow(arg-list) → { ) }
         */
        ArrayList<Expression> argList = new ArrayList<Expression>();

        Expression E = parseExpression();
        argList.add(E);

        while(checkToken(TokenType.COMMA_TOKEN)){
            matchToken(TokenType.COMMA_TOKEN);
            
            E = parseExpression();
            argList.add(E);
        }
        
        return argList;
    }

    /* Print AST */
    public void printTree(){
        
    }
}
