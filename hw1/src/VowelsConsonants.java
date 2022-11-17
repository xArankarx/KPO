import java.util.Scanner;

public class VowelsConsonants {
    private static final String vowelsList = "eyuioaаоуыэеёиюя";
    private static final String consonantsList = "qwrtpsdfghjklzxcvbnmбвгджзйклмнпрстфхцчшщ";

    public static void main(String[] args) {
        var in = new Scanner(System.in);
        var pair = countVowelsAndConsonants(in.next());
        System.out.format("Vowels: %d\n", pair.first);
        System.out.format("Consonants: %d\n", pair.second);
    }

    private static Pair<Integer, Integer> countVowelsAndConsonants(String string) {
        int vowels = 0, consonants = 0;
        for (var ch : string.toCharArray()) {
            if (vowelsList.contains("" + ch))
                ++vowels;
            else if (consonantsList.contains("" + ch))
                ++consonants;
        }
        return new Pair<Integer, Integer>(vowels, consonants);
    }

    private static final class Pair<T, U> {
        public T first;
        public U second;

        public Pair(T first, U second){
            this.first = first;
            this.second = second;
        }
    }
}

