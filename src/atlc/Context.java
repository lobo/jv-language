package atlc;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Context implements Opcodes {
    private final String className;
    private final ClassWriter cw;
    private GeneratorAdapter ga = null;
    private Deque<Map<String, Integer>> localVariablesStack;

    public Context(String className, ClassWriter cw) {
        this.cw = cw;
        this.className = className;
        this.localVariablesStack = new ArrayDeque<>();
    }

    public void start(Method method) {
        if (ga != null) {
            throw new RuntimeException("METHOD ALREADY STARTED");
        }
        this.ga = new GeneratorAdapter(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, method, null, null, cw);
    }

    public void addLocal(String name, Type type, Function<Context, Type> value) {
        int localId = ga.newLocal(type);
        getLocalVariables().put(name, localId);
    	assignLocal(name, value);
    }

    public void assignLocal(String name, Function<Context, Type> value) {
    	Type type = value.apply(this);
        assignLocal(name, type);
    }

    public void assignLocal(String name, Type type) { // Stores value from stack
        ga.storeLocal(getLocalVariables().get(name), type);
    }

    public void assignLocal(String name) {
        int varId = getLocalVariables().get(name);
        ga.storeLocal(varId, getVariableType(varId));
    }

    public void endMethod() {
        if (ga == null) {
            throw new RuntimeException("METHOD NOT STARTED");
        }
        ga.returnValue();
        ga.endMethod();
        ga = null;
    }

    public int getVariableId(String name) {
      return getLocalVariables().get(name);
    }

    public Type getVariableType(int variableId) {
        return ga.getLocalType(variableId);
    }

    public GeneratorAdapter getGa() {
        return ga;
    }

    public ClassWriter getCw() {
        return cw;
    }

    private Map<String, Integer> getLocalVariables() {
    	if (localVariablesStack.isEmpty()) {
    		localVariablesStack.push(new HashMap<>());
    	}
    	return localVariablesStack.peekFirst();
    }

    public Type getClassType() {
        return Type.getObjectType(this.className);
    }
}
