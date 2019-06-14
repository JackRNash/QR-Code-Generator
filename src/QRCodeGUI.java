import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QRCodeGUI extends JFrame {
    public static final int SQUARE_SIZE = 30; //size of a block in QR code in pixels

    public QRCodeGUI(int[][] matrix) {
        //transfer information into new matrix with a 4 cell border of zeroes for the quiet zone required for QR codes
        int[][] quietMat = new int[matrix.length + 8][matrix.length + 8];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                quietMat[i+4][j+4] = matrix[i][j];
            }
        }
        Cell cells = new Cell(quietMat);

        int squares = quietMat.length;
        int QRdim = squares * SQUARE_SIZE;

        JFrame frame = new JFrame("QR Code");
        frame.setSize(QRdim + SQUARE_SIZE, QRdim + 2*SQUARE_SIZE); //make sure code fully visible, tinker with later

        for (int i = 0; i < squares; i++) {
            for (int j = 0; j < squares; j++) {
                cells.addCell(SQUARE_SIZE*i, SQUARE_SIZE*j, SQUARE_SIZE);
            }
        }
        frame.add(cells);
        frame.setVisible(true);
    }

}

class Cell extends JPanel{
    private ArrayList<Rectangle> cells = new ArrayList<>();
    private int[][] mat;

    public Cell(int[][] matrix) {mat = matrix;}

    public void addCell(int x,int y,int width){
        cells.add(new Rectangle(x,y,width,width));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int i, j;
        for (Rectangle rec: cells) {
            i = rec.x/QRCodeGUI.SQUARE_SIZE;
            j = rec.y/QRCodeGUI.SQUARE_SIZE;
            if (mat[i][j] == 1 || mat[i][j] == 2) g.setColor(Color.black);
            else if(mat[i][j] == -1 || mat[i][j] == -2) g.setColor(Color.white);
            else if(mat[i][j] == 3) g.setColor(Color.orange); //non black/white colors for debugging purposes
            else if(mat[i][j] == -3) g.setColor(Color.yellow);
            else g.setColor(Color.lightGray);
            g2.fill(rec);
        }
    }
}

