package fr.epita.services;

import fr.epita.dataModel.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    public void createTable() throws SQLException {
        String createStatement = "CREATE TABLE IF NOT EXISTS QUESTION(ID INT PRIMARY KEY AUTO_INCREMENT , SUBJECT VARCHAR(2000), TOPICS VARCHAR(1000), DIFFICULTY INT, OPTION_ONE varchar(30), OPTION_TWO varchar(30), OPTION_THREE varchar(30), \n" +
                "OPTION_FOUR varchar(30), QUIZ_ID INT, QUESTIONTYPE varchar(30))";
        PreparedStatement preparedStatement = getConnection(Configuration.getInstance()).prepareStatement(createStatement);
        preparedStatement.execute();
    }


    public int insertTable(Question question) throws Exception {
        Configuration conf = Configuration.getInstance();
        int id = 0;


        String SQL_QUERY = "INSERT INTO QUESTION(SUBJECT,TOPICS, DIFFICULTY, OPTION_ONE, OPTION_TWO, OPTION_THREE, OPTION_FOUR, QUIZ_ID, QUESTIONTYPE) VALUES(?, ?, ? , ?, ?, ?, ?, ?, ?);";
        try {
            Connection connection = getConnection(conf);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY);
            preparedStatement.setString(1, question.getSubject());
            preparedStatement.setString(2, question.getTopics());
            preparedStatement.setInt(3, question.getDifficulty());
            if (question.getOptions() != null) {
                preparedStatement.setString(4, question.getOptions()[0]);
                preparedStatement.setString(5, question.getOptions()[1]);
                preparedStatement.setString(6, question.getOptions()[2]);
                preparedStatement.setString(7, question.getOptions()[3]);
            } else {
                preparedStatement.setString(4, "");
                preparedStatement.setString(5, "");
                preparedStatement.setString(6, "");
                preparedStatement.setString(7, "");
            }

            preparedStatement.setLong(8, question.getquizid());
            preparedStatement.setString(9, question.getQuestiontype());
            preparedStatement.execute();

            String QuestionId = "SELECT max(id) as ID FROM QUESTION where SUBJECT = '" + question.getSubject() + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QuestionId);
            while (resultSet.next()) {
                id = resultSet.getInt("ID");
            }

            connection.close();
        } catch (SQLException e) {
            Exception exception = new Exception();
            exception.initCause(e);
            exception.setObj(question);
            throw exception;
        }
        return id;

    }

    public List<Question> search(Question question) throws SQLException {
        List<Question> questionResult = new ArrayList<>();
        try {
            Configuration conf = Configuration.getInstance();

            Connection connection = getConnection(conf);
            PreparedStatement statement = connection
                    .prepareStatement("select SUBJECT, TOPICS, DIFFICULTY, QUESTIONTYPE, QUIZ_ID, OPTION_ONE, OPTION_TWO, OPTION_THREE, OPTION_FOUR, ID  from QUESTION where  lower(SUBJECT) LIKE ?");
            statement.setString(1, "%" + question.getSubject().toLowerCase() + "%");
            ResultSet resultset = statement.executeQuery();
            while (resultset.next()) {
                String subject = resultset.getString(1);
                String topics = resultset.getString(2);
                Integer difficulty = resultset.getInt(3);
                String questionType = resultset.getString(4);
                String option1 = resultset.getString(6);
                String option2 = resultset.getString(7);
                String option3 = resultset.getString(8);
                String option4 = resultset.getString(9);
                Integer qid = resultset.getInt(10);
                Question currentQuestion = new Question();
                currentQuestion.setSubject(subject);
                currentQuestion.setTopics(topics);
                currentQuestion.setDifficulty(difficulty);
                currentQuestion.setQuestiontype(questionType);
                String[] Option = new String[4];
                Option[0] = option1;
                Option[1] = option2;
                Option[2] = option3;
                Option[3] = option4;
                currentQuestion.setOptions(Option);
                currentQuestion.setQuestiontype(questionType);
                currentQuestion.setQuestiontype(questionType);
                currentQuestion.setQuestiontype(questionType);
                currentQuestion.setQuestionId(qid);
                questionResult.add(currentQuestion);
            }

            connection.close();
        } catch (java.lang.Exception ex) {
            System.out.println(ex.toString());
        }
        return questionResult;
    }


    private Connection getConnection(Configuration conf) throws SQLException {
        Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"), conf.getConfValue("db.user"),
                conf.getConfValue("db.password")); // TODO : externalize
        return connection;
    }

    public void updateTable(Question question) throws Exception {

        Configuration conf = Configuration.getInstance();
        int id = 0;
        try {
            String SQL_QUERY = "UPDATE QUESTION SET SUBJECT = '" + question.getSubject() + "', OPTION_ONE = '" + question.getOptions()[0].toString() + "', OPTION_TWO = '" + question.getOptions()[1].toString() + "', OPTION_THREE = '" + question.getOptions()[2].toString() + "', OPTION_FOUR = '" + question.getOptions()[3].toString() + "' WHERE ID = '" + question.getQuestionId() + "';";

            Connection connection = getConnection(conf);
            Statement stmt = connection.createStatement();
            stmt.execute(SQL_QUERY);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            Exception exception = new Exception();
            exception.initCause(e);
            exception.setObj(question);
            throw exception;
        } catch (java.lang.Exception ex) {
            System.out.println(ex);
        }
    }

    public void deleteTable(Question question) throws SQLException {
        Configuration conf = Configuration.getInstance();
        int id = 0;
        Connection connection = getConnection(conf);
        String DeleteQuery = "DELETE FROM QUESTION where ID = '" + question.getQuestionId() + "';";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(DeleteQuery);
        stmt.close();
        connection.close();
        System.out.println("question deleted");
    }

}

