package parser;
import java.io.FileWriter;
import java.io.IOException;

import parser.CMinusParser.Program;

public interface Parser {
    public Program parseProgram() throws Exception;
    public void printTree(FileWriter outputProgram) throws IOException;
}
