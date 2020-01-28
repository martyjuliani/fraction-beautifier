package com.juleq.beautifier;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Beautifier {

    public static void main(String[] args) {
        validateInput(args);
        Expression result = simplify(args[0]);
        print(result);
    }

    private static Expression simplify(String source) {
        CodePointCharStream input = CharStreams.fromString(source);
        return compile(input);
    }

    private static Expression compile(CharStream source) {
        BeautifierLexer lexer = new BeautifierLexer(source);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        BeautifierParser parser = new BeautifierParser(tokenStream);
        ParseTree tree = parser.expr();
        BeautifierVisitorImpl visitor = new BeautifierVisitorImpl();
        return visitor.visit(tree);
    }

    private static void validateInput(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect parameter number has entered to the command line.");
        }
        String pattern = "[-+/*()\\d]+";
        if (!args[0].matches(pattern)) {
            throw new IllegalArgumentException("Expression with forbidden characters has entered.");
        }
    }

    private static void print(Expression result) {
        result.getMatrix().forEach((k, v) -> System.out.println(v));
    }
}