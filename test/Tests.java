import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Tests {

    @Test
    void testIntToBinary() {
        assertEquals(strToArr("0"), Binary.intToBinary(0));
        assertEquals(strToArr("1"), Binary.intToBinary(1));
        assertEquals(strToArr("10"), Binary.intToBinary(2));
        assertEquals(strToArr("1010"), Binary.intToBinary(10));
        assertEquals(strToArr("1011"), Binary.intToBinary(11));
        assertEquals(strToArr("1111"), Binary.intToBinary(15));
    }

    @Test
    void testIntToBinaryFixLength() {
        assertEquals(strToArr("0000"), Binary.intToBinaryFixLength(0, 4));
        assertEquals(strToArr("0001"), Binary.intToBinaryFixLength(1, 4));
        assertEquals(strToArr("0010"), Binary.intToBinaryFixLength(2, 4));
        assertEquals(strToArr("0001010"), Binary.intToBinaryFixLength(10, 7));
        assertEquals(strToArr("001011"), Binary.intToBinaryFixLength(11, 6));
        assertEquals(strToArr("000001111"), Binary.intToBinaryFixLength(15, 9));
    }

    @Test
    void testBinaryToInt() {
        assertEquals(2, Binary.binaryToInt(strToArr("10")));
        assertEquals(0, Binary.binaryToInt(strToArr("0")));
        assertEquals(1, Binary.binaryToInt(strToArr("1")));
        assertEquals(6, Binary.binaryToInt(strToArr("00110")));
        assertEquals(7, Binary.binaryToInt(strToArr("111")));
    }

    @Test
    void testBinaryToIntDelimiter() {
        int[] arr1 = new int[2]; arr1[0] = 1; arr1[1] = 0;
        assertArrs(arr1, Binary.binaryToIntDelim(strToArr("10"), 1));
        int[] arr2 = new int[1]; arr2[0] = 2;
        assertArrs(arr2, Binary.binaryToIntDelim(strToArr("10"), 2));
        int[] arr3 = new int[3]; arr3[0] = 0; arr3[1] = 3; arr3[2] = 2;
        assertArrs(arr3, Binary.binaryToIntDelim(strToArr("001110"), 2));
    }

    @Test
    void testEncodeAlphaNum() {
        assertEquals(strToArr("00111001101010001010010100111011100001011101"),
                Encode.encodeAlphaNum("ABCDE123"));
        assertEquals(strToArr("00111001101"), Encode.encodeAlphaNum("AB"));
    }

    @Test
    void testAlphaToNum() {
        assertEquals(10, Encode.alphaToNumber('A'));
        assertEquals(35, Encode.alphaToNumber('Z'));
        assertEquals(0, Encode.alphaToNumber('0'));
        assertEquals(9, Encode.alphaToNumber('9'));
        assertEquals(36, Encode.alphaToNumber(' '));
        assertEquals(42, Encode.alphaToNumber('.'));
    }



    public ArrayList<Integer> strToArr(String s) { //helper method
        ArrayList<Integer> arr = new ArrayList<>();
        for(char c: s.toCharArray()) {
            arr.add(Integer.parseInt(c + ""));
        }
        return arr;
    }


    /**
     * helper method to compare arrays(want to have same elements at same indices
     */
    public void assertArrs(int[] arr1, int[] arr2) {
        assert arr1.length == arr2.length;
        for(int i=0; i<arr1.length; i++) {
            System.out.println(arr1[i] +"\t" + arr2[i]);
            assert arr1[i] == arr2[i];
        }
    }
}