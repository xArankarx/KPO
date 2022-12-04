/*
 * "Реверси"
 * Мыскин Николай, БПИ212
 * Main.java
 */

import java.util.Arrays;

/**
 * Класс, отвечающий за запуск приложения.
 */
public class Main {
    private static int errorCount = 0;

    /**
     * Метод - точка входа в приложение.
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        while (true) {
            try {
                Game game = new Game();
                game.begin();
                break;
            } catch (Exception e) {
                if (errorCount > 10) {
                    System.out.println("Too many errors. Exiting...");
                    break;
                }
                System.out.println("An unexpected error occurred. Trying to restart the game...");
                errorCount++;
            }
        }
    }
}
