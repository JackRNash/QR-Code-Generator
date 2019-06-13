public class Runner {
    //purely for running methods & testing purposes
    public static void main(String[] args) {
        String msg = "Random text to test this stuff so fast randm woo it is working! GO BIGGER I NEED A VERSION 7 COde MORE WORDS PLEASE";
        int ecLevel = 0;

        Matrix m = new Matrix(Encode.calcVersion(msg.length(), ecLevel));

        m.makeQRCode(msg, Encode.strToArrList("0010"), ecLevel);
        new GUI(m.getMatrix());

    }


}
