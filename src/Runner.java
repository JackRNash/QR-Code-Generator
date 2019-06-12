import java.util.ArrayList;

public class Runner {
    //purely for running methods & testing purposes
    public static void main(String[] args) {

//        Matrix m = new Matrix(1);
//        m.addSeparators();
//        m.addAllAlignmentPats(1);
//        m.addTimingPats();
//        m.addVersionInfo(1);
//        ArrayList<Integer> nums2 = Encode.encodeMsg("ABCDEFGHIJKLMOPQRSTUVWXY", Encode.strToArrList("0010"), 1, 0);
//
//        m.addErrorCorrAndMaskInfo(Encode.strToArrList("111011111000100"));
//        m.inputBinary(nums2);
//        m.applyFirstMask();
        String msg = "Hello world";
        int ecLevel = 3;

        Matrix m = new Matrix(Encode.calcVersion(msg.length(), ecLevel));
        m.makeQRCode(msg, Encode.strToArrList("0010"), ecLevel);
        new GUI(m.getMatrix());

    }


}
