package net.lotho.sql;

import net.lotho.Azazel;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager extends Manager {

    private Azazel instance;
    public Connection connection;

    public MySQLManager(Azazel instance) {
        super(instance);
        this.instance = instance;

        this.connect();
        this.createTables();
    }

    private void createTables() {
        this.createTable("players", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, activeTag VARCHAR(15), tokens integer NOT NULL");
        this.createTable("tags", "id integer NOT NULL PRIMARY KEY, name VARCHAR(15) NOT NULL, ownerUUID VARCHAR(36) NOT NULL");
    }

    public void connect() {
        try {
            String url = this.instance.configManager.getConfigFile().getString("database.url");
            String username = this.instance.configManager.getConfigFile().getString("database.username");
            String password = this.instance.configManager.getConfigFile().getString("database.password");

            this.setConnection(DriverManager.getConnection(url, username, password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setConnection(Connection newConnection) {
        this.connection = newConnection;
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                String url = this.instance.configManager.getConfigFile().getString("database.url");
                String username = this.instance.configManager.getConfigFile().getString("database.username");
                String password = this.instance.configManager.getConfigFile().getString("database.password");

                this.setConnection(DriverManager.getConnection(url, username, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.connection;
    }

    /**
     * Create a new table within the database.
     *
     * @param name The name of the table.
     * @param info The table information.
     */
    private void createTable(String name, String info) {
        new Thread(() -> {
            try {
                this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");").execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute an update to the database.
     *
     * @param query  The statement to the database.
     * @param values The values to be inserted into the statement.
     */
    public void execute(String query, Object... values) {
        new Thread(() -> {
            try {
                PreparedStatement statement = this.getConnection().prepareStatement(query);
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i +1), values[i]);
                }
                statement.execute();
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while executing an update on the database.");
                Bukkit.getConsoleSender().sendMessage("MySQL#execute : " + query);
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute a query to the database.
     *
     * @param query    The statement to the database.
     * @param callback The data callback (Async).
     * @param values   The values to be inserted into the statement.
     */
    public void select(String query, SelectCall callback, Object... values) {
        new Thread(() -> {
            try {
                PreparedStatement statement = this.getConnection().prepareStatement(query);
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i +1), values[i]);
                }
                callback.call(statement.executeQuery());
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("An error occurred while executing a query on the database.");
                Bukkit.getConsoleSender().sendMessage("MySQL#select : " + query);
                e.printStackTrace();
            }
        }).start();
    }
}
