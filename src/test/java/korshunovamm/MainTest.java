package korshunovamm;

import korshunovamm.Exceptions.BusyCellException;
import korshunovamm.Exceptions.InvalidMoveException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {
    @BeforeEach
    void clean() {
        Main.boardClean();
        Main.cleanOutput();
    }

    @Test
    void simpleTest1() throws IOException {
        String path = "InputTests/input1.txt";
        Main.readData(path);
        Main.playGame();
        String answer = "[a1, a3, b2, c3, d4, e1, e3, e5, f2, g1, h4, a7, b6, b8, c5, c7, d8, e7, f6, g5, g7, h6]";

        Assertions.assertThat(Main.getOutput().toString()).isEqualTo(answer);
    }

    @Test
    void simpleTest2() throws IOException {
        String path = "InputTests/input2.txt";
        Main.readData(path);
        Main.playGame();
        String answer = "[A1, g7]";

        Assertions.assertThat(Main.getOutput().toString()).isEqualTo(answer);
    }

    @Test
    void simpleTest3() throws IOException {
        String path = "InputTests/input3.txt";
        Main.readData(path);
        Main.playGame();
        String answer = "[F8, D4, f4]";

        Assertions.assertThat(Main.getOutput().toString()).isEqualTo(answer);
    }

//    @Test
//    void invalidMoveTest() throws IOException {
//        String path = "InputTests/input4.txt";
//        Main.readData(path);
//
//        Exception exception = assertThrows(InvalidMoveException.class, Main::playGame);
//
//        String expectedMessage = "invalid move";
//        String actualMessage = exception.getMessage();
//
//        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
//    }
//
//    @Test
//    void busyCellTest() throws IOException {
//        String path = "InputTests/input5.txt";
//        Main.readData(path);
//
//        Exception exception = assertThrows(BusyCellException.class, Main::playGame);
//        String expectedMessage = "busy cell";
//        String actualMessage = exception.getMessage();
//        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
//    }

    @Test
    void simpleTest6() throws IOException {
        String path = "InputTests/input6.txt";
        Main.readData(path);
        Main.playGame();
        String answer = "[A3, b2, d4, f2, g7, h2, h4]";

        Assertions.assertThat(Main.getOutput().toString()).isEqualTo(answer);
    }

    @Test
    void simpleTest7() throws IOException {
        String path = "InputTests/input7.txt";
        Main.readData(path);
        Main.playGame();
        String answer = "[A1, B6, a3, a5, h4]";

        Assertions.assertThat(Main.getOutput().toString()).isEqualTo(answer);
    }
}



