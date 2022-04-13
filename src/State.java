import java.util.ArrayList;
import java.util.Arrays;

public class State {
    private static char blackCircle = '\u26AB';
    private static char whiteCircle = '\u26AA';
    private static char blackSquare = '\u2B1B';
    private static char whiteSquare = '\u2B1C';
    private static char line = '\u23E4';


    private ArrayList<ArrayList<String>> board;
    private ArrayList<ArrayList<ArrayList<String>>> domain;
    private int n;

    public State(ArrayList<ArrayList<String>> board,
                 ArrayList<ArrayList<ArrayList<String>>> domain) {

        this.board = board;
        this.domain = domain;
        this.n = board.size();
    }

    public ArrayList<ArrayList<String>> getBoard() {
        return board;
    }

    public void setIndexBoard(int x, int y, String value) {
        board.get(x).set(y, value);
    }

    public void removeIndexDomain(int x, int y, String value) {
        domain.get(x).get(y).remove(value);
    }

    public ArrayList<ArrayList<ArrayList<String>>> getDomain() {
        return domain;
    }

    public State copy() {
        ArrayList<ArrayList<String>> cb = copyBoard(board);
        ArrayList<ArrayList<ArrayList<String>>> cd = copyDomain(domain);
        return(new State(cb, cd));
    }

    private ArrayList<ArrayList<String>> copyBoard(ArrayList<ArrayList<String>> cBoard) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(new ArrayList<>(Arrays.asList(new String[n])));
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res.get(i).set(j, cBoard.get(i).get(j));
            }
        }

        return res;
    }
    private ArrayList<ArrayList<ArrayList<String>>> copyDomain(ArrayList<ArrayList<ArrayList<String>>> cDomain) {
        ArrayList<ArrayList<ArrayList<String>>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ArrayList<ArrayList<String>> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(new ArrayList<>(Arrays.asList("", "")));
            }
            res.add(row);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < cDomain.get(i).get(j).size(); k++) {
                    res.get(i).get(j).set(k, cDomain.get(i).get(j).get(k));
                }
            }
        }

        return res;
    }

    public void printBoard() {
        for (ArrayList<String> strings : this.getBoard()) {
            for (String s : strings) {
                switch (s) {
                    case "w":System.out.print(whiteCircle + "  "); break;
                    case "W":System.out.print(whiteSquare + "  "); break;
                    case "b":System.out.print(blackCircle + "  "); break;
                    case "B":System.out.print(blackSquare + "  "); break;
                    default: System.out.print(line + "" + line + "  "); break;
                }
            }
            System.out.println("\n");
        }
    }

    public void printDomain() {
        for (ArrayList<ArrayList<String>> strings : this.getDomain()) {
            for (ArrayList<String> s : strings) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }
}
