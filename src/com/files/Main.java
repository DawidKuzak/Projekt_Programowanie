package com.files;
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
                System.out.println("Górny próg: ");
                int max = scanner.nextInt();
                String execute = "Select employees.first_name, employees.last_name, salaries.salary FROM employees, salaries WHERE employees.emp_no = salaries.emp_no AND salaries.salary BETWEEN " + min + " AND " + max;
                Connector con = new Connector();
                con.connect(execute);
        }
    }
}