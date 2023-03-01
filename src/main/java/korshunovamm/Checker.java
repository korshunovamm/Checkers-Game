package korshunovamm;

public final class Checker {
    private final int color;
    private Position position;
    private boolean isQueen;
    private boolean isKilled = false;

    Checker(Position cell, int color, boolean isQueen) {
        this.position = cell;
        this.color = color;
        this.isQueen = isQueen;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos) {
        position = pos;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getColor() {
        return color;
    }

    public boolean isWhiteChecker() {
        return color == Constants.COLOR_WHITE;
    }

    public boolean isBlackChecker() {
        return color == Constants.COLOR_BLACK;
    }

    public String getStringCell() {
        String caseUpperLower = isQueen ? "A" : "a";
        String yStr = Character.toString(position.getY() + caseUpperLower.codePointAt(0));
        String xStr = String.valueOf(position.getX() + 1);
        return yStr + xStr;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void setKilled(boolean killed) {
        isKilled = killed;
    }
}
