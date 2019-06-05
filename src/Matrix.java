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
    public boolean emptyRing(int size, int x, int y) {
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
        mat[3][3] = 1;
        drawRing(1, 3, 2,2);
        drawRing(-1, 5, 1, 1);
        drawRing(1, 7, 0, 0);

        //top right
        mat[mat.length - 4][3] = 1;
        drawRing(1, 3,mat.length - 5, 2);
        drawRing(-1, 5, mat.length - 6, 1);
        drawRing(1,7, mat.length - 7, 0);

        //bottom left
        mat[3][mat.length - 4] = 1;
        drawRing(1, 3,2, mat.length - 5);
        drawRing(-1, 5, 1, mat.length - 6);
        drawRing(1,7,0, mat.length - 7);
    }

    /**
     * Add the separators next to the finder codes. Also adds the dark module and reserves area for
     * version & format information. MUST ADD BEFORE THE TIMING PATTERN
     */
    public void addSeparators() {
        //top left separator
        for(int i = 0; i < 8; i++) mat[i][7] = -1;
        for(int j = 0; j < 8; j++) mat[7][j] = -1;

        //top right separator
        for(int i = mat.length - 8; i < mat.length; i++) mat[i][7] = -1;
        for(int j = 0; j < 8; j++) mat[mat.length - 8][j] = -1;

        //bottom left separator
        for(int i = 0; i < 8; i++) mat[i][mat.length - 8] = -1;
        for(int j = mat.length - 8; j < mat.length; j++) mat[7][j] = -1;

        //dark module
        mat[8][mat.length - 8] = 1;

        //reserved spaces
        for(int j = mat.length - 7; j <  mat.length; j++) mat[8][j] = 2; //different color for debugging purposes
        for(int i = 0; i < 9; i++) mat[i][8] = 2;
        for(int j = 0; j < 8; j++) mat[8][j] = 2;
        for(int i = mat.length - 8; i < mat.length; i++) mat[i][8] = 2;
    }

    /**
     * If clear, adds alignment pattern centered at (x, y)
     */
    public void addAlignmentPat(int x, int y) {
        if(!emptyRing(5, x - 2, y - 2)) return; //check largest ring, if not clear, skip
        mat[x][y] = 1;
        drawRing(-1, 3, x - 1, y - 1);
        drawRing(1, 5, x - 2, y - 2);
    }

    /**
     * Adds the timing patterns on the left and top side of the QR code
     */
    public void addTimingPats() {
        //vertical timing pat
        for(int j = 8; j < mat.length - 8; j++) {
            if(j % 2 == 0) mat[6][j] = 1;
            else mat[6][j] = -1;
        }

        //horizontal timing pat
        for(int i = 8; i < mat.length - 8 ; i++){
            if(i % 2 == 0) mat[i][6] = 1;
            else mat[i][6] = -1;
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
     * Helper method for coloring the graph
     */
    public int binaryToColor(int binary) {
        if(binary == 1) return 1; //black
        else return -1; //white
    }
}
