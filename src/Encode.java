import java.util.ArrayList;
import java.util.Collections;

public class Encode {
    private ArrayList<Integer> encode_type;
    private String msg;
    private int bitDelim;
    private char errorCorr;

    public Encode(String message, ArrayList<Integer> encode, char errorCorr) {
        //TO DO ONCE FINISHED, CHECK IF ANY BENEFIT TO HAVING THIS CONSTRUCTOR AND FIELDS, OR SHOULD JUST DELETE
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
        s = s.toUpperCase();
        ArrayList<Integer> binary = new ArrayList<>();
        for(int i = 0; i < s.length(); i+=2) {
            try {
                int num = 45 * charToInt(s.charAt(i)) + charToInt(s.charAt(i+1));
                binary.addAll(Binary.intToBinaryOfLength(num,11));
            } catch (IndexOutOfBoundsException e) {
                int num = charToInt(s.charAt(i)); //?? unsure if this is correct, haven't found proper documentation
                binary.addAll(Binary.intToBinaryOfLength(num, 6));
            }
        }
        return binary;
    }

    /**
     * Converts a character c to the appropriate number according to this table:
     * http://www.swetake.com/qrcode/qr_table1.html
     */
    public static int charToInt(char c) { //returns corresponding number for specific chars
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
     * NOTE: Right now, only supports alphanumeric
     */
    public static ArrayList<Integer> encodeMsg(String s, ArrayList<Integer> encode, int delim) {
        ArrayList<Integer> encMsg = new ArrayList<>(encode); //"Header"
        encMsg.addAll(Binary.intToBinaryOfLength(s.length(), delim)); //Character count
        encMsg.addAll(encodeAlphaNum(s)); //Message
        int size = encMsg.size();
        if(size + 4 < 19 * delim) { //for now, assume it's a 1-L QR code
            encMsg.addAll(new ArrayList<>(Collections.nCopies(4, 0))); //Terminator, TO IMPLEMENT: no terminator when at capacity
        } else if(size < 19 * delim) { //room for some of the terminator, but not all of it, so add as many as possible
            encMsg.addAll(new ArrayList<>(Collections.nCopies(19*delim - size, 0)));
        }


        //if the encoded message doesn't fill up n delimited blocks of binary, append zeroes until it does
        int num = encMsg.size()/8;
        while(encMsg.size() < (num + 1)*8) {
            encMsg.add(0);
        }

        //TO IMPLEMENT: if enc msg isn't at capacity, fill with alternating 11101100 & 00010001
        //for now, assume 1-L QR code
        ArrayList<Integer> one = strToArrList("11101100"); ArrayList<Integer> two = strToArrList("00010001");
        while(encMsg.size()/8 < 19) {
            encMsg.addAll(one);
            if(encMsg.size()/8 < 19) {
                encMsg.addAll(two);
            }
        }
        //Create ArrayList of message, converted from binary to decimal form
        ArrayList<Integer> msgArrList = new ArrayList<>();
        int[] msgArr = Binary.binaryToIntDelim(encMsg, 8);
        for(int i = 0; i < msgArr.length; i++) {
            msgArrList.add(msgArr[i]);
        }

        //TO FINISH: find error correcting codes, append (Need method for finding num of errors to correct)
        ErrorCorrection ec = new ErrorCorrection();
        int[] arr = ec.genErrorCorrWords(7, msgArrList);
        for(int i = arr.length - 1; i >= 0; i--) {
            encMsg.addAll(Binary.intToBinaryOfLength(arr[i], 8));
        }

        return encMsg;
    }

    public static ArrayList<Integer> strToArrList(String s) { //helper method
        ArrayList<Integer> arr = new ArrayList<>();
        for(char c: s.toCharArray()) {
            arr.add(Integer.parseInt(c + ""));
        }
        return arr;
    }

}
