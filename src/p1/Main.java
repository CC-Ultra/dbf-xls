package p1;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main
{
 static String getch()
	{
	 Scanner scan = new Scanner(System.in);
	 return scan.next();
	 }
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

 public static void main(String[] args) throws IOException
	{
//	 String fileadr="Исходники\\exp";
	 String fileadr="";
	 Scanner scan = new Scanner(System.in);

	 if(args.length!=0)
		{
		 fileadr= args[0];
		 DbfExtractor extractor= new DbfExtractor(fileadr);
		 fileadr= dbfToXlsPath(fileadr);
		 XlsWriter xlsWriter= new XlsWriter(extractor.headers.toStringNamesArr(), extractor.n, fileadr);
		 System.out.println("\nПишу в файл");
		 extractor.printAll(xlsWriter);
		 xlsWriter.close();
		 System.out.println("Готово");
		 return;
		 }

	 String fileList[]= getDirFilesList(fileadr);
	 if(fileList.length==0)
		{
		 System.out.println(".dbf файлы не найдены");
		 return;
		 }

	 System.out.println("Итак, в нашем распоряжении такие файлы:");
	 for(int i=0; i<fileList.length; i++)
		 System.out.println( (i+1) +". "+ fileList[i] );
	 boolean correctInput=false;
	 int n=0;
	 while(!correctInput)
		{
		 System.out.println("\nВыберите сколько из них читать (или 0, если все):");
		 try
			{
			 n= scan.nextInt();
			 if(n>=0 && n<=fileList.length)
				 correctInput=true;
			 else
				 throw new Exception();
			 }
		 catch(Exception inputErr)
			{
			 scan.nextLine();
			 System.out.println("Некорректный ввод. Еще раз");
			 }
		 }

	 if(n==0)
		{
		 System.out.println("\nПишу в файлы...");
		 n=fileList.length;
		 for(int i=0; i<n; i++)
			{
			 fileadr=fileList[i];
			 DbfExtractor extractor= new DbfExtractor(fileadr);
			 fileadr= dbfToXlsPath(fileadr);
			 XlsWriter xlsWriter= new XlsWriter(extractor.headers.toStringNamesArr(), extractor.n, fileadr);
			 System.out.println(fileadr);
			 extractor.printAll(xlsWriter);
			 xlsWriter.close();
			 }
		 System.out.println("Готово");
		 return;
		 }

	 int fileNums[]= new int[n];
	 correctInput=false;
	 while(!correctInput)
		{
		 System.out.println("\nВведите список файлов через пробел:");
		 try
			{
			 for(int i=0; i<n; i++)
				{
				 fileNums[i]= scan.nextInt()-1;
				 if( (fileNums[i]<0) || (fileNums[i] >= fileList.length) )
					 throw new Exception();
				 }
			 correctInput=true;
			 }
		 catch(Exception inputErr)
			{
			 scan.nextLine();
			 System.out.println("Некорректный ввод. Еще раз");
			 }
		 }
	 System.out.println("\nПишу в файлы...");
	 for(int i=0; i<n; i++)
		{
		 fileadr=fileList[fileNums[i] ];
		 DbfExtractor extractor= new DbfExtractor(fileadr);
		 fileadr= dbfToXlsPath(fileadr);
		 XlsWriter xlsWriter= new XlsWriter(extractor.headers.toStringNamesArr(), extractor.n, fileadr);
		 System.out.println(fileadr);
		 extractor.printAll(xlsWriter);
		 xlsWriter.close();
		 }
	 System.out.println("Готово");

//	 Headers headers[]= new Headers[fileList.length];
//	 ArrayList<HashSet<String> > statistic= new ArrayList<>();
//	 DbfExtractor extractor= new DbfExtractor(fileList[6] );
//	 System.out.println(fileList[6] );
//	 extractor.printRecord(5);

/*/
	 for(String path : fileList)
		{
		 DbfExtractor extractor= new DbfExtractor(path);
		 System.out.println(path);
//			 int r=rand.nextInt(extractor.n);
		 for(int i=0; i<extractor.n; i++)
			{
			 extractor.printRecord(i);
			 System.out.println();
			 }
		 System.out.println("******************\n");
		 }
/*/
/*/
	 for(int i=0; i<fileList.length; i++)
		{
		 System.out.println(fileList[i] );
		 headers[i]= new Headers(fileList[i] );
		 HashSet<String> oneFileTypes= new HashSet<>();
		 HashSet<Character> set= new HashSet<>();
		 for(DBFField field : headers[i].fields)
			{
			 System.out.println(field.getName() +": "+ field.getType().name() +"\tdec.count: "+ field.getDecimalCount() );
			 oneFileTypes.add(field.getType().name() );
			 set.add( (char)(field.getType().getCode() ) );
			 }
		 System.out.println();
		 statistic.add(oneFileTypes);
		 }
	 System.out.println("*****************************\n");
	 for(int i=0; i<fileList.length; i++)
		 System.out.println(fileList[i] +"\n"+ statistic.get(i) +"\n");
/*/
	 }
 }
