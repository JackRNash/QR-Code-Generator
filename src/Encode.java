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

    /**
     * Given a version, returns an array with the alphanumeric character capacity of the error correcting levels
     * L, M, Q, & H respectively. E.g. alphanumCapacityLookup(1) --> [25, 20, 16, 10]
     * So the character capacity of version 1 w/ error correction level l is 25
     * Unfortunately had to be hard coded, no formula exists(as far as I know)
     * Precondition: 1 <= version <= 40
     */
    public static int[] alphanumCapacityLookup(int version) {
       int[] arr = new int[0];

       switch(version) {
           case 1:
               arr = new int[] {25, 20, 16, 10}; break;
           case 2:
               arr = new int[] {47, 38, 29, 20}; break;
           case 3:
               arr = new int[] {77, 61, 47, 35}; break;
           case 4:
               arr = new int[] {114, 90, 67, 50}; break;
           case 5:
               arr = new int[] {154, 122, 87, 64}; break;
           case 6:
               arr = new int[] {195, 154, 108, 84}; break;
           case 7:
               arr = new int[] {224, 178, 125, 93}; break;
           case 8:
               arr = new int[] {279, 221, 157, 122}; break;
           case 9:
               arr = new int[] {335, 262, 189, 143}; break;
           case 10:
               arr = new int[] {395, 311, 221, 174}; break;
           case 11:
               arr = new int[] {468, 366, 259, 200}; break;
           case 12:
               arr = new int[] {535, 419, 296, 227}; break;
           case 13:
               arr = new int[] {619, 483, 352, 259}; break;
           case 14:
               arr = new int[] {667, 528, 376, 283}; break;
           case 15:
               arr = new int[] {758, 600, 426, 321}; break;
           case 16:
               arr = new int[] {854, 656, 470, 365}; break;
           case 17:
               arr = new int[] {938, 734, 531, 408}; break;
           case 18:
               arr = new int[] {1046, 816, 574, 452}; break;
           case 19:
               arr = new int[] {1153, 909, 644, 493}; break;
           case 20:
               arr = new int[] {1249, 970, 702, 557}; break; //
           case 21:
               arr = new int[] {1352, 1035, 742, 587}; break;
           case 22:
               arr = new int[] {1460, 1134, 823, 640}; break;
           case 23:
               arr = new int[] {1588, 1248, 890, 672}; break;
           case 24:
               arr = new int[] {1704, 1326, 963, 744}; break;
           case 25:
               arr = new int[] {1853, 1451, 1041, 779}; break;
           case 26:
               arr = new int[] {1990, 1542, 1094, 864}; break;
           case 27:
               arr = new int[] {2132, 1637, 1172, 910}; break;
           case 28:
               arr = new int[] {2223, 1732, 1263, 958}; break;
           case 29:
               arr = new int[] {2369, 1839, 1322, 1016}; break;
           case 30:
               arr = new int[] {2520, 1994, 1429, 1080}; break;
           case 31:
               arr = new int[] {2677, 2113, 1499, 1150}; break;
           case 32:
               arr = new int[] {2840, 2238, 1618, 1226}; break;
           case 33:
               arr = new int[] {3009, 2369, 1700, 1307}; break;
           case 34:
               arr = new int[] {3183, 2506, 1787, 1394}; break;
           case 35:
               arr = new int[] {3351, 2632, 1867, 1431}; break;
           case 36:
               arr = new int[] {3537, 2780, 1966, 1530}; break;
           case 37:
               arr = new int[] {3729, 2894, 2071, 1591}; break;
           case 38:
               arr = new int[] {3927, 3054, 2181, 1658}; break;
           case 39:
               arr = new int[] {4087, 3220, 2298, 1774}; break;
           case 40:
               arr = new int[] {4296, 3391, 2420, 1852}; break;
       }

       return arr;
    }

}
