package main.studentsDB;

import java.sql.*;
import java.util.List;

public class Organization {
    private int id;
    private String name;

    public Organization(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Organization(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void printOrganizations(Connection connection) {
        OrganizationsDAO organizationsDAO = new OrganizationsDAO();
        List<Organization> organizations = organizationsDAO.findAll(connection);
        System.out.printf("%-20s%n", "organization");
        System.out.println("------------------------");
        for (Organization o : organizations) {
            System.out.printf("%s%n", o.getName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (id != that.id) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
