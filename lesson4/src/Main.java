import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

    }

//    Task #1
//    Статический внутренний класс, используемый в качестве монитора
    static class Letter {
        private String lastLetter = "C";

        synchronized void print(String printLetter) {
            try {
                switch (printLetter) {
                    case "A" -> queue(printLetter, "C");
                    case "B" -> queue(printLetter, "A");
                    case "C" -> queue(printLetter, "B");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void queue(String printLetter, String requiredLetter) throws InterruptedException {
            while (!lastLetter.equals(requiredLetter)) {
                wait();
            }
            lastLetter = printLetter;
            System.out.print(lastLetter);
            notifyAll();
        }
    }

//    Метод из задания
    private static void printABC() {

        final int printNumber = 5; // Кол-ство вывода каждой буквы

        Letter letter = new Letter();

        new Thread(() -> {
            for(int i = 0; i < printNumber; i++) {
                letter.print("A");
            }
        }).start();

        new Thread(() -> {
            for(int i = 0; i < printNumber; i++) {
                letter.print("B");
            }
        }).start();

        new Thread(() -> {
            for(int i = 0; i < printNumber; i++) {
                letter.print("C");
            }
        }).start();

    }

//    task#2
//    метод, в котором 3 потока построчно пишут данные в файл (по 10 записей с периодом в 20 мс).
    private static void fileWrite() {

        File file = new File("lesson4/src/file");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            Thread t1 = createThread("MyThread#1", writer);
            Thread t2 = createThread("MyThread#2", writer);
            Thread t3 = createThread("MyThread#3", writer);

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();

            /*Не совсем понял условие задания,
            20 мсек это пауза между записями или между потоками.
            Если между записями, то данная реализация подойдет (при этом ничего не сказано об очереди записи).
            Если 20 мсек это пауза между потоками, то должно было быть следующее:

            t1.start();
            t1.join();
            Thread.sleep(20);
            t2.start();
            t2.join();
            Thread.sleep(20);
            t3.start();
            t3.join();*/

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//    Вспомогательный метод для создания потока
    private static Thread createThread(String name, final BufferedWriter writer) {
        return new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    writer.write(i + ") " + Thread.currentThread().getName() + "\n");
                    Thread.sleep(20);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, name);
    }

//    task #3
    public static class MFP {

        private final long SPEED = 50;  // период задержки

        final Object monitorPrint = new Object(); // монитор для печати
        final Object monitorScan = new Object();  // монитор для сканирования

        // метод печати, получает кол-ство страниц
        public void print(int numbPages) {
            synchronized (monitorPrint) {

                function(numbPages, true);
            }
        }

        // метод сканирования, получает кол-ство страниц
        public void scan(int numbPages) {
            synchronized (monitorScan) {

                function(numbPages, false);
            }
        }

        // методы print и scan практически делают одно и тоже
        private void function(int numbPages, boolean isPrint) {
            if(numbPages <= 0)
                return;

            System.out.print(isPrint ? "\nОтпечатано " : "\nОтсканировано ");
            for(int i = 0; i < numbPages; i++) {

                System.out.print((i == numbPages - 1) ? (i + " страницы") : (i + ", "));

                try {
                    Thread.sleep(SPEED);
                } catch (InterruptedException e) {
                    System.out.println(isPrint ? "\nОшибка при печати" : "\nОшибка при сканировании");
                    e.printStackTrace();
                }
            }
        }

    }
}