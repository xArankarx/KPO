/*
 * "Реверси"
 * Мыскин Николай, БПИ212
 * Board.java
 */

import java.util.*;

/**
 * Класс, отвечающий за функционал игрового поля.
 */
public class Board {
    public static final char WHITE = 'W';
    public static final char BLACK = 'B';
    public static final char EMPTY = '.';
    private static final int SIZE = 8;

    public Board previousBoard = null;
    public char currentPlayer = BLACK;
    public char opponent = WHITE;
    private final char[][] board = new char[SIZE][SIZE];

    Board() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
    }

    Board(char[][] board, char currentPlayer) {
        for (int i = 0; i < SIZE; i++) {
            this.board[i] = board[i].clone();
        }
        this.currentPlayer = currentPlayer;
    }

    /**
     * Метод, возвращающий количество белых фишек на игровом поле.
     *
     * @return количество белых фишек на игровом поле.
     */
    public int whiteCount() {
        return count(WHITE);
    }

    /**
     * Метод, возвращающий количество черных фишек на игровом поле.
     *
     * @return количество черных фишек на игровом поле.
     */
    public int blackCount() {
        return count(BLACK);
    }

    /**
     * Метод, возвращающий количество пустых клеток на игровом поле.
     *
     * @return количество пустых клеток на игровом поле.
     */
    public int emptyCount() {
        return count(EMPTY);
    }

    /**
     * Метод, отвечающий за подсчет количества клеток (фишек) определенного типа на поле.
     *
     * @param type тип клеток (фишек), количество которых нужно подсчитать.
     * @return количество клеток (фишек) определенного типа на поле.
     */
    private int count(char type) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == type) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Метод, отвечающий за определение, имеются ли на поле свободные клетки.
     */
    public boolean isNotFull() {
        return emptyCount() != 0;
    }

    /**
     * Метод, отвечающий за смену текущего игрока.
     */
    public void changePlayer() {
        if (currentPlayer == WHITE) {
            currentPlayer = BLACK;
            opponent = WHITE;
        } else {
            currentPlayer = WHITE;
            opponent = BLACK;
        }
    }

    /**
     * Метод, отвечающий за проделывание хода игроком.
     *
     * @param x номер строки, в которую игрок хочет поставить фишку.
     * @param y номер столбца, в который игрок хочет поставить фишку.
     */
    public void makeMove(int x, int y) {
        if (!isMoveValid(x, y)) {
            return;
        }
        previousBoard = new Board(board, currentPlayer);
        board[x][y] = currentPlayer;
        changeClosed(x, y);
        changePlayer();
    }

    /**
     * Метод, отвечающий за проделывание хода компьютером при легком режиме игры.
     */
    public double compMakeMoveEasy() {
        List<int[]> validMoves = findValidMoves();
        if (validMoves.isEmpty()) {
            return 0;
        }
        int[] bestMove = validMoves.get(0);
        double bestMoveValue = 0;
        for (int[] move : validMoves) {
            double moveValue;
            moveValue = evaluateEasy(move[0], move[1]);
            if (moveValue > bestMoveValue) {
                bestMove = move;
                bestMoveValue = moveValue;
            }
        }
        board[bestMove[0]][bestMove[1]] = currentPlayer;
        changeClosed(bestMove[0], bestMove[1]);
        changePlayer();
        return bestMoveValue;
    }

    /**
     * Метод, отвечающий за проделывание хода компьютером при продвинутом режиме игры.
     */
    public double compMakeMoveAdvanced() {
        List<int[]> validMoves = findValidMoves();
        if (validMoves.isEmpty()) {
            return 0;
        }
        int[] bestMove = validMoves.get(0);
        double bestMoveValue = 0;
        for (int[] move : validMoves) {
            double moveValue;
            moveValue = evaluateAdvanced(move[0], move[1]);
            if (moveValue > bestMoveValue) {
                bestMove = move;
                bestMoveValue = moveValue;
            }
        }
        board[bestMove[0]][bestMove[1]] = currentPlayer;
        changeClosed(bestMove[0], bestMove[1]);
        changePlayer();
        return bestMoveValue;
    }

    /**
     * Метод, отвечающий за нахождение всех возможных (валидных) ходов.
     *
     * @return список возможных ходов.
     */
    public List<int[]> findValidMoves() {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != EMPTY) {
                    continue;
                }
                if (isMoveValid(i, j)) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }
        return validMoves;
    }

    /**
     * Метод, отвечающий за проверку, является ли ход валидным.
     *
     * @param x номер строки, в которую игрок (компьютер) хочет поставить фишку.
     * @param y номер столбца, в который игрок (компьютер) хочет поставить фишку.
     * @return true, если ход валидный, и false, если ход невалидный.
     */
    public boolean isMoveValid(int x, int y) {
        boolean result = false;
        int temp = 0;
        for (int i = x - 1; i > -1; --i) {
            if (board[i][y] == EMPTY) {
                break;
            }
            if (board[i][y] == currentPlayer && temp > 0) {
                return true;
            } else if (board[i][y] == currentPlayer) {
                break;
            }
            if (board[i][y] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = x + 1; i < 8; ++i) {
            if (board[i][y] == EMPTY) {
                break;
            }
            if (board[i][y] == currentPlayer && temp > 0) {
                return true;
            } else if (board[i][y] == currentPlayer) {
                break;
            }
            if (board[i][y] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = y - 1; i > -1; --i) {
            if (board[x][i] == EMPTY) {
                break;
            }
            if (board[x][i] == currentPlayer && temp > 0) {
                return true;
            } else if (board[x][i] == currentPlayer) {
                break;
            }
            if (board[x][i] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = y + 1; i < 8; ++i) {
            if (board[x][i] == EMPTY) {
                break;
            }
            if (board[x][i] == currentPlayer && temp > 0) {
                return true;
            } else if (board[x][i] == currentPlayer) {
                break;
            }
            if (board[x][i] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = x - 1, j = y - 1; i > -1 && j > -1; --i, --j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer && temp > 0) {
                return true;
            } else if (board[i][j] == currentPlayer) {
                break;
            }
            if (board[i][j] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; ++i, ++j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer && temp > 0) {
                return true;
            } else if (board[i][j] == currentPlayer) {
                break;
            }
            if (board[i][j] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = x - 1, j = y + 1; i > -1 && j < 8; --i, ++j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer && temp > 0) {
                return true;
            } else if (board[i][j] == currentPlayer) {
                break;
            }
            if (board[i][j] == opponent) {
                ++temp;
            }
        }
        temp = 0;
        for (int i = x + 1, j = y - 1; i < 8 && j > -1; ++i, --j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer && temp > 0) {
                return true;
            } else if (board[i][j] == currentPlayer) {
                break;
            }
            if (board[i][j] == opponent) {
                ++temp;
            }
        }
        return result;
    }

    /**
     * Метод, отвечающий за оценку хода компьютера при легком режиме игры.
     *
     * @param x номер строки, в которую компьютер хочет поставить фишку.
     * @param y номер столбца, в который компьютер хочет поставить фишку.
     * @return оценка хода компьютера.
     */
    private double evaluateEasy(int x, int y) {
        double ss = 0;
        if (x == 0 || x == 7) {
            if (y == 0 || y == 7) {
                ss = 0.8;
            } else {
                ss = 0.4;
            }
        } else if (y == 0 || y == 7) {
            ss = 0.4;
        }
        return calculateClosed(x, y) + ss;
    }

    /**
     * Метод, отвечающий за оценку хода компьютера при продвинутом режиме игры.
     *
     * @param x номер строки, в которую компьютер хочет поставить фишку.
     * @param y номер столбца, в который компьютер хочет поставить фишку.
     * @return оценка хода компьютера.
     */
    private double evaluateAdvanced(int x, int y) {
        var tempBoard = new Board(board, currentPlayer);
        return evaluateEasy(x, y) - tempBoard.compMakeMoveEasy();
    }

    /**
     * Метод, отвечающий за расчет суммы ценностей всех фишек, которые будут замкнуты при ходе компьютера.
     *
     * @param x номер строки, в которую компьютер хочет поставить фишку.
     * @param y номер столбца, в который компьютер хочет поставить фишку.
     * @return сумма ценностей всех фишек, которые будут замкнуты при ходе компьютера.
     */
    private int calculateClosed(int x, int y) {
        int result = 0;
        int temp = 0;
        for (int i = x - 1; i > -1; --i) {
            if (board[i][y] == EMPTY) {
                break;
            }
            if (board[i][y] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[i][y] == opponent) {
                if (i == 0 || y == 0 || i == 7 || y == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        temp = 0;
        for (int i = x + 1; i < 8; ++i) {
            if (board[i][y] == EMPTY) {
                break;
            }
            if (board[i][y] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[i][y] == opponent) {
                if (i == 0 || y == 0 || i == 7 || y == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        temp = 0;
        for (int i = y - 1; i > -1; --i) {
            if (board[x][i] == EMPTY) {
                break;
            }
            if (board[x][i] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[x][i] == opponent) {
                if (x == 0 || i == 0 || x == 7 || i == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        temp = 0;
        for (int i = y + 1; i < 8; ++i) {
            if (board[x][i] == EMPTY) {
                break;
            }
            if (board[x][i] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[x][i] == opponent) {
                if (x == 0 || i == 0 || x == 7 || i == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        for (int i = x - 1, j = y - 1; i > -1 && j > -1; --i, --j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[i][j] == opponent) {
                if (i == 0 || j == 0 || i == 7 || j == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        temp = 0;
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; ++i, ++j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[i][j] == opponent) {
                if (i == 0 || j == 0 || i == 7 || j == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        temp = 0;
        for (int i = x - 1, j = y + 1; i > -1 && j < 8; --i, ++j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[i][j] == opponent) {
                if (i == 0 || j == 0 || i == 7 || j == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        temp = 0;
        for (int i = x + 1, j = y - 1; i < 8 && j > -1; ++i, --j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                result += temp;
                break;
            }
            if (board[i][j] == opponent) {
                if (i == 0 || j == 0 || i == 7 || j == 7) {
                    temp += 2;
                } else {
                    temp += 1;
                }
            }
        }
        return result;
    }

    /**
     * Метод, отвечающий за переворачивание фишек, которые будут замкнуты при ходе игрока (компьютера).
     *
     * @param x номер строки, в которую игрок (компьютер) хочет поставить фишку.
     * @param y номер столбца, в который игрок (компьютер) хочет поставить фишку.
     */
    private void changeClosed(int x, int y) {
        List<int[]> toChange = new ArrayList<>();
        for (int i = x - 1; i > -1; --i) {
            if (board[i][y] == EMPTY) {
                break;
            }
            if (board[i][y] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{i, y});
        }
        toChange.clear();
        for (int i = x + 1; i < 8; ++i) {
            if (board[i][y] == EMPTY) {
                break;
            }
            if (board[i][y] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{i, y});
        }
        toChange.clear();
        for (int i = y - 1; i > -1; --i) {
            if (board[x][i] == EMPTY) {
                break;
            }
            if (board[x][i] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{x, i});
        }
        toChange.clear();
        for (int i = y + 1; i < 8; ++i) {
            if (board[x][i] == EMPTY) {
                break;
            }
            if (board[x][i] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{x, i});
        }
        toChange.clear();
        for (int i = x - 1, j = y - 1; i > -1 && j > -1; --i, --j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{i, j});
        }
        toChange.clear();
        for (int i = x + 1, j = y + 1; i < 8 && j < 8; ++i, ++j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{i, j});
        }
        toChange.clear();
        for (int i = x - 1, j = y + 1; i > -1 && j < 8; --i, ++j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{i, j});
        }
        toChange.clear();
        for (int i = x + 1, j = y - 1; i < 8 && j > -1; ++i, --j) {
            if (board[i][j] == EMPTY) {
                break;
            }
            if (board[i][j] == currentPlayer) {
                for (int[] cell : toChange) {
                    board[cell[0]][cell[1]] = currentPlayer;
                }
                break;
            }
            toChange.add(new int[]{i, j});
        }
    }

    /**
     * Метод, отвечающий за вывод на экран текущего состояния игрового поля.
     */
    public void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8");
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Метод, отвечающий за вывод на экран текущего состояния игрового поля с подсветкой возможных ходов.
     *
     * @param cells список клеток, в которые можно поставить фишку.
     */
    public void printBoard(List<int[]> cells) {
        System.out.println("  1 2 3 4 5 6 7 8");
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                boolean flag = false;
                for (int[] cell : cells) {
                    if (cell[0] == i && cell[1] == j) {
                        System.out.print("* ");
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
