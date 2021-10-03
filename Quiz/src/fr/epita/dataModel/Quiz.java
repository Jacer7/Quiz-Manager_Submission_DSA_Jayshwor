package fr.epita.dataModel;

import fr.epita.services.QuizDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class Quiz {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean AddQuiz() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please Enter the Quiz Name ");
        title = scan.nextLine();
        System.out.println(title);
        boolean quizID = false;
        QuizDAO QD = new QuizDAO();
        try {
            quizID = QD.insertQuiz(title.toLowerCase());
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {

            if(quizID)
            {
                System.out.println("\n\nsuccessfully created a quiz with Name :- " + title);
            }
            else
            {
                System.out.println("Can not create :- " + title +" Quiz name");
            }
        }catch(Exception e) {
            System.out.println("Exception encountered in inserting Quiz name /n Error :- " +e);
        }
        finally {
        }
        return quizID;
    }
}
