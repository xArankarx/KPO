import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BubbleSort {
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        System.out.print("Please enter array length: ");
        var size = in.nextInt();
        var array = new Integer[size];
        for (var i = 0; i < size; ++i) {
            array[i] = in.nextInt();
        }
        bubbleSort(array);
        System.out.println(Stream.of(array).map(Objects::toString).collect(Collectors.joining(" ")));
    }

    private static <T extends Comparable<T>> void bubbleSort(T[] array) {
        for (var i = 1; i < array.length; ++i) {
            var sorted = true;
            for (var j = 0; j < array.length - i; ++ j) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    var temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    sorted = false;
                }
            }
            if (sorted)
                break;
        }
    }
}
