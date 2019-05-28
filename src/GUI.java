import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    public static final int SQUARE_SIZE = 20; //size of a block in QR code in pixels
    private int[][] mat; //stores the QR code as an n x n matrix of zeroes and ones

    public GUI(int[][] matrix) {
        super("QR Code");
        Cell cells = new Cell(matrix);

        mat = matrix;
        int squares = mat.length;
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
            if (mat[i][j] == 1) g.setColor(Color.black);
            else g.setColor(Color.white);
            g2.fill(rec);
        }
    }
}

