package battleship;

import java.util.*;

public class Board {

    final static int ACTIVE_SHIP = 1;
    final static int SHIP_CLOSE_BY = 2;
    final static char ACTIVE_SHIP_MARK = 'O';
    final static char FOG_MARK = '~';
    final static int n = 10;
    final static char A = 'A';
    boolean isGameWon = false;
    char[][] table = new char[n][n];
    Map<String, ArrayList<String>> shipCoordinates = new HashMap<>();
    Map<String, Boolean> shipSunk = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public Board() {
        HashMap<Integer, Integer> boardSettings = new HashMap<>();
        createBoard();
        printBoard();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        addShip(scanner, 5, boardSettings, "Aircraft Carrier");
        printBoard();

        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        addShip(scanner, 4, boardSettings, "Battleship");
        printBoard();

        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        addShip(scanner, 3, boardSettings, "Submarine");
        printBoard();

        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        addShip(scanner, 3, boardSettings, "Cruiser");
        printBoard();

        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        addShip(scanner, 2, boardSettings, "Destroyer");
        printBoard();
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public void makeShot() {
        String result = makeMove(scanner);
        if (checkShipsSunk()) {
            if (checkAllShipsSunk()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                isGameWon = true;
            } else {
                System.out.println("You sank a ship! Specify a new target:");
            }
        } else {
            System.out.printf("%s Try again:\n", result);
        }
    }

    private boolean checkAllShipsSunk() {
        for (boolean val : shipSunk.values()) {
            if (!val) {
                return false;
            }
        }

        return true;
    }

    private boolean checkShipsSunk() {
        loop:
        for (Map.Entry<String, ArrayList<String>> entry : shipCoordinates.entrySet()) {
            if (!shipSunk.get(entry.getKey())) {
                for (String cord : entry.getValue()) {
                    String[] cords = cord.split(" ");

                    int x = Integer.parseInt(cords[0]);
                    int y = Integer.parseInt(cords[1]);

                    if (table[x][y] == 'O') {
                        continue loop;
                    }
                }
                shipSunk.put(entry.getKey(), true);
                return true;
            }
        }
        return false;
    }

    private String makeMove(Scanner scanner) {
        int x;
        int y;

        while (true) {
            String[] cords = scanner.nextLine().split(" ");

            x = (int) (cords[0].charAt(1) - '1');
            y = (int) (cords[0].charAt(0) - 'A');

            if (cords[0].length() == 3) {
                x = 9;
                if (cords[0].charAt(2) != '0') {
                    System.out.println("Error! Wrong coordinates! Try again:");
                    continue;
                }
            }

            if (x > 9 || x < 0 || y > 9 || y < 0) {
                System.out.println("Error! Wrong coordinates! Try again:");
                continue;
            }

            if (table[y][x] == 'M') {
                System.out.println("Error! You already hit there! Try again:");
                continue;
            } else if (table[y][x] == 'X') {
                printBoardWithFog();
                return "You hit a ship!";
            } else {
                if (table[y][x] == '~') {
                    table[y][x] = 'M';
                    printBoardWithFog();
                    return "You missed.";
                } else if (table[y][x] == 'O') {
                    table[y][x] = 'X';
                    printBoardWithFog();
                    return "You hit a ship!";
                }
            }
        }
    }

    private void addShip(Scanner scanner, int length, HashMap<Integer, Integer> boardSettings, String name) {
        int x1;
        int y1;
        int x2;
        int y2;
        boolean vertical = false;

        loop:
        while (true) {
            String[] cords = scanner.nextLine().split(" ");
            x1 = (int) (cords[0].charAt(1) - '1');
            y1 = (int) (cords[0].charAt(0) - 'A');

            x2 = (int) (cords[1].charAt(1) - '1');
            y2 = (int) (cords[1].charAt(0) - 'A');

            if(cords[0].length() == 3) {
                x1  = 9;
            }
            if(cords[1].length() == 3) {
                x2  = 9;
            }

            if(x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            if(y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            //System.out.println("x1:" + x1 + " x2:" + x2);

            if (x2 > 9 || x2 < 0 || x1 < 0 || y2 > 9 || y2 < 0 || y1 < 0) {
                System.out.println("Error! Wrong coordinates! Try again:");
                continue;
            }

            if(!(x2-x1 == 0 || y2-y1 == 0)) {
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }

            if(!(x2 - x1 == length - 1 || y2 - y1 == length - 1)) {
                System.out.printf("Error! Wrong length of the %s! Try again:\n", name);
                continue;
            }


            if(y1 != y2) {
                vertical = true;
            }

            boolean correctPlacing = true;

            for(int i = 0; i < length; i++) {
                int key;
                int y;
                int x;
                if(vertical) {
                    y = y1+i;
                    x = x1;
                }
                else {
                    y = y1;
                    x = x1+i;
                }
                key = getKey(x, y);
                if(boardSettings.get(key) != null) {
                    correctPlacing = false;
                    break;
                }
            }
            if(!correctPlacing) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                continue;
            }
            else {
                ArrayList<String> shipCords = new ArrayList<>();
                for(int i = 0; i < length; i++) {
                    int key;
                    int y;
                    int x;
                    if(vertical) {
                        y = y1 + i;
                        x = x1;
                    }
                    else {
                        y = y1;
                        x = x1 + i;
                    }
                    key = getKey(x, y);
                    boardSettings.put(key, ACTIVE_SHIP);
                    for(int j = -1; j <= 1; j++) {
                        for(int k = -1; k <= 1; k++) {
                            int yCoord = y + j;
                            int xCoord = x + k;
                            if(withinBorders(xCoord, yCoord)) {
                                int hash = getKey(xCoord, yCoord);
                                boardSettings.put(hash, SHIP_CLOSE_BY);
                            }

                        }
                    }

                    table[y][x] = ACTIVE_SHIP_MARK;
                    shipCords.add(y + " " + x);
                }
                shipSunk.put(name, false);
                shipCoordinates.put(name, shipCords);
            }
            break;
        }
    }

    public void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for(int i = 0; i < n; i++) {
            System.out.print((char) (A + i));
            for(int j = 0; j < n; j++) {
                System.out.print(" " + table[i][j]);
            }
            System.out.println();
        }
    }

    public void printBoardWithFog() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for(int i = 0; i < n; i++) {
            System.out.print((char) (A + i));
            for(int j = 0; j < n; j++) {
                if (table[i][j] == 'O') {
                    System.out.print(" ~");
                } else {
                    System.out.print(" " + table[i][j]);
                }
            }

            System.out.println();
        }
    }

    private void createBoard() {
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                table[i][j] = FOG_MARK;
            }
        }
    }

    private boolean withinBorders(int x, int y) {
        if(x >= 0 && x < n && y >= 0 && y < n)
            return true;

        return false;
    }

    private int getKey(int x, int y) {
        return y + 10 * x;
    }
}
