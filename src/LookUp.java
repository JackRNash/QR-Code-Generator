import java.util.ArrayList;

public class LookUp {
    /*
     A class containing lookup methods, kept separate from the rest of the code. Unfortunately, a lot of the QR
     specifications seem to demand tables(no obvious formulas and didn't find any Google-ing)
     */

    /**
     * Lookup table for finding the number of blocks and number of data codewords per block, as well as the
     * number of error correcting codewords per block
     * Formatted as follows: [# EC codewords/block, # blocks in group 1, # data codewords in each of block of group 1,
     *                        # blocks in group 2, # data codewords in each of block of group 2]
     * Unfortunately, had to be hard coded(ugly, but at least it's simple)
     * Precondition: 0 <= ecLevel <= 3 (0, 1, 2, 3 represent L, M, Q, H respectively)
     *               1 <= version <= 40
     */
    public static int[] ecAndBlockLookup(int version, int ecLevel) {
        int[] arr = new int[5];
        switch(ecLevel) {
            case 0: //Error correction level L
                switch(version) {
                    case 1: arr = new int[] {7, 1, 19, 0, 0}; break;
                    case 2: arr = new int[] {10, 1, 34, 0 , 0}; break;
                    case 3: arr = new int[] {15, 1, 55, 0, 0}; break;
                    case 4: arr = new int[] {20, 1, 80, 0 , 0}; break;
                    case 5: arr = new int[] {26, 1, 108, 0, 0}; break;
                    case 6: arr = new int[] {18, 2, 68, 0, 0}; break;
                    case 7: arr = new int[] {20, 2, 78, 0 ,0}; break;
                    case 8: arr = new int[] {24, 2, 97, 0, 0}; break;
                    case 9: arr = new int[] {30, 2, 116, 0, 0}; break;
                    case 10: arr = new int[] {18, 2, 68, 2, 69}; break;
                    case 11: arr = new int[] {20, 4, 81, 0, 0}; break;
                    case 12: arr = new int[] {24, 2, 92, 2, 93}; break;
                    case 13: arr = new int[] {26, 4, 107, 0, 0}; break;
                    case 14: arr = new int[] {30, 3, 115, 1, 116}; break;
                    case 15: arr = new int[] {22, 5, 87, 1, 88}; break;
                    case 16: arr = new int[] {24, 5, 98, 1, 99}; break;
                    case 17: arr = new int[] {28, 1, 107, 5, 108}; break;
                    case 18: arr = new int[] {30, 5, 120, 1, 121}; break;
                    case 19: arr = new int[] {28, 3, 113, 4, 114}; break;
                    case 20: arr = new int[] {28, 3, 107, 5, 108}; break;
                    case 21: arr = new int[] {28, 4, 116, 4, 117}; break;
                    case 22: arr = new int[] {28, 2, 111, 7, 112}; break;
                    case 23: arr = new int[] {30, 4, 121, 5, 122}; break;
                    case 24: arr = new int[] {30, 6, 117, 4, 118}; break;
                    case 25: arr = new int[] {26, 8, 106, 4, 107}; break;
                    case 26: arr = new int[] {28, 10, 114, 2, 115}; break;
                    case 27: arr = new int[] {30, 8, 122, 4, 123}; break;
                    case 28: arr = new int[] {30, 3, 117, 10, 118}; break;
                    case 29: arr = new int[] {30, 7, 116, 7, 117}; break;
                    case 30: arr = new int[] {30, 5, 115, 10, 116}; break;
                    case 31: arr = new int[] {30, 13, 115, 3, 116}; break;
                    case 32: arr = new int[] {30, 17, 115, 0, 0}; break;
                    case 33: arr = new int[] {30, 17, 115, 1, 116}; break;
                    case 34: arr = new int[] {30, 13, 115, 6, 116}; break;
                    case 35: arr = new int[] {30, 12, 121, 7, 122}; break;
                    case 36: arr = new int[] {30, 6, 121, 14, 122}; break;
                    case 37: arr = new int[] {30, 17, 122, 4, 123}; break;
                    case 38: arr = new int[] {30, 4, 122, 18, 123}; break;
                    case 39: arr = new int[] {30, 20, 117, 4, 118}; break;
                    case 40: arr = new int[] {30, 19, 118, 6, 119}; break;
                }
            case 1: //Error correction level M
                switch(version) {
                    case 1: arr = new int[] {10, 1, 16, 0, 0 }; break;
                    case 2: arr = new int[] {16, 1, 28, 0, 0}; break;
                    case 3: arr = new int[] {26, 1, 44, 0, 0}; break;
                    case 4: arr = new int[] {18, 2, 32, 0, 0}; break;
                    case 5: arr = new int[] {24, 2, 43, 0, 0}; break;
                    case 6: arr = new int[] {16, 4, 27, 0, 0}; break;
                    case 7: arr = new int[] {18, 4, 31, 0, 0}; break;
                    case 8: arr = new int[] {22, 2, 38, 2, 39}; break;
                    case 9: arr = new int[] {22, 3, 36, 2, 37}; break;
                    case 10: arr = new int[] {26, 4, 43, 1, 44}; break;
                    case 11: arr = new int[] {30, 1, 50, 4, 51}; break;
                    case 12: arr = new int[] {22, 6, 36, 2, 37}; break;
                    case 13: arr = new int[] {22, 8, 37, 1, 38}; break;
                    case 14: arr = new int[] {24, 4, 40, 5, 41}; break;
                    case 15: arr = new int[] {24, 5, 41, 5, 42}; break;
                    case 16: arr = new int[] {28, 7, 45, 3, 46}; break;
                    case 17: arr = new int[] {28, 10, 46, 1, 47}; break;
                    case 18: arr = new int[] {26, 9, 43, 4, 44}; break;
                    case 19: arr = new int[] {26, 3, 44, 11, 45}; break;
                    case 20: arr = new int[] {26, 3, 41, 13, 42}; break;
                    case 21: arr = new int[] {26, 17, 42, 0, 0}; break;
                    case 22: arr = new int[] {28, 17, 46, 0, 0}; break;
                    case 23: arr = new int[] {28, 4, 47, 14, 48}; break;
                    case 24: arr = new int[] {28, 6, 45, 14, 46}; break;
                    case 25: arr = new int[] {28, 8, 47, 13, 48}; break;
                    case 26: arr = new int[] {28, 19, 46, 4, 47}; break;
                    case 27: arr = new int[] {28, 22, 45, 3, 46}; break;
                    case 28: arr = new int[] {28, 3, 45, 23, 46}; break;
                    case 29: arr = new int[] {28, 21, 45, 7, 46}; break;
                    case 30: arr = new int[] {28, 19, 47, 10, 48}; break;
                    case 31: arr = new int[] {28, 2, 46, 29, 47}; break;
                    case 32: arr = new int[] {28, 10, 46, 23, 47}; break;
                    case 33: arr = new int[] {28, 14, 46, 21, 47}; break;
                    case 34: arr = new int[] {28, 14, 46, 23, 47}; break;
                    case 35: arr = new int[] {28, 12, 47, 26, 48}; break;
                    case 36: arr = new int[] {28, 6, 47, 34, 48}; break;
                    case 37: arr = new int[] {28, 29, 46, 14, 47}; break;
                    case 38: arr = new int[] {28, 13, 46, 32, 47}; break;
                    case 39: arr = new int[] {28, 40, 47, 7, 48}; break;
                    case 40: arr = new int[] {28, 18, 47, 31, 48}; break;
                }
            case 2: //Error correction level Q
                switch(version) {
                    case 1: arr = new int[] {13, 1, 13, 0, 0}; break;
                    case 2: arr = new int[] {22, 1, 22, 0, 0}; break;
                    case 3: arr = new int[] {18, 2, 17, 0, 0}; break;
                    case 4: arr = new int[] {26, 2, 24, 0, 0}; break;
                    case 5: arr = new int[] {18, 2, 15, 2, 16}; break;
                    case 6: arr = new int[] {24, 4, 19, 0, 0}; break;
                    case 7: arr = new int[] {18, 2, 14, 4, 15}; break;
                    case 8: arr = new int[] {22, 4, 18, 2, 19}; break;
                    case 9: arr = new int[] {20, 4, 16, 4, 17}; break;
                    case 10: arr = new int[] {24, 6, 19, 2, 20}; break;
                    case 11: arr = new int[] {28, 4, 22, 4, 23}; break;
                    case 12: arr = new int[] {26, 4, 20, 6, 21}; break;
                    case 13: arr = new int[] {24, 8, 20, 4, 21}; break;
                    case 14: arr = new int[] {20, 11, 16, 5, 17}; break;
                    case 15: arr = new int[] {30, 5, 24, 7, 25}; break;
                    case 16: arr = new int[] {24, 15, 19, 2, 20}; break;
                    case 17: arr = new int[] {28, 1, 22, 15, 23}; break;
                    case 18: arr = new int[] {28, 17, 22, 1, 23}; break;
                    case 19: arr = new int[] {26, 17, 21, 4, 22}; break;
                    case 20: arr = new int[] {30, 15, 24, 5, 25}; break;
                    case 21: arr = new int[] {28, 17, 22, 6, 23}; break;
                    case 22: arr = new int[] {30, 7, 24, 16, 25}; break;
                    case 23: arr = new int[] {30, 11, 24, 14, 25}; break;
                    case 24: arr = new int[] {30, 11, 24, 16, 25}; break;
                    case 25: arr = new int[] {30, 7, 24, 22, 25}; break;
                    case 26: arr = new int[] {28, 28, 22, 6, 23}; break;
                    case 27: arr = new int[] {30, 8, 23, 26, 24}; break;
                    case 28: arr = new int[] {30, 4, 24, 31, 25}; break;
                    case 29: arr = new int[] {30, 1, 23, 37, 24}; break;
                    case 30: arr = new int[] {30, 15, 24, 25, 25}; break;
                    case 31: arr = new int[] {30, 42, 24, 1, 25}; break;
                    case 32: arr = new int[] {30, 10, 24, 35, 25}; break;
                    case 33: arr = new int[] {30, 29, 24, 19, 25}; break;
                    case 34: arr = new int[] {30, 44, 24, 7, 25}; break;
                    case 35: arr = new int[] {30, 39, 24, 14, 25}; break;
                    case 36: arr = new int[] {30, 46, 24, 10, 25}; break;
                    case 37: arr = new int[] {30, 49, 24, 10, 25}; break;
                    case 38: arr = new int[] {30, 48, 24, 14, 25}; break;
                    case 39: arr = new int[] {30, 43, 24, 22, 25}; break;
                    case 40: arr = new int[] {30, 34, 24, 34, 25}; break;
                }
            case 3: //Error correction level H
                switch(version) {
                    case 1: arr = new int[] {17, 1, 9, 0, 0}; break;
                    case 2: arr = new int[] {28, 1, 16, 0, 0}; break;
                    case 3: arr = new int[] {22, 2, 13, 0, 0}; break;
                    case 4: arr = new int[] {16, 4, 9, 0, 0}; break;
                    case 5: arr = new int[] {22, 2, 11, 2, 12}; break;
                    case 6: arr = new int[] {28, 4, 15, 0, 0}; break;
                    case 7: arr = new int[] {26, 4, 13, 1, 14}; break;
                    case 8: arr = new int[] {26, 4, 14, 2, 15}; break;
                    case 9: arr = new int[] {24, 4, 12, 4, 13}; break;
                    case 10: arr = new int[] {28, 6, 15, 2, 16}; break;
                    case 11: arr = new int[] {24, 3, 12, 8, 13}; break;
                    case 12: arr = new int[] {28, 7, 14, 4, 15}; break;
                    case 13: arr = new int[] {22, 12, 11, 4, 12}; break;
                    case 14: arr = new int[] {24, 11, 12, 5, 13}; break;
                    case 15: arr = new int[] {24, 11, 12, 7, 13}; break;
                    case 16: arr = new int[] {30, 3, 15, 13, 16}; break;
                    case 17: arr = new int[] {28, 2, 14, 17, 15}; break;
                    case 18: arr = new int[] {28, 2, 14, 19, 15}; break;
                    case 19: arr = new int[] {26, 9, 13, 16, 14}; break;
                    case 20: arr = new int[] {28, 15, 15, 10, 16}; break;
                    case 21: arr = new int[] {30, 19, 16, 6, 17}; break;
                    case 22: arr = new int[] {24, 34, 13, 0, 0}; break;
                    case 23: arr = new int[] {30, 16, 15, 14, 16}; break;
                    case 24: arr = new int[] {30, 30, 16, 2, 17}; break;
                    case 25: arr = new int[] {30, 22, 15, 13, 16}; break;
                    case 26: arr = new int[] {30, 33, 16, 4, 17}; break;
                    case 27: arr = new int[] {30, 12, 15, 28, 16}; break;
                    case 28: arr = new int[] {30, 11, 15, 31, 16}; break;
                    case 29: arr = new int[] {30, 19, 15, 26, 16}; break;
                    case 30: arr = new int[] {30, 23, 15, 25, 16}; break;
                    case 31: arr = new int[] {30, 23, 15, 28, 16}; break;
                    case 32: arr = new int[] {30, 19, 15, 35, 16}; break;
                    case 33: arr = new int[] {30, 11, 15, 46, 16}; break;
                    case 34: arr = new int[] {30, 59, 16, 1, 17}; break;
                    case 35: arr = new int[] {30, 22, 15, 41, 16}; break;
                    case 36: arr = new int[] {30, 2, 15, 64, 16}; break;
                    case 37: arr = new int[] {30, 24, 15, 46, 16}; break;
                    case 38: arr = new int[] {30, 42, 15, 32, 16}; break;
                    case 39: arr = new int[] {30, 10, 15, 67, 16}; break;
                    case 40: arr = new int[] {30, 20, 15, 61, 16}; break;
                }
        }
        return arr;
    }

    /**
     * A lookup method for finding the ArrayList associated with a given version number
     * Precondition: 7 <= version <= 40
     */
    public static ArrayList<Integer> versionBinaryLookup(int version) {
        ArrayList<Integer> arr = new ArrayList<>();
        switch(version) {
            case 7: arr = Encode.strToArrList("000111110010010100"); break;
            case 8: arr = Encode.strToArrList("001000010110111100"); break;
            case 9: arr = Encode.strToArrList("001001101010011001"); break;
            case 10: arr = Encode.strToArrList("001010010011010011"); break;
            case 11: arr = Encode.strToArrList("001011101111110110"); break;
            case 12: arr = Encode.strToArrList("001100011101100010"); break;
            case 13: arr = Encode.strToArrList("001101100001000111"); break;
            case 14: arr = Encode.strToArrList("001110011000001101"); break;
            case 15: arr = Encode.strToArrList("001111100100101000"); break;
            case 16: arr = Encode.strToArrList("010000101101111000"); break;
            case 17: arr = Encode.strToArrList("010001010001011101"); break;
            case 18: arr = Encode.strToArrList("010010101000010111"); break;
            case 19: arr = Encode.strToArrList("010011010100110010"); break;
            case 20: arr = Encode.strToArrList("010100100110100110"); break;
            case 21: arr = Encode.strToArrList("010101011010000011"); break;
            case 22: arr = Encode.strToArrList("010110100011001001"); break;
            case 23: arr = Encode.strToArrList("010111011111101100"); break;
            case 24: arr = Encode.strToArrList("011000111011000100"); break;
            case 25: arr = Encode.strToArrList("011001000111100001"); break;
            case 26: arr = Encode.strToArrList("011010111110101011"); break;
            case 27: arr = Encode.strToArrList("011011000010001110"); break;
            case 28: arr = Encode.strToArrList("011100110000011010"); break;
            case 29: arr = Encode.strToArrList("011101001100111111"); break;
            case 30: arr = Encode.strToArrList("011110110101110101"); break;
            case 31: arr = Encode.strToArrList("011111001001010000"); break;
            case 32: arr = Encode.strToArrList("100000100111010101"); break;
            case 33: arr = Encode.strToArrList("100001011011110000"); break;
            case 34: arr = Encode.strToArrList("100010100010111010"); break;
            case 35: arr = Encode.strToArrList("100011011110011111"); break;
            case 36: arr = Encode.strToArrList("100100101100001011"); break;
            case 37: arr = Encode.strToArrList("100101010000101110"); break;
            case 38: arr = Encode.strToArrList("100110101001100100"); break;
            case 39: arr = Encode.strToArrList("100111010101000001"); break;
            case 40: arr = Encode.strToArrList("101000110001101001"); break; //break not needed, just for clarity
        }
        return arr;
    }

    /**
     * Given a version, returns an array with the alphanumeric character capacity of the error correcting levels
     * L, M, Q, & H respectively. E.g. alphanumCapacityLookup(1) --> [25, 20, 16, 10]
     * So the character capacity of version 1 w/ error correction level l is 25
     * Unfortunately had to be hard coded, no formula exists(as far as I know)
     * Precondition: 1 <= version <= 41 (41 not a valid version #, just used for error purposes)
     */
    public static int[] alphanumCapacityLookup(int version) {
       int[] arr = new int[4];

       switch(version) {
           case 1: arr = new int[] {25, 20, 16, 10}; break;
           case 2: arr = new int[] {47, 38, 29, 20}; break;
           case 3: arr = new int[] {77, 61, 47, 35}; break;
           case 4: arr = new int[] {114, 90, 67, 50}; break;
           case 5: arr = new int[] {154, 122, 87, 64}; break;
           case 6: arr = new int[] {195, 154, 108, 84}; break;
           case 7: arr = new int[] {224, 178, 125, 93}; break;
           case 8: arr = new int[] {279, 221, 157, 122}; break;
           case 9: arr = new int[] {335, 262, 189, 143}; break;
           case 10: arr = new int[] {395, 311, 221, 174}; break;
           case 11: arr = new int[] {468, 366, 259, 200}; break;
           case 12: arr = new int[] {535, 419, 296, 227}; break;
           case 13: arr = new int[] {619, 483, 352, 259}; break;
           case 14: arr = new int[] {667, 528, 376, 283}; break;
           case 15: arr = new int[] {758, 600, 426, 321}; break;
           case 16: arr = new int[] {854, 656, 470, 365}; break;
           case 17: arr = new int[] {938, 734, 531, 408}; break;
           case 18: arr = new int[] {1046, 816, 574, 452}; break;
           case 19: arr = new int[] {1153, 909, 644, 493}; break;
           case 20: arr = new int[] {1249, 970, 702, 557}; break;
           case 21: arr = new int[] {1352, 1035, 742, 587}; break;
           case 22: arr = new int[] {1460, 1134, 823, 640}; break;
           case 23: arr = new int[] {1588, 1248, 890, 672}; break;
           case 24: arr = new int[] {1704, 1326, 963, 744}; break;
           case 25: arr = new int[] {1853, 1451, 1041, 779}; break;
           case 26: arr = new int[] {1990, 1542, 1094, 864}; break;
           case 27: arr = new int[] {2132, 1637, 1172, 910}; break;
           case 28: arr = new int[] {2223, 1732, 1263, 958}; break;
           case 29: arr = new int[] {2369, 1839, 1322, 1016}; break;
           case 30: arr = new int[] {2520, 1994, 1429, 1080}; break;
           case 31: arr = new int[] {2677, 2113, 1499, 1150}; break;
           case 32: arr = new int[] {2840, 2238, 1618, 1226}; break;
           case 33: arr = new int[] {3009, 2369, 1700, 1307}; break;
           case 34: arr = new int[] {3183, 2506, 1787, 1394}; break;
           case 35: arr = new int[] {3351, 2632, 1867, 1431}; break;
           case 36: arr = new int[] {3537, 2780, 1966, 1530}; break;
           case 37: arr = new int[] {3729, 2894, 2071, 1591}; break;
           case 38: arr = new int[] {3927, 3054, 2181, 1658}; break;
           case 39: arr = new int[] {4087, 3220, 2298, 1774}; break;
           case 40: arr = new int[] {4296, 3391, 2420, 1852}; break; //last break not needed, just for clarity
       }

       return arr;
    }

    /**
     * Method for finding the coordinates of alignment patterns(unfortunately has to be hard coded, no
     * formula for it). Given a version, returns an array of different ints and all POSSIBLE coordinates
     * of alignment patterns are all combinations of these numbers(remember if a finder pattern, timing belt etc.)
     * is in the way, the alignment pattern is not drawn
     * E.g. alignmentLookup(2) = [6, 18] so all possible locations of alignment patterns are (6,6), (6,18), (18,6), (18,18)
     * Precondition: 2 <= verison <= 40 (version 1 has no alignment patterns)
     */
    public static int[] alignmentLookup(int version) {
        int[] arr = new int[0];
        switch(version) {
            case 2: arr = new int[] {6, 18}; break;
            case 3: arr = new int[] {6, 22}; break;
            case 4: arr = new int[] {6, 26}; break;
            case 5: arr = new int[] {6, 30}; break;
            case 6: arr = new int[] {6, 34}; break;
            case 7: arr = new int[] {6, 22, 38}; break;
            case 8: arr = new int[] {6, 24, 42}; break;
            case 9: arr = new int[] {6, 26, 46}; break;
            case 10: arr = new int[] {6, 28, 50}; break;
            case 11: arr = new int[] {6, 30, 54}; break;
            case 12: arr = new int[] {6, 32, 58}; break;
            case 13: arr = new int[] {6, 34, 62}; break;
            case 14: arr = new int[] {6, 26, 46, 66}; break;
            case 15: arr = new int[] {6, 26, 48, 70}; break;
            case 16: arr = new int[] {6, 26, 50, 74}; break;
            case 17: arr = new int[] {6, 30, 54, 78}; break;
            case 18: arr = new int[] {6, 30, 56, 82}; break;
            case 19: arr = new int[] {6, 30, 58, 86}; break;
            case 20: arr = new int[] {6, 34, 62, 90}; break;
            case 21: arr = new int[] {6, 28, 50, 72, 94}; break;
            case 22: arr = new int[] {6, 26, 50, 74, 98}; break;
            case 23: arr = new int[] {6, 30, 54, 78, 102}; break;
            case 24: arr = new int[] {6, 28, 54, 80, 106}; break;
            case 25: arr = new int[] {6, 32, 58, 84, 110}; break;
            case 26: arr = new int[] {6, 30, 58, 86, 114}; break;
            case 27: arr = new int[] {6, 34, 62, 90, 118}; break;
            case 28: arr = new int[] {6, 26, 50, 74, 98, 122}; break;
            case 29: arr = new int[] {6, 30, 54, 78, 102, 126}; break;
            case 30: arr = new int[] {6, 26, 52, 78, 104, 130}; break;
            case 31: arr = new int[] {6, 30, 56, 82, 108, 134}; break;
            case 32: arr = new int[] {6, 34, 60, 86, 112, 138}; break;
            case 33: arr = new int[] {6, 30, 58, 86, 114, 142}; break;
            case 34: arr = new int[] {6, 34, 62, 90, 118, 146}; break;
            case 35: arr = new int[] {6, 30, 54, 78, 102, 126, 150}; break;
            case 36: arr = new int[] {6, 24, 50, 76, 102, 128, 154}; break;
            case 37: arr = new int[] {6, 28, 54, 80, 106, 132, 158}; break;
            case 38: arr = new int[] {6, 32, 58, 84, 110, 136, 162}; break;
            case 39: arr = new int[] {6, 26, 54, 82, 110, 138, 166}; break;
            case 40: arr = new int[] {6, 30, 58, 86, 114, 142, 170}; break; //last break for clarity, not necessary
        }
        return arr;
    }
}
