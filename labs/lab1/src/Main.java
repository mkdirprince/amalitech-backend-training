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

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}