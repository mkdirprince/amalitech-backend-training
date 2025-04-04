public class Main {


    // a method that multiply two matrices
    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int m = A.length; // The number of rows of matrix A
        int n = A[0].length; // The number of cols in matrix A
        int p = B[0].length; // The number of cols in matrix B

        // results of multiplied matrix
        int[][] C = new int[m][p];

        for (int i = 0; i < m; i++) {  // for each row of A
            for (int j = 0; j < p; j++) {  // for each col of B
                for (int k = 0; k < n; k++) { // sum of inner dimension
                    C[i][j] += A[i][k] * B[i][k]; //
                }
            }
        }

        return C;
    }

    public static void main(String[] args) {

        System.out.println("Hello");
    }
}