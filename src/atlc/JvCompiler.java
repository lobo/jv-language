package atlc;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class JvCompiler implements Opcodes {

    public static final Logger l = Logger.getLogger("JvCompiler");

    private static String SOURCE_EXTENSION = ".jv";

    public static void main(String args[]) throws Exception {
        // Parser.l.setLevel(Level.WARNING);
        String sourceFile = args[0];

        if (sourceFile.lastIndexOf(SOURCE_EXTENSION) == -1) {
            System.out.println("Invalid input file");
            return;
        }
        FileReader reader;
        try {
            reader = new FileReader(sourceFile);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid input file");
            return;
        }

        String className = getClassName(sourceFile);
        ClassWriter cw = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
        Symbol out = new Parser(new Scanner(reader, symbolFactory), symbolFactory, cw, className, () -> {
            try {
                writeClass(className, cw);
                generateJar(className);
            } catch (Exception e) {
                // Nothing to do
            }
        }).parse();
    }

    private static void writeClass(String className, ClassWriter cw) throws IOException {
        Path file = Paths.get("examples/jv/gen/" + className + ".class");
        Files.write(file, cw.toByteArray());
        System.out.println("Class generated at " + file.toString());
    }

    private static int generateJar(String className) throws Exception {
        if (!jarCommandAvailable()) {
            System.out.println("jar command not available. Skipping jar generation.");
            return 1;
        }
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("jar cfe app.jar " + className + " -C ./examples/jv/gen/ " + className + ".class");
        if (proc.waitFor() == 0) {
            System.out.println("Compilation successful. \nJar created: app.jar");
            return 0;
        }
        return 1;
    }

    private static String getClassName(String fileName) {
        String javaClass = FileSystems.getDefault().getPath(fileName).getFileName().toString();
        String className = javaClass.substring(0, 1).toUpperCase() + javaClass.substring(1);
        return className.substring(0,className.lastIndexOf(SOURCE_EXTENSION));
    }

    private static boolean jarCommandAvailable() throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("which jar"); // *nix
        if (proc.waitFor() == 0) {
            return true;
        }
        proc = rt.exec("where jar"); // windows
        if (proc.waitFor() == 0) {
            return true;
        }
        return false;
    }
}
