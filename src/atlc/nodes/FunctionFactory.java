package atlc.nodes;

import atlc.Context;
import atlc.expr.ArithmeticFactory;
import atlc.expr.ExceptionFactory;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.Type;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FunctionFactory {

    public void writeLine(Function<Context, Type> expr, Context context) {
        write(expr, context, true);
    }

    public void write(Function<Context, Type> expr, Context context) {
        write(expr, context, false);
    }

    public static void read(Function<Context, Type> targetType, Consumer<Context> target, Context context) {
        context.getGa().newInstance(Type.getType(Scanner.class));
        context.getGa().dup(); // Duplicate since we are constructing and calling
        context.getGa().getStatic(Type.getType(System.class), "in", Type.getType(InputStream.class));
        context.getGa().invokeConstructor(Type.getType(Scanner.class), Method.getMethod("void <init> (java.io.InputStream)"));
        Type type = targetType.apply(context);
        context.getGa().invokeVirtual(Type.getType(Scanner.class), getReadMethod(type));
        target.accept(context);
    }

    private static Method getReadMethod(Type type) {
        if (type.equals(Type.getType(Integer.class)) || type.equals(Type.INT_TYPE)) {
            return new Method("nextInt", Type.INT_TYPE, new Type[] {});
        }
        if (type.equals(Type.getType(Boolean.TYPE)) || type.equals(Type.BOOLEAN_TYPE)) {
            return new Method("nextBoolean", Type.BOOLEAN_TYPE, new Type[] {});
        }
        return new Method("nextLine", Type.getType(String.class), new Type[] {});
    }

    private static void write(Function<Context, Type> expr, Context context, boolean newLine) {
        context.getGa().getStatic(Type.getType(System.class), "out", Type.getType(PrintStream.class));
        Type type = expr.apply(context);
        context.getGa().invokeVirtual(
                Type.getType(PrintStream.class),
                new Method(newLine ? "println" : "print", Type.VOID_TYPE, new Type[] {type})
        );
    }

    public static void exit(Context context) {
        exit(context, new ArithmeticFactory().createLiteral(0));
    }

    public static void exit(Context context, Function<Context, Type> expr) {
        Type outType = expr.apply(context);
        if (!outType.equals(Type.INT_TYPE) || !outType.equals(Type.getType(Integer.class))) {
            // TODO: Throw exception since input is invalid
        }
        context.getGa().invokeStatic(
                Type.getType(System.class),
                new Method("exit", Type.VOID_TYPE, new Type[] {Type.INT_TYPE})
        );

    }

    public Consumer<Context> createFn(
            Type returnType,
            String name,
            List<Function<Context, Type>> arguments,
            Consumer<Context> closure
            ) {
        return context -> {
            List<Type> tl = new ArrayList<>();
            for (Function<Context, Type> argument : arguments) {
                tl.add(argument.apply(context));
            }
            Type[] types = tl.toArray(new Type[tl.size()]);
            context.start(new Method(name, returnType, types));
            closure.accept(context);
            // TODO save reference (and maybe create invoker code) to invoke later
            context.endMethod();
        };
    }

    public Consumer<Context> createFn(
            String name,
            List<Function<Context, Type>> arguments,
            Consumer<Context> closure
    ) {
        return this.createFn(Type.VOID_TYPE, name, arguments, closure);
    }

    public Function<Context, Type> createFnParam(
            Type type,
            String name
    ) {
        return context -> type;
    }

    public Function<Context, Type> invokeFn(
            String name,
            List<Function<Context, Type>> arguments
    ) {
        // TODO invoke pushing arguments.
        return context -> Type.VOID_TYPE;
    }
}
