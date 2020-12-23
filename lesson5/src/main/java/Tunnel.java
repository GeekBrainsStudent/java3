import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    // Объявляем Semaphore
    private final Semaphore stop;

    public Tunnel(Semaphore tunnelStop) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";

        // Получаем Semaphore
        stop = tunnelStop;
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);

                // Определяем можем ли войти в тоннель
                stop.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                // при выходе из тоннеля
                stop.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
