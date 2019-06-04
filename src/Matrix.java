public class Matrix {
    // For constructing the matrix that represents the QR code and will eventually be fed into the GUI
    // 1 represents black, -1 represents white
    private int[][] mat;

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

}
