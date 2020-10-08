package battleship;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Player 1, place your ships on the game field");
        Board board1 = new Board();
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        System.out.println("Player 2, place your ships to the game field");
        Board board2 = new Board();
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        while (true) {
            boolean result1 = initiateGame(1, board1, board2);
            if (result1) {
                break;
            }
            boolean result2 = initiateGame(2, board2, board1);
            if (result2) {
                break;
            }
        }
    }

    private static boolean initiateGame(int number, Board board, Board enemyBoard) {
        enemyBoard.printBoardWithFog();
        System.out.println("---------------------");
        board.printBoard();
        System.out.printf("Player %d, it's your turn!\n", number);
        enemyBoard.makeShot();
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        return enemyBoard.isGameWon();
    }
}
