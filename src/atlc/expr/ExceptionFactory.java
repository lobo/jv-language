package atlc.expr;

import atlc.Context;
import org.objectweb.asm.Type;

import java.util.function.Function;

public class ExceptionFactory {

    public static Function<Context, Type> createRuntime(String msg) {
        return context -> {
            Type type = Type.getType(RuntimeException.class);
            context.getGa().throwException(type, msg);
            return type;
        };
    }
}
