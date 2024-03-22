import Model.CPU;
import Model.int4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream out=new PrintStream(new File("res/out"));
        CPU cpu = new CPU("res/input1",out);
        cpu.load();
        cpu.printMemory();
    }
}