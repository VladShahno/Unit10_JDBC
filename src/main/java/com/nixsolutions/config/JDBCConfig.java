package com.nixsolutions.config;

import com.nixsolutions.data.TransportData;
import com.nixsolutions.service.JdbcTransportService;
import com.nixsolutions.service.ShortestPathService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCConfig {
    public void solutionOfTheTransportProblem() {

        Properties props = loadProperties();
        String url = props.getProperty("url");

        try (Connection connection = DriverManager.getConnection(url, props)) {
            connection.setAutoCommit(false);
            JdbcTransportService jdbcTransportService = new JdbcTransportService();
            TransportData transportData = jdbcTransportService.getTransportData(connection);

            List<Integer> solutions = new ArrayList<>();

            ShortestPathService shortestPathService = new ShortestPathService(
                    transportData.getCitiesCount(),
                    transportData.getRoutes());

            for (int i = 0; i < transportData.getProblems().size(); i++) {
                solutions.add(shortestPathService.
                        findShortestPath(transportData.
                        getProblems().
                        get(i).
                        get(1) - 1, transportData.
                        getProblems().
                        get(i).
                        get(2) - 1));
                JdbcTransportService.
                        insertSolutions(connection, transportData.
                        getProblems().
                        get(i).
                        get(0), solutions.
                        get(i));
            }
        } catch (SQLException e) {
            System.out.println("Connection Error!");
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = JdbcTransportService.class.getResourceAsStream("/jdbc.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return properties;
    }
}

