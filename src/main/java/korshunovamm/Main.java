package korshunovamm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public final class Main {
    private static final Board BOARD = new Board();
    private static List<String> turns;
    private static String[] white;
    private static String[] black;
    private static ArrayList<String> output = new ArrayList<>();


    private Main() {
    }

    public static ArrayList<String> getOutput() {
        return output;
    }
    public static void cleanOutput() {
        output = new ArrayList<>();
    }

    /**
     * Читает данные из файла и преобразовывает в массив белых шашек, черных шашек, ходов партии:
     * @param inputTXT - файл из которого читать
     */
    public static void readData(String inputTXT) throws IOException {
        Path path = Paths.get(inputTXT);
        List<String> inputData = Files.readAllLines(path, StandardCharsets.UTF_8);
        turns = inputData.subList(2, inputData.size());
        white = inputData.get(0).split(" ");
        black = inputData.get(1).split(" ");
    }


    /**
     * Для каждой шашки создает обЪект и инициализирует поля, расставляет шашки на доске
     * @param checkersToAdd - массив шашек
     * @param color - цвет добавляемых шашек
     */
    private static void putOnBoardCheckers(String[] checkersToAdd, int color) {
        for (String str : checkersToAdd) {
            boolean isQueen = str.matches("[A-Z]\\d");
            String checkerToAdd = str.toLowerCase();
            Position positionChecker = new Position(Integer.parseInt(checkerToAdd.substring(1)) - 1,
                    checkerToAdd.codePointAt(0) - "a".codePointAt(0));

            Checker checker = new Checker(positionChecker, color, isQueen);
            BOARD.addChecker(checker);
        }
    }

    /**
     * Преобразует входные строковые данные позиций и ходов шашек в индексы двумерного массива
     * @param moves - ходы партии в строковом представлении
     * @param movesList - ходы партии в представлении массива
     */
    private static void preproceedMove(String[] moves, ArrayList<Position> movesList) {
        for (int i = 1; i < moves.length; ++i) {
            String fromLower = moves[i - 1].toLowerCase();
            String toLower = moves[i].toLowerCase();

            Position from = new Position(Integer.parseInt(fromLower.substring(1)) - 1,
                    fromLower.codePointAt(0) - "a".codePointAt(0));
            Position to = new Position(Integer.parseInt(toLower.substring(1)) - 1,
                    toLower.codePointAt(0) - "a".codePointAt(0));

            movesList.add(from);
            movesList.add(to);
        }
    }

    /**
     * Парсит строки ходов партии и вызывает методы обрабатывающие перемещение и захват шашек
     */
    public static void playGame() {
        putOnBoardCheckers(white, Constants.COLOR_WHITE);
        putOnBoardCheckers(black, Constants.COLOR_BLACK);
        try {
            for (String s : turns) {
                String[] turn = s.split(" ");
                String whiteTurn = turn[0];
                String blackTurn = turn[1];

                if (whiteTurn.contains(":")) {
                    ArrayList<Position> attackMoves = new ArrayList<>();
                    String[] movesWhite = whiteTurn.split(":");
                    preproceedMove(movesWhite, attackMoves);
                    BOARD.makeAttack(attackMoves);
                } else {
                    ArrayList<Position> attackMoves = new ArrayList<>();
                    String[] movesWhite = whiteTurn.split("-");
                    preproceedMove(movesWhite, attackMoves);
                    BOARD.makeMove(attackMoves);
                }

                if (blackTurn.contains(":")) {
                    ArrayList<Position> attackMoves = new ArrayList<>();
                    String[] movesBlack = blackTurn.split(":");
                    preproceedMove(movesBlack, attackMoves);
                    BOARD.makeAttack(attackMoves);
                } else {
                    ArrayList<Position> attackMoves = new ArrayList<>();
                    String[] movesBlack = blackTurn.split("-");
                    preproceedMove(movesBlack, attackMoves);
                    BOARD.makeMove(attackMoves);
                }
            }

            ArrayList<String> whiteCheckers = BOARD.getWhiteCheckers();
            ArrayList<String> blackCheckers = BOARD.getBlackCheckers();
            output.addAll(whiteCheckers);
            output.addAll(blackCheckers);

            for (String s : whiteCheckers) {
                System.out.print(s + ' ');
            }
            System.out.println();
            for (String s : blackCheckers) {
                System.out.print(s + ' ');
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void boardClean() {
        BOARD.boardClean();
    }

    public static void main(String[] args) throws IOException {
        readData("input.txt");
        playGame();
    }
}
