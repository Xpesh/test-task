package com.haulmont.testtask.dao;

import org.hsqldb.jdbc.JDBCDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseManager {

    private static final String pathToCreationScript = "/sql/creation.sql";
    private static Connection connection;


    private DataBaseManager() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setDatabaseName("testdb");
        dataSource.setPassword("");
        dataSource.setURL("jdbc:hsqldb:mem:testdb");
        dataSource.setUser("SA");
        try {
            connection = dataSource.getConnection();
            executeCreationScript();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeCreationScript() {
        InputStream in;
        try {
            in = new FileInputStream(getClass().getResource(pathToCreationScript).getFile());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));

            String str = reader.readLine();
            StringBuilder script = new StringBuilder(str);
            while ((str = reader.readLine()) != null) {
                script.append(str);
            }
            String[] statements = script.toString().split(";");

            for (String s : statements) {
                PreparedStatement statement = connection.prepareStatement(s);
                statement.execute();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if(connection == null){
            new DataBaseManager();
        }
        return connection;
    }

}
