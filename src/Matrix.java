import java.util.ArrayList;

public class Matrix {
    // For constructing the matrix that represents the QR code and will eventually be fed into the GUI
    // 1 represents black, -1 represents white
    private int[][] mat;

    /**
     * Creates the matrix associated with a QR code of specified size(min is 21)
     */
    public Matrix(int size) {
        mat = new int[size][size];
        addFinders();
        addSeparators();
        addTimingPats();
    }

    public int[][] getMatrix() {
        return mat;
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
        int[] arr = alignmentLookup(version);
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr.length; j++) {
                addAlignmentPat(arr[i], arr[j]);
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
    public void addErrorCorrAndMaskInfo(ArrayList<Integer> input) {
        //bottom left
        for(int i = 0; i < 7; i++) {
            mat[8][mat.length - 1 - i] = binaryToColorSpecial(input.get(i));
        }
        int offset = 0;

        //top left
        for(int i = 0; i < 8; i ++) {
            mat[i + offset][8] = binaryToColorSpecial(input.get(i));
            if(i == 5) offset = 1;
        }
        mat[8][7] = binaryToColorSpecial(input.get(8));
        for(int i = 0; i < 6; i++) {
            mat[8][5 - i] = binaryToColorSpecial(input.get(9 + i));
        }

        //top right
        for(int i = 0; i < 8; i++) {
            mat[mat.length - 8 + i][8] = binaryToColorSpecial(input.get(7 + i));
        }
    }

    /**
     *
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
            //System.out.println("i: " + i + "\tj: " + j + "\tdirection: " + direction);

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
            case 2:
                arr = new int[] {6, 18}; break;
            case 3:
                arr = new int[] {6, 22}; break;
            case 4:
                arr = new int[] {6, 26}; break;
            case 5:
                arr = new int[] {6, 30}; break;
            case 6:
                arr = new int[] {6, 34}; break;
            case 7:
                arr = new int[] {6, 22, 38}; break;
            case 8:
                arr = new int[] {6, 24, 42}; break;
            case 9:
                arr = new int[] {6, 26, 46}; break;
            case 10:
                arr = new int[] {6, 28, 50}; break;
            case 11:
                arr = new int[] {6, 30, 54}; break;
            case 12:
                arr = new int[] {6, 32, 58}; break;
            case 13:
                arr = new int[] {6, 34, 62}; break;
            case 14:
                arr = new int[] {6, 26, 46, 66}; break;
            case 15:
                arr = new int[] {6, 26, 48, 70}; break;
            case 16:
                arr = new int[] {6, 26, 50, 74}; break;
            case 17:
                arr = new int[] {6, 30, 54, 78}; break;
            case 18:
                arr = new int[] {6, 30, 56, 82}; break;
            case 19:
                arr = new int[] {6, 30, 58, 86}; break;
            case 20:
                arr = new int[] {6, 34, 62, 90}; break;
            case 21:
                arr = new int[] {6, 28, 50, 72, 94}; break;
            case 22:
                arr = new int[] {6, 26, 50, 74, 98}; break;
            case 23:
                arr = new int[] {6, 30, 54, 78, 102}; break;
            case 24:
                arr = new int[] {6, 28, 54, 80, 106}; break;
            case 25:
                arr = new int[] {6, 32, 58, 84, 110}; break;
            case 26:
                arr = new int[] {6, 30, 58, 86, 114}; break;
            case 27:
                arr = new int[] {6, 34, 62, 90, 118}; break;
            case 28:
                arr = new int[] {6, 26, 50, 74, 98, 122}; break;
            case 29:
                arr = new int[] {6, 30, 54, 78, 102, 126}; break;
            case 30:
                arr = new int[] {6, 26, 52, 78, 104, 130}; break; //
            case 31:
                arr = new int[] {6, 30, 56, 82, 108, 134}; break;
            case 32:
                arr = new int[] {6, 34, 60, 86, 112, 138}; break;
            case 33:
                arr = new int[] {6, 30, 58, 86, 114, 142}; break;
            case 34:
                arr = new int[] {6, 34, 62, 90, 118, 146}; break;
            case 35:
                arr = new int[] {6, 30, 54, 78, 102, 126, 150}; break;
            case 36:
                arr = new int[] {6, 24, 50, 76, 102, 128, 154}; break;
            case 37:
                arr = new int[] {6, 28, 54, 80, 106, 132, 158}; break;
            case 38:
                arr = new int[] {6, 32, 58, 84, 110, 136, 162}; break;
            case 39:
                arr = new int[] {6, 26, 54, 82, 110, 138, 166}; break;
            case 40:
                arr = new int[] {6, 30, 58, 86, 114, 142, 170}; break; //break for clarity, not necessary
        }
        return arr;
    }
}
