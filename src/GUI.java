import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    public static final int SQUARE_SIZE = 20; //size of a block in QR code in pixels

    public GUI(int[][] matrix) {
        super("QR Code");

        //transfer information into new matrix with a 2 cell border of zeroes for the quiet zone required for QR codes
        int[][] quietMat = new int[matrix.length + 4][matrix.length + 4];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                quietMat[i+2][j+2] = matrix[i][j];
            }
        }
        Cell cells = new Cell(quietMat);

        int squares = quietMat.length;
        int QRdim = squares * SQUARE_SIZE;

        JFrame frame = new JFrame();
        frame.setSize(QRdim + SQUARE_SIZE, QRdim + 2*SQUARE_SIZE); //make sure code fully visible, tinker with later
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
            i = rec.x/GUI.SQUARE_SIZE;
            j = rec.y/GUI.SQUARE_SIZE;
            if (mat[i][j] == 1 || mat[i][j] == 2) g.setColor(Color.black);
            else if(mat[i][j] == -1 || mat[i][j] == -2) g.setColor(Color.white);
            else if(mat[i][j] == 3) g.setColor(Color.orange); //non black/white colors for debugging purposes
            else g.setColor(Color.lightGray);
            g2.fill(rec);
        }
    }
}

