package korshunovamm.Exceptions;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException() {
        super("invalid move");
    }
}
