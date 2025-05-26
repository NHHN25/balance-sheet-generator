package com.gs.controllers;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    public static Connection getConnection() throws Exception {
        String url = Config.get("db.url");
        String user = Config.get("db.user");
        String pass = Config.get("db.pass");
        return DriverManager.getConnection(url, user, pass);
    }
}
