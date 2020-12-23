import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {

    private static int CARS_COUNT;

    // Объявляем объект CyclicBarrier
    private final CyclicBarrier waitStart;

    // Объявляем объект CountDownLatch
    private final CountDownLatch finish;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier waitStart, CountDownLatch finish) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;

        // получаем CyclicBarrier с MainClass
        this.waitStart = waitStart;

        // получаем CountDownLatch с MainClass
        this.finish = finish;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");

            // ждем, когда все будут готовы
            waitStart.await();

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            Участник финишировал
            finish.countDown();

//            Если участник победил
            if(finish.getCount() == CARS_COUNT - 1)
//                Печатаем его имя
                System.out.println(this.name + " - WIN");
        }
    }
}
