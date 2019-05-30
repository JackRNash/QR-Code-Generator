import java.util.ArrayList;
import java.util.Collections;

public class Encode {
    private ArrayList<Integer> encode_type;
    private String msg;
    private int bitDelim;
    private char errorCorr;

    public Encode(String message, ArrayList<Integer> encode, char errorCorr) {
        msg = message.toUpperCase();
        encode_type = encode;
        this.errorCorr = errorCorr;
        if(encode.get(3) == 1) bitDelim = 10; //Numeric
        else if(encode.get(2) == 1) bitDelim = 9; //Alphanumeric
        else bitDelim = 8; //8Bit or Kanji
    }

    /**
     * Encodes an alphanumeric String s and returns it as an integer array of ones and zeroes(binary representation)
     */
    public static ArrayList<Integer> encodeAlphaNum(String s) {
        ArrayList<Integer> binary = new ArrayList<>();
        for(int i = 0; i < s.length(); i+=2) {
            try {
                int num = 45 * alphaToNumber(s.charAt(i)) + alphaToNumber(s.charAt(i+1));
                binary.addAll(Binary.intToBinaryOfLength(num,11));
            } catch (IndexOutOfBoundsException e) {
                int num = alphaToNumber(s.charAt(i)); //?? unsure if this is correct, haven't found proper documentation
                binary.addAll(Binary.intToBinaryOfLength(num, 6));
            }
        }
        return binary;
    }

    /**
     * Converts a character c to the appropriate number according to this table:
     * http://www.swetake.com/qrcode/qr_table1.html
     */
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

    /**
     * Encodes a message completely and returns the corresponding ArrayList(mask not applied)
     * String s: message to encode
     * ArrayList<Integer> encode: type of message(number, alphanum, 8bit, or Kanj)
     * int delim: how to delimit message(determined by type of message)
     *
     * NOTE: Right now, only supports alphanumeric and have yet to implement the error correction
     * part
     */
    public static ArrayList<Integer> encodeMsg(String s, ArrayList<Integer> encode, int delim) {
        ArrayList<Integer> encMsg = new ArrayList<>(encode); //"Header"
        encMsg.addAll(Binary.intToBinaryOfLength(s.length(), delim)); //Character count
        encMsg.addAll(encodeAlphaNum(s)); //Message
        encMsg.addAll(new ArrayList<>(Collections.nCopies(4, 0))); //Terminator, TO IMPLEMENT: no terminator when at capacity

        //if the encoded message doesn't fill up n delimited blocks of binary, append zeroes until it does
        int num = encMsg.size()/delim;
        if(encMsg.size() - delim*num > 0) {
            while(encMsg.size() <= (num + 1)*delim) {
                encMsg.add(0);
            }
        }
        //TO IMPLEMENT: if enc msg isn't at capacity, fill with alternating 11101100 & 00010001

        //TO IMPLEMENT: find error correcting codes, append

        return encMsg;
    }

}
