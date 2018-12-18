package main.studentsDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAO {
    public static void initDB(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS organizations "
                            + "(id INT(5) NOT NULL AUTO_INCREMENT,"
                            + " name VARCHAR(50) UNIQUE, "
                            + "PRIMARY KEY(id)); ");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS groups "
                            + "(id INT(5) NOT NULL AUTO_INCREMENT,"
                            + " name VARCHAR(50) UNIQUE, "
                            + " organization_id INT(5) NOT NULL, "
                            + "PRIMARY KEY(id), "
                            + "FOREIGN KEY (organization_id) REFERENCES organizations (id)); ");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS students "
                            + "(id INT(5) NOT NULL AUTO_INCREMENT,"
                            + " fullName VARCHAR(50) UNIQUE, "
                            + " birthday DATE NULL DEFAULT NULL, "
                            + " phoneNumber VARCHAR(50) NULL DEFAULT NULL UNIQUE, "
                            + " email VARCHAR(50) NULL DEFAULT NULL UNIQUE, "
                            + " group_id INT(5) NOT NULL, "
                            + "PRIMARY KEY(id), "
                            + "FOREIGN KEY (group_id) REFERENCES groups (id)); ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillTestDB(Connection connection) {
        StudentsDAO studentsDAO = new StudentsDAO();
        GroupsDAO groupsDAO = new GroupsDAO();
        OrganizationsDAO organizationsDAO = new OrganizationsDAO();
        Organization o1 = new Organization("ХПИ");
        Organization o2 = new Organization("Академия Шаг");
        organizationsDAO.create(connection, o1);
        organizationsDAO.create(connection, o2);

        Group g1 = new Group("КН-36а", organizationsDAO.findIdByName(connection, "ХПИ"));
        Group g2 = new Group("АГТ-35а", organizationsDAO.findIdByName(connection, "ХПИ"));
        Group g3 = new Group("КН-36б", organizationsDAO.findIdByName(connection, "Академия Шаг"));

        groupsDAO.create(connection, g1);
        groupsDAO.create(connection, g2);
        groupsDAO.create(connection, g3);

        String numbers1 = "+3804571289";
        String numbers2 = "+3804577301";
        String numbers3 = "+3804591623";
        String numbers4 = "+3804504814";
        String numbers5 = "+3804549233";
        String numbers6 = "+3804573389";
        String email1 = "football_player@email.com";
        String email2 = "i_love_york@email.com";
        String email3 = "belaya_zima@email.com";
        String email4 = "startrekforever@emai.com";
        String email5 = "rekolfa@emai.com";
        String email6 = "taiatov@emai.com";

        Student s1 = new Student("Першиков Андрей Георгиевич",
                Date.valueOf("1997-06-04"), numbers1, email1, groupsDAO.findIdByName(connection, "КН-36а"));
        Student s2 = new Student("Носиков Павел Николаевич",
                Date.valueOf("1996-08-22"), numbers2, email2, groupsDAO.findIdByName(connection, "КН-36а"));
        Student s3 = new Student("Хомякова Юлия Дмитриевна",
                Date.valueOf("1997-01-11"), numbers3, email3, groupsDAO.findIdByName(connection, "АГТ-35а"));
        Student s4 = new Student("Козаев Валерий Олегович",
                Date.valueOf("1995-03-09"), numbers4, email4, groupsDAO.findIdByName(connection, "АГТ-35а"));
        Student s5 = new Student("Дробышев Александр Сергеевич",
                Date.valueOf("1997-05-14"), numbers5, email5, groupsDAO.findIdByName(connection, "КН-36б"));
        Student s6 = new Student("Носков Николай Викторович",
                Date.valueOf("1997-09-01"), numbers6, email6, groupsDAO.findIdByName(connection, "КН-36б"));

        studentsDAO.create(connection, s1);
        studentsDAO.create(connection, s2);
        studentsDAO.create(connection, s3);
        studentsDAO.create(connection, s4);
        studentsDAO.create(connection, s5);
        studentsDAO.create(connection, s6);
    }

    public static void cleanDB(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    " DROP TABLE students; ");
            statement.executeUpdate(
                    " DROP TABLE groups; ");
            statement.executeUpdate(
                    " DROP TABLE organizations; ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Student> findAll(Connection connection, String name) {
        List<Student> list = new ArrayList<>();
        GroupsDAO groupsDAO = new GroupsDAO();
        int groupId = groupsDAO.findIdByName(connection, name);
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * " +
                             "FROM students WHERE group_id=?")) {

            preparedStatement.setInt(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Student(resultSet.getInt("id"),
                        resultSet.getString("fullName"),
                        resultSet.getDate("birthday"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"),
                        groupId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> findEntityBy(Connection connection, String param, String tg) {
        List<Student> list = new ArrayList<>();
        String query = "", like = "";
        switch (tg) {
            case "-n":
                query = "SELECT * " +
                        "FROM students WHERE fullName LIKE ?";
                like = "% " + param + " %";
                break;
            case "-s":
                query = "SELECT * " +
                        "FROM students WHERE fullName LIKE ?";
                like = param + " %";
                break;
            case "-p":
                query = "SELECT * " +
                        "FROM students WHERE phoneNumber =?";
                like = param;
                break;
            default:
                System.out.println("Не корректный ввод");
                return list;
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, like);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Student(resultSet.getInt("id"),
                        resultSet.getString("fullName"),
                        resultSet.getDate("birthday"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"),
                        resultSet.getInt("group_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void create(Connection connection, Student student) {
        if (!student.validation()) {
            return;
        }
        if (student.getGroupId() == -1) {
            System.out.println("Указанная для студента группа не существует," +
                    " пожалуйста создайте сначала группу");
            return;
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO " +
                             "students (fullName, birthday, phoneNumber, email, group_id) " +
                             "VALUES (?, ?, ?, ?, ?);")) {
            preparedStatement.setString(1, student.getFullName());
            preparedStatement.setDate(2, student.getBirthday());
            preparedStatement.setString(3, student.getPhoneNumber());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setInt(5, student.getGroupId());
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByName(Connection connection, String name) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM students " +
                             "WHERE fullName=?;")) {
            preparedStatement.setString(1, name);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByGroupId(Connection connection, int groupId) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM students " +
                             "WHERE group_id=?;")) {
            preparedStatement.setInt(1, groupId);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Connection connection, Student student, String name) {
        student.validation();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE students " +
                             "SET fullName=?, birthday=?, phoneNumber=?, email=?, group_id=? " +
                             "WHERE fullName=?;")) {
            preparedStatement.setString(1, student.getFullName());
            preparedStatement.setDate(2, student.getBirthday());
            preparedStatement.setString(3, student.getPhoneNumber());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setInt(5, student.getGroupId());
            preparedStatement.setString(6, name);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
