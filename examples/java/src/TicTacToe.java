import java.util.Scanner;

public class TicTacToe {
    public static final int N = 0;
    private static final int X = 1;
    public static final int O = 2;

    private static final int PLAYING = 0;
    private static final int DRAW = 1;
    private static final int X_WON = 2;
    private static final int O_WON = 3;

    public static final int SIZE = 3;
    private static final int CELLS = (SIZE * SIZE) + 1; // PHONE :D
    private static int[] board = new int[CELLS];
    private static int currentState;
    private static int currentPlayer;
    private static int currentCell;

    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        initGame();
        do {
            playerMove(currentPlayer);
            updateGame(currentPlayer, currentCell);
            printBoard();
            if (currentState == X_WON) {
                System.out.println(":D X WON ):");
            } else if (currentState == O_WON) {
                System.out.println(":( O WON C:");
            } else if (currentState == DRAW) {
                System.out.println(":( DRAW ):");
            }
            currentPlayer = (currentPlayer == X) ? O : X;
        } while (currentState == PLAYING);
    }

    private static void initGame() {
        for (int cell = 0; cell < CELLS; ++cell) {
            board[cell] = N;
        }
        currentState = PLAYING;
        currentPlayer = X;
    }

    private static void playerMove(int pl) {
        boolean validInput = false;
        do {
            if (pl == X) {
                System.out.print("Player 'X', move [1-9]: ");
            } else {
                System.out.print("Player 'O', move [1-9]: ");
            }
            int cell = in.nextInt();
            if (cell >=1 && cell < CELLS && board[cell] == N) {
                currentCell = cell;
                board[currentCell] = pl;
                validInput = true;
            } else {
                System.out.println("La celda '" + cell + "' no es valida");
            }
        } while (!validInput);
    }

    private static void updateGame(int pl, int currentCell) {
        if (hasWon(pl, currentCell)) {
            currentState = (pl == X) ? X_WON : O_WON;
        } else if (isDraw()) {
            currentState = DRAW;
        } else {
            currentState = PLAYING;
        }
    }

    private static boolean isDraw() {
        for (int cell = 1; cell < CELLS; ++cell) {
            if (board[cell] == N) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasWon(int pl, int c) {
        /**
         * 1 2 3
         * 4 5 6
         * 7 8 9
         **/
        switch(c) {
            case 1:
                return board[1] == pl && board[2] == pl && board[3] == pl
                    || board[1] == pl && board[4] == pl && board[7] == pl
                    || board[1] == pl && board[5] == pl && board[9] == pl
                ;
            case 2:
                return board[1] == pl && board[2] == pl && board[3] == pl
                    || board[2] == pl && board[5] == pl && board[8] == pl
                ;
            case 3:
                return board[1] == pl && board[2] == pl && board[3] == pl
                    || board[3] == pl && board[6] == pl && board[9] == pl
                    || board[3] == pl && board[5] == pl && board[7] == pl
                ;
            case 4:
                return board[4] == pl && board[5] == pl && board[6] == pl
                    || board[1] == pl && board[4] == pl && board[7] == pl
                ;
            case 5:
                return board[4] == pl && board[5] == pl && board[6] == pl
                    || board[2] == pl && board[5] == pl && board[8] == pl
                    || board[1] == pl && board[5] == pl && board[9] == pl
                    || board[3] == pl && board[5] == pl && board[7] == pl
                ;
            case 6:
                return board[4] == pl && board[5] == pl && board[6] == pl
                    || board[3] == pl && board[6] == pl && board[9] == pl
                ;
            case 7:
                return board[7] == pl && board[8] == pl && board[9] == pl
                    || board[1] == pl && board[4] == pl && board[7] == pl
                    || board[3] == pl && board[5] == pl && board[7] == pl
                ;
            case 8:
                return board[7] == pl && board[8] == pl && board[9] == pl
                    || board[2] == pl && board[5] == pl && board[8] == pl
                ;
            case 9:
                return board[7] == pl && board[8] == pl && board[9] == pl
                    || board[3] == pl && board[6] == pl && board[9] == pl
                    || board[1] == pl && board[5] == pl && board[9] == pl
                ;
            default:
                return false; // ERROR
        }
    }

    private static void printBoard() {
        for (int cell = 1; cell < CELLS; ++cell) {
            printCell(board[cell]);
            if (cell % SIZE == 0) {
                System.out.println("\n");
            }
        }
        System.out.println();
    }

    private static void printCell(int content) {
        switch (content) {
            case N: System.out.print(" _ "); break;
            case O: System.out.print(" O "); break;
            case X: System.out.print(" X "); break;
        }
    }
}
