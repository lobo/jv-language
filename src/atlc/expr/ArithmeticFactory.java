package atlc.expr;

import atlc.Context;
import org.objectweb.asm.Type;

import java.util.function.Function;

public class ArithmeticFactory extends ExprFactory<Integer> {

    @Override
    protected Type getType() {
        return Type.INT_TYPE;
    }

    @Override
    public Function<Context, Type> createLiteral(Integer obj) {
        return context -> {
            context.getGa().push(obj);
            return this.getType();
        };
    }

}
