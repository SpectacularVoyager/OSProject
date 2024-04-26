package Model.Instructions.Implementation;


import Model.Instructions.Implementation.Add;
import Model.int2;
import Model.int4;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static Model.CONSTANTS.*;

@Getter
@Setter
public class CPU {
    int4 R;
    int4 IR;
    int IC;
    int SI;

    boolean C;
    int4[] _M = new int4[100];
    int4[] buffer = new int4[10];
    Scanner in;
    PrintStream out;

    int PI = 0, TI = 0;
    boolean isFinished = false;
    PCB job = null;


    public int4[] M() {
        return _M;
    }

    public void resetMemory() {
        _M = new int4[100];
    }

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
        PI = 0;
        TI = 0;
        resetMemory();
        for (int i = 0; i < M().length; i++) {
            M()[i] = new int4("");
        }
        buffer = new int4[10];
    }

    public void end() {
        isFinished = true;
    }

    void err(String s) {
        System.out.println("ERROR :\t" + s);
        System.exit(0);
    }


    void log(String s) {
        System.out.println(s);
    }

    public void terminate(int i) {
        System.out.println(job);
        switch (i) {
            case 0 -> log("EXIT");
            case OUT_OF_DATA_ERROR -> err("OUT OF DATA");
            case LINE_LIMIT_ERROR -> err("LINE LIMIT ERROR");
            case TIME_LIMIT_ERROR -> err("TIME LIMIT ERROR");
            case OPCODE_ERROR -> err(String.format("UNRECOGNISED OPCODE:\t" + IR.getHigher()));
            case OPERAND_ERROR -> err("OPERAND ERROR");
            case INVALID_PAGE -> err("INVALID PAGE");
        }
        end();
    }

    public boolean isOutOfTime() {
        return TI == 2;
    }

    private void MOS() {
        if (PI != 0) {
            if (isOutOfTime()) {
                terminate(3);
            } else {
                if (PI == OPCODE_INTERUPT) {
                    terminate(OPCODE_ERROR);
                } else if (PI == OPERATION_INTERUPT) {
                    terminate(OPERAND_ERROR);
                }
            }
        } else {

            if (SI == GD) {
                readBuffer();
                int low = IR.getLower().toInt();
                for (int i = 0; i < 10; i++) {
                    M()[low++] = buffer[i];
                }

                if (isOutOfTime()) terminate(3);

            } else if (SI == PD) {

                if (isOutOfTime()) terminate(3);

                int low = IR.getLower().toInt();
                for (int i = 0; i < 10; i++) {
                    buffer[i] = M()[low++];
                }
                printBuffer();
            } else if (SI == H) {
                terminate(0);
            } else {
//            throw new UnsupportedOperationException();
            }
        }
    }

    public void execute() {
        while (IC < 19) {
            //LOAD INSTRUCTION
            IR = M()[IC++];
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
                R = M()[low.toInt()];
            } else if (IR.beginsWith("SR")) {
                int2 low = IR.getLower();
                M()[low.toInt()] = R;
            } else if (IR.beginsWith("CR")) {
                int2 low = IR.getLower();
                C = R.toInt() == M()[low.toInt()].toInt();
            } else if (IR.beginsWith("BT")) {
                if (C) {
                    IC = IR.getLower().toInt();
                }
            } else if (IR.beginsWith("AD")) {
                int2 low = IR.getLower();
                int x = M()[low.toInt()].toInt();
                x += R.toInt();
                R = new int4(x);
            } else if (IR.beginsWith("SB")) {
                int2 low = IR.getLower();
                int x = R.toInt();
                x -= M()[low.toInt()].toInt();
                R = new int4(x);
            } else if (IR.beginsWith("JP")) {
                int2 low = IR.getLower();
                IC = IR.getLower().toInt();
            } else if (IR.isnull()) {

            } else {
//                break;
                programInterrupt(OPCODE_INTERUPT);
//                throw new UnsupportedOperationException("UNSUPPORTED OPCODE:\t" + IR.getHigher());
            }

            if (job.time == job.totalTime) {
                TI = 2;
            }
            if (!isFinished) job.time++;


        }

    }

    private void readBuffer() {
        String s = in.nextLine();
        if (s.startsWith("//")) {
            readBuffer();
        }
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
        job.lines++;
        if (job.lines > job.totalLines) {
            terminate(2);
        }
        Arrays.stream(buffer).filter(Objects::nonNull).forEach(x -> out.print(x.toString()));
        out.println();
    }

    private void loadBuffer(int low) {
        for (int i = 0; i < 10; i++) {
            buffer[i] = M()[low++];
        }
    }

    private void storeBuffer(int low) {
        for (int i = 0; i < 10; i++) {
            M()[low++] = buffer[i];
        }
    }

    public void load() {
        int x = 0;
        while (in.hasNext()) {
            readBuffer();
            if (buffer[0].beginsWith("$AMJ")) {
                job = new PCB(buffer[1].toInt(), buffer[2].toInt(), buffer[3].toInt());
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
                    M()[x] = buffer[k];
                    if (k >= 9 || buffer[k].beginsWith("\n")) {
                        break;
                    }
                }
            }
        }

    }

    public void programInterrupt(int p) {
        PI = p;
        MOS();
    }

    public void printMemory() {
        for (int i = 0; i < M().length; i++) {
            System.out.printf("%d:\t", i);
            M()[i].printHex();
        }
    }
}
