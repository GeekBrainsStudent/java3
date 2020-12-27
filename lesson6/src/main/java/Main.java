import java.util.ArrayList;
import java.util.Arrays;

public class Main {

//    lastOccurrenceElement - это элемент, после которого копируется массив
//    по условию задания четверка
    public int[] copyFromLastOccurrenceElement (int[] src, final int lastOccurrenceElement)
            throws RuntimeException {

        int length = src.length;

        int i = length - 1;

        for(; i >= 0; i--) {
            if(src[i] == lastOccurrenceElement) {
                int[] target = new int[(length - 1) - i];
                for(int j = 0; j < target.length; j++)
                    target[j] = src[++i];
                return target;
            }
        }

        throw new RuntimeException("В массиве нет " + lastOccurrenceElement);
    }

//    проверка массива на то, что он состоит из чисел x и y,
//    и хотя бы одно вхождение каждой из чисел
    public boolean checkConsistArray(int[] src, int x, int y) {

        boolean containX = false;
        boolean containY = false;

        for(int i : src) {

            if(i == x)
                containX = true;

            if(i == y)
                containY = true;

            if(i != x && i != y)
                return false;
        }

        return containX && containY;
    }
}
