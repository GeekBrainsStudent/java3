import database.ConnectDB;
import database.QueryDB;
import database.SchemeDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GoodStore {

    private ConnectDB connect;
    private QueryDB query;
    private final Scanner scan;

    GoodStore() {

        try {
            connect = new ConnectDB();
            query = new QueryDB(connect.getConnect());

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Ошибка соединения с базой");
            e.printStackTrace();
        }

        scan = new Scanner(System.in);
    }

    public void work() {

        while(true) {
            showInfo();
            String in = scan.next();

            switch (in) {
                case "q":
                    quit();
                    return;
                case "1":
                    printPrice();
                    break;
                case "2":
                    setPrice();
                    break;
                case "3":
                    selectGoods();
                    break;
                default:
                    System.out.println("Введите номер пункта");
                    break;
            }
        }
    }

    private void showInfo() {
        System.out.println();
        System.out.println("Административная панель магазина товаров Goods:");
        System.out.println("1) Узнать цену товара по названию;");
        System.out.println("2) Установить новую цену товару;");
        System.out.println("3) Список товаров в ценовом диапазоне.");
        System.out.print("Введите номер интересующего Вас пункта (для выхода нажмите 'q'): ");
    }

    private void quit() {
        try {
            connect.close();
            scan.close();

        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии соединениия");
            e.printStackTrace();
        }
    }

    private void printPrice() {
        try {
            System.out.print("\nВведите название товара: ");
            String goodName = scan.next();
            double price = query.getPrice(goodName);
            if(price == 0.0) {
                System.out.println("Такого товара нет");
                return;
            }
            System.out.printf("Цена составляет: %.2f\n", price);
        } catch (NoSuchElementException e) {
            System.out.println("\nВы ничего не ввели");
        }
    }

    private void setPrice() {
        try {
            System.out.print("\nВведите название товара: ");
            String goodName = scan.next();
            System.out.print("Введите новую цену: ");
            double newPrice = scan.nextDouble();

            query.updatePrice(goodName, newPrice);

        } catch (InputMismatchException e) {
            System.out.println("\nВы ввели неправильную цену");
            scan.next();
        } catch (NoSuchElementException e) {
            System.out.println("\nВы ничего не ввели");
        }

    }

    private void selectGoods() {
        try {
            System.out.print("\nВведите минимальную цену: ");
            double minPrice = scan.nextDouble();
            System.out.print("Введите максимальную цену: ");
            double maxPrice = scan.nextDouble();

            query.selectPriceRange(minPrice, maxPrice);

        } catch (InputMismatchException e) {
            System.out.println("\nВы ввели неправильную цену");
            scan.next();
        } catch (NoSuchElementException e) {
            System.out.println("\nВы ничего не ввели");
        }
    }
}
