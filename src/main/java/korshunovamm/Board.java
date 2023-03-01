package korshunovamm;

import korshunovamm.Exceptions.BusyCellException;
import korshunovamm.Exceptions.GeneralErroeException;
import korshunovamm.Exceptions.InvalidMoveException;
import korshunovamm.Exceptions.NoCheckerOnDiagBetween;
import korshunovamm.Exceptions.WhiteCellException;


import java.util.ArrayList;
import java.util.Collections;


public final class Board {
    private static final int BOARD_DIMENSION = Constants.BOARD_DIMENSION;
    private static final int COLOR_WHITE = Constants.COLOR_WHITE;
    private static final int COLOR_BLACK = Constants.COLOR_BLACK;
    private ArrayList<Checker> checkers = new ArrayList<>();
    private final int[][] boardOfCheckersColors = new int[BOARD_DIMENSION][BOARD_DIMENSION];

    public int getBoardOfCheckersColorsOfPos(int i, int j) {
        return boardOfCheckersColors[i][j];
    }

    /**
     * Убирает шашки с доски и обнуляет массив checkers
     */
    public void boardClean() {
        checkers = new ArrayList<>();
        for (int i = 0; i < BOARD_DIMENSION; ++i) {
            for (int j = 0; j < BOARD_DIMENSION; ++j) {
                boardOfCheckersColors[i][j] = 0;
            }
        }
    }

    /**
     * @return объект шашки, стоящей на позиции pos
     * @param pos - позиция
     */
    public Checker getCheckerFromPosition(Position pos) {
        for (Checker checker : checkers) {
            if (checker.getPosition().equals(pos)) {
                return checker;
            }
        }
        throw new GeneralErroeException();
    }

    /**
     * Возвращает позицию шашки, стоящей на диагонали между шашкой fromChecker и  позицией на доске to
     * Используется, когда нужно найти какую шашку нужно побить при ходе
     * @param fromChecker - шашка, которая делает ход(бить)
     * @param to - позиция, на которую шашка должна походить
     * @return - позиция шашки на диагонали
     */
    public Position getPosOnDiagBetween(Checker fromChecker, Position to) {
        if (!fromChecker.isQueen()) {
            int x = fromChecker.getX() - (fromChecker.getX() - to.getX()) / 2;
            int y = fromChecker.getY() - (fromChecker.getY() - to.getY()) / 2;
            return new Position(x, y);
        } else {
            int directionX = to.getX() - fromChecker.getX() > 0 ? 1 : -1;
            int directionY = to.getY() - fromChecker.getY() > 0 ? 1 : -1;
            int j = fromChecker.getY() + directionY;
            for (int i = fromChecker.getX() + directionX; i * directionX < to.getX() && j * directionY < to.getY();
                 i += directionX, j += directionY) {
                    if (boardOfCheckersColors[i][j] != fromChecker.getColor() && boardOfCheckersColors[i][j] != 0) {
                        return new Position(i, j);
                }
            }
        }
        throw new NoCheckerOnDiagBetween();
    }

    private boolean isInQeenPosition(Checker checker) {
        return checker.getColor() == COLOR_BLACK && checker.getPosition().getX() == 0
                || checker.getColor() == COLOR_WHITE && checker.getPosition().getX() == 7;
    }

    /**
     * Добавляет шашку на доску и заполняет на доске ячейку, на которой будет шашка, цветом этой шашки,
     * т.е. помечает ячейку на доске цветом шашки, которая будет стоять в этой ячейке
     * @param checker - шашка, которую добавляют на доску
     */
    public void addChecker(Checker checker) {
        checkers.add(checker);
        boardOfCheckersColors[checker.getX()][checker.getY()] = checker.getColor();
    }

    private void killChecker(Checker checker) {
        checker.setKilled(true);
        checkers.removeIf(Checker::isKilled);
    }

    private void boardCLeanFromChecker(Checker checker) {
        boardOfCheckersColors[checker.getX()][checker.getY()] = 0;
    }

    private void boardCLeanFromChecker(Position pos) {
        boardOfCheckersColors[pos.getX()][pos.getY()] = 0;
    }

    private void boardUpdateChecker(Position pos, int color) {
        boardOfCheckersColors[pos.getX()][pos.getY()] = color;
    }

    private void killCheckers() {
        checkers.removeIf(Checker::isKilled);
    }

    /**
     * Проверяет есть ли по соседству с шашкой, шашки противоположного цвета, если да, то можно ли их побить
     * @param x - x координата шашки
     * @param y - y координата шашки
     * @param directionX - направление по оси X, по которому сранивать соседа (+1 или -1)
     * @param directionY - направление по оси Y, по которому сранивать соседа (+1 или -1)
     * @param color - цвет шашки
     * @return bool - есть ли возможность побить соседа
     */
    private boolean checkNeighbors(int x, int y, int directionX, int directionY, int color) {
        int newX = x + directionX;
        int newY = y + directionY;
        if (newX < BOARD_DIMENSION && newY < BOARD_DIMENSION && newX >= 0 && newY >= 0) {
            boolean isNearEnemy = boardOfCheckersColors[newX][newY] != color
                    && boardOfCheckersColors[newX][newY] != 0;
            if (isNearEnemy && newX + directionX < BOARD_DIMENSION && newY + directionY < BOARD_DIMENSION
                    && newX + directionX >= 0 && newY + directionY >= 0) {
                return boardOfCheckersColors[newX + directionX][newY + directionY] == 0;
            }
        }
        return false;
    }

    private void checkForNeedToAttack(Checker checker) {
        int x = checker.getX();
        int y = checker.getY();
        int color = checker.getColor();
        if (!checker.isQueen()) {
            if (checkNeighbors(x, y, 1, 1, color) || checkNeighbors(x, y, -1, 1, color)
                    || checkNeighbors(x, y, 1, -1, color) || checkNeighbors(x, y, -1, -1, color)) {
                throw new InvalidMoveException();
            }
        }
    }

    private void checkForInvalidMove(Checker from, Position to) {
        if (!from.isQueen() && Math.abs(from.getX() - to.getX()) != 1 && Math.abs(from.getY() - to.getY()) != 1) {
            throw new InvalidMoveException();
        } else if (from.isQueen() && Math.abs(from.getX() - to.getX()) != Math.abs(from.getY() - to.getY())) {
            throw new InvalidMoveException();
        }
    }

    private void checkForBusyCell(Position pos) {
        for (Checker checker : checkers) {
            if (checker.getPosition().equals(pos)) {
                throw new BusyCellException();
            }
        }
    }

    private void checkForWhiteCell(Position pos) {
        if ((pos.getX() + pos.getY()) % 2 != 0) {
            throw new WhiteCellException();
        }
    }

    /**
     * Вызывает для каждого битья шашки функцию обработки одного хода удара
     * @param attackMoves - массив хода партии, когда шашка должна бить
     */
    public void makeAttack(ArrayList<Position> attackMoves) {
        for (int i = 1; i < attackMoves.size(); i += 2) {
            makeOneStepAttack(attackMoves.get(i - 1), attackMoves.get(i));
        }

        Checker lastChecker = getCheckerFromPosition(attackMoves.get(attackMoves.size() - 1));
        checkForNeedToAttack(lastChecker);
    }

    /**
     * Делает один ход удара шашки, обрабатывает всевозможные исключения и не правильные ходы,
     * убивая врага, освобождает от него ячейку на доске, и удаляет убитого из checkers
     * @param from - позиция, с которой нужно бить
     * @param to - позиция, на которую нужно перейти после удара
     */
    public void makeOneStepAttack(Position from, Position to) {
        checkForBusyCell(to);
        checkForWhiteCell(to);

        Checker fromChecker = getCheckerFromPosition(from);

        Position enemyPos = getPosOnDiagBetween(fromChecker, to);
        Checker enemy = getCheckerFromPosition(enemyPos);

        killChecker(enemy);
        boardCLeanFromChecker(enemy);
        boardUpdateChecker(to, fromChecker.getColor());
        boardCLeanFromChecker(fromChecker);

        fromChecker.setPosition(to);

        if (isInQeenPosition(fromChecker)) {
            fromChecker.setQueen(true);
        }
    }

    /**
     * Делает тривиальный ход на доске(без битья) с проверкой на исключения и правильность хода
     * @param moves - массив из двух элементов: позиция от куда пойти - куда пойти
     */
    public void makeMove(ArrayList<Position> moves) {
        Position from = moves.get(0);
        Position to = moves.get(1);
        Checker fromChecker = getCheckerFromPosition(from);

        int fromColor = fromChecker.getColor();
        for (Checker checker: checkers) {
            if (checker.getColor() == fromColor) {
                checkForNeedToAttack(checker);
            }
        }

        checkForBusyCell(to);
        checkForWhiteCell(to);

        checkForInvalidMove(fromChecker, to);

        boardCLeanFromChecker(from);
        fromChecker.setPosition(to);
        boardUpdateChecker(to, fromChecker.getColor());

        if (isInQeenPosition(fromChecker)) {
            fromChecker.setQueen(true);
        }
    }


    public ArrayList<String> getWhiteCheckers() {
        return getCheckers(COLOR_WHITE);
    }

    public ArrayList<String> getBlackCheckers() {
        return getCheckers(COLOR_BLACK);
    }

    public void printCheckers() {
        System.out.println("WHITE");
        for (Checker checker : checkers) {
            if (checker.getColor() == COLOR_WHITE) {
                System.out.println(checker.getX() + " " + checker.getY() + " = " + checker.getStringCell());
            }
        }
        System.out.println("BLACK");
        for (Checker checker : checkers) {
            if (checker.getColor() == COLOR_BLACK) {
                System.out.println(checker.getX() + " " + checker.getY() + " = " + checker.getStringCell());
            }
        }
    }

    private ArrayList<String> getCheckers(int color) {
        ArrayList<String> res = new ArrayList<>();
        for (Checker checker : checkers) {
            if (checker.getColor() == color) {
                res.add(checker.getStringCell());
            }
        }
        Collections.sort(res);
        return res;
    }
}
