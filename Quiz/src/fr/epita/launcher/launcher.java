package fr.epita.launcher;

import fr.epita.dataModel.Question;
import fr.epita.dataModel.Quiz;
import fr.epita.services.Exception;
import fr.epita.services.QuestionDAO;
import fr.epita.services.QuizDAO;
import fr.epita.services.StudentDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class launcher {

    public static void main(String[] args) throws Exception, SQLException, IOException {
        QuizDAO quizDAO = new QuizDAO();
        quizDAO.createTable();

        StudentDAO studentDAO = new StudentDAO();
        studentDAO.createTable();
        // TODO Auto-generated method stub
        Main_Menu();
    }

    private static void Main_Menu() {

        int Utype = 0;
        Utype = displayMenu();
        if (Utype == 1) {
            displayTeacherOptions();
        } else {
            System.out.println("Something went wrong");
        }
    }

    public static void displayTeacherOptions() {
        System.out.println("\n\n Teacher's Menu \n");
        int selectedOption = 0;
        boolean check = true;
        while (check) {
            selectedOption = 0;
            selectedOption = TeacherMenu();

            switch (selectedOption) {
                case 1:
                    quizCreation();
                    System.out.println();
                    chooseMenu();
                    check = false;
                    break;
                case 2:
                    Question question = new Question();
                    try {
                        saveQuestion(question);
                        check = false;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println();
                    chooseMenu();
                    break;
                case 3:
                    updateQuestion();
                    System.out.println();
                    break;
                case 4:
                    deleteQuestion();
                    System.out.println();
                    chooseMenu();
                    check = false;
                    break;
                case 5:
                    System.out.println(" Good Bye ");
                    check = false;
                    break;
                default:
                    System.out.println("Wrong Choice");
            }

        }
    }

    public static int displayMenu() {
        System.out.println("WELCOME TO QUIZ MANAGER");
        Scanner scan = new Scanner(System.in);
        int type;
        type = displayType(scan);
        return type;
    }

    private static int displayType(Scanner sc) {
        System.out.println("Choose option are you teacher or student");
        System.out.println("1.Teacher");
        System.out.println("2.Student");
        System.out.println("Enter your choice (1 , 2) : \n");
        return sc.nextInt();
    }

    private static int TeacherMenu() {

        Scanner selectTeacher = new Scanner(System.in);
        System.out.println("1.1 for Quiz Creation");
        System.out.println("2.2 for Question Creation");
        System.out.println("3.3 to update Question");
        System.out.println("4.4 to Delete Question");
        System.out.println("5.5 to Quit Application");
        System.out.println("Enter your choice in number from 1 to 5 (1, 2, 3, 4, 5)");
        int select;
        select = selectTeacher.nextInt();
        return select;
    }

    private static void quizCreation() {
        boolean qid = false;
        System.out.println("Creating Quiz");
        Quiz qz = new Quiz();
        qid = qz.AddQuiz();
        if (qid) {
            System.out.println();
            System.out.println("Please go to menu to add question in this quiz");
        }
    }

    public static void saveQuestion(Question question) throws Exception, SQLException {
        try {
            System.out.println("Creating Question");
            String QuizName;
            int quizId = 0;
            QuestionDAO questionDAO = new QuestionDAO();
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the Quiz Name in which you want to add question");
            QuizName = sc.nextLine();

            try {
                QuizDAO quizDAO = new QuizDAO();
                quizId = quizDAO.search(QuizName.toLowerCase());

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                System.out.println("some error in seraching the Quiz \n Error is : " + e);
                e.printStackTrace();
                System.exit(1);
            }

            if (quizId == 0) {
                System.out.println("Quiz Not found with " + QuizName + " name");
            } else {
                question.setQuizid(quizId);
                boolean check = true;
                String ans;

                while (check) {
                    questionDAO.createTable();
                    setQuestion(question);
                    int QsId = questionDAO.insertTable(question);
                    String questionType = question.getQuestiontype();
                    if (QsId > 0 && questionType.equalsIgnoreCase("MCQ")) {
                        if (questionType.equalsIgnoreCase("Normal")) {
                            System.out.println();
                            System.out.println("Data Inserted Succesfully");
                            System.out.println();
                        } else {
                        }
                    }

                    System.out.println("To enter one more question press y else n");
                    ans = sc.next();

                    if (!ans.equalsIgnoreCase("y")) {
                        check = false;
                    }
                }
            }
        } catch (java.lang.Exception ex) {
            System.out.println(ex.toString());
        }

    }

    private static void setQuestion(Question qs) {
        Scanner Sc_Ques = new Scanner(System.in);
        System.out.println("Enter Question ");
        qs.setSubject(Sc_Ques.nextLine());

        System.out.println("Is it a MCQ type Question Or Normal Question? ");
        System.out.println("A for MCQ  OR  B for normal type");
        String Qstype = Sc_Ques.nextLine();

        if (Qstype.equalsIgnoreCase("A")) {
            qs.setQuestiontype("MCQ");
            addOptions(qs);
        } else {
            qs.setQuestiontype("Normal");
            addDifficulty(qs);
        }


    }

    private static void addOptions(Question qs) {

        Scanner scanner = new Scanner(System.in);

        String[] Option = new String[4];
        int max = 4;
        System.out.println("Enter 4 options");
        for (int i = 0; i < 4; i++) {
            System.out.println("Option " + (i + 1) + ":  ");
            Option[i] = scanner.nextLine();
        }
        qs.setOptions(Option);
        addDifficulty(qs);
    }


    private static void addDifficulty(Question qs) {

        Scanner sc_dif = new Scanner(System.in);
        System.out.println("Enter the dificulty level");
        System.out.print("1:Easy");
        System.out.print("2: Medium");
        System.out.println("3: Hard");
        System.out.println("Chose 1, 2,  3");

        qs.setDifficulty(sc_dif.nextInt());

        AddTopicName(qs);
    }

    private static void AddTopicName(Question question) {
        System.out.println("Enter the topic of question: example: array,iteration");
        Scanner scanner = new Scanner(System.in);
        String result = String.join(",", scanner.next());
        question.setTopics(result);
    }


    public static void updateQuestion() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Question which you want to update");
        String Qus = sc.nextLine();
        try {
            Question criteria = new Question();
            criteria.setSubject(Qus);

            //when
            QuestionDAO dao = new QuestionDAO();
            List<Question> searchResults = dao.search(criteria);

            //then
            if (searchResults.size() == 1) {
                Scanner sc1 = new Scanner(System.in);
                System.out.println("Enter new question you want to update");
                String Qus_1 = sc1.nextLine();
                criteria.setSubject(Qus_1);
                criteria.setQuestionId(searchResults.get(0).getQuestionId());
                String[] Option = new String[4];
                Option[0] = searchResults.get(0).getOptions()[0];
                Option[1] = searchResults.get(0).getOptions()[1];
                Option[2] = searchResults.get(0).getOptions()[2];
                Option[3] = searchResults.get(0).getOptions()[3];
                criteria.setOptions(Option);
                String questionType = searchResults.get(0).getQuestiontype();
                if (questionType.equalsIgnoreCase("MCQ")) {
                    System.out.println("Do you want to update?(Y/N)");
                    Scanner sc2 = new Scanner(System.in);
                    String check = sc2.nextLine();
                    if (check.equalsIgnoreCase("Y")) {
                        updateOption(criteria);
                    }
                    dao.updateTable(criteria);
//                    CorrectAnswerOption(criteria.getQuestionId(), "U");
                    System.out.println("Data updated succesfully");

                } else {
                    dao.updateTable(criteria);
                }
            } else {
                System.out.println("Non update found");
            }
        } catch (java.lang.Exception ex) {

        }
    }

    public static void deleteQuestion() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your question");
        String Qus = sc.nextLine();
        try {
            Question criteria = new Question();
            criteria.setSubject(Qus);

            //when
            QuestionDAO dao = new QuestionDAO();
            List<Question> searchResults = dao.search(criteria);

            //then
            if (searchResults.size() == 1) {
                criteria.setQuestionId(searchResults.get(0).getQuestionId());
                dao.deleteTable(criteria);

            } else {
                System.out.println("Not Found");
            }
        } catch (java.lang.Exception ex) {

        }
    }

    private static void chooseMenu() {
        Scanner Sc_Qsn = new Scanner(System.in);
        System.out.println();
        System.out.println("1: Teacher Menu, 2: Main Menu (Enter your Choice(1 , 2))");
        int Qsntype = Sc_Qsn.nextInt();
        if (Qsntype == 1) {
            displayTeacherOptions();
        } else {
            Main_Menu();
        }
    }

    private static void updateOption(Question question) {

        Scanner scanner = new Scanner(System.in);

        String[] Option = new String[4];
        int max = 4;
        System.out.println("Enter 4 options");
        for (int i = 0; i < question.getOptions().length; i++) {
            String op = "";
            System.out.println("Option " + (i + 1) + ":  ");
            op = scanner.nextLine();
            if (op.isEmpty() == false) {
                Option[i] = op;
            } else {
                Option[i] = question.getOptions()[i];
            }
        }
        question.setOptions(Option);


    }

}
