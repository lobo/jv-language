package atlc.nodes;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

@SuppressWarnings("unchecked")
public class ProgramFactory implements Opcodes {
    public static ClassWriter create(String name, List<MethodNode> methods, InsnList main) {
        ClassWriter cw = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
        MethodVisitor mv;
        cw.visit(
            V1_8,
            ACC_PUBLIC + ACC_SUPER,
            name,
            null,
            "java/lang/Object",
            null
        );
        methods.forEach(methodNode -> methodNode.accept(cw));
        {
            mv = cw.visitMethod(
                ACC_PUBLIC + ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null
            );
            mv.visitCode();
            main.accept(mv);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw;
    }
}
