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
    void testIntToBinaryOfLength() {
        assertEquals(strToArr("0000"), Binary.intToBinaryOfLength(0, 4));
        assertEquals(strToArr("0001"), Binary.intToBinaryOfLength(1, 4));
        assertEquals(strToArr("0010"), Binary.intToBinaryOfLength(2, 4));
        assertEquals(strToArr("0001010"), Binary.intToBinaryOfLength(10, 7));
        assertEquals(strToArr("001011"), Binary.intToBinaryOfLength(11, 6));
        assertEquals(strToArr("000001111"), Binary.intToBinaryOfLength(15, 9));
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
        int[] arr1 = new int[2]; arr1[0] = 1;
        assertArrs(arr1, Binary.binaryToIntDelim(strToArr("10"), 1));
        int[] arr2 = new int[1]; arr2[0] = 2;
        assertArrs(arr2, Binary.binaryToIntDelim(strToArr("10"), 2));
        int[] arr3 = new int[3]; arr3[1] = 3; arr3[2] = 2;
        assertArrs(arr3, Binary.binaryToIntDelim(strToArr("001110"), 2));
        int[] arr4= new int[4]; arr4[0] = 2; arr4[1] = 1; arr4[2] = 3; arr4[3] = 1;
        assertArrs(arr4, Binary.binaryToIntDelim(strToArr("1001111"), 2));
    }

    @Test
    void testEncodeMsg() {
        assertEquals(strToArr("0010000001000001110011010100010100101001110111000010111010000000"),
                Encode.encodeMsg("ABCDE123", strToArr("0010"), 9));
    }

    @Test
    void testEncodeAlphaNum() {
        assertEquals(strToArr("00111001101010001010010100111011100001011101"),
                Encode.encodeAlphaNum("ABCDE123"));
        assertEquals(strToArr("00111001101"), Encode.encodeAlphaNum("AB"));
    }

    @Test
    void testCharToInt() {
        assertEquals(10, Encode.charToInt('A'));
        assertEquals(35, Encode.charToInt('Z'));
        assertEquals(0, Encode.charToInt('0'));
        assertEquals(9, Encode.charToInt('9'));
        assertEquals(36, Encode.charToInt(' '));
        assertEquals(42, Encode.charToInt('.'));
    }

    @Test
    void testGFMult() {
        assertEquals(58, Finite.gfMult(29,2));
        assertEquals(0, Finite.gfMult(0, 10));
        assertEquals(205, Finite.gfMult(232, 2));
    }

    @Test
    void testToAlphaToNum() {
        Finite f = new Finite();

        int[] arr = new int[3];
        arr[1] = 2; arr[2] = 4;
        assertArrs(arr, f.toNum(f.toAlpha(arr)));

        assertArrs(arr, f.toAlpha(f.toNum(arr)));

        int[] arr2 = new int[3]; arr2[1] = 4; arr2[2] = 16;
        arr[0] = -1;
        assertArrs(arr, f.toAlpha(arr2));

        assertArrs(arr2, f.toNum(arr));
    }

    @Test
    void testGFPolyMult() {
        Finite f = new Finite();
        int[] arr1 = new int[2]; arr1[0] = 1; arr1[1] = 1;
        int[] arr2 = new int[2]; arr2[0] = 2; arr2[1] = 1;
        int[] arr3 = new int[3]; arr3[0] = 2; arr3[1] = 3; arr3[2] = 1;
        assertArrs(arr3, f.gfPolyMult(arr1, arr2));

        int[] arr4 = new int[2]; arr4[0] = 4; arr4[1] = 1;
        int[] arr5 = new int[4]; arr5[3] = 1; arr5[2] = 7; arr5[1] = 14; arr5[0] = 8;
        assertArrs(arr5, f.gfPolyMult(arr3, arr4));

        assertArrs(new int[3], f.gfPolyMult(new int[2], new int[2])); //handles zeroes properly
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
            //System.out.println("arr1: " + arr1[i] + "\tarr2: " + arr2[i]);
            assert arr1[i] == arr2[i];
        }
    }
}