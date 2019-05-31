import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Tests {

    @Test
    void testIntToBinary() {
        assertEquals(strToArrList("0"), Binary.intToBinary(0));
        assertEquals(strToArrList("1"), Binary.intToBinary(1));
        assertEquals(strToArrList("10"), Binary.intToBinary(2));
        assertEquals(strToArrList("1010"), Binary.intToBinary(10));
        assertEquals(strToArrList("1011"), Binary.intToBinary(11));
        assertEquals(strToArrList("1111"), Binary.intToBinary(15));
    }

    @Test
    void testIntToBinaryOfLength() {
        assertEquals(strToArrList("0000"), Binary.intToBinaryOfLength(0, 4));
        assertEquals(strToArrList("0001"), Binary.intToBinaryOfLength(1, 4));
        assertEquals(strToArrList("0010"), Binary.intToBinaryOfLength(2, 4));
        assertEquals(strToArrList("0001010"), Binary.intToBinaryOfLength(10, 7));
        assertEquals(strToArrList("001011"), Binary.intToBinaryOfLength(11, 6));
        assertEquals(strToArrList("000001111"), Binary.intToBinaryOfLength(15, 9));
    }

    @Test
    void testBinaryToInt() {
        assertEquals(2, Binary.binaryToInt(strToArrList("10")));
        assertEquals(0, Binary.binaryToInt(strToArrList("0")));
        assertEquals(1, Binary.binaryToInt(strToArrList("1")));
        assertEquals(6, Binary.binaryToInt(strToArrList("00110")));
        assertEquals(7, Binary.binaryToInt(strToArrList("111")));
    }

    @Test
    void testBinaryToIntDelimiter() {
        int[] arr1 = new int[2]; arr1[0] = 1;
        assertArrs(arr1, Binary.binaryToIntDelim(strToArrList("10"), 1));
        int[] arr2 = new int[1]; arr2[0] = 2;
        assertArrs(arr2, Binary.binaryToIntDelim(strToArrList("10"), 2));
        int[] arr3 = new int[3]; arr3[1] = 3; arr3[2] = 2;
        assertArrs(arr3, Binary.binaryToIntDelim(strToArrList("001110"), 2));
        int[] arr4= new int[4]; arr4[0] = 2; arr4[1] = 1; arr4[2] = 3; arr4[3] = 1;
        assertArrs(arr4, Binary.binaryToIntDelim(strToArrList("1001111"), 2));
    }

    @Test
    void testEncodeMsg() {
        assertEquals(strToArrList("0010000001000001110011010100010100101001110111000010111010000000"),
                Encode.encodeMsg("ABCDE123", strToArrList("0010"), 9));
    }

    @Test
    void testEncodeAlphaNum() {
        assertEquals(strToArrList("00111001101010001010010100111011100001011101"),
                Encode.encodeAlphaNum("ABCDE123"));
        assertEquals(strToArrList("00111001101"), Encode.encodeAlphaNum("AB"));
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
        assertEquals(58, ErrorCorrection.gfMult(29,2));
        assertEquals(0, ErrorCorrection.gfMult(0, 10));
        assertEquals(205, ErrorCorrection.gfMult(232, 2));
    }

    @Test
    void testToAlphaToNum() {
        ErrorCorrection f = new ErrorCorrection();

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
        ErrorCorrection f = new ErrorCorrection();
        int[] arr1 = new int[2]; arr1[0] = 1; arr1[1] = 1;
        int[] arr2 = new int[2]; arr2[0] = 2; arr2[1] = 1;
        int[] arr3 = new int[3]; arr3[0] = 2; arr3[1] = 3; arr3[2] = 1;
        assertArrs(arr3, f.gfPolyMult(arr1, arr2));
        assertArrs(arr3, f.gfPolyMult(arr2, arr1));

        int[] arr4 = new int[2]; arr4[0] = 4; arr4[1] = 1;
        int[] arr5 = new int[4]; arr5[3] = 1; arr5[2] = 7; arr5[1] = 14; arr5[0] = 8;
        assertArrs(arr5, f.gfPolyMult(arr3, arr4));
        assertArrs(arr5, f.gfPolyMult(arr4, arr3));

        assertArrs(new int[3], f.gfPolyMult(new int[2], new int[2])); //handles zeroes properly
    }

    @Test
    void testGenPoly() {
        ErrorCorrection f = new ErrorCorrection();
        int[] arr2 = new int[3];
        arr2[0] = 2; arr2[1]=3; arr2[2] = 1;
        assertArrs(arr2, f.genPoly(2));

        int[] arr1 = new int[4];
        arr1[0]=8; arr1[1] = 14; arr1[2] = 7; arr1[3] = 1;
        assertArrs(arr1, f.genPoly(3));

        int[] arr3 = new int[8];
        arr3[0] = 21; arr3[1]=102; arr3[2]=238; arr3[3]=149; arr3[4]=146; arr3[5]=229; arr3[6]=87; arr3[7]=0;
        assertArrs(arr3, f.toAlpha(f.genPoly(7)));

        int[] arr4 = {45, 32, 94, 64, 70, 118, 61, 46, 67, 251, 0};
        assertArrs(arr4, f.toAlpha(f.genPoly(10)));
    }

    @Test
    void testGFPolyDiv() {
        ErrorCorrection ec = new ErrorCorrection();
        int[] msgArr = {17, 236, 17, 236, 17, 236, 64, 67, 77, 220, 114, 209, 120, 11, 91, 32};

        int[] gen = ec.genPoly(10);
        int size = gen.length + msgArr.length - 1;
        int[] arr = ec.gfPolyDiv(gen, msgArr, size);
//        System.out.println("Size final: " + arr.length + "\tSize gen: " + gen.length + "\tSize msg: " + msgArr.length);
        int[] arrExp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 236, 17, 236, 17, 233, 10, 197, 98, 211, 183, 176, 114, 110, 89, 0};
        assertArrs(arrExp, arr);

//        for(int i = 0; i < gen.length; i++) {
//            System.out.print(gen[i] + " ");
//        }
        //System.out.println();
        gen = ErrorCorrection.resizeArr(gen, size);
        gen = ErrorCorrection.shiftArr(gen);

//        for(int i = 0; i < gen.length; i++) {
//            System.out.print(gen[i] + " ");
//        }
        //System.out.println();

        int[] arr2 = ec.gfPolyDiv(gen, arr, size);
        int[] arrExp2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 236, 17, 236, 16, 89, 134, 159, 97, 25, 251, 178, 152, 61, 0, 0};
//        for(int i = 0; i < arr2.length; i++) {
//            System.out.println(arr2[i]);
//        }
        assertArrs(arrExp2, arr2);

    }

    @Test
    void testGenErrorCorrWords() {
        ErrorCorrection ec = new ErrorCorrection();
        ArrayList<Integer> msg = new ArrayList<>();
        msg.add(32); msg.add(91); msg.add(11); msg.add(120); msg.add(209); msg.add(114); msg.add(220); msg.add(77);
        msg.add(67); msg.add(64); msg.add(236); msg.add(17); msg.add(236); msg.add(17); msg.add(236); msg.add(17);
        //System.out.println(msg);
        int[] msgArr = ec.genErrorCorrWords(10, msg);
//        for(int i = 0; i < msgArr.length; i++) {
//            System.out.println(msgArr[i]);
//        }
        //assertArrs( , ec.genErrorCorrWords(72, msg));
    }





    public ArrayList<Integer> strToArrList(String s) { //helper method
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