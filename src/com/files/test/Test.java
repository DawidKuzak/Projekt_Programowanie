package com.files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    @org.junit.Test
    public void test_export(){
        try{
        String query = "Select * FROM employees LIMIT 3";
        String perfect = "{ \n" +
                "\"emp_no\": \"54322\"\n" +
                "\"birth_date\": \"1949-07-02\"\n" +
                "\"first_name\": \"Heather\"\n" +
                "\"last_name\": \"Jordan\"\n" +
                "\"hire_date\": \"1969-07-17\"\n" +
                "} \n" +
                "{ \n" +
                "\"emp_no\": \"54323\"\n" +
                "\"birth_date\": \"1949-09-02\"\n" +
                "\"first_name\": \"Nathan\"\n" +
                "\"last_name\": \"Urx\"\n" +
                "\"hire_date\": \"1969-10-21\"\n" +
                "} \n" +
                "{ \n" +
                "\"emp_no\": \"54324\"\n" +
                "\"birth_date\": \"1949-09-11\"\n" +
                "\"first_name\": \"Paul\"\n" +
                "\"last_name\": \"Yewbeam\"\n" +
                "\"hire_date\": \"1970-01-02\"\n" +
                "} \n";
        Export export = new Export();
        export.MySqlToJson(query);
        File file = new File(
                "J:\\IdeaProjects\\Projekt_Programowanie\\MySQLDB.json");
        BufferedReader br
                = new BufferedReader(new FileReader(file));
        String st;
        String all ="";
        while ((st = br.readLine()) != null){
            all += st + "\n";
        }
        Assert.assertEquals(all, perfect);
        } catch (Exception e) {
        System.out.println(e);
    }

    }
}