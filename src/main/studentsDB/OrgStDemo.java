package main.studentsDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import static main.studentsDB.ConnectionData.PASSWORD;
import static main.studentsDB.ConnectionData.URL;
import static main.studentsDB.ConnectionData.USER;

public class OrgStDemo {
    public static void main(String[] args) {
        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("База данных по студентам подключена");
            StudentsDAO studentsDAO = new StudentsDAO();
            GroupsDAO groupsDAO = new GroupsDAO();
            OrganizationsDAO organizationsDAO = new OrganizationsDAO();
            StudentsDAO.cleanDB(connection); // удалить после первого запуска
            StudentsDAO.initDB(connection); // удалить после первого запуска
            Scanner sc = new Scanner(System.in);
            String command = "";
            while (!(command.equals("end"))) {
                switch (command) {
                    case "help": {
                        System.out.println("show organizations\n" +
                                "show groups\n" +
                                "show students\n" +
                                "show student by\n" +
                                "add student\n" +
                                "add group\n" +
                                "add organization\n" +
                                "delete student\n" +
                                "delete group\n" +
                                "edit student\n" +
                                "edit group\n" +
                                "export to excel\n" +
                                "import excel\n" +
                                "clean DB\n" +
                                "fill test DB\n" +
                                "init DB\n" +
                                "end\n");
                    }
                    break;
                    case "show organizations": {
                        Organization.printOrganizations(connection);
                    }
                    break;
                    case "show groups": {
                        System.out.println("Название организации: ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            Group.printGroups(connection, s);
                        }
                    }
                    break;
                    case "show students": {
                        System.out.println("Название группы: ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            Student.printStudents(connection, s);
                        }
                    }
                    break;
                    case "show student by": {
                        System.out.println("Для поска студента введите: \n" +
                                "-n Имя студента или -s Фамилия студента или -p Телефон студента");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            String[] ss = s.split(" ");
                            if (ss.length != 2) {
                                System.out.println("Не корректный ввод");
                            } else {
                                Student.printStudentBy(connection, ss[1], ss[0]);
                            }
                        }
                    }
                    break;
                    case "add student": {
                        System.out.println("Введите данные студента таким образом: \n" +
                                "Хомякова Анна Федоровна 1996-02-22 3804172289 ekforever@emai.com АГТ-35а");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            String[] ss = s.split(" ");
                            if (ss.length != 7) {
                                System.out.println("Не корректный ввод");
                            } else {
                                String fullName = ss[0] + " " + ss[1] + " " + ss[2];
                                String birthday = ss[3];
                                String phoneNumber = ss[4];
                                String email = ss[5];
                                String group = ss[6];
                                Student student = new Student(fullName, Date.valueOf(birthday),
                                        phoneNumber, email, groupsDAO.findIdByName(connection, group));
                                studentsDAO.create(connection, student);
                            }
                        }
                    }
                    break;
                    case "add group": {
                        System.out.println("Введите название группы и организации," +
                                "в которой вы хотите ее создать, через пробел: ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            String[] ss = s.split(" ");
                            if (ss.length != 2) {
                                System.out.println("Не корректный ввод");
                            } else {

                                groupsDAO.create(connection, new Group(ss[0],
                                        organizationsDAO.findIdByName(connection, ss[1])));
                            }
                        }
                    }
                    break;
                    case "add organization": {
                        System.out.println("Введите название организации: ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            organizationsDAO.create(connection, new Organization(s));
                        }
                    }
                    break;
                    case "delete student": {
                        System.out.println("Введите полное имя студента(ФИО): ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            studentsDAO.deleteByName(connection, s);
                        }
                    }
                    break;
                    case "delete group": {
                        System.out.println("Все студенты в удаленной группе бедут удалены\n" +
                                "Введите название группы: ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            groupsDAO.delete(connection, s);
                        }
                    }
                    break;
                    case "edit student": {
                        System.out.println("Введите ФИО студента и полные новые данные о нем\n" +
                                "Например: Хомякова Юлия Дмитриевна Першикова Анна Федоровна " +
                                "1996-02-22 3809129289 forever@emai.com АГТ-35а");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            String[] ss = s.split(" ");
                            if (ss.length != 10) {
                                System.out.println("Не корректный ввод");
                            } else {
                                String name = ss[0] + " " + ss[1] + " " + ss[2];
                                String fullName = ss[3] + " " + ss[4] + " " + ss[5];
                                String birthday = ss[6];
                                String phoneNumber = ss[7];
                                String email = ss[8];
                                String group = ss[9];
                                Student student = new Student(fullName, Date.valueOf(birthday),
                                        phoneNumber, email, groupsDAO.findIdByName(connection, group));
                                studentsDAO.update(connection, student, name);
                            }
                        }
                    }
                    break;
                    case "edit group": {
                        System.out.println("Введите старое и новое название группы через пробел: ");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            String[] ss = s.split(" ");
                            if (ss.length != 2) {
                                System.out.println("Не корректный ввод");
                            } else {
                                groupsDAO.update(connection, ss[0], new Group(ss[1]));
                            }
                        }
                    }
                    break;
                    case "export to excel": {
                        try {
                            CreateExcel.exportToExcel(connection);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case "import excel": {
                        System.out.println("Введите путь к файлу\n" +
                                "Например: D:\\Students\\st.xls");
                        if (sc.hasNextLine()) {
                            String s = sc.nextLine();

                            try {
                                ReadExcel.importExcel(connection, s);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                    case "clean DB": {
                        StudentsDAO.cleanDB(connection);
                        System.out.println("Все таблицы удалены");
                    }
                    break;
                    case "fill test DB": {
                        StudentsDAO.fillTestDB(connection);
                    }
                    break;
                    case "init DB": {
                        StudentsDAO.initDB(connection);
                    }
                }
                System.out.println("Введите команду, чтобы увидеть список доступных команд введите help");
                if (sc.hasNextLine()) {
                    command = sc.nextLine();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
