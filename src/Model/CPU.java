package Model;


import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static Model.CONSTANTS.*;

public class CPU {
    int4 R;
    int4 IR;
    int IC;
    int SI;
    boolean C;
    int4[] M = new int4[100];
    int4[] buffer = new int4[10];
    Scanner in;
    PrintStream out;

    public CPU(String file, PrintStream out) throws FileNotFoundException {
        in = new Scanner(new FileInputStream(file));
        init();
        this.out = out;
    }

    public void init() {
        R = new int4(0);
        IR = new int4(0);
        IC = 0;
        C = false;
        M = new int4[100];
        for (int i = 0; i < M.length; i++) {
            M[i] = new int4("");
        }
        buffer = new int4[10];
    }

    private void MOS() {
        if (SI == GD) {
            readBuffer();
            int low = IR.getLower().toInt();
            for (int i = 0; i < 10; i++) {
                M[low++] = buffer[i];
            }
        } else if (SI == PD) {
            int low = IR.getLower().toInt();
            for (int i = 0; i < 10; i++) {
                buffer[i] = M[low++];
            }
            printBuffer();
        } else if (SI == H) {
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void execute() {
        while (IC < 19) {
            //LOAD INSTRUCTION
            IR = M[IC++];
            if (IR.beginsWith("GD")) {
                SI = GD;
                MOS();
            } else if (IR.beginsWith("PD")) {
                SI = PD;
                MOS();
            } else if (IR.beginsWith("H")) {
                SI = H;
                MOS();
                break;
            } else if (IR.beginsWith("LR")) {
                int2 low = IR.getLower();
                R = M[low.toInt()];
            } else if (IR.beginsWith("SR")) {
                int2 low = IR.getLower();
                M[low.toInt()] = R;
            } else if (IR.beginsWith("CR")) {
                int2 low = IR.getLower();
                C = R.toInt() == M[low.toInt()].toInt();
            } else if (IR.beginsWith("BT")) {
                if (C) {
                    IC = IR.getLower().toInt();
                }
            } else {
//                break;
            }

        }
    }

    private void readBuffer() {
        String s = in.nextLine();
        String temp = "";
        for (int i = 0; i < 10; i++) {
            if (s.length() >= 4) {
                temp = new String(s.toCharArray());
                s = s.substring(4);
                buffer[i] = new int4(temp);
            } else {
                buffer[i] = new int4(s);
                s = "";
            }
        }

    }

    private void printBuffer() {
        Arrays.stream(buffer).filter(Objects::nonNull).forEach(x -> out.print(x.toString()));
        out.println();
    }

    private void loadBuffer(int low) {
        for (int i = 0; i < 10; i++) {
            buffer[i] = M[low++];
        }
    }

    private void storeBuffer(int low) {
        for (int i = 0; i < 10; i++) {
            M[low++] = buffer[i];
        }
    }

    public void load() {
        int x = 0;
        while (in.hasNext()) {
            readBuffer();
            if (buffer[0].beginsWith("$AMJ")) {
                init();
            } else if (buffer[0].beginsWith("$END")) {
                x = 0;
                out.printf("\n\n");
            } else if (buffer[0].beginsWith("$DTA")) {
                IC = 0;
//                printMemory();
                execute();
            } else {
                for (int k = 0; x < 100; x++, k++) {
                    M[x] = buffer[k];
                    if (k >= 9 || buffer[k].beginsWith("\n")) {
                        break;
                    }
                }
            }
        }

    }

    public void printMemory() {
        for (int i = 0; i < M.length; i++) {
            System.out.printf("%d:\t", i);
            M[i].printHex();
        }
    }
}
