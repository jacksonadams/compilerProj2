package parser;

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

    /* Helper functions */
    public void checkToken (TokenType token){

    }
    public void advanceToken (){

    }
    public void matchToken(TokenType token) {
        checkToken(token);
        advanceToken();
    }
    
    /* 17 classes */
    public class Program {
        public Program(Decl[] declList) {

        }

        public void print (String pSpace) {
            String mySpace = pSpace + "    ";
        }
    }
    public class Param { 
        // example: int x
    }
    public class Decl { 
        // abstract, will be either varDecl or funDecl
    }
    public class VarDecl { 
        // example: int x 
        // or int x = 10
    }
    public class FunDecl { 
        // example: int gcd (int x, int y) { }
    }
    public class Statement { 
        // abstract, will be one of the other 5 statements
    }
    public class ExpressionStmt { 
        // example: a = 3;
    }
    public class CompoundStmt { 
        // a sequence of other statements inside { }
        // example: { x = 3; y = y + 3; }
    }
    public class SelectionStmt { 
        // example: if (statement) { } else { }
    }
    public class IterationStmt { 
        // example: while (x > 0) { }
    }
    public class ReturnStmt { 
        // example: return x;
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
        public CallExpression (VarExpression LHS, Param[] params){

        }
    }
    public class NumExpression {
        // example: 3

    }
    public class VarExpression {
        // example: x
    }

    /* Parse Functions */
    public void parseProgram (){

    }

    /* Print AST */
    public void printTree(){

    }
}
