import java.util.Scanner;
import java.util.Vector;

public class Fibonacci {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        var N = in.nextInt();
        var fibonacciRow = getFibonacciRow(N);
        for (var fibonacciNumber : fibonacciRow) {
            System.out.println(fibonacciNumber);
        }
    }

    private static Vector<Integer> getFibonacciRow(int amount) {
        var row = new Vector<Integer>();
        for (var i = 1; i <= amount; ++i)
            row.add(getFibonacciNumber(i));
        return row;
    }

    private static int getFibonacciNumber(int value) {
        if (value == 1 || value == 2) {
            return 1;
        }
        return getFibonacciNumber(value - 1) + getFibonacciNumber(value - 2);
    }
}
