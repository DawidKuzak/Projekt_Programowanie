import java.sql.*;
import java.util.Scanner;
public class Main{
    public static void main(String args[]){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Wybierz co chcesz zrobić:");
        System.out.println("1. Eksportować dane do XML");
        System.out.println("2. Importować dane");
        System.out.println("3. Wpisać własną komendę");
        int choice = keyboard.nextInt();

        switch(choice) {
            case 3:
                Scanner scanner = new Scanner(System.in);
                String query = scanner.nextLine();
                Connector connector = new Connector();
                connector.connect(query);
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