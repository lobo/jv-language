import org.objectweb.asm.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FactorialDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "Factorial", null, "java/lang/Object", null);

        cw.visitSource("Factorial.java", null);

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
            mv.visitLocalVariable("this", "LFactorial;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC, "factorial", "(J)J", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(3, l0);
            mv.visitVarInsn(LLOAD, 0);
            mv.visitInsn(LCONST_1);
            mv.visitInsn(LCMP);
            Label l1 = new Label();
            mv.visitJumpInsn(IFGT, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(4, l2);
            mv.visitInsn(LCONST_1);
            mv.visitInsn(LRETURN);
            mv.visitLabel(l1);
            mv.visitLineNumber(6, l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(LLOAD, 0);
            mv.visitVarInsn(LLOAD, 0);
            mv.visitInsn(LCONST_1);
            mv.visitInsn(LSUB);
            mv.visitMethodInsn(INVOKESTATIC, "Factorial", "factorial", "(J)J", false);
            mv.visitInsn(LMUL);
            mv.visitInsn(LRETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("number", "J", null, l0, l3, 0);
            mv.visitMaxs(6, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(10, l0);
            mv.visitIntInsn(BIPUSH, 6);
            mv.visitVarInsn(ISTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(11, l1);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("Factorial de ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(12, l2);
            mv.visitInsn(ICONST_1);
            mv.visitVarInsn(ISTORE, 2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_APPEND, 2, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER}, 0, null);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitVarInsn(ILOAD, 1);
            Label l4 = new Label();
            mv.visitJumpInsn(IF_ICMPGT, l4);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(13, l5);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(": ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitInsn(I2L);
            mv.visitMethodInsn(INVOKESTATIC, "Factorial", "factorial", "(J)J", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLineNumber(12, l6);
            mv.visitIincInsn(2, 1);
            mv.visitJumpInsn(GOTO, l3);
            mv.visitLabel(l4);
            mv.visitLineNumber(14, l4);
            mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
            mv.visitInsn(RETURN);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLocalVariable("i", "I", null, l3, l4, 2);
            mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l7, 0);
            mv.visitLocalVariable("n", "I", null, l1, l7, 1);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static void main(String[] args) {
        try {
            Path file = Paths.get("examples/java/gen/Factorial.class");
            Files.write(file, FactorialDump.dump());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
