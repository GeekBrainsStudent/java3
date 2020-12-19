package database;

import java.sql.*;

public class QueryDB {

    private final int goods_number = 10_000;    // кол-ство товаров
    private final Connection connect;

    public QueryDB(Connection connect) {

        this.connect = connect;

        if(createTable())
            fillTable();
        else
            System.out.println("Не удалось подключиться к базе данных");
    }

    private boolean createTable() {

        final String drop = "DROP TABLE IF EXISTS goods";

        final String create = "CREATE TABLE IF NOT EXISTS "+ SchemeDB.Table.NAME + " (" +
                SchemeDB.Table.Cols.ID + " INT AUTO_INCREMENT PRIMARY KEY," +
                SchemeDB.Table.Cols.NAME + " VARCHAR(255) NOT NULL," +
                SchemeDB.Table.Cols.PRICE + " DOUBLE NOT NULL)";

        try(Statement stmt = connect.createStatement()) {
            stmt.execute(drop);
            stmt.execute(create);
            return true;

        } catch (SQLException e) {
            System.out.println("\nОшибка при создании таблицы товаров");
            e.printStackTrace();
            return false;
        }
    }

    private void fillTable() {

        final String query = "INSERT INTO " + SchemeDB.Table.NAME +
                " (" + SchemeDB.Table.Cols.NAME + ", " + SchemeDB.Table.Cols.PRICE + ") VALUES(?, ?)";

        try(PreparedStatement pstmt = connect.prepareStatement(query)) {

            connect.setAutoCommit(false);

            for(int i = 0; i < goods_number; i++) {
                pstmt.setString(1,"good" + i);
                pstmt.setDouble(2, getRandomPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

            connect.setAutoCommit(true);

        } catch (SQLException e) {
            System.out.println("\nОшибка при заполнении таблицы");
            e.printStackTrace();
        }
    }

    private double getRandomPrice() {
        return Math.random() * 100.0 + 1.0;
    }

    public double getPrice(final String goodName) {

        String query = "SELECT "+ SchemeDB.Table.Cols.PRICE + " FROM goods WHERE " +
                SchemeDB.Table.Cols.NAME + " = '" + goodName + "'";

        try( Statement stmt = connect.createStatement();
                ResultSet result = stmt.executeQuery(query)) {
            if(!result.isClosed())
                return result.getDouble(SchemeDB.Table.Cols.PRICE);
            return 0.0;
        } catch (SQLException e) {
            System.out.println("\nОшибка при запросе цены");
            e.printStackTrace();
            return -1.0;
        }
    }

    public void updatePrice(String goodName, double newPrice) {

        String query = "UPDATE " + SchemeDB.Table.NAME + " SET " + SchemeDB.Table.Cols.PRICE + " = " + newPrice +
                " WHERE " + SchemeDB.Table.Cols.NAME + " = '" + goodName + "'";

        try(Statement stmt = connect.createStatement()) {
            if(stmt.executeUpdate(query) == 1) {
                System.out.println("Цена обновлена");
            }
            else {
                System.out.println("Цена не обновлена, проверьте запрос");
            }

        } catch (SQLException e) {
            System.out.println("\nОшибка при изменении цены");
            e.printStackTrace();
        }
    }

    public void selectPriceRange(double min, double max) {

        String query = "SELECT * FROM " + SchemeDB.Table.NAME + " WHERE " +
                SchemeDB.Table.Cols.PRICE + " BETWEEN " + min + " AND " + max;

        try(Statement stmt = connect.createStatement();
            ResultSet res = stmt.executeQuery(query)) {

            while(res.next()) {
                String name = res.getString(SchemeDB.Table.Cols.NAME);
                double price = res.getDouble(SchemeDB.Table.Cols.PRICE);
                System.out.printf("Название: %s, цена: %.2f%n",name,price);
            }


        } catch (SQLException e) {
            System.out.println("\nОшибка при выборе диапазона цен");
            e.printStackTrace();
        }
    }
}
