package csc4330;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.*;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import net.jqwik.api.*;

/*
 Class containing JUnit 5 & jqwik tests made for a basic implementation of Merge Sort in Java
 Author: Connor Morris
*/
public class SortingTest {

    // Example of testing for positive cases and boundary cases
    @ParameterizedTest(name = "{0}")
    @MethodSource("arg_generator")
    public void multiTest(String description, int[] expected, int[] arr, int l_index, int r_index) {
        Sorting.sort(arr, l_index, r_index);
        assertArrayEquals(expected, arr);
    }

    // Helper function that generates a stream of arguments for the multiTest() function
    private static Stream<Arguments> arg_generator() {
        // positive cases (algorithm performs as intended)
        Arguments p1 = Arguments.of("even size array", new int[]{-88, -2, 1, 4, 32, 81}, new int[]{-2, 81, 4, 32, 1, -88}, 0, 5);
        Arguments p2 = Arguments.of("odd size array", new int[]{-95, -76, -8, 17, 47, 59, 83}, new int[]{47, -8, 59, 17, -95, 83, -76}, 0, 6);
        Arguments p3 = Arguments.of("half sorted array, first half", new int[]{-26, 25, 48, 68, 73, 5, 49, -32, -70, 53}, new int[]{68, -26, 73, 25, 48, 5, 49, -32, -70, 53}, 0, 4);
        Arguments p4 = Arguments.of("half sorted array, second half", new int[]{68, -26, 73, 25, 48, -70, -32, 5, 49, 53}, new int[]{68, -26, 73, 25, 48, 5, 49, -32, -70, 53}, 5, 9);

        // boundary cases (edge cases)
        Arguments b1 = Arguments.of("empty array", new int[]{}, new int[]{}, 0, 0);
        Arguments b2 = Arguments.of("single-element array", new int[]{1}, new int[]{1}, 0, 0);
        Arguments b3 = Arguments.of("array with duplicate values", new int[]{-1, -1, 1, 1}, new int[]{1, -1, 1, -1}, 0, 3);
        Arguments b4 = Arguments.of("already sorted array", new int[]{-2, -1, 0, 1, 2}, new int[]{-2, -1, 0, 1, 2}, 0, 4);
        Arguments b5 = Arguments.of("reverse-sorted array", new int[]{-2, -1, 0, 1, 2}, new int[]{2, 1, 0, -1, -2}, 0, 4);

        return Stream.of(p1, p2, p3, p4, b1, b2, b3, b4, b5);
    }

    // Example of testing for a negative case (invalid input)
    @Test
    public void nullArrayTest() {
        int[] arr = null;
        assertThrows(NullPointerException.class, () -> {
            Sorting.sort(arr, 0, 1);
        });
    }

    // Example of testing for performance cases (performs efficiently with arrays of varying sizes)
    @Property(tries = 5000, generation = GenerationMode.RANDOMIZED, edgeCases = EdgeCasesMode.NONE)
    @Label("Testing random int arrays against default sort")
    public void multiTest(@ForAll int[] arr) {
        int[] copy_arr = arr.clone();
        Sorting.sort(arr, 0, arr.length - 1); // use manual implementation
        Arrays.sort(copy_arr); // use built-in implementation
        assertArrayEquals(copy_arr, arr);
    }

    // Example of testing for idempotency cases (stability across repeated executions)
    @RepeatedTest(value = 1000)
    public void idempotencyTest() {
        int[] arr1 = {-2, 81, 4, 32, 1, -88};
        int[] arr2 = {47, -8, 59, 17, -95, 83, -76};
        int[] arr3 = {68, -26, 73, 25, 48, 5, 49, -32, -70, 53};
        int[] arr4 = {68, -26, 73, 25, 48, 5, 49, -32, -70, 53};
        Sorting.sort(arr1, 0, 5); // sort an even sized array
        Sorting.sort(arr2, 0, 6); // sort an odd sized array
        Sorting.sort(arr3, 0, 4); // sort the first half of an array
        Sorting.sort(arr4, 5, 9); // sort the second half of an array
        assertArrayEquals(new int[]{-88, -2, 1, 4, 32, 81}, arr1);
        assertArrayEquals(new int[]{-95, -76, -8, 17, 47, 59, 83}, arr2);
        assertArrayEquals(new int[]{-26, 25, 48, 68, 73, 5, 49, -32, -70, 53}, arr3);
        assertArrayEquals(new int[]{68, -26, 73, 25, 48, -70, -32, 5, 49, 53}, arr4);
    }
}