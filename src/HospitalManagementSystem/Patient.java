package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;

    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;

    }

    public void addPatient()
    {
        System.out.print("Enter Patient Name: ");
        String Name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int Age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String Gender = scanner.next();

        try{
            String query = "INSERT INTO patients(Name,Age,Gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Name);
            preparedStatement.setInt(2,Age);
            preparedStatement.setString(3,Gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0) {
                System.out.println("Patient Added Successfully!!");
            }
            else
            {
                System.out.println("Failed to Add Patient!!");
            }



        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient()
    {
        String query = "select * from patients";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(resultSet.next()){
                int Id = resultSet.getInt("Id");
                String Name = resultSet.getString("Name");
                int Age = resultSet.getInt("Age");
                String Gender = resultSet.getString("Gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-12s|\n",Id,Name,Age,Gender);
                System.out.println("+------------+--------------------+----------+------------+");

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id)
    {
        String query = "SELECT * FROM patients WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }else
            {
                return false;
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();

        }
        return false;
    }

}
