package org.example;

import java.util.Arrays;
import java.util.Collections;
import static org.example.heapSort.heapSort;

/**
 * Main class used to try functionality.
 */

public class Main {

    /**
     * Constructor to avoid warning.
     */
    private Main() {}

    /**
     * Main function of program.
     */
    public static void main() {
        int[] arr = {42, 17, 89, 5, 73, 21, 94, 38, 60, 11};
        heapSort.heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
