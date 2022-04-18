import java.util.ArrayList;

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
        State state = new State(board, domain);
        State temp;
        long tStart;
        long tEnd;
        int[] arr;

        drawLine();
        System.out.println("Initial Board: \n");
        state.printBoard();
        drawLine();

        // =======================================================================

        // backtrack

        // =======================================================================
        tStart = System.nanoTime();

        temp = state.copy();
        arr = getEmptyNode(temp);
        backtrack(arr[0], arr[1], temp);

        drawLine();
        System.out.println("Backtrack Board: \n");
        temp.printBoard();
        drawLine();

        tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart) / 1000000000.000000000);
        // =======================================================================

        // backtrack

        // =======================================================================

        // =======================================================================

        // backtrack with MRV_heuristic

        // =======================================================================

        tStart = System.nanoTime();

        temp = state.copy();

        arr = MRV_heuristic(temp);
        MRV_Backtrack(arr[0], arr[1], temp);

        drawLine();
        System.out.println("Backtrack with MRV Board: \n");
        temp.printBoard();
        drawLine();

        tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart) / 1000000000.000000000);

        // =======================================================================

        // backtrack with MRV_heuristic

        // =======================================================================
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
                    if (a.equalsIgnoreCase(cBoard.get(j).get(k)) && !a.equals("E")) {
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
                    String a = cBoard.get(i).get(j);
                    if (a.equalsIgnoreCase(cBoard.get(i).get(k)) && !a.equals("E")) {
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
        return allAssigned(state) && isConsistent(state);
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

    private int[] getEmptyNode(State state) {
        int i;
        int j;
        int[] arr = new int[2];
        for (i = n - 1; i >= 0; i--) {
            for (j = n - 1; j >= 0; j--) {
                if (state.getBoard().get(i).get(j).equals("E")) {
                    arr[0] = i;
                    arr[1] = j;
                    return arr;
                }
            }
        }
        return arr;
    }

    private void backtrack(int row, int col, State state) {

        if (isFinished(state)) {
            return;
        }

        for (String str : state.getDomain().get(row).get(col)) {

            if (isFinished(state)) {
                return;
            }

            state.setIndexBoard(row, col, str);

            if (isConsistent(state)) {
                int[] temp = getEmptyNode(state);
                backtrack(temp[0], temp[1], state);
            }

        }

        if (isFinished(state)) {
            return;
        }

        state.setIndexBoard(row, col, "E");
    }

    private void MRV_Backtrack(int row, int col, State state) {

        if (isFinished(state)) {
            return;
        }

        for (String str : state.getDomain().get(row).get(col)) {

            if (isFinished(state)) {
                return;
            }

            state.setIndexBoard(row, col, str);

            if (isConsistent(state)) {
                int[] temp = MRV_heuristic(state);
                MRV_Backtrack(temp[0], temp[1], state);
            }

        }

        if (isFinished(state)) {
            return;
        }
        if (state.getDomain().get(row).get(col).size() == 0) {
            state.getDomain().get(row).get(col).add("b");
            state.getDomain().get(row).get(col).add("w");
        } else if (state.getDomain().get(row).get(col).size() == 1) {

            if (state.getDomain().get(row).get(col).get(0).equals("b")) {
                state.getDomain().get(row).get(col).add("w");
            } else {
                state.getDomain().get(row).get(col).add("b");
            }
        }
        state.setIndexBoard(row, col, "E");
    }

    private int[] MRV_heuristic(State state) {
        State tempState = state.copy();
        int[] arr = new int[2];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (tempState.getBoard().get(i).get(j).equals("E")) {

                    tempState.setIndexBoard(i, j, "w");
                    if (!isConsistent(tempState)) {
                        state.removeIndexDomain(i, j, "w");
                        arr[0] = i;
                        arr[1] = j;
                        return arr;
                    }

                    tempState.setIndexBoard(i, j, "b");
                    if (!isConsistent(tempState)) {

                        state.removeIndexDomain(i, j, "b");
                        arr[0] = i;
                        arr[1] = j;
                        return arr;
                    }
                    tempState.setIndexBoard(i, j, "E");
                }
            }
        }

        arr = getEmptyNode(state);
        return arr;
    }
}
