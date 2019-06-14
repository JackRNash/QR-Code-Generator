import java.util.ArrayList;

public class Matrix {
    // For constructing the matrix that represents the QR code and will eventually be fed into the GUI
    // 1 represents black, -1 represents white
    private int[][] mat;
    private int version;

    /**
     * Creates the matrix associated with a QR code of specified size(min is 21)
     */
    public Matrix(int version) {
        this.version = version;
        int size = 4*version + 17;
        mat = new int[size][size];
    }

    public int[][] getMatrix() {
        return mat;
    }

    /**
     * Method to use to make a QR code.
     * String message: message to send
     * ArrayList<Integer> encodeType: type of encoding(alphanumeric, kanji etc.) in QR specifications format that
     * message should be encoded as
     * int ecLevel: Error correction level where 0, 1, 2, 3 correspond to L, M, Q, H respectively
     */
    public void makeQRCode(String message, ArrayList<Integer> encodeType, int ecLevel) {
        addFinders();
        addSeparators();
        addAllAlignmentPats(version);
        addTimingPats();

        int mask = bestMask();
        addErrorCorrAndMaskInfo(LookUp.ecMaskLookUp(ecLevel, mask));
        addVersionInfo(version);

        inputBinary(Encode.encodeMsg(message, encodeType, version, ecLevel));


        applyMask(mask, mat);
    }

    /**
     * Draws a ring of specified color(1: black, -1: white) of a specified size, moving to the right, then down, then
     * left, then back up, starting at the specified coordinates in the matrix
     */
    public void drawRing(int color, int size, int x, int y) {
        for(int i = x; i < size + x; i++) {
            mat[i][y] = color;
        }
        for(int j = y; j < y + size; j++) {
            mat[size + x - 1][j] = color;
        }

        for(int i = size + x - 1; i >= x; i--) {
            mat[i][y + size - 1] = color;
        }

        for(int j = y + size - 1; j >= y; j--) {
            mat[x][j] = color;
        }
    }

    /**
     * Travels the same path as draw ring would with the given inputs. Returns true if along this path
     * all entries in mat are 0 and false if there are any 1 or -1 entries. This method will be useful when adding
     * alignment patterns because if they would overlap with finding patterns or separators, we skip them
     */
    public boolean isEmptyRing(int size, int x, int y) {
        for(int i = x; i < size + x; i++) {
            if(mat[i][y] != 0) return false;
        }
        for(int j = y; j < y + size; j++) {
            if(mat[size + x - 1][j] != 0) return false;
        }
        for(int i = size + x - 1; i >= x; i--) {
            if(mat[i][y + size - 1] != 0) return false;
        }
        for(int j = y + size - 1; j >= y; j--) {
            if(mat[x][j] != 0) return false;
        }
        return true;
    }

    /**
     * Adds the finder patterns in the top right, top left, and bottom left
     */
    public void addFinders() {
        //top left
        mat[3][3] = 2;
        drawRing(2, 3, 2,2);
        drawRing(-2, 5, 1, 1);
        drawRing(2, 7, 0, 0);

        //top right
        mat[mat.length - 4][3] = 2;
        drawRing(2, 3,mat.length - 5, 2);
        drawRing(-2, 5, mat.length - 6, 1);
        drawRing(2,7, mat.length - 7, 0);

        //bottom left
        mat[3][mat.length - 4] = 2;
        drawRing(2, 3,2, mat.length - 5);
        drawRing(-2, 5, 1, mat.length - 6);
        drawRing(2,7,0, mat.length - 7);
    }

    /**
     * Add the separators next to the finder codes. Also adds the dark module and reserves area for
     * version & format information. MUST ADD BEFORE THE TIMING PATTERN
     */
    public void addSeparators() {
        //top left separator
        for(int i = 0; i < 8; i++) mat[i][7] = -2;
        for(int j = 0; j < 8; j++) mat[7][j] = -2;

        //top right separator
        for(int i = mat.length - 8; i < mat.length; i++) mat[i][7] = -2;
        for(int j = 0; j < 8; j++) mat[mat.length - 8][j] = -2;

        //bottom left separator
        for(int i = 0; i < 8; i++) mat[i][mat.length - 8] = -2;
        for(int j = mat.length - 8; j < mat.length; j++) mat[7][j] = -2;

        //dark module
        mat[8][mat.length - 8] = 2;

        //reserved spaces
        for(int j = mat.length - 7; j <  mat.length; j++) mat[8][j] = 3; //different color for debugging purposes
        for(int i = 0; i < 9; i++) mat[i][8] = 3;
        for(int j = 0; j < 8; j++) mat[8][j] = 3;
        for(int i = mat.length - 8; i < mat.length; i++) mat[i][8] = 3;
    }

    /**
     * If clear, adds alignment pattern centered at (x, y)
     */
    public void addAlignmentPat(int x, int y) {
        if(!isEmptyRing(5, x - 2, y - 2)) return; //check largest ring, if not clear, skip
        mat[x][y] = 2;
        drawRing(-2, 3, x - 1, y - 1);
        drawRing(2, 5, x - 2, y - 2);
    }

    /**
     * Adds all appropriate alignment patterns for a given version
     */
    public void addAllAlignmentPats(int version) {
        int[] arr = LookUp.alignmentLookUp(version);
        for (int i : arr) {
            for (int j : arr) {
                addAlignmentPat(i, j);
            }
        }
    }

    /**
     * Adds the timing patterns on the left and top side of the QR code
     */
    public void addTimingPats() {
        //vertical timing pat
        for(int j = 8; j < mat.length - 8; j++) {
            if(j % 2 == 0) mat[6][j] = 2;
            else mat[6][j] = -2;
        }

        //horizontal timing pat
        for(int i = 8; i < mat.length - 8 ; i++){
            if(i % 2 == 0) mat[i][6] = 2;
            else mat[i][6] = -2;
        }
    }

    /**
     * Takes in a size 15 ArrayList that contains information on the error correction level and
     * mask chosen, and inputs this information in the corresponding parts of the QR code
     */
    public void addErrorCorrAndMaskInfo(ArrayList<Integer> errorMaskInfo) {
        //bottom left
        for(int i = 0; i < 7; i++) {
            mat[8][mat.length - 1 - i] = binaryToColorSpecial(errorMaskInfo.get(i));
        }
        int offset = 0;

        //top left
        for(int i = 0; i < 8; i ++) {
            mat[i + offset][8] = binaryToColorSpecial(errorMaskInfo.get(i));
            if(i == 5) offset = 1;
        }
        mat[8][7] = binaryToColorSpecial(errorMaskInfo.get(8));
        for(int i = 0; i < 6; i++) {
            mat[8][5 - i] = binaryToColorSpecial(errorMaskInfo.get(9 + i));
        }

        //top right
        for(int i = 0; i < 8; i++) {
            mat[mat.length - 8 + i][8] = binaryToColorSpecial(errorMaskInfo.get(7 + i));
        }
    }

    /**
     * Add information encoded in binary(all at once)
     * Precondition: binary.size() <= the capacity of the QR code
     */
    public void inputBinary(ArrayList<Integer> binary) {
        int index = 1; int i = mat.length - 1 - 1; int j = mat.length - 1;
        int direction = -1; //-1 for up, +1 for down
        int len = binary.size();
        if(mat[i+1][j] == 0 && len > 0) mat[i + 1][j] = binaryToColor(binary.get(0)); //skipped over by structure of while loop
        else index = 0;

        //Must maintain   i >= 0 && j >= 0 && i < mat.length && j < mat.length
        while(index < len) {
            if(i >= 0 && j >= 0 && i < mat.length && j < mat.length && mat[i][j] == 0) {
                mat[i][j] = binaryToColor(binary.get(index));
                index++;
            }

            j += direction; i+= 1;

            if(i >= 0 && j >= 0 && i < mat.length && j < mat.length && mat[i][j] == 0 && index < len) {
                mat[i][j] = binaryToColor(binary.get(index));
                index++;
                i+= -1;
            } else if(j == mat.length) {
                direction = -1; //go up
                i -= 3;
                if(i == 5) i--; //skip vert timing pattern

            } else if(j == -1) {
                direction = 1;//go down
                i -= 3;
                if(i == 5) i--; //skip vert timing pattern
            } else {
                i+= -1;
            }

        }
    }

    /**
     * Applies the specified mask type on the input matrix
     * Precondition: 0 <= type <= 7
     */
    public void applyMask(int type, int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                if (m[i][j] == 1 || m[i][j] == -1) { //only 1 or -1 if part of the message(skips over finder pat etc.)
                    if ((type == 0 && (i + j) % 2 == 0)
                            || (type == 1 && j % 2 == 0)
                            || (type == 2 && i % 3 == 0)
                            || (type == 3 && (i + j) % 3 == 0)
                            || (type == 4 && (Math.floor(j / 2) + Math.floor(i / 3)) % 2 == 0)
                            || (type == 5 && (i * j) % 2 + (i * j) % 3 == 0)
                            || (type == 6 && ((i * j) % 2 + (i * j) % 3) % 2 == 0)
                            || (type == 7 && ((i + j) % 2 + (i * j) % 3) % 2 == 0)) {
                        m[i][j] *= -1; //flip from black to white or white to black
                    }
                }
            }
        }
    }

    /**
     * Returns the mask # that scores the lowest on the four tests
     * */
    public int bestMask() {
        int min = Integer.MAX_VALUE;
        int index = 0;
        for(int i = 0; i < 8; i++) {
            int[][] m = mat.clone();
            applyMask(i, m);
            int score = maskTest(m);
            if(score < min) {
                min = score;
                index = i;
            }
        }
        return index;
    }

    /**
     * Applies the four tests of a mask to the input matrix(mask applied already) and returns the score
     * Matrix should therefore be square
     */
    public int maskTest(int[][] m) {
        int sum = 0;

        //Test 1: If 5 of same color in a row, add 3, and add 1 more for every additional tile
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m.length - 4; j++) {
                if(isSameColor(m[i][j], m[i][j+1], m[i][j+2], m[i][j+3], m[i][j+4])) {
//                    if(matrix[i][j] > 0) {
//                        matrix[i][j] = 3;
//                        matrix[i][j+1] = 3;
//                        matrix[i][j+2] = 3;
//                        matrix[i][j+3] = 3;
//                        matrix[i][j+4] = 3;
//                    } else {
//                        matrix[i][j] = -3;
//                        matrix[i][j+1] = -3;
//                        matrix[i][j+2] = -3;
//                        matrix[i][j+3] = -3;
//                        matrix[i][j+4] = -3;
//                    }
                    sum += 3;
                    int temp = j+4;
                    while((j + 5 < m.length) && isSameColor(m[i][temp], m[i][j+5])) {
//                        if(matrix[i][j+5] > 0) matrix[i][j+5] = 3;
//                        else matrix[i][j+5] = -3;
                        sum += 1;
                        j++;
                    }
                }
            }
        }
        for(int j = 0; j < m.length; j++) {
            for(int i = 0; i < m.length - 4; i++) {
                if(isSameColor(m[i][j], m[i+1][j], m[i+2][j], m[i+3][j], m[i+4][j])) {
//                    if(matrix[i][j] > 0) {
//                        matrix[i][j] = 3;
//                        matrix[i+1][j] = 3;
//                        matrix[i+2][j] = 3;
//                        matrix[i+3][j] = 3;
//                        matrix[i+4][j] = 3;
//                    } else {
//                        matrix[i][j] = -3;
//                        matrix[i+1][j] = -3;
//                        matrix[i+2][j] = -3;
//                        matrix[i+3][j] = -3;
//                        matrix[i+4][j] = -3;
//                    }

                    sum += 3;
                    int temp = i+4;
                    while((i + 5 < m.length) && isSameColor(m[temp][j], m[i+5][j])) {
//                        if(matrix[i+5][j] > 0) matrix[i+5][j] = 3;
//                        else matrix[i+5][j] = -3;
                        sum += 1;
                        i++;
                    }
                }
            }
        }

        //Test 2: Penalty for each 2x2 block of same color
        for(int i = 0; i < m.length - 1; i++) {
            for(int j = 0; j < m.length - 1; j++) {
                if(isSameColor(m[i][j], m[i+1][j], mat[i][j+1], m[i+1][j+1])) {
//                    if(matrix[i][j] > 0) {
//                        matrix[i][j] = 3;
//                        matrix[i+1][j] = 3;
//                        matrix[i+1][j+1] = 3;
//                        matrix[i][j+1] = 3;
//                    } else {
//                        matrix[i][j] = -3;
//                        matrix[i+1][j] = -3;
//                        matrix[i+1][j+1] = -3;
//                        matrix[i][j+1] = -3;
//                    }
                    sum += 3;
                }
            }
        }

        //Test 3: Big penalty for pattern [B, W, B, B, B, W, B, W, W, W, W] or reverse
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m.length - 9; j++) {
                if((m[i][j] > 0 && m[i][j+1] < 0 && m[i][j+2] > 0 && m[i][j+3] > 0 && m[i][j+4] > 0 && m[i][j+5] < 0
                && m[i][j+6] > 0 && m[i][j+7] < 0 && m[i][j+8] < 0 && m[i][j+9] < 0 && m[i][j+10] < 0) ||
                (m[i][j] < 0 && m[i][j+1] < 0 && m[i][j+2] < 0 && m[i][j+3] < 0 && m[i][j+4] > 0 && m[i][j+5] < 0
                && m[i][j+6] > 0 && m[i][j+7] > 0 && m[i][j+8] > 0 && m[i][j+9] < 0 && m[i][j+10] > 0)) {
                    sum += 40;
                }
            }
        }
        for(int j = 0; j < m.length; j++) {
            for(int i = 0; i < m.length - 9; i++) {
                if((m[i][j] > 0 && m[i+1][j] < 0 && m[i+2][j] > 0 && m[i+3][j] > 0 && m[i+4][j] > 0 && m[i+5][j] < 0
                        && m[i+6][j] > 0 && m[i+7][j] < 0 && m[i+8][j] < 0 && m[i+9][j] < 0 && m[i+10][j] < 0) ||
                        (m[i][j] < 0 && m[i+1][j] < 0 && m[i+2][j] < 0 && m[i+3][j] < 0 && m[i+4][j] > 0 && m[i+5][j] < 0
                                && m[i+6][j] > 0 && m[i+7][j] > 0 && m[i+8][j] > 0 && m[i+9][j] < 0 && m[i+10][j] > 0)) {
                    sum += 40;
                }
            }
        }

        //Test 4: Penalty based on weird ratio of black to white modules
        int totalSqrs = m.length * m.length;
        int darkSqrs = 0;
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m.length; j++) {
                if(m[i][j] > 0) darkSqrs++;
            }
        }
        int ratio = 100 * darkSqrs / totalSqrs;
        int lower = ratio/5;
        int upper = lower + 1;
        sum += 10 * Math.min(Math.abs(5*lower - 50)/5, Math.abs(5*upper - 50)/5);


        return sum;
    }

    /**
     * Helper method to compare two colors(since using 2 to denote a special black and 1 to also denote black & similar
     * for white), need a way of checking if two squares are the same color that's more involved than a == b
     */
    public boolean isSameColor(int a, int b) {
        if(a > 0 && b > 0) return true;
        if(a < 0 && b < 0) return true;
        return false;
    }

    /**
     * Same as other isSameColor, just checks that all 4 variables are the same color
     */
    public boolean isSameColor(int a, int b, int c, int d) {
        if(a > 0 && b > 0 && c > 0 && d > 0) return true;
        if(a < 0 && b < 0 && c < 0 && d < 0) return true;
        return false;
    }

    /**
     * Same as other isSameColor, just checks that all 5 variables are the same color
     */
    public boolean isSameColor(int a, int b, int c, int d, int e) {
        if(a > 0 && b > 0 && c > 0 && d > 0 && e > 0) return true;
        if(a < 0 && b < 0 && c < 0 && d < 0 && e < 0) return true;
        return false;
    }


    /**
     * Helper method for coloring the graph(for the encoded message)
     */
    public int binaryToColor(int binary) {
        if(binary == 1) return 1; //black
        else return -1; //white
    }

    /**
     * Helper method for coloring the graph(for special things like version, error correction, and mask info)
     */
    public int binaryToColorSpecial(int binary) {
        if(binary == 1) return 2; //black
        else return -2; //white
    }

    /**
     * Adds the version info on matrices (could've written[and tested and debugged] a method to generate these,
     * but there are so few and hard-coding them in is feasible and more efficient(in terms of programming time),
     * so I opted for that)
     * Precondition: 1 <= version <= 40
     */
    public void addVersionInfo(int version) {
        if(version < 7) return; //only input if version >= 7
        ArrayList<Integer> arr = LookUp.versionBinaryLookUp(version);
        //note that arr.size() == 18 (hence arr.size() - 1 == 17 which is used in the following code)
        //input top right version info
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 3; j++) {
                mat[mat.length - 11 + j][i] = binaryToColorSpecial(arr.get(17 - (3*i + j)));
            }
        }
        //input bottom left version info
        for(int j = 0; j < 3; j++) {
            for(int i = 0; i < 6; i++) {
                mat[i][mat.length - 11 + j] = binaryToColorSpecial(arr.get(17 - (3*i + j)));
            }
        }
    }

}
