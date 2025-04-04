public class Main {

    // Returns the maximum grade
    public static int maxGrade(int[] scores) {
        int max = scores[0];

        for (int num : scores) {
            if (num > max) {
                max = num;
            }
        }

        return max;
    }

    // Returns the minimum grade
    public static int minGrade(int[] scores) {

        int min = scores[0];

        for (int num : scores) {
            if (num < min) {
                min = num;
            }
        }

        return min;
    }

    // Returns the sum of all the scores
    public static int sumScores(int[] scores) {
        int sumScores = 0;

        for (int score : scores) {
            sumScores += score;
        }

        return sumScores;
    }

    // Returns the average grade
    public static double averageGrade(int[] scores) {

        int sumOfScores = sumScores(scores);

        return (double) sumOfScores / scores.length;
    }

    // prints the graph
    public static void printGraph(int[] stats) {
        // maximum frequency
        int maxStat = maxGrade(stats);

        // Print the graph from top to bottom
        for (int i = maxStat; i > 0; i--) {
            System.out.printf("%2d  > ", i); // Print the current count number
            for (int stat : stats) {
                // Print '#####' for each column where the count is greater than or equal to the current level
                if (stat >= i) {
                    System.out.printf("%-10s", "    #####");

                } else {
                    // Otherwise print spaces to keep the columns aligned
                    System.out.printf("%-10s", "     ");
                }
            }
            System.out.println(); // Move to the next row
        }

        // Print the base of the graph (the grade intervals)
        System.out.println("     +-----------+---------+---------+---------+---------+");
        System.out.println("     I    0-20   I   21-40 I  41-60  I  61-80  I  81-100 I");
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}