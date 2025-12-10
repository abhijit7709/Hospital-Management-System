package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url ="jdbc:mysql://localhost:3306/hospital";

    private static final String username ="root";

    private static final String password ="Abhi@7709";
    public static void main(String args[])
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient Patient = new Patient(connection,scanner);
            Doctor Doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        // Add Patient
                        Patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patients
                        Patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        // View Doctor
                        Doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointment
                        bookAppointment(Patient,Doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU ! FOR USING HOSPITAL MANAGEMENT SYSTEM ");
                        return;
                    default:
                        System.out.println("Enter Valid Choice: ");
                        break;

                }

            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }



    public static void bookAppointment(Patient patient ,Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id:");
        int Patient_Id = scanner.nextInt();
        System.out.print("Enter Doctor Id:");
        int Doctor_Id = scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD):");
        String Appointment_date = scanner.next();

        if(patient.getPatientById(Patient_Id) && Doctor.getDoctorById(Doctor_Id)){
            if(checkDoctorAvailability(Doctor_Id, Appointment_date,connection)){
                String AppointmentQuery = "INSERT INTO Appointments(Patient_Id , Doctor_Id,Appointment_Date) VALUES(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(AppointmentQuery);
                    preparedStatement.setInt(1, Patient_Id);
                    preparedStatement.setInt(2, Doctor_Id);
                    preparedStatement.setString(3, Appointment_date);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                         System.out.println("Appointment Booked!");
                    }else{
                          System.out.println("Failed To Appointment Booked!");
                        }     
                   
                }catch (SQLException e) {
                    e.printStackTrace();
                    
                }
            }else{
                System.out.println(" Doctor Not Available On This Date");
            }
        }
        else{
            System.out.println("Either Doctor or Patient doesn't Exit!!!");
        }
    }
    
    public static boolean checkDoctorAvailability(int Doctor_Id, String Appointment_date, Connection connection){
        String query = "SELECT COUNT(*) FROM Appointments WHERE Doctor_Id = ? AND Appointment_date =?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Doctor_Id);
            preparedStatement.setString(2, Appointment_date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
