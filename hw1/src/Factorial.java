import java.util.Scanner;

public class Factorial {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        var value = in.nextInt();
        System.out.println(getFactorial(value));
    }

    private static long getFactorial(int value) {
        long factorial = 1;
        for (var i = 2; i <= value; ++i) {
            factorial *= i;
        }
        return factorial;
    }
}
