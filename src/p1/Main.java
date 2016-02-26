package p1;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main
{
 static String[] getDirFilesList()
	{
	 return getDirFilesList("");
	 }
 static String[] getDirFilesList(String dirpath)
	{
	 String result[];
	 File path;
	 if(dirpath.length()==0)
		 path= new File(".");
	 else
		{
		 path= new File(dirpath);
		 if(dirpath.charAt(dirpath.length()-1) != '\\')
			 dirpath+="\\";
		 }
	 String fileList[];
	 ArrayList<String> filter= new ArrayList<>();
	 fileList= path.list();
	 for(int i=0; i<fileList.length; i++)
		{
		 fileList[i]= dirpath + fileList[i];
		 if(fileList[i].contains(".dbf") )
			 filter.add(fileList[i] );
//		 System.out.println(fileList[i] );
		 }
//	 System.out.println();
	 result= filter.toArray(new String[filter.size() ] );
	 return result;
	 }
 static String dbfToXlsPath(String dbfPath)
	{
	 String result;
	 result= dbfPath.substring(0, dbfPath.lastIndexOf('.') ) +".xls";
	 return result;
	 }
 static Calendar calendarDateFromIntArr(int[] dateArr)
	{
	 Calendar result= Calendar.getInstance();
	 result.set(Calendar.DAY_OF_MONTH, dateArr[0] );
	 result.set(Calendar.MONTH, dateArr[1] -1 );
	 result.set(Calendar.YEAR, dateArr[2] +2000 );
	 return result;
	 }

 public static void main(String[] args) throws IOException
	{
//	 String fileadr="Исходники\\exp";
	 String fileadr="";
	 Scanner scan = new Scanner(System.in);
	 String fileList[];

	 if(args.length!=0)
		{
		 fileadr= args[0];
		 fileList= new String[]{fileadr};
		 }
	 else
		{
		 fileList= getDirFilesList(fileadr);
		 if(fileList.length==0)
			{
			 System.out.println(".dbf файлы не найдены");
			 return;
			 }
		 }
	 System.out.println("Итак, в нашем распоряжении такие файлы:");
	 for(int i=0; i<fileList.length; i++)
		 System.out.println( (i+1) +". "+ fileList[i] );
	 System.out.println("\nПоля, по которым будет проводиться выборка:");
	 System.out.println("DATKV");
	 System.out.println("DDOC");

	 boolean correctInput=false;
	 String inputDate;
	 Calendar requiredDate=null;
	 while(!correctInput)
		{
		 System.out.println("\nВведите дату в формате \"ДД.ММ.ГГ\":");
		 try
			{
			 inputDate= scan.next();
			 String strDateArr[]= inputDate.split("[.]");
			 if(strDateArr.length==3)
				{
				 int intDateArr[]= new int[3];
				 if(strDateArr[0].length()>2 || strDateArr[1].length()>2 || strDateArr[2].length()>2)
					 throw new Exception();
				 intDateArr[0]= Integer.parseInt(strDateArr[0] );
				 intDateArr[1]= Integer.parseInt(strDateArr[1] );
				 intDateArr[2]= Integer.parseInt(strDateArr[2] );
				 if(intDateArr[0]==0 || intDateArr[1]==0)
					 throw new Exception();
				 else
					{
					 requiredDate=calendarDateFromIntArr(intDateArr);
					 correctInput=true;
					 }
				 }
			 else
				 throw new Exception();
			 }
		 catch(Exception inputErr)
			{
			 scan.nextLine();
			 System.out.println("Некорректный ввод. Еще раз");
			 }
		 }

	 System.out.println("\nПишу в файлы...");
	 for(String path : fileList)
		{
		 DbfExtractor extractor= new DbfExtractor(path);
		 path= dbfToXlsPath(path);
		 System.out.println(path);
		 TreeSet<Integer> set= new TreeSet<>();
		 for(int i=0; i<extractor.n; i++)
			{
			 if(extractor.isRequiredValAt_Date(requiredDate, extractor.extractRecord(i), 1) )
				 set.add(i);
			 if(extractor.isRequiredValAt_Date(requiredDate, extractor.extractRecord(i), 11) )
				 set.add(i);
			 }
		 if(set.size()==0)
			{
			 System.out.println("Записей удовлетворяющих запросу не найдено\n");
			 continue;
			 }
		 else
			{
			 XlsWriter xlsWriter= new XlsWriter(extractor.headers.toStringNamesArr(), set.size(), path);
			 Integer records[]= new Integer[set.size() ];
			 records= set.toArray(records);
			 for(int i=0; i<set.size(); i++)
				 extractor.printRecordByIndex(xlsWriter,records[i],i);
			 xlsWriter.close();
			 }
		 }
	 System.out.println("Готово");
	 }
 }
