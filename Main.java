import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.Scanner;
public class Main{
    public static void main(String args[]){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Wybierz co chcesz zrobić:");
        System.out.println("1. Eksportować dane do Json");
        System.out.println("2. Importować dane z Json do nowej tabeli");
        System.out.println("3. Wpisać własną komendę:");
        System.out.println("4. Wypisz pracowników z płacą pomiędzy");
        int choice = keyboard.nextInt();
        String query;
        Scanner scanner = new Scanner(System.in);
        switch(choice) {
            case 1:
                System.out.println("Jaką tabelę chcesz eksportować: ");
                String dbname = scanner.nextLine();
                query = "Select * from " + dbname;
                Export export = new Export();
                export.MySqlToJson(query);
                break;
            case 2:
                System.out.println("Jaką nazwę ma mieć nowa tabela: ");
                String tablename = scanner.nextLine();
                System.out.println("Podaj ilość kolummn: ");
                Scanner scan = new Scanner(System.in);
                int count = scan.nextInt();
                ImportJS importjs = new ImportJS();
                importjs.JsonToMysql(tablename, count);
                break;
            case 3:
                query = scanner.nextLine();
                Connector connector = new Connector();
                connector.connect(query);
                break;
            case 4:
                System.out.println("Dolny próg: ");
                int min = scanner.nextInt();
                System.out.println("Górny próg");
                int max = scanner.nextInt();
                String execute = "Select employees.first_name, employees.last_name, salaries.salary FROM employees, salaries WHERE employees.emp_no = salaries.emp_no AND salaries.salary BETWEEN " + min + " AND " + max;
                Connector con = new Connector();
                con.connect(execute);
        }
    }
}
class Connector {
    public void connect(String query) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.wmi.amu.edu.pl/s462090_Projekt_Programowanie", "s462090", "HasloTestowe123");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++){
                System.out.print(rsmd.getColumnName(i) + ", ");
            }
            System.out.println("");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class Export {
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

class ImportJS {
    public void JsonToMysql(String dbname, int columnsNumber){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.wmi.amu.edu.pl/s462090_Projekt_Programowanie", "s462090", "HasloTestowe123");
            String[] columnames = new String[columnsNumber];
            String querytoex = "INSERT INTO " + dbname + " VALUES";
            File file = new File(
                    "J:\\IdeaProjects\\Projekt_Programowanie\\MySQLDB.json");
            BufferedReader br
                    = new BufferedReader(new FileReader(file));
            String st;
            String finalquery="";
            String[] kv = new String[columnsNumber];
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