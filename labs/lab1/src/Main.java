import java.util.Scanner;

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

    // Print the graph for the score distribution and  frequency
    public static void getGraph(int[] scores) {
        // Grade distribution
        int[] stats = new int [5]; // [0, 0, 0, 0]
        for (int score : scores) {
            if (score > 80) {
                stats[4]++;
            } else if (score >= 61) {
                stats[3]++;
            } else if (score >= 41) {
                stats[2]++;
            } else if (score >= 21) {
                stats[1]++;
            } else {
                stats[0]++;
            }
        }

        printGraph(stats);
    }

    public static void main(String[] args) {

        // scanner for getting inputs on the cli
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the total number students: ");
        int N = 0; // total number of students

        // checks of there is a next value for the input
        if (sc.hasNextInt()) {
            N = sc.nextInt();
        }  else {
            System.out.println("Invalid input. Please enter a valid integer");
            return;
        }

        // prompts user to enter the grades of the user
        System.out.println("Enter the grades of the students separated by a space");
        int[] scores = new int[N];
        for (int i = 0; i < N; i++) {
            if (sc.hasNextInt()) {
                scores[i] = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a valid integer");
                return;
            }
        }

        // computed statistics
        System.out.printf("The maximum grade is %d\n", maxGrade(scores));
        System.out.printf("The minimum grade is %d\n", minGrade(scores));
        System.out.printf("The average grade is %f\n", averageGrade(scores));

        System.out.println();

        //prints the graph
        System.out.println("Graph");
        getGraph(scores);

        // close the scanner
        sc.close();
    }
}