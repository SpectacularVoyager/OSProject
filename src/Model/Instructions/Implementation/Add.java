package Model.Instructions.Implementation;

import Model.Instructions.Instruction;
import Model.int4;

public class Add implements Instruction {
    @Override
    public boolean match(int4 inst) {
        return inst.beginsWith("AD");
    }

    @Override
    public void evaluate(CPU cpu, int4 inst) {
        if (match(inst)) {
            int x = cpu.M()[inst.getLower().toInt()].toInt();
            x += cpu.R.toInt();
            cpu.R = new int4(x);
        }
    }
}
