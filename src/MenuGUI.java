import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class MenuGUI extends JFrame implements ActionListener {
    private String msg;
    private JButton button;
    private JTextArea msgTxt;
    private int ecLevel;
    private JSlider slider;
    private JFrame frame;

    public MenuGUI() {
        frame = new JFrame("QR Code Generator");
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        JLabel title = new JLabel("Generate a QR code");
        title.setFont(new Font("", Font.PLAIN, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.ipady = 20;
        pane.add(title, c);

        JLabel msg = new JLabel("Message:");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        pane.add(msg, c);

        msgTxt = new JTextArea(2, 2);
        msgTxt.setLineWrap(true);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.ipady = 10;
        pane.add(msgTxt, c);

        JLabel ec = new JLabel("Error Correction Level(% of msg that can have errors and be corrected):");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.ipady = 0;
        c.insets = new Insets(10,0,0,0);
        pane.add(ec, c);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 3, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
                Hashtable<Integer, JLabel> ecLabels = new Hashtable<>();
        ecLabels.put(0, new JLabel("7%"));
        ecLabels.put(1, new JLabel("15%"));
        ecLabels.put(2, new JLabel("25%"));
        ecLabels.put(3, new JLabel("30%"));
        slider.setLabelTable(ecLabels);
        slider.setPaintLabels(true);
        JPanel sliderPanel = new JPanel(new FlowLayout());
        sliderPanel.setPreferredSize(new Dimension(600, 80));
        sliderPanel.add(slider);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,0,0);
        pane.add(sliderPanel, c);

        button = new JButton("Generate");
        button.addActionListener(this);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        pane.add(button, c);

        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
            msg = msgTxt.getText();
            ecLevel = slider.getValue();
            int version = Encode.calcVersion(msg.length(), ecLevel);
            if(version > 40) {
                JOptionPane.showMessageDialog(frame, "Your message is too long for your selected error " +
                        "correction level. \nEither make your message shorter or lower your selected error correction " +
                        "level.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Matrix m = new Matrix(version);
                m.makeQRCode(msg, Encode.strToArrList("0010"), ecLevel);
                new QRCodeGUI(m.getMatrix());
            }
        }
    }
}
