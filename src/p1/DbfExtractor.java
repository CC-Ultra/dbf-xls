package p1;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by snayper on 04.02.2016.
 */
public class DbfExtractor
	{
	 String charset;
	 String filepath;
	 Headers headers;
	 int n;

	 @SuppressWarnings("deprecation")
	 public DbfExtractor(String _filepath)
		{
		 filepath=_filepath;
		 try
			{
			 DBFReader dbfIn= new DBFReader(new FileInputStream(filepath) );
			 n= dbfIn.getRecordCount();
			 charset= dbfIn.getCharactersetName();
			 headers= new Headers(filepath);
			 }
		 catch(IOException nullHeaders)
			{
			 System.out.println("Не удалось получить заголовки для "+ filepath);
			 }
		 }
	 public Object[] extractRecord(int index) throws IOException
		{
		 Object result[]= null;
		 DBFReader dbfIn= new DBFReader(new FileInputStream(filepath) );
		 if(index<0 || index>=n)
			{
			 System.out.println("Плохой индекс: "+ index);
			 return result;
			 }
		 for(int i=0; i<index; i++)
			 result= dbfIn.nextRecord();
		 result= dbfIn.nextRecord();
		 return result;
		 }
	 public double extractFieldAs_double(Object[] record,int position)
		{
		 double result=-50000000d;
		 if(record[position]!=null)
			 result= (double)record[position];
		 return result;
		 }
	 public String extractFieldAs_String(Object[] record,int position)
		{
		 String result="";
		 result= (String)record[position];
		 try
			{
			 if(record[position]!=null)
				 result= new String(result.getBytes(charset), "windows-1251");
			 }
		 catch(IOException charsetErr)
			{
			 System.out.println("charset не поддерживатся");
			 }
		 return result;
		 }
	 public Date extractFieldAs_Date(Object[] record,int position)
		{
		 Date result=null;
		 if(record[position]!=null)
			 result= (Date)record[position];
		 return result;
		 }
	 public void printRecord(XlsWriter xlsWriter, Object[] record, int xlsIndex) throws IOException
		{
		 for(int j=0; j<record.length; j++)
			{
			 char fieldType= (char)headers.fields[j].getType().getCode();
			 switch(fieldType)
				{
				 case 'C':
					 xlsWriter.writeAs_String(extractFieldAs_String(record,j).trim(), xlsIndex, j);
					 break;
				 case 'N':
					 xlsWriter.writeAs_double(extractFieldAs_double(record,j), xlsIndex, j);
					 break;
				 case 'D':
					 Date data= extractFieldAs_Date(record,j);
					 if(data!=null)
						 xlsWriter.writeAs_Date(data, xlsIndex, j);
					 break;
				 default:
					 xlsWriter.writeAs_String(record[j].toString(), xlsIndex, j);
				 }
			 }
		 }
	 public void printRecordByIndex(XlsWriter xlsWriter, int dbfIndex, int xlsIndex) throws IOException
		{
		 Object record[]= extractRecord(dbfIndex);
		 if(record==null)
			{
			 System.out.println("Не удалось прочесть запись "+ dbfIndex +". Внезапный конец файла. Возможно файл поврежден");
			 return;
			 }
		 printRecord(xlsWriter,record,xlsIndex);
		 }
	 public void printN(XlsWriter xlsWriter,int nRecords) throws IOException
		{
		 if(nRecords>=n)
			{
			 printAll(xlsWriter);
			 return;
			 }
		 for(int i=0; i<nRecords; i++)
			 printRecordByIndex(xlsWriter,i,i);
		 }
	 public void printAll(XlsWriter xlsWriter) throws IOException
		{
		 DBFReader dbfIn= new DBFReader(new FileInputStream(filepath) );
		 for(int i=0; i<n; i++)
			{
			 Object record[]= dbfIn.nextRecord();
			 if(record==null)
				{
				 System.out.println("Не удалось прочесть запись "+ i +". Внезапный конец файла. Возможно файл поврежден");
				 xlsWriter.close();
				 return;
				 }
			 printRecord(xlsWriter,record,i);
			 }
		 }
	 }
