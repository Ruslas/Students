package main.studentsDB;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student {
    private int id;
    private String fullName;
    private Date birthday;
    private String phoneNumber;
    private String email;
    private int groupId;

    public Student(String fullName, Date birthday, String phoneNumber, String email, int groupId) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.groupId = groupId;
    }

    public Student(int id, String fullName, Date birthday, String phoneNumber, String email, int groupId) {
        this.id = id;
        this.fullName = fullName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.groupId = groupId;
    }

    public Student(String fullName, Date birthday, String phoneNumber, String email) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        try {
            validationName(fullName);
            this.fullName = fullName;
        } catch (StudentException e) {
            e.printStackTrace();
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        try {
            validationBirthday(birthday.toString());
            this.birthday = birthday;
        } catch (StudentException e) {
            e.printStackTrace();
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        try {
            validationNumber(phoneNumber);
            this.phoneNumber = phoneNumber;
        } catch (StudentException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static void printStudents(Connection connection, String name) {
        StudentsDAO studentsDAO = new StudentsDAO();
        List<Student> students = studentsDAO.findAll(connection, name);
        System.out.printf("%30s%15s\t%-20s%25s%n",
                "Full Name", "birthday", "phonenumber", "email");
        System.out.println("-------------------------------------------------" +
                "-------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%30s%15s\t%-20s%25s%n", s.getFullName(), s.getBirthday(), s.getPhoneNumber(), s.getEmail());
        }
    }

    public static void printStudentBy(Connection connection, String param, String tg) {
        StudentsDAO studentsDAO = new StudentsDAO();
        List<Student> students = studentsDAO.findEntityBy(connection, param, tg);
        System.out.printf("%30s%15s\t%-20s%25s%n",
                "Full Name", "birthday", "phonenumber", "email");
        System.out.println("-------------------------------------------------" +
                "-------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%30s%15s\t%-20s%25s%n", s.getFullName(), s.getBirthday(), s.getPhoneNumber(), s.getEmail());
        }
    }

    public static boolean validationName(String fullName) throws StudentException {
        Pattern pattern = Pattern.compile("([a-zA-Z-\\s]+)|([а-яА-Я-\\s]+)");
        Matcher matcher = pattern.matcher(fullName);
        if (!matcher.matches() || fullName.length() > 50) {
            throw new StudentException("Не корректное имя студента");
        }
        return matcher.matches();
    }

    public static boolean validationNumber(String phoneNumber) throws StudentException {
        Pattern pattern = Pattern.compile("\\+?[\\d]{7,11}");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new StudentException("Не корректное имя студента");
        }
        return matcher.matches();
    }

    public static boolean validationBirthday(String birthday) throws StudentException {
        Pattern pattern = Pattern.compile("((19[\\d]{2})|(20[0-2][\\d]))-" +
                "((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))");
        Matcher matcher = pattern.matcher(birthday);
        if (!matcher.matches()) {
            throw new StudentException("Не корректный день рождения студента");
        }
        return matcher.matches();
    }

    public boolean validation() {
        try {
            validationName(fullName);
            validationNumber(phoneNumber);
            validationBirthday(birthday.toString());
        } catch (StudentException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (groupId != student.groupId) return false;
        if (fullName != null ? !fullName.equals(student.fullName) : student.fullName != null) return false;
        if (birthday != null ? !birthday.equals(student.birthday) : student.birthday != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(student.phoneNumber) : student.phoneNumber != null) return false;
        return email != null ? email.equals(student.email) : student.email == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + groupId;
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", birthday=" + birthday +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
