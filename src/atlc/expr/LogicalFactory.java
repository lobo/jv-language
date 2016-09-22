package atlc.expr;

import atlc.Context;
import atlc.nodes.FlowControlFactory;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.function.Consumer;
import java.util.function.Function;

public class LogicalFactory extends ExprFactory<Boolean> {

    @Override
    protected Type getType() {
        return Type.BOOLEAN_TYPE;
    }

    @Override
    public Function<Context, Type> createLiteral(Boolean obj) {
        return context -> {
            context.getGa().push(obj);
            return this.getType();
        };
    }

    public Function<Context, Type> createNot(Function<Context, Type> e) {
        return context -> {
            e.apply(context);
            context.getGa().not();
            return this.getType();
        };
    }

    @Override
    public Function<Context, Type> createBinary(final int op, Function<Context, Type> e1, Function<Context, Type> e2) {
        return context -> {
            GeneratorAdapter ga = context.getGa();
            final Label ifTrueRun = ga.newLabel();
            final Label elseRun = ga.newLabel();
            final Label endIf = ga.newLabel();

            Type t1 = e1.apply(context);
            Type t2 = e2.apply(context);
            if (t1.equals(t2)) {
                ga.ifCmp(t1, op, ifTrueRun);
                ga.goTo(elseRun);

                ga.visitLabel(ifTrueRun);
                this.createLiteral(true).apply(context);
                ga.goTo(endIf);
                ga.visitLabel(elseRun);
                this.createLiteral(false).apply(context);
                ga.visitLabel(endIf);
            } else {
                context.getGa().pop2();
                return ExceptionFactory.createRuntime("Type mismatch: " + t1 + " vs. " + t2).apply(context);
            }
            return this.getType();
        };
    }
}
