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
		 double result=0;
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
	 public void printRecord(int index) throws IOException
		{
		 Object record[]= extractRecord(index);
		 if(record==null)
			{
			 System.out.println("Не удалось прочесть запись. Внезапный конец файла");
			 return;
			 }
		 System.out.println("Запись "+ index +":");
		 for(int i=0; i<record.length; i++)
			{
			 System.out.print(headers.fields[i].getName() +": ");
			 char fieldType= (char)headers.fields[i].getType().getCode();
			 switch(fieldType)
				{
				 case 'C':
					 System.out.println(extractFieldAs_String(record,i) );
					 break;
				 case 'N':
					 System.out.println(extractFieldAs_double(record,i) );
					 break;
				 case 'D':
					 System.out.println(extractFieldAs_Date(record,i) );
					 break;
				 default:
					 System.out.println(record[i].toString() );
				 }
			 }
		 System.out.println();
		 }
	 }
