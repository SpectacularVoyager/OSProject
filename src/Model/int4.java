package Model;

public class int4 {

    char[] arr = new char[4];
    public static int4 NULL = new int4("");

    public int4(int a) {
        arr[3] = (char) ((a % 10) + '0');
        a = a / 10;
        arr[2] = (char) ((a % 10) + '0');
        a = a / 10;
        arr[1] = (char) ((a % 10) + '0');
        a = a / 10;
        arr[0] = (char) ((a % 10) + '0');
    }

    public int4(String s) {
        if (s.length() < 4) {
            int i;
            for (i = 0; i < s.length(); i++) {
                arr[i] = s.charAt(i);
            }
            for (; i < 4; i++) {
                arr[i] = 0;
            }
        } else {
            arr = s.substring(0, 4).toCharArray();
        }
    }

    public int2 getLower() {
        return new int2(new String(arr).substring(2));
    }

    public int2 getHigher() {
        return new int2(new String(arr).substring(0, 2));
    }

    public int toInt() {
        return (arr[0] - '0') * 1000 + (arr[1] - '0') * 100 + (arr[2] - '0') * 10 + (arr[3] - '0');
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            if (arr[i] == '\0') {
                break;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    public boolean isnull() {
        return (arr[0] == 0) && (arr[1] == 0) && (arr[2] == 0) && (arr[3] == 0);
    }

    public boolean beginsWith(String s) {
        if (s.length() > 4) {
            throw new RuntimeException("TOO MANY BYTES FOR INT4");
        }
        for (int i = 0; i < s.length(); i++) {
            if (arr[i] != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public void printHex() {
        System.out.printf("%c %c %c %c\n", arr[0], arr[1], arr[2], arr[3]);
    }
}
