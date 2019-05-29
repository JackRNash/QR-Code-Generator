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

    /**
     * Given a number in binary(stored as an ArrayList), returns the corresponding int
     * in decimal form
     */
    public static int binaryToInt(ArrayList<Integer> b) {
        int sum = 0;
        int len = b.size();
        for(int i=0; i < len; i++) {
            sum += b.get(len - 1 - i) * Math.pow(2, i);
        }
        return sum;
    }

    /**
     * Given a number in binary(stored as an ArrayList) and an int delimiter which specifies
     * how many bits to consider for each number, returns array of integers in decimal form
     * Precondition: delimiter > 0
     */
    public static int[] binaryToIntDelim(ArrayList<Integer> b, int delimiter) {
        int nums = b.size()/delimiter;
        boolean needExtra = false;
        int[] arr;
        if(nums < (double)(b.size()/delimiter)) {//in case b.size()/delimiter is a fraction
            needExtra = true;
            arr = new int[nums + 1];
        }
        else {arr = new int[nums];}
        for(int i=0; i < nums*delimiter; i+= delimiter) {
            System.out.println(b.subList(i, i + delimiter));
            if(i+delimiter == b.size())  arr[i/delimiter] = binaryToInt(new ArrayList<Integer> (b.subList(i, nums*delimiter)));
            else arr[i/delimiter] = binaryToInt(new ArrayList<Integer> (b.subList(i, i + delimiter)));
        }
        if(needExtra) arr[nums] = binaryToInt(new ArrayList<Integer>(b.subList(nums, b.size() - nums*delimiter)));
        return arr;
    }
}
