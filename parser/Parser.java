package parser;
import parser.CMinusParser.Program;

public interface Parser {
    public Program parseProgram() throws Exception;
    public void printTree();
}
