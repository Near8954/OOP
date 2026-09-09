package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import java.util.Arrays;

class MainTest {

    @Test
    void heapify() {
        int maxArraySize = 1001;
        for (int arraySize = 0; arraySize < maxArraySize; ++arraySize) {
            int testsCount = 100;
            int[] given = new int[arraySize];
            Random randomise = new Random(6769);
            for (int i = 0; i < testsCount; ++i) {
                for (int j = 0; j < arraySize; ++j) {
                    given[j] = randomise.nextInt();
                }
                Main.heapify(given);
                for (int k = 0; k < given.length / 2 - 1; ++k) {
                    assertTrue(given[k] >= given[k * 2 + 1] && given[k] >= given[k * 2 + 2]);
                }
            }
        }
    }

    @Test
    void heapSort() {
        int maxArraySize = 1001;
        for (int arraySize = 0; arraySize < maxArraySize; ++arraySize) {
            int testsCount = 100;
            int[] given = new int[arraySize];
            Random randomise = new Random(6769);
            for (int i = 0; i < testsCount; ++i) {
                for (int j = 0; j < arraySize; ++j) {
                    given[j] = randomise.nextInt();
                }
                int[] expected = given.clone();
                Arrays.sort(expected);
                Main.heapSort(given);
                assertArrayEquals(given, expected);
            }
        }
    }
}