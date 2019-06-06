import java.util.ArrayList;

public class Runner {
    //purely for running methods & testing purposes
    public static void main(String[] args) {
//        int[][] test = new int[10][10];
//        for(int i = 0; i < test.length; i++) {
//            for(int j = 0; j < test.length; j++) {
//                //if((i+j) % 2 == 0) test[i][j] = 1; //test drawing alternating black and white squares
//                if(Math.round(Math.random()) == 1) test[i][j] = 1; //random black vs white squares
//            }
//        }
        Matrix m = new Matrix(21);
//        m.addAlignmentPat(6, 6);
//        m.addAlignmentPat(6, 18);
//        m.addAlignmentPat(18, 6);
//        m.addAlignmentPat(18, 18);
        m.addTimingPats();
        ArrayList<Integer> nums = new ArrayList<>();

        for(int i = 0; i < 118; i ++) {
            //int x = (int)(2*Math.random());
            nums.add(1);
        }

        ArrayList<Integer> nums2 = strToArrList("010111001111110011000001110");

//        ArrayList<Integer> errAndMask = strToArrList("01100");
//        ErrorCorrection ec = new ErrorCorrection();
//        int[] errCorr = ec.genPoly(10);
//        for(int i = 0; i < errCorr.length; i++) {
//            System.out.println(errCorr[i]);
//        }
        m.addErrorCorrAndMaskInfo(strToArrList("110011000101111"));
        m.inputBinary(nums);
        new GUI(m.getMatrix());

    }

    public static ArrayList<Integer> strToArrList(String s) { //helper method
        ArrayList<Integer> arr = new ArrayList<>();
        for(char c: s.toCharArray()) {
            arr.add(Integer.parseInt(c + ""));
        }
        return arr;
    }
}
