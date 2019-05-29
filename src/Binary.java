import java.util.ArrayList;
import java.util.Collections;

public class Binary {
    //Contains methods useful for converting numbers and strings to binary & operations on binary strings

    /**
     * Given an integer int, converts it to binary and returns the string
     */
    public static ArrayList<Integer> intToBinary(int num) { //CONVERT TO ARRAYLIST INTEGER
        ArrayList<Integer> s = new ArrayList<>();
        if (num == 0) {
            s.add(0);
            return s;
        }
        while(num > 0) {
            s.add(num%2);
            num /= 2;
        }
        Collections.reverse(s);
        return s;
    }

    /**
     * Given an integer int, converts it to binary and ensures the string returned is of length len
     */
    public static ArrayList<Integer> intToBinaryFixLength(int num, int len) {
        ArrayList<Integer> s = intToBinary(num);
        int difference = len - s.size();
        ArrayList<Integer> t = new ArrayList<>();
        for(int i = 0; i < difference; i++) {
            t.add(0);
        }
        t.addAll(s);
        return t;
    }
}
