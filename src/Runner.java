public class Runner {
    //purely for running methods & testing purposes
    public static void main(String[] args) {
        int[][] test = new int[10][10]; //test drawing alternating black and white squares
        for(int i = 0; i < test.length; i++) {
            for(int j = 0; j < test.length; j++) {
                if((i+j) % 2 == 0) test[i][j] = 1;
            }
        }
        new GUI(test);
    }
}
