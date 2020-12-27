import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class MainTest {

    private Main main;

    @BeforeEach
    void init() {
        main = new Main();
    }

//    Task 1

    @DisplayName("Task1Test")
    @ParameterizedTest
    @MethodSource("getArgsForTask1")
    void task1Test(int[] res, int[] src, int lastOccurrenceElement) {
        Assertions.assertArrayEquals(res, main.copyFromLastOccurrenceElement(src, lastOccurrenceElement));
    }

    static Stream<Arguments> getArgsForTask1() {
        return Stream.of(
                Arguments.arguments(new int[] {1,2,3,5}, new int[] {4,1,2,3,5}, 4),
                Arguments.arguments(new int[] {3,5}, new int[] {4,1,2,4,3,5}, 4),
                Arguments.arguments(new int[] {}, new int[] {4,56,4,56,43,4}, 4)
        );
    }

    @Test
    @DisplayName("Exception")
    public void task1Test2() {
        Assertions.assertThrows(RuntimeException.class,
                () -> main.copyFromLastOccurrenceElement(new int[] {}, 4),
                "В массиве нет 4");
    }

//    Task 2

    @DisplayName("Task2Test")
    @ParameterizedTest
    @MethodSource("getArgsForTask2")
    void task2Test(int[] src, int x, int y) {
        Assertions.assertTrue(main.checkConsistArray(src, x, y));
    }

    static Stream<Arguments> getArgsForTask2() {
        return Stream.of(
            Arguments.arguments(new int[] {1,1,1,4}, 1, 4),
            Arguments.arguments(new int[] {1,4,1,4,1,1}, 1, 4),
            Arguments.arguments(new int[] {4,4,4,4}, 1, 4),
            Arguments.arguments(new int[] {1,1}, 1, 4),
            Arguments.arguments(new int[] {1,2,1,1,4,4,4,1}, 1, 4)
        );
    }
}