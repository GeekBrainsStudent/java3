import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {


    }

    //    task 1
    public static <E> void swap(E[] arr, int ind1, int ind2) {
        int length = arr.length;
//        Проверка заданных параметров на корректность
        if(length == 0 || ind1 < 0 || ind2 < 0 || ind1 >= length || ind2 >= length) {
            System.out.println("Проверьте входные данные");
            return;
        }

        E temp = arr[ind1];
        arr[ind1] = arr[ind2];
        arr[ind2] = temp;
    }

    //    task 2
    public static <E> ArrayList<E> transformToArrayList(E[] arr) {
        ArrayList<E> list = new ArrayList<>();
        Arrays.stream(arr).forEach(e -> list.add(e));
        return list;
    }

    //    task 3
    static class Fruit {
        float weight;
    }

    static class Apple extends Fruit {

        Apple() {
            weight = 1.0f;
        }

        @Override
        public String toString() {
            return "apple";
        }
    }

    static class Orange extends Fruit {

        Orange() {
            weight = 1.5f;
        }

        @Override
        public String toString() {
            return "orange";
        }
    }

    static class Box<E extends Fruit> {

        private final ArrayList<E> fruits;

        Box() {
            fruits = new ArrayList<>();
        }

        public void addFruit(E fruit) {
            if(fruits.size() != 0){
                if(getFruit().weight != fruit.weight) {
                    System.out.println("Нельзя добавлять разные фрукты в один ящик");
                    return;
                }
            }
            fruits.add(fruit);
        }

        public E popFruit() {
            if(fruits.size() == 0)
                return null;
            return fruits.remove(fruits.size() - 1);
        }

        public E getFruit() {
            if(fruits.size() == 0)
                return null;
            return fruits.get(fruits.size() - 1);
        }

        public float getWeight() {
            float weight = 0.0f;
            if(getFruit() != null)
                weight = fruits.size() * getFruit().weight;
            return weight;
        }

        public boolean compare(Box<? extends Fruit> box) {
            return this.getWeight() == box.getWeight();
        }

        public boolean transferTo(Box<E> box) {
            if(this.equals(box)) {
                System.out.println("Смысл?");
                return false;
            }

            if(fruits.size() == 0){
                System.out.println("Ящик пуст");
                return false;
            }

            if(box.getFruit() != null) {
                if(getFruit().weight != box.getFruit().weight) {
                    System.out.println("Нельзя класть разные фрукты в один ящик");
                    return false;
                }
            }

            int size = fruits.size();
            for(int i = 0; i < size; i++) {
                box.addFruit(popFruit());
            }
            return true;
        }

        @Override
        public String toString() {
            String fruitName = getFruit() == null ? "Fruit" : getFruit().getClass().getSimpleName();
            return "Box<" + fruitName + ">" +
                    fruits;
        }
    }
}