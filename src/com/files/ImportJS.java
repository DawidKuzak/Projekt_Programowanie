package com.files;
import java.sql.*;
import java.io.*;
import java.util.*;
public class ImportJS {
    public void JsonToMysql(String dbname, int columnsNumber){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.wmi.amu.edu.pl/s462090_Projekt_Programowanie", "s462090", "HasloTestowe123");
            String querytoex = "INSERT INTO " + dbname + " VALUES";
            File file = new File(
                    "J:\\IdeaProjects\\Projekt_Programowanie\\MySQLDB.json");
            BufferedReader br
                    = new BufferedReader(new FileReader(file));
            String st;
            String finalquery="";
            String [] kv;
            int i = 1;
            int whos = 1;
            finalquery = "CREATE TABLE `" + dbname + "` (";
            while ((st = br.readLine()) != null){
                kv = st.split(":");
                if (kv.length==2) {
                    String letters = kv[0].replaceAll("\"", "`");
                    String secondlet = kv[1].replaceAll("\"", "'");
                    if (whos <= columnsNumber) {
                        if (whos == 0) {
                            finalquery += letters + " char(100), ";
                            whos++;
                        } else {
                            if (whos == columnsNumber) {
                                finalquery += letters + " char (100));";
                                whos++;
                            } else {
                                finalquery += letters + " char(100), ";
                                whos++;
                            }
                        }

                    }
                    if (i==columnsNumber){
                        i=1;
                        querytoex += secondlet + "),";
                    }
                    else{
                        if (i == 1) {
                            i++;
                            querytoex += "(";
                            String without = secondlet.replaceAll(" ", "");
                            querytoex += without + ",";
                        } else {
                            if (i <columnsNumber) {
                                querytoex += secondlet + ",";
                                i++;
                            }
                        }}
                }
            }
            StringBuilder sb = new StringBuilder(querytoex);
            sb.deleteCharAt(querytoex.length() - 1);
            querytoex = sb.toString();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(finalquery);
            stmt.executeUpdate(querytoex);
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}