/* 
    Main Class

    Test the functionality of the parser by opening an 
    input file containing C- code using the Scanner from 
    Project 1, creating an abstract syntax tree, and then 
    printing that abstract syntax tree to a file.

    Design the output format so that it can be easily 
    understood and easily read back in.
*/

package parser;

import scanner.CMinusScanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {

        // Get the input file that has the C- code
        File inputFile = new File("scanner/input.txt");
        FileReader codeFile = new FileReader(inputFile);
        BufferedReader inputReader = new BufferedReader(codeFile);
        
        // Create scanner
        CMinusScanner myScanner = new CMinusScanner(inputReader);

        // Get the output file to print into
        FileWriter outputFile = new FileWriter("scanner/output.txt"); 

        // Create parser
        CMinusParser myParser = new CMinusParser(myScanner);

        // Close output file
        outputFile.close();
    }
}
