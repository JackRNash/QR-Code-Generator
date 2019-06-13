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
     * int version: version of the QR code
     * int ecLevel: error correction level
     *
     * NOTE: Right now, only supports alphanumeric
     */
    public static ArrayList<Integer> encodeMsg(String s, ArrayList<Integer> encode, int version, int ecLevel) {
        int delim = encodeTypeDelim(encode);
        ArrayList<Integer> encMsg = new ArrayList<>(encode); //"Header"
        encMsg.addAll(Binary.intToBinaryOfLength(s.length(), delim)); //Character count
        encMsg.addAll(encodeAlphaNum(s)); //Message
        int size = encMsg.size();
        int[] blocks = LookUp.ecAndBlockLookUp(version, ecLevel);

        int dataWords = blocks[1]*blocks[2] + blocks[3]*blocks[4];
//        System.out.println("version: " + version + "\tecLevel: " + ecLevel);
//        System.out.println("blocks[1]: " + blocks[1] + "\tblocks[2]: " + blocks[2] + "\tblocks[3]: " + blocks[3]);
//        System.out.println("data words: " + dataWords);
        if(size + 4 < dataWords * delim) {
            encMsg.addAll(new ArrayList<>(Collections.nCopies(4, 0))); //Terminator, no terminator when at capacity
        } else if(size < dataWords * delim) { //room for some of the terminator, but not all of it, so add as many as possible
            encMsg.addAll(new ArrayList<>(Collections.nCopies(19*delim - size, 0)));
        }


        //if the encoded message doesn't fill up n delimited blocks of binary, append zeroes until it does
        //int num = encMsg.size()/8;
        //while(encMsg.size() < (num + 1)*8) { //unclear how this handles edge cases
        while(encMsg.size() % 8 != 0) {
            encMsg.add(0);
        }

        //If enc msg isn't at capacity, fill with alternating 11101100 & 00010001
        ArrayList<Integer> one = strToArrList("11101100"); ArrayList<Integer> two = strToArrList("00010001");
        while(encMsg.size()/8 < dataWords) {
            encMsg.addAll(one);
            if(encMsg.size()/8 < dataWords) {
                encMsg.addAll(two);
            }
        }

        //Create appropriate blocks(each block is an ArrayList in groups, stored in order)
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>();
        int j = 0;
        for(int k = 0; k < blocks[1]; k++) {
            groups.add(new ArrayList<>(encMsg.subList(j, j + blocks[2] * 8)));
            j += blocks[2] * 8;
        }
        for(int k = 0; k < blocks[3]; k++) {
            groups.add(new ArrayList<>(encMsg.subList(j, j + blocks[4] * 8)));
            j += blocks[4] * 8;
        }

        //Make master list of all messages(one for each block) w/ numbers in decimal form
        ArrayList<ArrayList<Integer>> msgArrListGroups = new ArrayList<>();
        for(ArrayList<Integer> msgBlock: groups) {
            //Create ArrayList of message, converted from binary to decimal form
            ArrayList<Integer> msgArrList = new ArrayList<>();
            int[] msgArr = Binary.binaryToIntDelim(msgBlock, 8);
            for (int i = 0; i < msgArr.length; i++) {
                msgArrList.add(msgArr[i]);
            }
            msgArrListGroups.add(msgArrList);
        }

        //Make master list of all error correcting codes(one for each block) w/ numbers in decimal form
        ErrorCorrection ec = new ErrorCorrection();
        ArrayList<ArrayList<Integer>> ecWordsGroups = new ArrayList<>();
        for(ArrayList<Integer> msgArrList: msgArrListGroups) {
            //Find error correcting codes
            int[] arr = ec.genErrorCorrWords(blocks[0], msgArrList);
            ArrayList<Integer> ecWords = new ArrayList<>();
            for (int i = arr.length - 1; i >= 0; i--) {
                ecWords.addAll(Binary.intToBinaryOfLength(arr[i], 8));
            }
            ecWordsGroups.add(ecWords);
        }

        //Interleave the msg words
        ArrayList<Integer> finalMessage = new ArrayList<>();
        int maxLen = Math.max(blocks[2], blocks[4]); //most amount of numbers in a block
        for(int k = 0; k < maxLen; k++) {
            for(ArrayList<Integer> msgBlock: groups) {
                //Go through each block and append next word
                if(msgBlock.size() > k * 8) {
                    finalMessage.addAll(new ArrayList<>(msgBlock.subList(k*8, (k+1) * 8)));
                }
            }
        }

        //Interleave the error correcting words(same process as message words, comes after)
        for(int k = 0; k < blocks[0]; k++) { //every block has same amount of EC words
            for(ArrayList<Integer> ecWords: ecWordsGroups) {
                finalMessage.addAll(new ArrayList<>(ecWords.subList(k*8, (k+1) * 8)));
            }
        }

        //Add remainder bits(if necessary)
        int rem = LookUp.remainderBitsLookUp(version);
        for(int i = 0; i < rem; i++) {
            finalMessage.add(0);
        }

        return finalMessage;
    }

    /**
     * Helper method for finding the delimiter # for each encode type
     */
    public static int encodeTypeDelim(ArrayList<Integer> encode) {
        int delim;
        if(encode.get(3) == 1) delim = 10; //Numeric
        else if(encode.get(2) == 1) delim = 9; //Alphanumeric
        else delim = 8; //8Bit or Kanji
        return delim;
    }

    public static ArrayList<Integer> strToArrList(String s) { //helper method
        ArrayList<Integer> arr = new ArrayList<>();
        for(char c: s.toCharArray()) {
            arr.add(Integer.parseInt(c + ""));
        }
        return arr;
    }

    /**
     * Given an amount of characters and an error correction level(0-3 inclusive representing L, M, Q, H respectively)
     * finds the minimum version that has the capacity for this
     * Returns 41 if the message is too long
     * Precondition: 0 <= errors <= 3
     */
    public static int calcVersion(int chars, int error) {
        int version = 1;
        int currentCapacity = 0;
        while(version < 42 && currentCapacity < chars) { //exits loop with version = 42 or one greater than min needed
            currentCapacity = LookUp.alphanumCapacityLookUp(version)[error];
            version++;
        }
        return version - 1;
    }

}
