package korshunovamm.Exceptions;

public class BusyCellException extends RuntimeException {
    public BusyCellException() {
        super("busy cell");
    }
}
