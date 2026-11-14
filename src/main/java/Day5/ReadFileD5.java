package Day5;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReadFileD5 {
    public static List<Map<String, String>> readExcelData(String filePath, String sheetName) {

        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheet(sheetName);

            // Đọc dòng tiêu đề
            Row headerRow = sheet.getRow(0);

            DataFormatter formatter = new DataFormatter();

            // Đọc từng dòng dữ liệu, bỏ qua dòng tiêu đề
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                Map<String, String> data = new LinkedHashMap<>();

                // Đọc từng cột
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {

                    String columnName = formatter.formatCellValue(headerRow.getCell(j));
                    String cellValue = formatter.formatCellValue(row.getCell(j));

                    data.put(columnName, cellValue);
                }

                dataList.add(data);
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
