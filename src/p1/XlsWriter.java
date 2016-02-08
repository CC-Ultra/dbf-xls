package p1;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by snayper on 08.02.2016.
 */
public class XlsWriter
	{
	 @SuppressWarnings("deprecation")
	 static void test() throws IOException
		{
		 HSSFWorkbook xlsDoc= new HSSFWorkbook();
		 Sheet sheet= xlsDoc.createSheet("Test");
		 Row row= sheet.createRow(0);
		 Cell cell[]= new Cell[3];
		 cell[0]= row.createCell(0);
		 cell[1]= row.createCell(1);
		 cell[2]= row.createCell(2);

		 cell[0].setCellValue(50.6285d);
//		 cell[0].setCellValue(12.7d);
		 cell[1].setCellValue("String\t123\t22");

		 DataFormat dataFormat= xlsDoc.createDataFormat();
		 CellStyle dateStyle= xlsDoc.createCellStyle();
		 dateStyle.setDataFormat(dataFormat.getFormat("dd.mm.yy") );
		 sheet.autoSizeColumn(1);
		 sheet.autoSizeColumn(2);
		 setBorder(xlsDoc,cell[0],null, 6,2,6,6);
		 setBorder(xlsDoc,cell[1],null, 2,5,6,6);
		 setBorder(xlsDoc,cell[2],dateStyle, 5,6,6,6);
		 cell[2].setCellValue(new Date(114,9,21) );

		 xlsDoc.write(new FileOutputStream("c:\\c123\\dbf\\test.xls") );
		 xlsDoc.close();
		 }
	 public static void setBorder(Workbook wb, Cell cell, CellStyle desiredStile, int borderL, int borderR, int borderT, int borderB)
		{
		 CellStyle style;
		 if(desiredStile==null)
			 style= wb.createCellStyle();
		 else
		 	 style=desiredStile;
		 style.setBorderBottom( (short)borderB);
		 style.setBorderLeft( (short)borderL);
		 style.setBorderRight( (short)borderR);
		 style.setBorderTop( (short)borderT);
		 cell.setCellStyle(style);
		 }
	 public static void main(String[] args) throws IOException
		{
		 test();
		 }
	 }
