public class FizzBuzz {
    public static void main(String[] args) {
        for (short number = 1; number <= 100; ++number) {
            System.out.println(getFizzBuzz(number));
        }
    }

    private static String getFizzBuzz(Short value) {
        if (value % 3 == 0 && value % 5 == 0)
            return "FizzBuzz";
        if (value % 3 == 0)
            return "Fizz";
        if (value % 5 == 0)
            return "Buzz";
        return value.toString();
    }
}