import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {

    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        // Для одновременного старта и для вывода сообщения о начале гонки, вводим объект CyclicBarrier
        final CyclicBarrier waitStart = new CyclicBarrier(CARS_COUNT,
                () -> System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!"));

        // Для запрета одновременного заезда в тоннель больше половины машин, вводим семафор
        final Semaphore tunnelStop = new Semaphore((int) CARS_COUNT/2);

        // Для вывода объявления об окончании гонки вводим объект CountDownLatch
        final CountDownLatch finish = new CountDownLatch(CARS_COUNT);

        // Передаем Semaphore в консруктор Tunnel
        Race race = new Race(new Road(60), new Tunnel(tunnelStop), new Road(40));


        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {

            // Передаем CyclicBarrier и CountDownLatch в конструктор Car
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), waitStart, finish);
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        // ожидаем завершения гонки всеми учатниками
        try {
            finish.await();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
