import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QRCodeGUI extends JFrame {
    public static final int MAX_SQUARE_SIZE = 30; //size of a block in QR code in pixels(max is preferred size,
    public static final int MIN_SQUARE_SIZE = 5; //will shrink down to min size based on length of message)
    public static final int PREF_QR_SIZE = 400; //Preferred size of the final QR code(in pixels)
    public int squareSize;

    public QRCodeGUI(int[][] matrix) {
        //transfer information into new matrix with a 4 cell border of zeroes for the quiet zone required for QR codes
        int[][] quietMat = new int[matrix.length + 8][matrix.length + 8];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                quietMat[i+4][j+4] = matrix[i][j];
            }
        }
        if(MAX_SQUARE_SIZE*quietMat.length < PREF_QR_SIZE) squareSize = MAX_SQUARE_SIZE;
        else squareSize = Math.max(PREF_QR_SIZE /quietMat.length, MIN_SQUARE_SIZE);

        Cell cells = new Cell(quietMat, squareSize);

        int squares = quietMat.length;
        int QRdim = squares * squareSize;

        JFrame frame = new JFrame("QR Code");
        frame.setSize(QRdim + squareSize, QRdim + 2* squareSize); //make sure code fully visible, tinker with later

        for (int i = 0; i < squares; i++) {
            for (int j = 0; j < squares; j++) {
                cells.addCell(squareSize *i, squareSize *j, squareSize);
            }
        }
        frame.add(cells);
        frame.setVisible(true);
    }

}

class Cell extends JPanel{
    private ArrayList<Rectangle> cells = new ArrayList<>();
    private int[][] mat;
    private int size;

    public Cell(int[][] matrix, int size) {
        mat = matrix;
        this.size = size;
    }

    public void addCell(int x,int y,int width){
        cells.add(new Rectangle(x,y,width,width));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int i, j;
        for (Rectangle rec: cells) {
            i = rec.x/size;
            j = rec.y/size;
            //For debugging purposes, use the below
//            if (mat[i][j] == 1 || mat[i][j] == 2) g.setColor(Color.black);
//            else if(mat[i][j] == -1 || mat[i][j] == -2) g.setColor(Color.white);
//            else if(mat[i][j] == 3) g.setColor(Color.orange); //non black/white colors for debugging purposes
//            else if(mat[i][j] == -3) g.setColor(Color.yellow);
//            else g.setColor(Color.lightGray);
            //For real use, this this
            if(mat[i][j] > 0) g.setColor(Color.black);
            else g.setColor(Color.white);
            g2.fill(rec);
        }
    }
}

