import java.util.Scanner;

public class Main {


    // a method that multiply two matrices
    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int m = A.length; // The number of rows of matrix A
        int n = A[0].length; // The number of cols in matrix A
        int p = B[0].length; // The number of cols in matrix B

        // resulting matrix
        int[][] C = new int[m][p];

        for (int i = 0; i < m; i++) {  // for each row of A
            for (int j = 0; j < p; j++) {  // for each col of B
                for (int k = 0; k < n; k++) { // sum of inner dimension
                    C[i][j] += A[i][k] * B[k][j]; //
                }
            }
        }

        //return the resulting matrix
        return C;
    }


    // parses the input to check if the correct input was provided
    public static int[] parsedInputs(String input) {
        String[] parts = input.split(","); // split the input by comma

        if (parts.length != 2) {
            System.out.println("Error: Please enter the size in the correct format (e.g., 2,3).");
            return new int[]{-1, -1};
        }

        try {
            // parse or converts the inputs to integers while removing whitespace
            int rowA = Integer.parseInt(parts[0].trim());
            int colA = Integer.parseInt(parts[1].trim());

            // if the input values are not positive we return -1, -1
            if (rowA < 0 || colA < 0) {
               return new int[]{-1, -1};
            }

            // returned the parsed inputs as an array
            return new int[]{rowA, colA};
        } catch (NumberFormatException e) {
            // if there is an error with the parsing of the inputs
            return new int[]{-1, -1};
        }

    }

    // method to get the matrix from the user
    public static int[][] getMatrix(Scanner sc, String label) {
        int rows, cols;

        // Loop until valid matrix dimensions are provided
        while (true) {
            System.out.printf("Please enter the sizes of Matrix %s (e.g., 2,3 for 2 rows and 3 columns):%n", label);
            String input = sc.nextLine();
            int[] dimensions = parsedInputs(input);
            if (dimensions[0] != -1 && dimensions[1] != -1) {
                rows = dimensions[0];
                cols = dimensions[1];
                break; // valid input, exit loop
            }

            // Show error and prompt again
            System.out.println("Invalid input. Try again.");
        }

        // user info
        System.out.println("Enter the matrix values row by row (e.g., for a 2x3 matrix):");
        System.out.println("12 4 4");
        System.out.println("3 4 5");
        System.out.printf("Please enter Matrix %s:%n", label);

        //  Allocate space for the matrix
        int[][] matrix = new int[rows][cols];

        // populate matrix
        populateMatrix(matrix, rows, cols, sc);
        return matrix;
    }

    // method to populate the matrix the user types
    public static void populateMatrix (int[][] matrix, int rows, int cols, Scanner sc) {
        for (int i = 0; i < rows; i++) {

            // Loop until valid matrix values are given
            while (true) {
                System.out.printf("Row %d: ", i + 1);
                String line = sc.nextLine().trim();
                String[] values = line.split("\\s+");

                // Check if user provided correct number of values for the row
                if (values.length != cols) {
                    System.out.println("Incorrect number of values. Please enter " + cols + " integers.");
                    continue;
                }

                boolean isValid = true;

                // Parse and validate each value in the row
                for (int j = 0; j < cols; j++) {
                    try {
                        int val = Integer.parseInt(values[j]);
                        if (val < 0) {
                            System.out.println("Invalid input: only non-negative integers allowed.");
                            isValid = false;
                            break;
                        }

                        // Assign the parsed value to the matrix
                        matrix[i][j] = val;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input: Please enter integers only.");
                        isValid = false;
                        break;
                    }
                }

                if (isValid) break; // move to next row
            }
        }
    }

    // prints out matrix values to the console
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf(" %d ", matrix[i][j]);
            }
            System.out.print("|\n");
        }
    }

    // main function
    public static void main(String[] args) {

        // scanner to read inputs from cli
        Scanner sc = new Scanner(System.in);

        // getting the matrix for matrix A
        System.out.println("Matrix A:");
        int[][] matrixA = getMatrix(sc, "A");



        System.out.println("\nMatrix B:");
        int[][] matrixB = getMatrix(sc, "B");


        // if the number of columns of matrix A is not equal to the rows of matrix B then, multiplying the matrix won't be possible
        if (matrixA[0].length != matrixB.length) {
            System.out.println("Matrix multiplication is not possible. The number of columns in Matrix A must be equal to the number of rows in Matrix B.");
            return;
        }

        // new line
        System.out.println();

        // results of multiplying the matrix
        int[][] matrixC = multiplyMatrices(matrixA, matrixB);

        // printing the results to the screen
        System.out.println("The result of the multiplying the two matrices are;");
        printMatrix(matrixC);

        //close scanner
        sc.close();


    }
}