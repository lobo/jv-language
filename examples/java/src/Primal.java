public class Primal {

    private static long intSqrt(long number) {
        if (number <= 0L) {
            return 0L;
        }

        long sqrt = 1L;

        // Do the exponential search.
        while (4 * sqrt * sqrt <= number) {
            sqrt *= 2;
        }

        long left = sqrt;
        long right = 2 * sqrt;
        long middle = 0;

        // Do the binary search over the range that is guaranteed to contain
        // the integer square root.
        while (left < right) {
            middle = left + (right - left) / 2;

            if (middle * middle < number) {
                left = middle + 1;
            } else if (middle * middle > number) {
                right = middle - 1;
            } else {
                return middle;
            }
        }

        // Correct the binary search "noise". This iterates no more than 3
        // times.
        long ret = middle + 1;

        while (ret * ret > number) {
            --ret;
        }

        return ret;
    }

    private static boolean isPrime(long number) {
        if (number == 2) {
            return true;
        }
        if (number < 2 || number % 2 == 0) {
            return false;
        }
        for (int i = 3; i < intSqrt(number); i+=2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int n = 7;
        System.out.println("Es primo " + n + "?");
        System.out.println(isPrime(n) ? "SI" : "NO");
    }

}
