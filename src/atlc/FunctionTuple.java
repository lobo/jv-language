package atlc;

import org.objectweb.asm.Type;

import java.util.function.Function;

public class FunctionTuple {

    public final Function<Context, Type> first;
    public final Function<Context, Type> second;

    public static FunctionTuple dummy() {
        return new FunctionTuple(ctx -> null, ctx -> null);
    }

    public FunctionTuple(Function<Context, Type> first, Function<Context, Type> second) {
        this.first = first;
        this.second = second;
    }
}
