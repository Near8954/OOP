package org.example;

public class Utils {
    public static void swapInArray(int[] array, int left, int right) {
        int swap = array[left];
        array[left] = array[right];
        array[right] = swap;
    }
}
