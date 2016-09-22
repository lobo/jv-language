package atlc.expr;

import atlc.Context;
import org.objectweb.asm.Type;

import java.util.function.Function;

public class StrExprFactory extends ExprFactory<String> {


    @Override
    protected Type getType() {
        return Type.getType(String.class);
    }

    @Override
    public Function<Context, Type> createLiteral(String obj) {
        return context -> {
            context.getGa().push(obj);
            return this.getType();
        };
    }

    @Override
    public Function<Context, Type> createBinary(int op, Function<Context, Type> e1, Function<Context, Type> e2) {
        throw new RuntimeException("not available");
    }
}
