public class Finite {
    //For working with arithmetic in finite fields(in particular GF(256))

    private int[] alphas; //look up table where alphas[i] == 2^i in GF(256)
    private int[] nums; //look up table where nums[x] == i where x == 2^i

    /**
     * Fill out look up tables & store in memory
     */
    public Finite() {
        alphas = new int[256];
        nums = new int[256];
        alphas[0] = 1;

        for (int i = 1; i < 256; i++) {
            int x = gfMult(alphas[i - 1], 2);
            alphas[i] = x;
            nums[x] = i;
        }
    }

    /**
     * Multiplies two numbers in GF(256)
     * Any number x in GF(256) is in between 0 and 255(inclusive),
     * if larger, must XOR w/ 285 (per QR code specification)
     */
    public static int gfMult(int n1, int n2) {
        if (n1 > 255) n1 = n1 ^ 285; //NOTE: Might not need first two ifs, revisit later(cheap so keep in for now)
        if (n2 > 255) n2 = n2 ^ 255;
        int num = n1 * n2;
        if (num > 255) num = num ^ 285;
        return num;
    }


    public void printLookUps() { //method for debugging purposes only
        for (int i = 0; i < 256; i++) System.out.println("i: " + i + "\t2^i: " + alphas[i] + "\tnum: " + nums[i]);
    }

}
