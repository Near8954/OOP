package org.example;

/**
 * Class for heap sort, which contains all necessary functions.
 */
public class heapSort {
    /**
     * Constructor to avoid warning.
     */
    private heapSort() {}

    /**
     * Sifts down element in array to help make heap.
     * @param array given array
     * @param i index of element which siftDown sifts
     * @param n length of array
     */

    private static void siftDown(int[] array, int i, int n) {
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        int max = i;
        if (left < n && array[max] < array[left]) {
            max = left;
        }
        if (right < n && array[max] < array[right]) {
            max = right;
        }
        if (max != i) {
            Utils.swapInArray(array, i, max);
            siftDown(array, max, n);
        }
    }
    /**
     * Makes heap from given array.
     * @param array given array
     */
    private static void heapify(int[] array) {
        int n = array.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(array, i, n);
        }
    }

    /**
     * Sorts given array using heapify.
     * @param array given array
     */
    public static void heapSort(int[] array) {
        heapify(array);
        int border = array.length - 1;
        while (border > 0) {
            Utils.swapInArray(array, 0, border);
            siftDown(array, 0, border);
            border--;
        }
    }
}
