package com.nixsolutions;

import com.nixsolutions.config.JDBCConfig;

public class MainApp {
    public static void main(String[] args) {

        JDBCConfig jdbcConfig = new JDBCConfig();
        jdbcConfig.solutionOfTheTransportProblem();
    }
}
