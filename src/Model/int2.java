package Model;

import java.util.Arrays;

public class int2 {
    char[] arr = new char[2];

    public int2(int a) {

        arr[1] = (char) ((a % 10) + '0');
        a = a / 10;
        arr[0] = (char) ((a % 10) + '0');
    }

    public int2(String s) {
        if (s.length() < 2) {
            throw new RuntimeException("NOT ENOUGH BYTES FOR INT4");
        }
        arr = s.substring(0, 2).toCharArray();
    }

    public int toInt() {
        return (arr[0] - '0') * 10 + (arr[1] - '0');
    }

    @Override
    public String toString() {
        return new String(arr);
    }
}
