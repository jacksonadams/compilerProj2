package parser;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

import javax.security.auth.x500.X500Principal;

import scanner.CMinusScanner;
import scanner.Token;
import scanner.Token.TokenType;

public class CMinusParser implements Parser {
    /*
     * This is a recursive descent parser.
     * Given a program written in the C- language,
     * Create an Abstract Syntax Tree (AST).
     * 
     * Use the Scanner from Project #1 to scan tokens.
     * 
     * Classes
     * 1. Program
     * 2. Param
     * 3. Decl
     * 4. VarDecl
     * 5. FunDecl
     * 6. Statement
     * 7. ExpressionStmt
     * 8. CompoundStmt
     * 9. SelectionStmt
     * 10. IterationStmt
     * 11. ReturnStmt
     * 12. Expression
     * 13. AssignExpression
     * 14. BinaryExpression
     * 15. CallExpression
     * 16. NumExpression
     * 17. VarExpression
     * 
     * Parse Functions
     * 1. parseProgram
     * 2. parseDecl
     * 3. parseDecl2
     * 4. parseFunDecl
     * 5. parseParams
     * 6. parseParamList
     * 7. parseParam
     * 8. parseCompoundStmt
     * 9. parseLocalDecl
     * 10. parseStatementList
     * 11. parseStatement
     * 12. parseExpressionStmt
     * 13. parseSelectionStmt
     * 14. parseIterationStmt
     * 15. parseReturnStmt
     * 16. parseExpression
     * 17. parseExpression2
     * 18. parseExpression3
     * 19. parseSimpleExpr2
     * 20. parseAdditiveExpr
     * 21. parseAdditiveExpr2
     * 22. parseTerm
     * 23. parseTerm2
     * 24. parseFactor
     * 25. parseVarCall
     * 26. parseArgs
     * 27. parseArgList
     * 
     * Extra Functions
     * 1. matchToken() - check and advance
     * 2. advanceToken() - just advance
     * 3. checkToken() - just check
     * 
     * What should AST look like?
     * 
     * Program {
     * =
     * int x
     * 10
     * 
     * =
     * int y
     * 20
     * 
     * int z
     * 
     * int myFunction {
     * Params {
     * int x1
     * int x2
     * }
     * =
     * int sum
     * +
     * x1
     * x2
     * return
     * sum
     * }
     * }
     */

    /* Constructor */
    private CMinusScanner scanner;
    public Program program;

    public CMinusParser(CMinusScanner inScanner) throws Exception{
        scanner = inScanner;
        program = parseProgram();

        // For debugging purposes
        // program.print();
    }

    /* Helper functions */
    public Boolean checkToken(TokenType token) {
        return (scanner.viewNextToken().getType() == token);
    }

    public Token advanceToken() {
        return scanner.getNextToken();
    }

    /*public Token matchToken(TokenType token) {
        Token nextToken = scanner.getNextToken();

        if (nextToken.getType() != token) {
            // throw error
        }

        return nextToken;
    }*/
    public void matchToken (TokenType token) throws Exception{
        TokenType match = scanner.getNextToken().getType();
        if(match != token){
            throw new Exception("Token match error");
        }
        // otherwise, continue
    }

    /* 17 classes */
    public class Program {
        public ArrayList<Decl> decls;

        public Program(ArrayList<Decl> decls) {
            this.decls = decls;
        }

        void print() {
            System.out.println("Program {");
            for (int i = 0; i < decls.size(); i++) {
                if (decls.get(i) != null) {
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

        public FunDecl(String returnType, VarExpression name, List<Param> params, CompoundStmt content) {
            this.returnType = returnType;
            this.name = name;
            this.params = params;
            this.content = content;
        }

        void print(String parentSpace) {
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + this.returnType);
            name.print(mySpace);
            System.out.println(mySpace + "Params {");
            for (int i = 0; i < params.size(); i++) {
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
        // ast:
        // =
        // a
        // 3
        VarExpression LHS;
        Expression RHS;

        public ExpressionStmt(VarExpression LHS, Expression RHS) {
            this.LHS = LHS;
            this.RHS = RHS;
        }

        void print(String parentSpace) {
            String mySpace = parentSpace + "  ";
            System.out.println(mySpace + "");
        }
    }

    public class CompoundStmt extends Statement {
        // a sequence of other statements inside { }
        // example: { x = 3; y = y + 3; }
        public CompoundStmt(ArrayList<Decl> localDecls, ArrayList<Statement> statements) {

        }

        void print(String parentSpace) {
        }
    }

    public class SelectionStmt extends Statement {
        // example: if (statement) { } else { }
        public SelectionStmt(Expression condition, Statement ifSequence, Statement elseSequence) {

        }
        public SelectionStmt(Expression condition, Statement ifSequence, Statement elseSequence) {

        }

        void print(String parentSpace) {
        }
    }

    public class IterationStmt extends Statement {
        // example: while (x > 0) { }
        public IterationStmt(Expression condition, CompoundStmt sequence) {

        }

        void print(String parentSpace) {
        }
    }

    public class ReturnStmt extends Statement {
        // example: return x;
        // could also be blank: return;
        public ReturnStmt(Expression LHS) {

        }

        void print(String parentSpace) {
        }
    }

    abstract class Expression {
        // abstract expression, will be one of the other 5
        abstract void print(String parentSpace);
    }

    public class AssignExpression extends Expression {
        // example: x = y, x = 3
        // has to be a var on the left
        public AssignExpression(VarExpression LHS, Expression RHS) {

        }

        void print(String parentSpace) {
        }
    }

    public class BinaryExpression extends Expression {
        // example: 3 + 4, a + b
        public BinaryExpression(Expression LHS, TokenType op, Expression RHS) {

        }

        void print(String parentSpace) {
        }
    }

    public class CallExpression extends Expression {
        // example: gcd(3, 4)
        public CallExpression(VarExpression LHS, Expression[] args) {

        }

        void print(String parentSpace) {
        }
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
    public Program parseProgram() throws Exception {
        // program -> decl {decl}
        // first(program) = {void, int}
        // follow(program) = {$}

        // Program returnProgram = new Program();

        ArrayList<Decl> declList = new ArrayList<Decl>();

        // check if next token is in first set
        while (checkToken(TokenType.INT_TOKEN) || checkToken(TokenType.VOID_TOKEN)) {
            Decl nextDecl = parseDecl();
            declList.add(nextDecl);
        }

        // if we're no longer in the first set, check if we're in the follow set - if
        // yes, continue, if not, error
        /*if (!checkToken(TokenType.EOF_TOKEN)) {
            throw new Exception("Syntax error: expected int or void. (parseProgram)");
        }*/

        return new Program(declList);
    }

    private Decl parseDecl() throws Exception {
        // decl -> void ID fun-decl | int ID decl'
        // first(decl) = {void, int}
        // follow(decl) = {$, int, void}
        /* 
        Decl decl = null;

        if (checkToken(TokenType.VOID_TOKEN)) {
            Token temp;

            matchToken(TokenType.VOID_TOKEN);
            String returnType = "void";

            temp = matchToken(TokenType.IDENT_TOKEN);
            VarExpression name = new VarExpression((String) temp.getData());

            // decl = parseFunDecl();
        } else if (checkToken(TokenType.INT_TOKEN)) {
            Token temp;

            matchToken(TokenType.INT_TOKEN);

            temp = matchToken(TokenType.IDENT_TOKEN);
            VarExpression name = new VarExpression((String) temp.getData());

            // Expression RHS = parseDecl2();

        }*/

        Decl decl = null;

        if(checkToken(TokenType.VOID_TOKEN)){
            String returnType = "void";
            matchToken(TokenType.VOID_TOKEN);
            VarExpression name = new VarExpression((String)scanner.getNextToken().getData());
            return parseFunDecl(returnType, name);
        } else if (checkToken(TokenType.INT_TOKEN)){
            String returnType = "int";
            matchToken(TokenType.INT_TOKEN);
            String name = (String)scanner.getNextToken().getData();
            return parseDecl2(returnType, name);
        }

        return decl;
    }

    private Decl parseDecl2(String returnType, String name) throws Exception {
        /*
         * decl' → ; | [NUM] | fun-decl
         * First(decl') → { ;, [, ( }
         * Follow(decl') → { $, void, int }
         */
        Decl decl2 = null;

        if (checkToken(TokenType.SEMI_TOKEN)) {
            matchToken(TokenType.SEMI_TOKEN);
            return new VarDecl(new VarExpression(name));
        } else if (checkToken(TokenType.LEFT_BRACKET_TOKEN)) {
            matchToken(TokenType.LEFT_BRACKET_TOKEN);
            VarExpression varName = new VarExpression(name, (int)scanner.getNextToken().getData());
            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
            return new VarDecl(varName);
        } else if (checkToken(TokenType.LEFT_PAREN_TOKEN)) {
            decl2 = parseFunDecl(returnType, new VarExpression(name));
        } else {
            throw new Exception("Syntax error: Was expecting ; [ or (");
        }

        return decl2;
    }

    private Decl parseVarDecl(String name) throws Exception {
        /*
         * var-decl → ; | [NUM]
         * First(var-decl) → { ;, [ }
         * Follow(var-decl) → { int, “}”, ;, ID, NUM, (, *, /, +, -, ;, {, if, while,
         * return }
         */
        Decl varDecl = null;

        if(checkToken(TokenType.SEMI_TOKEN)){
            matchToken(TokenType.SEMI_TOKEN);
        } else if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);
            VarExpression varName = new VarExpression(name, (int)scanner.getNextToken().getData());
            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
        } else {
            throw new Exception("Syntax error: was expecting ; or [");
        }

        return varDecl;
    }

    private Decl parseFunDecl(String type, VarExpression name) throws Exception {
        /*
         * fun-decl → “(” params-list “)” compound-stmt
         * First(fun-decl) → { ( }
         * Follow(fun-decl) → { $, void, int, }
         */
        matchToken(TokenType.LEFT_PAREN_TOKEN);
        ArrayList<Param> params = parseParams();
        matchToken(TokenType.RIGHT_PAREN_TOKEN);
        CompoundStmt content = parseCompoundStmt();

        return new FunDecl(type, name, params, content);
    }

    private ArrayList<Param> parseParams() throws Exception {
        /*
         * params → param-list | void
         * First(params) → { int, void }
         * Follow(params) → { ) }
         */
        ArrayList<Param> params = new ArrayList<Param>();

        if(checkToken(TokenType.INT_TOKEN)){
            params = parseParamsList();
        } else if (checkToken(TokenType.RIGHT_PAREN_TOKEN)){
            // end of params, continue with empty list
        } else {
            throw new Exception("Syntax error: was expecting end of params )");
        }

        return params;
    }

    private ArrayList<Param> parseParamsList() throws Exception {
        /*
         * param-list → param {, param}
         * First(params-list) → { int }
         * Follow(param-list) → { ) }
         */
        ArrayList<Param> paramList = new ArrayList<Param>();

        Param nextParam = parseParam();
        paramList.add(nextParam);

        while(checkToken(TokenType.COMMA_TOKEN)){
            nextParam = parseParam();
            paramList.add(nextParam);
        }

        return paramList;
    }

    private Param parseParam() throws Exception {
        /*
         * param → int ID [“[“ “]”]
         * First(param) → { int }
         * Follow(param) → { “,”, ) }
         */
        matchToken(TokenType.INT_TOKEN);
        VarExpression name = new VarExpression((String)scanner.getNextToken().getData());
        if(checkToken(TokenType.LEFT_BRACKET_TOKEN)){
            matchToken(TokenType.LEFT_BRACKET_TOKEN);
            matchToken(TokenType.RIGHT_BRACKET_TOKEN);
        }

        return new Param(name);
    }

    private CompoundStmt parseCompoundStmt() throws Exception {
        /*
         * compound-stmt → “{“ local-declarations statement-list “}”
         * First(compound-stmt) → { { }
         * Follow(compound-stmt) → { $, void, int, “}”, ID, NUM, (, *, /, +, -, ;, {,
         * if, while, return, else }
         */

        matchToken(TokenType.LEFT_BRACE_TOKEN);
        ArrayList<Decl> localDecls = parseLocalDeclarations();
        ArrayList<Statement> statementList = parseStatementList();
        System.out.println("Looking for }");
        matchToken(TokenType.RIGHT_BRACE_TOKEN);

        return new CompoundStmt(localDecls, statementList);
    }

    private ArrayList<Decl> parseLocalDeclarations() throws Exception{
        /*
         * local-declarations → {int ID var-decl}
         * First(local-declarations) → { ε, int }
         * Follow(local-declarations) → { “}”, ID, NUM, (, *, /, +, -, ;, {, if, while,
         * return }
         */
        ArrayList<Decl> localDecls = new ArrayList<Decl>();

        while(checkToken(TokenType.INT_TOKEN)){
            matchToken(TokenType.INT_TOKEN);
            String name = (String)scanner.getNextToken().getData();
            Decl nextDecl = parseVarDecl(name);
            localDecls.add(nextDecl);
        }

        return localDecls;
    }

    private ArrayList<Statement> parseStatementList() throws Exception {
        /*
         * statement-list → {statement}
         * First(statement-list) → { ε. ID, NUM, (, *, /, +, -, ;, {, if, while, return
         * }
         * Follow(statement-list) → { “}” }
         */
        ArrayList<Statement> statements = new ArrayList<Statement>();

        while(checkToken(TokenType.IDENT_TOKEN) || 
        checkToken(TokenType.NUM_TOKEN) ||
        checkToken(TokenType.LEFT_PAREN_TOKEN) || 
        checkToken(TokenType.MULT_TOKEN) ||
        checkToken(TokenType.DIVIDE_TOKEN) ||
        checkToken(TokenType.PLUS_TOKEN) || 
        checkToken(TokenType.MINUS_TOKEN) ||
        checkToken(TokenType.SEMI_TOKEN) ||
        checkToken(TokenType.LEFT_BRACE_TOKEN) || 
        checkToken(TokenType.IF_TOKEN)||
        checkToken(TokenType.WHILE_TOKEN) || 
        checkToken(TokenType.RETURN_TOKEN)){
            System.out.println("reading statements");
            Statement nextStatement = parseStatement();
            statements.add(nextStatement);
        }

        if(!checkToken(TokenType.RIGHT_BRACE_TOKEN)){
            throw new Exception("Expected } at end of statement list.");
        }

        return statements;
    }

    private Statement parseStatement() throws Exception{
        /*
         * statement → expression-stmt | compound-stmt | selection-stmt | iteration-stmt
         * | return-stmt
         * First(statement) → { ID, NUM, (, ε, *, /, +, -, ;, {, if, while, return }
         * Follow(statement) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        Statement S = null;

        if(checkToken(TokenType.IDENT_TOKEN) || checkToken(TokenType.NUM_TOKEN) || checkToken(TokenType.LEFT_PAREN_TOKEN) || checkToken(TokenType.MULT_TOKEN)|| checkToken(TokenType.DIVIDE_TOKEN)|| checkToken(TokenType.PLUS_TOKEN)|| checkToken(TokenType.MINUS_TOKEN)|| checkToken(TokenType.SEMI_TOKEN)){
            return parseExpressionStmt();
        } else if (checkToken(TokenType.IF_TOKEN)){
            return parseSelectionStmt();
        } else if (checkToken(TokenType.WHILE_TOKEN)){
            return parseIterationStmt();
        } else if (checkToken(TokenType.RETURN_TOKEN)){
            return parseReturnStmt();
        } else if (checkToken(TokenType.LEFT_BRACE_TOKEN)){
            return parseCompoundStmt();
        }

        return S;
    }

    private ExpressionStmt parseExpressionStmt() {
        /*
         * expression-stmt → [expression] ;
         * First(expression-stmt) → { ID, NUM, (, ε, *, /, +, -, ; }
         * Follow(expression-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return,
         * else }
         */
        ExpressionStmt ES = null;

        System.out.println("Parsing Expression Stmt");

        return ES;
    }

    private SelectionStmt parseSelectionStmt() throws Exception{
        /*
         * selection-stmt → if “(“ expression “)” statement [else statement]
         * First(selection-stmt) → { if }
         * Follow(selection-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else
         * }
         */
        SelectionStmt SS = null;
        System.out.println("Parsing Selection Stmt");

        matchToken(TokenType.IF_TOKEN);
        matchToken(TokenType.LEFT_PAREN_TOKEN);
        Expression condition = parseExpression();
        matchToken(TokenType.RIGHT_PAREN_TOKEN);
        Statement ifSequence = parseStatement();
        if(checkToken(TokenType.ELSE_TOKEN)){
            matchToken(TokenType.ELSE_TOKEN);
            Statement elseSequence = parseStatement();
            return new SelectionStmt(condition, ifSequence, elseSequence);
        }

        return SS;
    }

    private IterationStmt parseIterationStmt() {
        /*
         * iteration-stmt → while “(” expression “)” statement
         * First(iteration-stmt) → { while }
         * Follow(iteration-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else
         * }
         */
        IterationStmt IS = null;
        System.out.println("Parsing Iteration Stmt");

        return IS;
    }

    private ReturnStmt parseReturnStmt() {
        /*
         * return-stmt → return [expression] ;
         * First(return-stmt) → { return }
         * Follow(return-stmt) → { }, ID, NUM, (, *, /, ;, {, if, while, return, else }
         */
        ReturnStmt RS = null;

        System.out.println("Parsing Return Stmt");
        return RS;
    }

    private Expression parseExpression() {
        /*
         * expression → ID expression’ | NUM simple-expression’ | (expression)
         * simple-expression’
         * First(expression) → { ID, NUM, (, ε, *, /, +, - }
         * Follow(expression) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression E = null;
        System.out.println("Parsing Expression");

        return E;
    }

    private Expression parseExpression2() {
        /*
         * expression’ → = expression | [expression] expression’’ | (args)
         * simple-expression’ | simple-expression’
         * First(expression’) → { =, ID, NUM, (, ε, *, /, +, - }
         * Follow(expression’) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression E2 = null;


        System.out.println("Parsing Expression 2");

        return E2;
    }

    private Expression parseExpression3() {
        /*
         * expression’’ → = expression | simple-expression’
         * First(expression’’) → { ID, NUM, (, ε, *, /, +, - }
         * Follow(expression’’) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression E3 = null;


        System.out.println("Parsing Expression 3");

        return E3;
    }

    private Expression parseSimpleExpr2() {
        /*
         * simple-expression’ → additive-expression’ [relop additive expression]
         * First(simple-expression’) → { ε, *, /, +, - }
         * Follow(simple-expression’) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression SE2 = null;

        return SE2;
    }

    private Expression parseAdditiveExpr() {
        /*
         * additive-expression → term {addop term}
         * First(additive-expression) → { (, ID, NUM }
         * Follow(additive-expression) → { ;, ), ], “,”, *, /, +, - }
         */
        Expression AE = null;

        return AE;
    }

    private Expression parseAdditiveExpr2() {
        /*
         * additive-expression’ → term’ {addop term}
         * First(additive-expression’) → { ε, *, /, +, - }
         * Follow(additive-expression’) → { <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression AE2 = null;

        return AE2;
    }

    private Expression parseTerm() {
        /*
         * term → factor {mulop factor}
         * First(term) → { (, ID, NUM }
         * Follow(term) → { +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression T = null;

        return T;
    }

    private Expression parseTerm2() {
        /*
         * term’ → {mulop factor}
         * First(term’) → { ε. *, / }
         * Follow(term’) → { +, - }
         */
        Expression T2 = null;

        return T2;
    }

    private Expression parseFactor() {
        /*
         * factor → “(” expression “)” | ID varcall | NUM
         * First(factor) → { (, ID, NUM }
         * Follow(factor) → { *, /, +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        Expression F = null;

        return F;
    }

    private CallExpression parseVarCall() {
        /*
         * varcall → “(“ args “)” | “[“ expression “]” | ε
         * First(varcall) → { (, [, ε }
         * Follow(varcall) → { *, /, +, -, <, >, =, !, ;, ), ], “,”, *, /, +, - }
         */
        CallExpression varcall = null;

        return varcall;
    }

    private Expression parseArgs() {
        /*
         * args → arg-list | ε
         * First(args) → { ID, NUM, (, ε, *, / }
         * Follow(args) → { ) }
         */
        Expression args = null;

        return args;
    }

    private ArrayList<Expression> parseArgList() {
        /*
         * arg-list → expression {, expression}
         * First(arg-list) → { ID, NUM, (, ε, *, / }
         * Follow(arg-list) → { ) }
         */
        ArrayList<Expression> argList = null;

        return argList;
    }

    /* Print AST */
    public void printTree() {
        program.print();
    }
}
