import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class SandboxDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "Sandbox", null, "java/lang/Object", null);

        cw.visitSource("Sandbox.java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(1, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "LSandbox;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(5, l0);
            mv.visitInsn(ICONST_4);
            mv.visitVarInsn(ISTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(6, l1);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(ICONST_2);
            mv.visitInsn(IMUL);
            mv.visitVarInsn(ISTORE, 2);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(8, l2);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitVarInsn(ILOAD, 1);
            Label l3 = new Label();
            mv.visitJumpInsn(IF_ICMPNE, l3);
            mv.visitInsn(ICONST_1);
            Label l4 = new Label();
            mv.visitJumpInsn(GOTO, l4);
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_APPEND, 2, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER}, 0, null);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(l4);
            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            mv.visitVarInsn(ISTORE, 3);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(11, l5);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ILOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLineNumber(12, l6);
            mv.visitInsn(RETURN);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l7, 0);
            mv.visitLocalVariable("n1", "I", null, l1, l7, 1);
            mv.visitLocalVariable("n2", "I", null, l2, l7, 2);
            mv.visitLocalVariable("b1", "Z", null, l5, l7, 3);
            mv.visitMaxs(2, 4);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static void main(String[] args) {
        try {
            Path file = Paths.get("examples/java/gen/HelloWorld.class");
            Files.write(file, SandboxDump.dump());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
