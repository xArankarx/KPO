/*
 * "Реверси"
 * Мыскин Николай, БПИ212
 * Game.java
 */

import java.util.List;
import java.util.Scanner;

/**
 * Класс, отвечающий за управление процессом игры.
 */
class Game {
    private static final Scanner scanner = new Scanner(System.in);

    private enum GameMode {EASY, ADVANCED, PVP}

    private Board board;
    private GameMode gameMode = GameMode.EASY;
    private boolean isCancellingEnabled = false;
    private int bestRecord = 0;
    private String player1;
    private String player2 = "Computer";

    /**
     * Метод, отвечающий за начало игры - выбор режима игры, имен игроков, включение/отключение отмены ходов.
     */
    public void begin() {
        isCancellingEnabled = false;
        System.out.println("Welcome to the game \"Reversi\"!");
        System.out.println("Best score: " + bestRecord + "\n");
        System.out.println("Choose game mode:");
        System.out.println("\t1 - Easy");
        System.out.println("\t2 - Advanced");
        System.out.println("\t3 - Player vs Player");
        if (!scanner.hasNextInt()) {
            System.out.flush();
            System.out.println("Invalid game mode. Please, try again.\n");
            scanner.next();
            begin();
            return;
        }
        int mode = scanner.nextInt();
        switch (mode) {
            case 1:
                gameMode = GameMode.EASY;
                break;
            case 2:
                gameMode = GameMode.ADVANCED;
                break;
            case 3:
                gameMode = GameMode.PVP;
                break;
            default:
                System.out.flush();
                System.out.println("Invalid game mode. Please, try again.\n");
                scanner.nextLine();
                begin();
                return;
        }
        System.out.println("Do you want to enable move cancelling? (y/n)");
        var answer = scanner.next();
        if (answer.equals("y")) {
            isCancellingEnabled = true;
        }
        System.out.println("Enter your name:");
        player1 = scanner.next();
        if (gameMode == GameMode.PVP) {
            System.out.println("Enter second player name:");
            player2 = scanner.next();
        }
        start();
        System.out.println("Do you want to continue playing? (y/n)");
        answer = scanner.next();
        if (answer.equals("y")) {
            System.out.flush();
            begin();
        } else {
            System.out.println("Goodbye!");
        }
    }

    /**
     * Метод, отвечающий за инициализацию игрового поля и запуск игры.
     */
    private void start() {
        System.out.flush();
        board = new Board();
        if (gameMode == GameMode.PVP) {
            beginPVP();
        } else {
            beginPVC();
        }
        System.out.println("Game over.");
    }

    /**
     * Метод, отвечающий за процесс игры между двумя игроками.
     */
    private void beginPVP() {
        boolean isPlayer1MovePossible = true;
        boolean isPlayer2MovePossible = true;
        boolean player1Cancelled = false;
        boolean player2Cancelled = false;
        while ((isPlayer1MovePossible || isPlayer2MovePossible) && board.isNotFull()) {
            if (board.currentPlayer == Board.BLACK) {
                System.out.println(player1 + " (black) makes the move.");
                var move = getNextMove();
                if (move[0] == -1) {
                    isPlayer1MovePossible = false;
                    board.changePlayer();
                    continue;
                }
                isPlayer2MovePossible = true;
                board.makeMove(move[0], move[1]);
                board.printBoard();
                if (isCancellingEnabled && askForMoveCancel() && !player1Cancelled) {
                    board = board.previousBoard;
                    System.out.println("Move cancelled!\n");
                    player1Cancelled = true;
                    continue;
                }
                player1Cancelled = false;
            } else {
                System.out.println(player2 + " (white) makes the move.");
                var move = getNextMove();
                if (move[0] == -1) {
                    isPlayer2MovePossible = false;
                    board.changePlayer();
                    continue;
                }
                isPlayer1MovePossible = true;
                board.makeMove(move[0], move[1]);
                board.printBoard();
                if (isCancellingEnabled && askForMoveCancel() && !player2Cancelled) {
                    board = board.previousBoard;
                    System.out.println("Move cancelled!\n");
                    player2Cancelled = true;
                    continue;
                }
                player2Cancelled = false;
            }
        }
        displayWinner(false);
    }

    /**
     * Метод, отвечающий за процесс игры между игроком и компьютером.
     */
    private void beginPVC() {
        boolean isPlayer1MovePossible = true;
        boolean isComputerMovePossible = true;
        var player1Cancelled = false;
        while ((isPlayer1MovePossible || isComputerMovePossible) && board.isNotFull()) {
            if (board.currentPlayer == Board.BLACK) {
                System.out.println(player1 + " (black) makes the move.");
                var move = getNextMove();
                if (move[0] == -1) {
                    isPlayer1MovePossible = false;
                    board.changePlayer();
                    continue;
                }
                isComputerMovePossible = true;
                board.makeMove(move[0], move[1]);
                board.printBoard();
                if (isCancellingEnabled && askForMoveCancel() && !player1Cancelled) {
                    board = board.previousBoard;
                    System.out.println("Move cancelled!\n");
                    player1Cancelled = true;
                    continue;
                }
                player1Cancelled = false;
            } else {
                System.out.println(player2 + " (white) makes the move...");
                double moveValue;
                if (gameMode == GameMode.EASY) {
                    moveValue = board.compMakeMoveEasy();
                } else {
                    moveValue = board.compMakeMoveAdvanced();
                }
                if (moveValue == 0) {
                    isComputerMovePossible = false;
                    board.changePlayer();
                    continue;
                }
                isPlayer1MovePossible = true;
            }
        }
        displayWinner(true);

    }

    /**
     * Метод, отвечающий за определение победителя. Побеждает тот, у кого больше фишек на поле.
     *
     * @return имя победителя, либо null, если случилась ничья.
     */
    private String getWinner() {
        var blackCount = board.blackCount();
        var whiteCount = board.whiteCount();
        if (blackCount > whiteCount) {
            return player1;
        } else if (whiteCount > blackCount) {
            return player2;
        } else {
            return null;
        }
    }

    /**
     * Метод, отвечающий за вывод победителя и итогового счета.
     *
     * @param isComputerPlayer true, если играет компьютер, false, если играют два игрока.
     */
    private void displayWinner(boolean isComputerPlayer) {
        var winner = getWinner();
        var blackCount = board.blackCount();
        var whiteCount = board.whiteCount();
        if (winner != null) {
            System.out.println("Score - " + blackCount + ":" + whiteCount);
            System.out.println(winner + " is the winner!");
        } else {
            System.out.println("Draw!");
        }
        if (blackCount > bestRecord) {
            bestRecord = blackCount;
        }
        if (!isComputerPlayer) {
            if (whiteCount > bestRecord) {
                bestRecord = whiteCount;
            }
        }
    }

    /**
     * Метод, отвечающий за получение координат следующего хода.
     * Возможные ходы отображаются на доске и выводятся в консоль списом координат.
     *
     * @return массив из двух элементов, первый из которых - номер строки, второй - номер столбца.
     */
    private int[] getNextMove() {
        List<int[]> validMoves = board.findValidMoves();
        if (validMoves.isEmpty()) {
            System.out.println("No valid moves. Pass.");
            return new int[]{-1, -1};
        }
        board.printBoard(validMoves);
        System.out.println("Available moves:");
        for (int i = 0; i < validMoves.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + (validMoves.get(i)[0] + 1) + " " + (validMoves.get(i)[1] + 1));
        }
        System.out.println("Enter your move:");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid move number. Try again.");
            scanner.next();
            return getNextMove();
        }
        int moveNumber = scanner.nextInt();
        if (moveNumber < 1 || moveNumber > validMoves.size()) {
            System.out.println("Invalid move number. Try again.");
            scanner.next();
            return getNextMove();
        }
        return validMoves.get(moveNumber - 1);
    }

    /**
     * Метод, отвечающий за получение ответа пользователя на вопрос о том, хочет ли он отменить ход.
     *
     * @return true, если пользователь хочет отменить ход, false - иначе.
     */
    private boolean askForMoveCancel() {
        System.out.println("Do you want to cancel your move? (y/n)");
        String answer = scanner.next();
        return answer.equals("y");
    }
}
