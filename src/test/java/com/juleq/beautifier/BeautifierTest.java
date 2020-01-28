package com.juleq.beautifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit4.class)
class BeautifierTest {

    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();

    @BeforeEach
    public void before() {
        System.setOut(new PrintStream(this.consoleContent));
    }

    @AfterEach
    public void after() {
        System.setOut(this.originalStdOut);
        this.consoleContent = new ByteArrayOutputStream();
    }

    @Test
    void simplifierTest() {
        String eol = System.lineSeparator();
        String expected =
                "                  7   " + eol +
                "                ----- " + eol +
                "    1+2          8+9  " + eol +
                "------------ + -------" + eol +
                "        4        10   " + eol +
                " -3 * -----           " + eol +
                "       5-6            " + eol;

        String[] expression = {"(1+2)/(-3*(4/(5-6)))+(7/(8+9))/10"};
        Beautifier.main(expression);

        assertEquals(consoleContent.toString(), expected);
    }

    @Test
    void detectInvalidCharacters() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Beautifier.main(new String[]{"invalid"}));

        String expectedMessage = "Expression with forbidden characters has entered.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void detectIncorrectNumberOfParameters() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Beautifier.main(new String[]{"1+2", "3*4"}));

        String expectedMessage = "Incorrect parameter number has entered to the command line.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}