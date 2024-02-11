package ustin.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ustin.models.Person;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ReadExcelFileToList {
    private final Logger logger = LoggerFactory.getLogger(ReadExcelFileToList.class);

    public List<Person> personList = new ArrayList<>();

    public List<Person> readExcelData(String fileName) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(fileName); // поток для чтения файла

            Workbook workbook = null; // создание книги эксель

            if (fileName.toLowerCase().endsWith("xlsx")) {     // если имеет новое расширение
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {   // если имеет старое расширение
                workbook = new HSSFWorkbook(fis);
            }
            int numOfSheets = 0;
            if (workbook != null) {
                numOfSheets = workbook.getNumberOfSheets();  // получаем кол-во страниц в книге
            }

            for (int i = 0; i < numOfSheets; i++) {    // проходим по всем страницам
                Sheet sheet = workbook.getSheetAt(i);   // получаем лист

                Iterator<Row> rowIterator = sheet.iterator();  // итерируемся по всем строкам
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();   // получаем строку
                    if (row.getRowNum() != 0 && row.getRowNum() < 10) {    // выбор строк с данными
                        Person person = new Person();

                        Iterator<Cell> cellIterator = row.cellIterator();  // итерируемся по всем ячейкам

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();  // получаем ячейку

                            switch (cell.getColumnIndex()) {    // получаем индекс столбца
                                case 0 -> person.setNum(cell.getNumericCellValue());
                                case 1 -> person.setFirstName(cell.getStringCellValue());
                                case 2 -> person.setLastName(cell.getStringCellValue());
                                case 3 -> person.setGender(cell.getStringCellValue());
                                case 4 -> person.setCountry(cell.getStringCellValue());
                                case 5 -> person.setAge(cell.getNumericCellValue());
                                case 6 -> person.setDate(cell.getStringCellValue());
                                case 7 -> person.setId(cell.getNumericCellValue());
                            }
                        }
                        personList.add(person);
                    }
                }
            }
            fis.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        return personList;
    }
}
