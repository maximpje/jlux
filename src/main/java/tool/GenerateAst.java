package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

// generate abstract classes

public class GenerateAst {
    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {

        writer.println(" static class " + className + " extends " + baseName + " {"); // Constructor.
        writer.println(" " + className + "(" + fieldList + ") {"); // Store parameters in fields.

        String[] fields = fieldList.split(", ");

        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(" this." + name + " = " + name + ";");
        }

        writer.println(" }");
        writer.println();

        // Fields
        for (String field : fields) {
            writer.println(" final " + field + ";");
        }
        writer.println(" }");
    }
    private static void defineAst(
            String outputDir, String baseName, List<String> types) // Fields
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("package io.github.maximpje;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        writer.println("}");
        writer.close();
    }
    public static void main(String[] args) throws IOException {
//        if (args.length != 1) {
//            System.err.println("Usage: generate_ast <output directory>");
//            System.exit(64);
//        }
        //String outputDir = args[0];
        String outputDir = "src/main/java/io/github/maximpje";
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal : Object value",
                "Unary : Token operator, Expr right"
        ));
    }
}
