package com.project.crud.database;

import com.project.crud.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    Connection con;

    public Database() {
        con = getConnection();
        createStudentsTable();
    }

    // THIS IS FOR SEARCH STUDENTS             final String command = "SELECT * FROM `students` where firstName REGEXP 'shawn'";;

    public Connection getConnection() {
        try {
            String url = "URL";
            String username = "USERNAME";
            String password = "PASSWORD";

            System.out.println( "Successfully connected to the database!" );
            return DriverManager.getConnection( url, username, password );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in getConnection() function: " + err.getMessage() );
        }

        return null;
    }

    public List< Student > getStudents() {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return null;
        }

        List< Student > list = new ArrayList<>();
        try {
            final String command = "SELECT * FROM `students`";
            final PreparedStatement getStudents = con.prepareStatement( command );
            final ResultSet response = getStudents.executeQuery();

            while ( response.next() ) {
                list.add( new Student(
                        Integer.parseInt( response.getString( 1 ) ),
                        response.getString( 2 ),
                        response.getString( 3 ),
                        Integer.parseInt( response.getString( 4 ) ),
                        Integer.parseInt( response.getString( 5 ) ),
                        response.getString( 6 ),
                        response.getString( 7 ),
                        response.getString( 8 ) )
                );
            }
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in getConnection() function: " + err.getMessage() );
        }

        return list;
    }

    public void createStudentsTable() {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return;
        }

        try {
            final PreparedStatement createTable = con.prepareStatement( "create table if not exists students ( studentNo int not null, firstName char(32) not null, lastName char(32) not null, yearLevel int not null, age int not null, gender char(10) not null, program char(64) not null, imagePath char(255) not null )" );
            final int tableExists = createTable.executeUpdate();

            if ( tableExists == 0 ) System.out.println( "A `students` table already exists. Skipping..." );
                else System.out.println( "A `students` table doesn't exist. Successfully created one!" );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in getConnection() function: " + err.getMessage() );
        }
    }

    public void insertStudent( int studentNumber, String firstName, String lastName, int yearLevel, int age, String gender, String program, String imagePath ) {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return;
        }

        try {
            final String command = String.format( "insert into students ( studentNo, firstName, lastName, yearLevel, age, gender, program, imagePath ) values ( %d, '%s', '%s', %d, %d, '%s', '%s', '%s' )", studentNumber, firstName, lastName, yearLevel, age, gender, program, imagePath );
            final PreparedStatement insertStudent = con.prepareStatement( command );
            final int successful = insertStudent.executeUpdate();

            if ( successful == 1 ) System.out.println( "Successfully added a new entry into the `students` table." );
                else System.err.println( "Something went wrong! You should check it out, maybe it's the database... or you!" );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in insertStudent() function: " + err.getMessage() );
        }
    }

    public void updateStudent( int studentNumber, String updatedFirstName, String updatedLastName, int updatedYearLevel, int updatedAge, String updatedGender, String updatedProgram, String updatedImagePath ) {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return;
        }

        try {
            final String command = String.format( "update `students` set firstName = %s, lastName = %s, yearLevel = %d, age = %d, gender = %s, program = %s, imagePath = %s where studentNo = %d", updatedFirstName, updatedLastName, updatedYearLevel, updatedAge, updatedGender, updatedProgram, updatedImagePath, studentNumber  );
            final PreparedStatement updateStudent = con.prepareStatement( command );
            final int successful = updateStudent.executeUpdate();

            if ( successful == 1 ) System.out.println( "Successfully updated the entry with a student number " + studentNumber + " into the `students` table." );
            else System.err.println( "Something went wrong! You should check it out, maybe it's the database... or you!" );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in insertStudents() function: " + err.getMessage() );
        }
    }

    public void removeStudent( int studentNumber ) {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return;
        }

        try {
            final String command = "delete from `students` where studentNo = " + studentNumber;
            final PreparedStatement removeStudent = con.prepareStatement( command );
            final int successful = removeStudent.executeUpdate();

            if ( successful == 1 ) System.out.println( "Successfully removed the entry with a student number " + studentNumber + " from the `students` table." );
            else System.err.println( "Something went wrong! You should check it out, maybe it's the database... or you!" );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in removeStudent() function: " + err.getMessage() );
        }
    }

    public boolean checkIfDuplicate( int studentNumber ) {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return true;
        }

        try {
            final String command = String.format( "select * from `students` where `studentNo` like %d", studentNumber );
            final PreparedStatement checkDuplicate = con.prepareStatement( command );
            final ResultSet hasDuplicate = checkDuplicate.executeQuery();

            return hasDuplicate.next();
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in checkIfDuplicate() function: " + err.getMessage() );
        }

        return false;
    }
}
