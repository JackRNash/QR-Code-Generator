public class Finite {
    /* For working with arithmetic in finite fields(in particular GF(256))
       NOTE: When I say alpha notation, I mean the number x is really 2^x
       e.g. 4 in alpha notation is 2. When I say normal form, I just mean
       numbers are equal in value to the number stated e.g. 4 in normal form is 4
       Reed Solomon error correcting uses alpha notation for many parts of the process
     */

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
        nums[1] = 0; //would be 255 otherwise, this is better to work w/
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

    /**
     * Multiplies two polynomials in GF(256)
     * Input array arr# represents the polynomial  arr#[0] + arr#[1]x + ... + arr#[n]x^n
     * & arr#.length = n + 1. Preferably arrays are the smallest possible such that arr[n] = 0
     */
    public int[] gfPolyMult(int[] arr1, int[] arr2) {
        /*Let arr1 be length n + 1, then the polynomial associated w/ arr1 is at most degree n
          Similar for arr2, hence product polynomial is at most degree (arr1.length - 1) + (arr2.length - 1)
         */
        int[] arr = new int[arr1.length + arr2.length - 1];
        arr1 = toAlpha(arr1);
        arr2 = toAlpha(arr2);
        for(int i = 0; i < arr1.length; i++) {
            if(arr1[i] == -1) continue; //DON'T multiply if corresponding coefficient in original array is 0

            for(int j = 0; j < arr2.length; j++) {
                if(arr2[j] == -1) continue;

                arr[i+j] = toNum((arr1[i] + arr2[j]) % 255) ^ arr[i+j];
            }

        }

        return arr;
    }

    /**
     * Returns the generator polynomial of degree deg
     * Precondition: deg > 0
     */
    public int[] genPoly(int deg) {
        int[] arr = new int[2]; arr[1] = 1; arr[0] = 1; //polynomial is (1 + x)
        if (deg == 1) return arr;
        arr[0] = alphas[deg-1]; // polynomial is now (a^(deg-1) + x)
        return gfPolyMult(genPoly(deg - 1), arr);
    }

    /**
     * Converts the entries in arr from normal numbers to alpha notation where the new entries when raised to the
     * power of 2 are the old entries. E.g. [2, 4] --> [1, 2]
     */
    public int[] toAlpha(int[] arr1) {
        int[] arr = arr1.clone();
        for(int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) arr[i] = -1; //internal code, required or else toAlpha(toNum([0])) == [1] != [0] (NOT ideal)

            else arr[i] = nums[arr[i]];
        }
        return arr;
    }

    /**
     * Converts a normal number n to alpha notation
     */
    public int toAlpha(int n) {
        if (n == 0) return -1; //internal code, required or else toAlpha(toNum([0])) == [1] != [0] (NOT ideal)
        else return nums[n];
    }

    /**
     * Converts the entries in arr from alpha notation to normal numbers where the new entries are the old entries
     * raised to the power of 2. E.g. [1, 2] --> [2, 4]
     */
    public int[] toNum(int[] arr1) {
        int[] arr = arr1.clone();
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == -1) arr[i] = 0; //internal code, no other way to get 2^x = 0 for some x
            else arr[i] = alphas[arr[i]];
        }
        return arr;
    }

    /**
     * Converts a number n in alpha notation to normal form
     */
    public int toNum(int n) {
        if(n == -1) return 0; //internal code, no other way to get 2^x = 0 for some x
        else return alphas[n];
    }

    public void printLookUps() { //method for debugging purposes only
        for (int i = 0; i < 256; i++) System.out.println("i: " + i + "\t2^i: " + alphas[i] + "\tnum: " + nums[i]);
    }

}
