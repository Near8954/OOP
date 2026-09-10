package org.example;


/**
 * Additional class with some helpful functions.
 */
public class Utils {

    /**
     * Constructor to avoid warning.
     */
    private Utils() {}

    /**
     * Swaps two int elements in array.
     * @param array Given array
     * @param first first argument to swap
     * @param second second argument to swap
     */
    public static void swapInArray(int[] array, int first, int second) {
        int swap = array[first];
        array[first] = array[second];
        array[second] = swap;
    }
}
