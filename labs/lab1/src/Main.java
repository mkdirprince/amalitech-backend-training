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
    
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}