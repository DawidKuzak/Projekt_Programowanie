package com.files;
import java.sql.*;
import java.io.*;
public class Export {
    public void MySqlToJson(String query){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.wmi.amu.edu.pl/s462090_Projekt_Programowanie", "s462090", "HasloTestowe123");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            FileWriter myWriter = new FileWriter("MySQLDB.json");
            while (rs.next()){
                myWriter.write("{ \n");
                for (int i=1; i<=columnsNumber; i++){
                    String columnValue = rs.getString(i);
                    String columnName = rsmd.getColumnName(i);
                    myWriter.write('"' + columnName + '"' + ": " + '"' + columnValue + '"' + "\n");
                }
                myWriter.write("} \n");
            }
            myWriter.write("}");
            myWriter.close();
            con.close();
            System.out.println("Wyeksportowano tabelę do MySQLDB.json");
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}