public class Runner {
    //purely for running methods & testing purposes
    public static void main(String[] args) {
        String msg = "HELLO WORLD";
        int ecLevel = 2;

        Matrix m = new Matrix(Encode.calcVersion(msg.length(), ecLevel));

        m.makeQRCode(msg, Encode.strToArrList("0010"), ecLevel);
        new GUI(m.getMatrix());

    }


}
