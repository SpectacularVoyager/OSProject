package Model.Instructions;

import Model.Instructions.Implementation.CPU;
import Model.int4;

public interface Instruction {
    boolean match(int4 inst);

    void evaluate(CPU cpu, int4 inst);
}
