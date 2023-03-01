package korshunovamm;

public final class Position {
    private final int x;
    private final int y;

    Position(int i, int j) {
        x = i;
        y = j;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Position pos) {
        return pos.x == x && pos.y == y;
    }

    public String getStringPosition(boolean isQueen) {
        String caseUpperLower = isQueen ? "A" : "a";
        String yStr = Character.toString(x + caseUpperLower.codePointAt(0));
        String xStr = String.valueOf(y + 1);
        return yStr + xStr;
    }
}
