package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import java.util.Arrays;

class MainTest {

    private int[] genRandomArray(int size, long seed) {
        Random randomise = new Random(seed);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = randomise.nextInt();
        }
        return arr;
    }

    @Test
    void testEmptyArray() {
        int[] given = {};
        int[] expected = {};
        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }

    @Test
    void testOneElementArray() {
        int[] given = {42};
        int[] expected = {42};
        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }

    @Test
    void testTwoElementsArrayAlreadySorted() {
        int[] given = {1, 2};
        int[] expected = {1, 2};
        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }

    @Test
    void testTwoElementsArrayUnsorted() {
        int[] given = {2, 1};
        int[] expected = {1, 2};
        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }

    @Test
    void testRandomArraySmall() {
        int[] given = genRandomArray(10, 12345L);
        int[] expected = given.clone();
        Arrays.sort(expected);

        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }

    @Test
    void testRandomArrayMedium() {
        int[] given = genRandomArray(100, 6769L);
        int[] expected = given.clone();
        Arrays.sort(expected);

        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }

    @Test
    void testRandomArrayLarge() {
        int[] given = genRandomArray(1000, 98765L);
        int[] expected = given.clone();
        Arrays.sort(expected);

        HeapSort.heapSort(given);
        assertArrayEquals(expected, given);
    }
}

