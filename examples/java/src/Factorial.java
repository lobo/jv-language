public class Factorial {
    private static long factorial(long number) {
        if (number <= 1)
            return 1;
        else
            return number * factorial(number - 1);
    }

    public static void main(String[] args) {
        int n = 6;
        System.out.println("Factorial de " + n);
        for (int i = 1; i <= n; i++)
            System.out.println(i + ": " + factorial(i));
    }

}
