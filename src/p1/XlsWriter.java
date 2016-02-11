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
	 HSSFWorkbook xlsDoc;
	 Sheet sheet;

	 public XlsWriter()
		{
		 xlsDoc= new HSSFWorkbook();
		 sheet= xlsDoc.createSheet("Test");
		 }
	 public XlsWriter(String[] headers,int n)
		{
		 xlsDoc= new HSSFWorkbook();
		 sheet= xlsDoc.createSheet("Test");
		 init(headers,n);
		 }
	 void init(String[] headers,int n)
		{
		 int l=headers.length;
//создаю, окрашиваю и заполняю строку заголовков
		 Row headersRow= sheet.createRow(0);
		 Cell headersCell[]= new Cell[l];
		 for(int j=0; j<l; j++)
			{
			 headersCell[j]= headersRow.createCell(j);
			 headersCell[j].setCellStyle( xlsDoc.createCellStyle() );
			 setBackgroundColor(headersCell[j], IndexedColors.YELLOW.index);
			 headersCell[j].setCellValue(headers[j] );
			 }
//создаю остальные строки
		 Cell dataCell[][]= new Cell[n][];
		 for(int i=0; i<n; i++)
			{
			 Row row= sheet.createRow(i+1);
			 dataCell[i]= new Cell[l];
			 for(int j=0; j<l; j++)
				{
				 dataCell[i][j]= row.createCell(j);
				 dataCell[i][j].setCellStyle( xlsDoc.createCellStyle() );
				 }
			 }
//рисую пол, потолок и линию под заголовками
		 for(int j=0; j<l; j++)
			{
			 setBorder(headersCell[j], -1,-1,6,2);
			 if(n==0)
				 setBorder(headersCell[j], -1,-1,-1,6);
			 else
				 setBorder(dataCell[n-1][j], -1,-1,-1,6);
			 }
//рисую правый и левый борт
		 setBorder(headersCell[0], 6,-1,-1,-1);
		 setBorder(headersCell[l-1], -1,6,-1,-1);
		 for(int i=0; i<n; i++)
			{
			 setBorder(dataCell[i][0], 6,-1,-1,-1);
			 setBorder(dataCell[i][l-1], -1,6,-1,-1);
			 }
//рисую горизонтальные границы
		{
		 int i=1;
		 while(i<n)
			{
			 for(int j=0; j<l; j++)
				 setBorder(dataCell[i][j], -1,-1,1,-1);
			 i++;
			 }
		 }
//рисую вертикальные границы
		 for(int j=1; j<l; j++)
			{
			 setBorder(headersCell[j], 1,-1,-1,-1);
			 int i=0;
			 while(i<n)
				{
				 setBorder(dataCell[i][j], 1,-1,-1,-1);
				 i++;
				 }
			 }
		 }
	 public void setBackgroundColor(Cell cell, short colorIndex)
		{
		 CellStyle colorStile =cell.getCellStyle();
		 colorStile.setFillForegroundColor(colorIndex);
		 colorStile.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 cell.setCellStyle(colorStile);
		 }
//	 public static void setBorder(Workbook wb, Cell cell, CellStyle desiredStile, int borderL, int borderR, int borderT, int borderB)
	 public void setBorder(Cell cell, int borderL, int borderR, int borderT, int borderB)
		{
		 CellStyle style= cell.getCellStyle();
//		 if(desiredStile==null)
//			 style= wb.createCellStyle();
//		 else
//			 style=desiredStile;
		 if(borderB>=0)
			 style.setBorderBottom( (short)borderB);
		 if(borderL>=0)
			 style.setBorderLeft( (short)borderL);
		 if(borderR>=0)
			 style.setBorderRight( (short)borderR);
		 if(borderT>=0)
			 style.setBorderTop( (short)borderT);
		 cell.setCellStyle(style);
		 }
	 public void writeAs_String(String data,int i,int j)
		{
		 Row row= sheet.getRow(i+1);
		 Cell cell= row.getCell(j);
		 cell.setCellValue(data);
		 sheet.autoSizeColumn(j);
		 }
	 public void writeAs_double(double data,int i,int j)
		{
		 Row row= sheet.getRow(i+1);
		 Cell cell= row.getCell(j);
		 cell.setCellValue(data);
		 sheet.autoSizeColumn(j);
		 }
	 public void writeAs_Date(Date data,int i,int j)
		{
		 Row row= sheet.getRow(i+1);
		 Cell cell= row.getCell(j);
		 CellStyle style= cell.getCellStyle();
		 DataFormat dataFormat= xlsDoc.createDataFormat();
		 style.setDataFormat(dataFormat.getFormat("dd.mm.yy") );
		 cell.setCellValue(data);
		 sheet.autoSizeColumn(j);
		 }
	 public void close() throws IOException
		{
//		 xlsDoc.write(new FileOutputStream("c:\\c123\\dbf\\Result.xls") );
		 xlsDoc.write(new FileOutputStream("Result.xls") );
		 xlsDoc.close();
		 }

	 @SuppressWarnings("deprecation")
	 void test() throws IOException
		{
		 writeAs_String("0s0", 1,2);
		 writeAs_String("s0s", 2,1);
		 writeAs_double(227d, 0,0);
		 writeAs_double(0.343d, 4,4);
		 writeAs_Date(new Date(114,9,21), 3,0);
/*/
		 Row row= sheet.createRow(0);
		 Cell cell[]= new Cell[3];
		 cell[0]= row.createCell(0);
		 cell[1]= row.createCell(1);
		 cell[2]= row.createCell(2);

		 cell[0].setCellValue(50.6285d);
		 cell[1].setCellValue("String\t123\t22");

		 DataFormat dataFormat= xlsDoc.createDataFormat();
		 CellStyle dateStyle= xlsDoc.createCellStyle();
		 dateStyle.setDataFormat(dataFormat.getFormat("dd.mm.yy") );
		 sheet.autoSizeColumn(1);
		 sheet.autoSizeColumn(2);
//		 setBorder(cell[0], 6,2,6,6);
//		 setBorder(cell[1], 2,5,6,6);
//		 setBorder(cell[2], 5,6,6,6);

		 setBorder(cell[0], 6,2,6,6);
		 setBorder(cell[1], 2,5,6,6);
		 setBorder(cell[2], 5,6,6,6);
//		 setBorder(xlsDoc,cell[0],null, 6,2,6,6);
//		 setBorder(xlsDoc,cell[1],null, 2,5,6,6);
//		 setBorder(xlsDoc,cell[2],dateStyle, 5,6,6,6);
		 cell[2].setCellValue(new Date(114,9,21) );
/*/
		 xlsDoc.write(new FileOutputStream("c:\\c123\\dbf\\test.xls") );
		 xlsDoc.close();
		 }

	 public static void main(String[] args) throws IOException
		{
		 XlsWriter x= new XlsWriter();
		 String headers[]= {"abc", "def", "12333", "4", "678905"};
		 x.init(headers, 5);
		 x.test();
		 }
	 }
