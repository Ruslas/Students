package main.studentsDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupsDAO {
    public List<Group> findAll(Connection connection, String orgName) {
        List<Group> list = new ArrayList<>();
        OrganizationsDAO organizationsDAO = new OrganizationsDAO();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * " +
                             "FROM groups WHERE organization_id=?")) {

            preparedStatement.setInt(1, organizationsDAO.findIdByName(connection, orgName));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int organizationId = resultSet.getInt("organization_id");
                list.add(new Group(id, name, organizationId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void create(Connection connection, Group group) {
        try {
            group.validation();
        } catch (StudentException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (group.getOrganizationId() == -1) {
            System.out.println("Указанная организация не существует," +
                    " пожалуйста создайте сначала организацию");
            return;
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO " +
                             "groups (name, organization_id) " +
                             "VALUES (?, ?);")) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setInt(2, group.getOrganizationId());
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection connection, String name) {
        GroupsDAO groupsDAO = new GroupsDAO();
        StudentsDAO studentsDAO = new StudentsDAO();
        studentsDAO.deleteByGroupId(connection, groupsDAO.findIdByName(connection, name));
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM groups " +
                             "WHERE name=?;")) {
            preparedStatement.setString(1, name);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Connection connection, String oldName, Group group) {
        try {
            group.validation();
        } catch (StudentException e) {
            e.printStackTrace();
            return;
        }
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE groups " +
                             "SET name=? " +
                             "WHERE name=?;")) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setString(2, oldName);
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
                             "FROM groups WHERE name=?")) {

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
