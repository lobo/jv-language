package atlc.nodes;

import atlc.Context;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.function.Consumer;
import java.util.function.Function;

public class FlowControlFactory {

    public static Consumer<Context> createIf(
            Function<Context, Type> condition,
            Consumer<Context> ifTrueBlock,
            Consumer<Context> elseBlock
    ) {
        return context -> {
            GeneratorAdapter ga = context.getGa();
            final Label ifTrueRun = ga.newLabel();
            final Label elseRun = ga.newLabel();
            final Label endIf = ga.newLabel();

            condition.apply(context);
            ga.ifZCmp(GeneratorAdapter.NE, ifTrueRun);
            ga.goTo(elseRun);

            ga.visitLabel(ifTrueRun);
            ifTrueBlock.accept(context);
            ga.goTo(endIf);
            ga.visitLabel(elseRun);
            if (elseBlock != null) {
                elseBlock.accept(context);
            }
            ga.visitLabel(endIf);
        };
    }

    public static Consumer<Context> createWhile(
            Function<Context, Type> condition,
            Consumer<Context> closure
    ) {
        return context -> {
            GeneratorAdapter ga = context.getGa();
            final Label startWhile = ga.newLabel();
            final Label startClosure = ga.newLabel();
            final Label endWhile = ga.newLabel();

            ga.visitLabel(startWhile);
            condition.apply(context);
            ga.ifZCmp(GeneratorAdapter.NE, startClosure);
            ga.goTo(endWhile);

            ga.visitLabel(startClosure);
            closure.accept(context);
            ga.goTo(startWhile);

            ga.visitLabel(endWhile);
        };
    }

    public static Consumer<Context> createDoWhile(
            Function<Context, Type> condition,
            Consumer<Context> closure
    ) {
        return context -> {
            GeneratorAdapter ga = context.getGa();
            final Label startWhile = ga.newLabel();

            ga.visitLabel(startWhile);
            closure.accept(context);
            condition.apply(context);
            ga.ifZCmp(GeneratorAdapter.NE, startWhile);
        };
    }
}
