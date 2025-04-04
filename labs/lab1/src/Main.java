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

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}