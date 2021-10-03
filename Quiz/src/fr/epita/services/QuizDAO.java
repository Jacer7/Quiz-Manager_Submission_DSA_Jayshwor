package fr.epita.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class QuizDAO {
    public void createTable() throws SQLException {
        String createStatement = "CREATE TABLE IF NOT EXISTS QUIZ(ID int not null auto_increment, QUIZ_NAME  varchar(30))";
        PreparedStatement preparedStatement = getConnection(Configuration.getInstance()).prepareStatement(createStatement);
        preparedStatement.execute();
    }


    private Connection getConnection(Configuration conf) throws SQLException {
        Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"), conf.getConfValue("db.user"),
                conf.getConfValue("db.password")); // TODO : externalize
        return connection;
    }


    public boolean insertQuiz(String QuizName) throws SQLException {
        Configuration conf = Configuration.getInstance();
        Connection connection = getConnection(conf);
        int id = 0;
        id = search(QuizName);
        if (id == 0) {
            String query = "insert into QUIZ (QUIZ_NAME) values (?) ";
            PreparedStatement preparedstatement = connection.prepareStatement(query);

            preparedstatement.setString(1, QuizName);
            preparedstatement.execute();
            connection.close();
            return true;
        } else {
            System.out.println("This Quiz already exist");
            return false;
        }
    }

    public int search(String QuizName) throws SQLException {
        int id = 0;
        Configuration conf = Configuration.getInstance();
        Connection connection = getConnection(conf);
        String query = "Select ID , QUIZ_NAME From QUIZ where lower(QUIZ_NAME) = '" + QuizName + "'";

        PreparedStatement preparedstatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedstatement.executeQuery();
        while (resultSet.next()) {
            id = resultSet.getInt("ID");
        }
        preparedstatement.close();
        resultSet.close();
        connection.close();
        return id;
    }

}
