/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.unesco.appesca.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author yesus
 */
public class CSVToExcel {

    public static ByteArrayOutputStream convert(ArrayList<String> linhasCSV) throws FileNotFoundException {
        ArrayList<ArrayList<String>> allRowAndColData = null;
        ArrayList<String> oneRowData = null;
//        String fName = "C:\\input.csv";
//        String currentLine;
//        FileInputStream fis = new FileInputStream(fName);
//        DataInputStream myInput = new DataInputStream(fis);
//        int i = 0;

        allRowAndColData = new ArrayList<>();
        
        for(String linha: linhasCSV){
            oneRowData = new ArrayList<>();
            String oneRowArray[] = linha.split(";");
            for (int j = 0; j < oneRowArray.length; j++) {
                oneRowData.add(oneRowArray[j]);
            }
            
            allRowAndColData.add(oneRowData);
        }
        
//        while ((currentLine = myInput.readLine()) != null) {
//            oneRowData = new ArrayList<String>();
//            String oneRowArray[] = currentLine.split(";");
//            for (int j = 0; j < oneRowArray.length; j++) {
//                oneRowData.add(oneRowArray[j]);
//            }
//            allRowAndColData.add(oneRowData);
//            System.out.println();
//            i++;
//        }

        try {
            XSSFWorkbook workBook = new XSSFWorkbook();
            Sheet sheet = workBook.createSheet("exportacao");
            for (int i = 0; i < allRowAndColData.size(); i++) {
                ArrayList<?> ardata = (ArrayList<?>) allRowAndColData.get(i);
                Row row = sheet.createRow((short) 0 + i);
                for (int k = 0; k < ardata.size(); k++) {
                    System.out.print(ardata.get(k));
                    Cell cell = row.createCell((short) k);
                    cell.setCellValue(ardata.get(k).toString().replace("\"", ""));
                }
//                System.out.println();
            }
            
//            FileOutputStream fileOutputStream = new FileOutputStream("C:\\outputFile.xls");
           
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            workBook.write(bos);
            
            return bos;
          
//            fileOutputStream.close();
        } catch (IOException ex) {
        }
        
        return null;
    }
}
