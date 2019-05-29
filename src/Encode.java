import java.lang.reflect.Array;
import java.util.ArrayList;

public class Encode {
    private ArrayList<Integer> ENCODE_TYPE;
    private String msg;
    private int bitLen;
    private char errorCorr;

    public Encode(String message, ArrayList<Integer> encode, char errorCorr) {
        msg = message.toUpperCase();
        ENCODE_TYPE = encode;
        this.errorCorr = errorCorr;
        if(encode.get(3) == 1) bitLen = 10; //Numeric
        else if(encode.get(2) == 1) bitLen = 9; //Alphanumeric
        else bitLen = 8; //8Bit or Kanji
    }

    public static ArrayList<Integer> encodeAlphaNum(String s) {
        ArrayList<Integer> binary = new ArrayList<>();
        for(int i = 0; i < s.length(); i+=2) {
            try {
                int num = 45 * alphaToNumber(s.charAt(i)) + alphaToNumber(s.charAt(i+1));
                binary.addAll(Binary.intToBinaryFixLength(num,11));
            } catch (IndexOutOfBoundsException e) {
                int num = alphaToNumber(s.charAt(i)); //?? unsure if this is correct, haven't found proper documentation
                binary.addAll(Binary.intToBinaryFixLength(num, 6));
            }
        }
        return binary;
    }

    public static int alphaToNumber(char c) { //returns corresponding number for specific chars
        if(65 <= c && c <= 90) return c - 55; //A --> 10, rest of letters accordingly (c is a letter)
        if(48 <= c && c <= 57) return c - 48; //c is a number
        if(c == '$') return 37;
        if(c == '%') return 38;
        if(c == '*') return 39;
        if(c == '+') return 40;
        if(c == '-') return 41;
        if(c == '.') return 42;
        if(c == '/') return 43;
        if(c == ':') return 44;
        return 36; //space, made default if given a character out of bounds
    }

}
