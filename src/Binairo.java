import java.util.ArrayList;
import java.util.Random;

public class Binairo {
    private final ArrayList<ArrayList<String>> board;
    private final ArrayList<ArrayList<ArrayList<String>>> domain;
    private final int n;

    public Binairo(ArrayList<ArrayList<String>> board,
            ArrayList<ArrayList<ArrayList<String>>> domain,
            int n) {
        this.board = board;
        this.domain = domain;
        this.n = n;
    }

    public void start() {
        long tStart = System.nanoTime();
        State state = new State(board, domain);

        drawLine();
        System.out.println("Initial Board: \n");
        state.printBoard();
        drawLine();

        int[] arr = getRandomStartNode(state);
        backtrack(arr[0], arr[1], state);

        drawLine();
        System.out.println("Backtrack Board: \n");
        state.printBoard();
        drawLine();

        long tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart) / 1000000000.000000000);
    }

    private boolean checkNumberOfCircles(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();
        // row
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(i).get(j);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks > n / 2 || numberOfWhites > n / 2) {
                return false;
            }
        }
        // column
        for (int i = 0; i < n; i++) {
            int numberOfWhites = 0;
            int numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(j).get(i);
                switch (a) {
                    case "w", "W" -> numberOfWhites++;
                    case "b", "B" -> numberOfBlacks++;
                }
            }
            if (numberOfBlacks > n / 2 || numberOfWhites > n / 2) {
                return false;
            }
        }
        return true;
    }

    private boolean checkAdjacency(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        // Horizontal
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 2; j++) {
                ArrayList<String> row = cBoard.get(i);
                String c1 = row.get(j).toUpperCase();
                String c2 = row.get(j + 1).toUpperCase();
                String c3 = row.get(j + 2).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }
        // column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n - 2; i++) {
                String c1 = cBoard.get(i).get(j).toUpperCase();
                String c2 = cBoard.get(i + 1).get(j).toUpperCase();
                String c3 = cBoard.get(i + 2).get(j).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkIfUnique(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        // check if two rows are duplicated
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = cBoard.get(i).get(k);
                    if (a.equals(cBoard.get(j).get(k)) && !a.equals("E")) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        // check if two columns are duplicated

        for (int j = 0; j < n - 1; j++) {
            for (int k = j + 1; k < n; k++) {
                int count = 0;
                for (int i = 0; i < n; i++) {
                    if (cBoard.get(i).get(j).equals(cBoard.get(i).get(k))) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allAssigned(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String s = cBoard.get(i).get(j);
                if (s.equals("E"))
                    return false;
            }
        }
        return true;
    }

    private boolean isFinished(State state) {
        return allAssigned(state) && checkNumberOfCircles(state) && checkAdjacency(state) && checkIfUnique(state);
    }

    private boolean isConsistent(State state) {
        return checkNumberOfCircles(state) && checkAdjacency(state) && checkIfUnique(state);
    }

    private void drawLine() {
        for (int i = 0; i < n * 2; i++) {
            System.out.print("\u23E4\u23E4");
        }
        System.out.println();
    }

    private int[] getRandomStartNode(State state) {
        Random generator = new Random();
        int n = state.getN();
        int i = generator.nextInt(n);
        int j = generator.nextInt(n);
        int[] arr = { i, j };
        while (!state.getBoard().get(i).get(j).equals("E")) {
            i = generator.nextInt(n);
            j = generator.nextInt(n);
            arr[0] = i;
            arr[1] = j;
        }
        return arr;
    }

    private void backtrack(int row, int col, State state) {

        if (isFinished(state)) {
            return;
        }

        if (state.getBoard().get(row).get(col).equals("E")) {

            state.setIndexBoard(row, col, "w");
            if (isConsistent(state)) {
                if (row + 1 < state.getN()) {
                    backtrack(row + 1, col, state);
                }
                if (row - 1 >= 0) {
                    backtrack(row - 1, col, state);
                }
                if (col + 1 < state.getN()) {
                    backtrack(row, col + 1, state);
                }
                if (col - 1 >= 0) {
                    backtrack(row, col - 1, state);
                }
            }

            if (isFinished(state)) {
                return;
            }

            state.setIndexBoard(row, col, "b");
            if (isConsistent(state)) {
                if (row + 1 < state.getN()) {
                    backtrack(row + 1, col, state);
                }
                if (row - 1 >= 0) {
                    backtrack(row - 1, col, state);
                }
                if (col + 1 < state.getN()) {
                    backtrack(row, col + 1, state);
                }
                if (col - 1 >= 0) {
                    backtrack(row, col - 1, state);
                }
            }

            if (isFinished(state)) {
                return;
            }

            state.setIndexBoard(row, col, "E");
        }
    }
}
