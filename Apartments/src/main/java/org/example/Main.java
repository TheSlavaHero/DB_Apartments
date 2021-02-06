package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb01?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "password";
    private static int argument = 0;

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();
                consoleMessage();
                hardcodeApartments();
                viewApartments();

            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {

        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Apartments");
            st.execute("CREATE TABLE Apartments (" +
                    "id         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "district   VARCHAR(20) NOT NULL," +
                    "street     VARCHAR(20) NOT NULL," +
                    "area       VARCHAR(20) NOT NULL," +
                    "rooms      VARCHAR(20) DEFAULT NULL," +
                    "price      VARCHAR(20) DEFAULT NULL)"
            );
        }
    }
    private static void consoleMessage() {
        System.out.println("1 - id");
        System.out.println("2 - district");
        System.out.println("4 - street");
        System.out.println("8 - area");
        System.out.println("16 - rooms");
        System.out.println("32 - price");
        System.out.println("Enter sum of the categories you want to see. Example: to view id(1), street(4) and price(32), type 32+4+1=37");
        Scanner sc = new Scanner(System.in);
        while (getArgument() < 1 || getArgument() > 63) {
            setArgument(sc.nextInt());
        }

    }

    private static void hardcodeApartments() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO Apartments (district,street,area,rooms,price) VALUES (\"Highgate\", \"Shaftesbury Avenue\", 60, 2, 10000)");
            st.execute("INSERT INTO Apartments (district,street,area,rooms,price) VALUES (\"Hampstead\", \"Abby Road\", 308, 4, 52000)");
            st.execute("INSERT INTO Apartments (district,street,area,rooms,price) VALUES (\"Maida Vale\", \"Carnaby Street\", 115, 3, 19990)");
            st.execute("INSERT INTO Apartments (district,street,area,rooms,price) VALUES (\"Marylebone\", \"Baker Street\", 45, 1, 7500)");
            st.execute("INSERT INTO Apartments (district,street,area,rooms,price) VALUES (\"Paddington\", \"Portobello Road\", 97, 2, 16300)");
        }
    }
    private static void viewApartments() throws SQLException {
        String word = createWord(getArgument());
        PreparedStatement ps = conn.prepareStatement(
                "SELECT " + word + " FROM Apartments");
        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
    private static String createWord(int number) {
        boolean b = false;
        StringBuilder sb = new StringBuilder();
        if (number - 32 >= 0) { number = number - 32;
            sb.append("price");
            b = true; }
        comma(sb,b,number);
        if (number - 16 >= 0) { number = number - 16;
            sb.append("rooms");
            b = true; }
        comma(sb,b,number);
        if (number - 8 >= 0) { number = number - 8;
            sb.append("area");
            b = true; }
        comma(sb,b,number);
        if (number - 4 >= 0) { number = number - 4;
            sb.append("street");
            b = true; }
        comma(sb,b,number);
        if (number - 2 >= 0) { number = number - 2;
            sb.append("district");
            b = true; }
        comma(sb,b,number);
        if (number - 1 >= 0) { sb.append("id");
            b = true; }
        comma(sb,b,number);
        return sb.toString();
    }

    private static void comma(StringBuilder sb, boolean b, int n) {
        if (b == true && n > 0) {
            sb.append(", ");
            b = false;
        }
    }

    public static int getArgument() {
        return argument;
    }

    public static void setArgument(int argument) {
        Main.argument = argument;
    }
}
