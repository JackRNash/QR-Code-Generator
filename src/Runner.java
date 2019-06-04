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
        Matrix m = new Matrix(25);
        m.addFinders();
        m.addSeparators();
        m.addAlignmentPat(6, 6);
        m.addAlignmentPat(6, 18);
        m.addAlignmentPat(18, 6);
        m.addAlignmentPat(18, 18);
        m.addTimingPats();
        new GUI(m.getMatrix());

    }
}
