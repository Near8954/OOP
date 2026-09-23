package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

    @Test
    void testGameExecutionAndExit() {
        String simulatedInput = "0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Game game = new Game();
        assertDoesNotThrow(game::startGame);
    }
}