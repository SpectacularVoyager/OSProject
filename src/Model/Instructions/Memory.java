package Model.Instructions;

import Model.int4;

public class Memory {
    private int4[][] m;

    public Memory() {
        m = new int4[3][100];
    }

    public int4[] M(int r) {
        return m[r];
    }
}
