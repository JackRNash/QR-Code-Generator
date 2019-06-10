import java.util.ArrayList;

public class Runner {
    //purely for running methods & testing purposes
    public static void main(String[] args) {

        Matrix m = new Matrix(25);
//        m.addAlignmentPat(6, 6);
//        m.addAlignmentPat(6, 18);
//        m.addAlignmentPat(18, 6);
//        m.addAlignmentPat(18, 18);
        m.addAllAlignmentPats(2);
        m.addTimingPats();

        ArrayList<Integer> nums2 = Encode.encodeMsg("hello world", Encode.strToArrList("0010"), 9);

        m.addErrorCorrAndMaskInfo(Encode.strToArrList("111011111000100"));
        //m.inputBinary(nums2);
        m.applyFirstMask();
        new GUI(m.getMatrix());

    }


}
