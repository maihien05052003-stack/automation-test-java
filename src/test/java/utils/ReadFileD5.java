package utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadFileD5 {
    private static final String FILE_NAME = "dataDay5.xlsx";

    // Đọc toàn bộ dòng dữ liệu, bỏ qua dòng tiêu đề.
    public static Object[][] readExcel() {
        try (InputStream input = ReadFileD5.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy " + FILE_NAME + " trong src/test/resources");
            }
            try (Workbook workbook = new XSSFWorkbook(input)) {
                Sheet sheet = workbook.getSheetAt(0);
                List<Object[]> rows = new ArrayList<>();
                DataFormatter formatter = new DataFormatter();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Object[] data = new Object[10];
                    for (int j = 0; j < 10; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        String value = cell == null ? "" : formatter.formatCellValue(cell).trim();
                        data[j] = value.isEmpty() ? null : value;
                    }
                    rows.add(data);
                }
                return rows.toArray(new Object[0][]);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc dữ liệu Excel: " + e.getMessage(), e);
        }
    }

    @DataProvider(name = "bookDemoData")
    public static Iterator<Object[]> bookDemoData() {
        List<Object[]> result = new ArrayList<>();
        for (Object[] row : readExcel()) result.add(row);
        return result.iterator();
    }

}
