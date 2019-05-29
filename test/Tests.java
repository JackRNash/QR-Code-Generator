import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Tests {

    @Test
    void intToBinary() {
        assertEquals(strToArr("0"), Binary.intToBinary(0));
        assertEquals(strToArr("1"), Binary.intToBinary(1));
        assertEquals(strToArr("10"), Binary.intToBinary(2));
        assertEquals(strToArr("1010"), Binary.intToBinary(10));
        assertEquals(strToArr("1011"), Binary.intToBinary(11));
        assertEquals(strToArr("1111"), Binary.intToBinary(15));
    }

    @Test
    void intToBinaryFixLength() {
        assertEquals(strToArr("0000"), Binary.intToBinaryFixLength(0, 4));
        assertEquals(strToArr("0001"), Binary.intToBinaryFixLength(1, 4));
        assertEquals(strToArr("0010"), Binary.intToBinaryFixLength(2, 4));
        assertEquals(strToArr("0001010"), Binary.intToBinaryFixLength(10, 7));
        assertEquals(strToArr("001011"), Binary.intToBinaryFixLength(11, 6));
        assertEquals(strToArr("000001111"), Binary.intToBinaryFixLength(15, 9));
    }

    @Test
    void encodeAlphaNum() {
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
}