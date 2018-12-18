package main.studentsDB;

import java.sql.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group {
    private int id;
    private String name;
    private int organizationId;

    public Group(int id, String name, int organizationId) {
            this.id = id;
            this.name = name;
            this.organizationId = organizationId;
    }

    public Group(String name, int organizationId) {
        this.name = name;
        this.organizationId = organizationId;
    }

    public Group(String name) {
            this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public static void printGroups(Connection connection, String orgName) {
        GroupsDAO groupsDAO = new GroupsDAO();
        List<Group> groups = groupsDAO.findAll(connection, orgName);
        System.out.printf("%-20s%n", "group");
        System.out.println("------------------------");
        for (Group g : groups) {
            System.out.printf("%s%n", g.getName());
        }
    }

    public boolean validation() throws StudentException {
        Pattern pattern = Pattern.compile("([a-zA-Zа-яА-Я]+)-([\\d]+).*");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches() || name.length() > 50) {
            throw new StudentException("Не корректное название группы");
        }
        return matcher.matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != group.id) return false;
        if (organizationId != group.organizationId) return false;
        return name != null ? name.equals(group.name) : group.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + organizationId;
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", organizationId=" + organizationId +
                '}';
    }
}
