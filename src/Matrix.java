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

        addErrorCorrAndMaskInfo(LookUp.ecMaskLookUp(ecLevel));
        addVersionInfo(version);

        inputBinary(Encode.encodeMsg(message, encodeType, version, ecLevel));


        applyFirstMask();
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
     * Applies the first mask
     */
    public void applyFirstMask() {
        for(int i = 0; i < mat.length; i++) {
            for(int j = 0; j < mat.length; j++) {
                if((i + j) % 2 == 0 && (mat[i][j] == 1 || mat[i][j] == -1)) mat[i][j] *= -1;
            }
        }
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
