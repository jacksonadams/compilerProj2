package parser;
import parser.CMinusParser.Program;

public interface Parser {
    public Program parseProgram();
    public void printTree();
}
