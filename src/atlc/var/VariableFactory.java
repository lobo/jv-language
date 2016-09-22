package atlc.var;

import atlc.Context;
import atlc.expr.ArithmeticFactory;
import atlc.expr.LogicalFactory;
import atlc.expr.StrExprFactory;
import org.objectweb.asm.Type;

import java.util.function.Consumer;
import java.util.function.Function;

public class VariableFactory {

    public Function<Context, Type> getType(String name) {
        return context -> context.getVariableType(context.getVariableId(name));
    }

    public Function<Context, Type> loadLocal(String name) {
        return context -> {
              int id = context.getVariableId(name);
              context.getGa().loadLocal(id);
              return context.getVariableType(id);
        };
    }

    public Consumer<Context> assignLocal(String name, Function<Context, Type> value) {
        return context -> context.assignLocal(name, value);
    }

    public Consumer<Context> assignLocal(String name) {
        return context -> context.assignLocal(name);
    }

    public Consumer<Context> createLocal(String name, Type type) {
        if (type.equals(Type.getType(Boolean.class))) {
            return createLocal(name, type, new LogicalFactory().createLiteral(false));
        }
        if (type.equals(Type.getType(Integer.class))) {
            return createLocal(name, type, new ArithmeticFactory().createLiteral(0));
        }
        if (type.equals(Type.getType(String.class))) {
            return createLocal(name, type, new StrExprFactory().createLiteral(""));
        }
        return null;
    }

    public Consumer<Context> createLocal(String name, Type type, Function<Context, Type> value) {
        return context -> context.addLocal(name, type, value);
    }
}
