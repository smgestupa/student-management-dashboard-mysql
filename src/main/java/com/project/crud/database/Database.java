package com.project.crud.database;

import java.sql.*;

public class Database {

    Connection con;

    public Database() {
        con = getConnection();
        createStudentsTable();
    }

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

    public void createStudentsTable() {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return;
        }

        try {
            PreparedStatement createTable = con.prepareStatement( "create table if not exists students ( studentNo int not null, firstName char(32) not null, lastName char(32) not null, yearLevel int not null, age int not null, gender char(10) not null, program char(64) not null, imagePath char(255) not null )" );
            final int tableExists = createTable.executeUpdate();
            if ( tableExists == 0 ) System.out.println( "A `students` table already exists. Skipping..." );
                else System.out.println( "A `students` table doesn't exist. Successfully created one!" );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in getConnection() function: " + err.getMessage() );
        }
    }

    public void insertStudents( int studentNumber, String firstName, String lastName, int yearLevel, int age, String gender, String program, String imagePath ) {
        if ( con == null ) {
            System.err.println( "Warning! Connection is null == you are not connected to the database!" );
            return;
        }

        try {
            final String command = String.format( "insert into students ( studentNo, firstName, lastName, yearLevel, age, gender, program, imagePath ) values ( %d, '%s', '%s', %d, %d, '%s', '%s', '%s' )", studentNumber, firstName, lastName, yearLevel, age, gender, program, imagePath );
            PreparedStatement insertStudent = con.prepareStatement( command );

            final int successful = insertStudent.executeUpdate();
            if ( successful == 1 ) System.out.println( "Successfully added a new entry into the `students` table." );
                else System.err.println( "Something went wrong! You should check it out, maybe it's the database... or you!" );
        } catch ( SQLException err ) {
            System.err.println( "Warning! SQLException has occurred in insertStudents() function: " + err.getMessage() );
        }
    }
}
