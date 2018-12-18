package main.studentsDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

public class CreateExcel {
    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static HSSFCellStyle createStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    public static void exportToExcel(Connection connection) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Organizations sheet");

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);
        HSSFCellStyle style2 = createStyle(workbook);

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue("id");
        cell.setCellStyle(style);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("name");
        cell.setCellStyle(style);


        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM organizations;");
            while (resultSet.next()) {
                rownum++;
                row = sheet.createRow(rownum);
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(id);
                cell.setCellStyle(style2);

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(name);
                cell.setCellStyle(style2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HSSFSheet sheet2 = workbook.createSheet("Groups sheet");

        rownum = 0;

        row = sheet2.createRow(rownum);

        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue("id");
        cell.setCellStyle(style);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("name");
        cell.setCellStyle(style);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("organization_id");
        cell.setCellStyle(style);


        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM groups;");
            while (resultSet.next()) {
                rownum++;
                row = sheet2.createRow(rownum);
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int organization_id = resultSet.getInt("organization_id");

                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(id);
                cell.setCellStyle(style2);

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(name);
                cell.setCellStyle(style2);

                cell = row.createCell(2, CellType.NUMERIC);
                cell.setCellValue(organization_id);
                cell.setCellStyle(style2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HSSFSheet sheet3 = workbook.createSheet("Students sheet");

        rownum = 0;

        row = sheet3.createRow(rownum);

        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue("id");
        cell.setCellStyle(style);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("fullName");
        cell.setCellStyle(style);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("birthday");
        cell.setCellStyle(style);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("phoneNumber");
        cell.setCellStyle(style);

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("email");
        cell.setCellStyle(style);

        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue("group_id");
        cell.setCellStyle(style);

        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");
            while (resultSet.next()) {
                rownum++;
                row = sheet3.createRow(rownum);
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");
                String birthday = resultSet.getDate("birthday").toString();
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                int group_id = resultSet.getInt("group_id");

                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(id);
                cell.setCellStyle(style2);

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(fullName);
                cell.setCellStyle(style2);

                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(birthday);
                cell.setCellStyle(style2);

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(phoneNumber);
                cell.setCellStyle(style2);

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(email);
                cell.setCellStyle(style2);

                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(group_id);
                cell.setCellStyle(style2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sheet.autoSizeColumn(1);
        sheet2.autoSizeColumn(1);
        sheet3.autoSizeColumn(1);
        sheet3.autoSizeColumn(2);
        sheet3.autoSizeColumn(3);
        sheet3.autoSizeColumn(4);
        sheet3.autoSizeColumn(5);

        File file = new File("D:\\Students\\students.xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
        outFile.close();
    }
}
