package main.studentsDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrganizationsDAO {
    public List<Organization> findAll(Connection connection) {
        List<Organization> list = new ArrayList<Organization>();
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM organizations;");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                list.add(new Organization(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void create(Connection connection, Organization organization) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO " +
                             "organizations (name) " +
                             "VALUES (?);")) {
            preparedStatement.setString(1, organization.getName());
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int findIdByName(Connection connection, String name) {
        int id = -1;
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT id " +
                             "FROM organizations WHERE name=?")) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
