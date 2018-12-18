package main.studentsDB;

import java.sql.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ReadExcel {
    public static void importExcel(Connection connection, String pass) throws IOException {

        FileInputStream inputStream = new FileInputStream(new File(pass));

        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        readOrg(connection, workbook);

        readGroup(connection, workbook);

        readStudent(connection, workbook);

        inputStream.close();
    }

    private static void readStudent(Connection connection, HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.getSheetAt(2);

        Iterator<Row> rowIterator = sheet.iterator();
        boolean title = true;
        while (rowIterator.hasNext()) {
            if (title) {
                rowIterator.next();
            }
            title = false;
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getCell(0).getNumericCellValue() == 0) {
                break;
            }
            String fullName = "";
            int id = 0;
            String birthday = "1900-01-01";
            String phoneNumber = "";
            int phoneNumberInt = 0;
            String email = "";
            int group_id = 0;
            int i = 0;

            while (cellIterator.hasNext() && i < 6) {
                Cell cell = cellIterator.next();

                switch (i) {
                    case 0:
                        id = (int) cell.getNumericCellValue();
                        break;
                    case 1:
                        fullName = cell.getStringCellValue();
                        break;
                    case 2:
                        birthday = cell.getStringCellValue();
                        break;
                    case 3: {
                        if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                            phoneNumber = cell.getStringCellValue();
                        } else {
                            phoneNumberInt = (int) cell.getNumericCellValue();
                        }
                    }
                    break;
                    case 4:
                        email = cell.getStringCellValue();
                        break;
                    case 5:
                        group_id = (int) cell.getNumericCellValue();
                        break;
                }
                i++;
            }
            Date birth = null;
            try {
                birth = Date.valueOf(birthday);
            } catch (Exception e) {
            }

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement("INSERT INTO " +
                                 "students (fullName, birthday, phoneNumber, email, group_id) " +
                                 "VALUES (?, ?, ?, ?, ?);")) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setDate(2, birth);
                if (phoneNumberInt == 0) {
                    preparedStatement.setString(3, phoneNumber);
                } else {
                    preparedStatement.setInt(3, phoneNumberInt);
                }
                preparedStatement.setString(4, email);
                preparedStatement.setInt(5, group_id);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void readGroup(Connection connection, HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.getSheetAt(1);

        Iterator<Row> rowIterator = sheet.iterator();
        boolean title = true;
        while (rowIterator.hasNext()) {
            if (title) {
                rowIterator.next();
            }
            title = false;
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getCell(0).getNumericCellValue() == 0) {
                break;
            }
            String name = "";
            int id = 0;
            int orgID = 0;
            int i = 0;

            while (cellIterator.hasNext() && i < 3) {
                Cell cell = cellIterator.next();

                switch (i) {
                    case 0:
                        id = (int) cell.getNumericCellValue();
                        break;
                    case 1:
                        name = cell.getStringCellValue();
                        break;
                    case 2:
                        orgID = (int) cell.getNumericCellValue();
                        break;
                }
                i++;
            }

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement("INSERT INTO " +
                                 "groups (name, organization_id) " +
                                 "VALUES (?, ?);")) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, orgID);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void readOrg(Connection connection, HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        boolean title = true;
        while (rowIterator.hasNext()) {
            if (title) {
                rowIterator.next();
            }
            title = false;
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getCell(0).getNumericCellValue() == 0) {
                break;
            }
            String name = "";
            int id = 0;
            int i = 0;

            while (cellIterator.hasNext() && i < 2) {
                Cell cell = cellIterator.next();

                switch (i) {
                    case 0:
                        id = (int) cell.getNumericCellValue();
                        break;
                    case 1:
                        name = cell.getStringCellValue();
                        break;
                }
                i++;
            }
            try (PreparedStatement preparedStatement =
                         connection.prepareStatement("INSERT INTO " +
                                 "organizations (name) " +
                                 "VALUES (?);")) {
                preparedStatement.setString(1, name);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
