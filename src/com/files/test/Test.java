package com.files;
import java.sql.*;
import junit.framework.Assert;
public class Test {

    @org.junit.Test
    public void test_insert() {
        try{
            String tablename = "employees_test";
            int count = 5;
            ImportJS importjs = new ImportJS();
            importjs.JsonToMysql(tablename, count);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.wmi.amu.edu.pl/s462090_Projekt_Programowanie", "s462090", "HasloTestowe123");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from " + tablename);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            Assert.assertEquals(columnsNumber, count);
            stmt.executeUpdate("DROP TABLE IF EXISTS `" + tablename + "`");
        }catch (Exception e) {
            System.out.println(e);
        }

    }
}