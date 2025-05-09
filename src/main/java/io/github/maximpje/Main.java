package io.github.maximpje;

// import monster rah
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static boolean hadError = false;


    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token); // temporary prints tokens
        }
        if (hadError) System.exit(65);
    }

    // called when an error occurs
    static void error(int line, String message) {
        report(line, "", message);
    }

    // default error report
    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    // passes a file to the run method
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    // prompts LOX code and passes to the run method
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }


    public static void main(String[] args) throws IOException{

        if (args.length > 1) { // exits if multiple args given; only one expected
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) { // passes the given file path to the runFile method
            runFile(args[0]);
        } else { // prompt LOX code when no args given
            runPrompt();
        }
    }
}
