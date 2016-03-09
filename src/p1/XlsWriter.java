package p1;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by snayper on 08.02.2016.
 */
public class XlsWriter
	{
	 HSSFWorkbook xlsDoc;
	 Sheet sheet;
	 String xlsFilePath;

	 public XlsWriter(String path)
		{
		 xlsDoc= new HSSFWorkbook();
		 sheet= xlsDoc.createSheet("Лист 1");
		 xlsFilePath=path;
		 }
	 public XlsWriter(String[] headers,int n,String path)
		{
		 xlsDoc= new HSSFWorkbook();
		 sheet= xlsDoc.createSheet("Лист 1");
		 init(headers,n);
		 xlsFilePath=path;
		 }

	 void init(String[] headers,int n)
		{
		 HSSFCellStyle headerLeftCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle headerRightCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle headerCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle leftCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle rightCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle leftBottomCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle rightBottomCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle bottomCellStyle= xlsDoc.createCellStyle();
		 HSSFCellStyle centerCellStyle= xlsDoc.createCellStyle();
		 setBorder(headerLeftCellStyle, 6,1,6,2);
		 setBorder(headerRightCellStyle, 1,6,6,2);
		 setBorder(headerCellStyle, 1,1,6,2);
		 setBorder(leftCellStyle, 6,1,-1,1);
		 setBorder(rightCellStyle, 1,6,-1,1);
		 setBorder(leftBottomCellStyle, 6,1,-1,6);
		 setBorder(rightBottomCellStyle, 1,6,-1,6);
		 setBorder(bottomCellStyle, 1,1,-1,6);
		 setBorder(centerCellStyle, 1,1,-1,1);
		 headerLeftCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		 headerLeftCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 headerRightCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		 headerRightCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		 headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//закончил задавать стили
		 int l=headers.length;
//создаю, окрашиваю и заполняю строку заголовков
		 Row headersRow= sheet.createRow(0);
		 Cell headersCell[]= new Cell[l];
		 for(int j=0; j<l; j++)
			{
			 headersCell[j]= headersRow.createCell(j);
			 headersCell[j].setCellStyle(headerCellStyle);
			 headersCell[j].setCellValue(headers[j] );
			 }
		 headersCell[0].setCellStyle(headerLeftCellStyle);
		 headersCell[l-1].setCellStyle(headerRightCellStyle);
//создаю остальные строки, заполняя стандартным стилем
		 Cell dataCell[][]= new Cell[n][];
		 for(int i=0; i<n; i++)
			{
			 Row row= sheet.createRow(i+1);
			 dataCell[i]= new Cell[l];
			 for(int j=0; j<l; j++)
				{
				 dataCell[i][j]= row.createCell(j);
				 dataCell[i][j].setCellStyle(centerCellStyle);
				 }
			 }
//теперь корректирую стили в зависимости от расположения в таблице
		 if(n==0)
			{
			 setBorder(headerCellStyle, -1,-1,-1,6);
			 setBorder(headerLeftCellStyle, -1,-1,-1,6);
			 setBorder(headerRightCellStyle, -1,-1,-1,6);
			 }
		 else
			{
//рисую правый и левый борт
			 for(int i=0; i<n-1; i++)
				{
				 dataCell[i][0].setCellStyle(leftCellStyle);
				 dataCell[i][l-1].setCellStyle(rightCellStyle);
				 }
//рисую пол с углами
			 int i=n-1;
			 for(int j=0; j<l; j++)
				 dataCell[i][j].setCellStyle(bottomCellStyle);
			 dataCell[i][0].setCellStyle(leftBottomCellStyle);
			 dataCell[i][l-1].setCellStyle(rightBottomCellStyle);
			 }
		 for(int i=0; i<n; i++)
			{
			 writeAs_double( (i+1), i,0);
			 writeAs_double(DbfExtractor.EMPTY_NUM, i,14);
			 }
		 }
	 public void setBorder(HSSFCellStyle style, int borderL, int borderR, int borderT, int borderB)
		{
		 if(borderB>=0)
			 style.setBorderBottom( (short)borderB);
		 if(borderL>=0)
			 style.setBorderLeft( (short)borderL);
		 if(borderR>=0)
			 style.setBorderRight( (short)borderR);
		 if(borderT>=0)
			 style.setBorderTop( (short)borderT);
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
		 if(data==DbfExtractor.EMPTY_NUM)
			 cell.setCellValue( (String)null);
		 else
			 cell.setCellValue(data);
		 sheet.autoSizeColumn(j);
		 }
	 public void writeAs_Date(Date data,int i,int j)
		{
		 Row row= sheet.getRow(i+1);
		 Cell cell= row.getCell(j);
		 HSSFCellStyle style= xlsDoc.createCellStyle();
		 style.cloneStyleFrom(cell.getCellStyle() );
		 cell.setCellStyle(style);
		 DataFormat dataFormat= xlsDoc.createDataFormat();
		 style.setDataFormat(dataFormat.getFormat("dd.mm.yy") );
		 cell.setCellValue(data);
		 sheet.autoSizeColumn(j);
		 }
	 public void close() throws IOException
		{
//		 xlsDoc.write(new FileOutputStream("c:\\c123\\dbf\\Result.xls") );
		 xlsDoc.write(new FileOutputStream(xlsFilePath) );
		 xlsDoc.close();
		 }

	 @SuppressWarnings("deprecation")
	 void test() throws IOException
		{
		 writeAs_double(227d, 0,0);
		 writeAs_String("0s0", 1,2);
		 writeAs_String("s0s", 2,1);
		 writeAs_Date(new Date(114,9,21), 3,0);
		 writeAs_double(0.343d, 4,4);
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
		 close();
		 }

	 @SuppressWarnings("deprecation")
	 public static void main(String[] args) throws IOException
		{
//		 XlsWriter x= new XlsWriter("c:\\c123\\dbf\\test.xls");
//		 String headers[]= {"abc", "def", "12333", "4", "678905"};
//		 x.init(headers, 5);
//		 x.test();
		 Calendar x= Calendar.getInstance();
//		 x.setTime(new Date(114,11,21) );
		 x.set(Calendar.YEAR,2014);
		 x.set(Calendar.MONTH,-1);
		 x.set(Calendar.DAY_OF_MONTH,21);
		 System.out.println("Year:\t"+ x.get(Calendar.YEAR) );
		 System.out.println("Month:\t"+ (x.get(Calendar.MONTH)+1) );
		 System.out.println("Day:\t"+ x.get(Calendar.DAY_OF_MONTH) );
		 }
	 }
