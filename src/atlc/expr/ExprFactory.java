package atlc.expr;

import atlc.Context;
import org.objectweb.asm.Type;

import java.util.function.Function;

abstract class ExprFactory<T> {

    abstract Type getType();
    abstract Function<Context, Type> createLiteral(T obj);

    public Function<Context, Type> createBinary(final int op, Function<Context, Type> e1, Function<Context, Type> e2) {
        return context -> {
            Type t1 = e1.apply(context);
            Type t2 = e2.apply(context);
            if (t1.equals(t2)) {
                context.getGa().math(op, t1);
                return t1;
            } else {
                return ExceptionFactory.createRuntime("Type mismatch: " + t1 + " vs. " + t2).apply(context);
            }
        };
    }

}
