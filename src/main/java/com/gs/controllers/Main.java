package com.gs.controllers;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        try (var conn = DbUtil.getConnection()) {
            System.out.println("Connected: " + !conn.isClosed());
        }
    }
}