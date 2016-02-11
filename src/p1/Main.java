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

 public static void main(String[] args) throws IOException
	{
//	 String fileadr="Исходники\\exp";
	 String fileadr="";
	 Scanner scan = new Scanner(System.in);

	 String fileList[]= getDirFilesList(fileadr);
	 System.out.println("Итак, в нашем распоряжении такие файлы:");
	 for(int i=0; i<fileList.length; i++)
		 System.out.println( (i+1) +". "+ fileList[i] );
	 System.out.print("\nВыбери из какого читать (введи его номер)\n№=");
	 int fileNumber= scan.nextInt()-1;
	 DbfExtractor extractor= new DbfExtractor(fileList[fileNumber] );
	 System.out.println("В файле "+ extractor.n +" записей");
	 System.out.print("Сколько записей из таблицы взять? (не превышай общее число записей)\nn=");
	 int n= scan.nextInt();
	 int records[]= new int[n];
	 System.out.println("Введи список записей через пробел:");
	 for(int i=0; i<n; i++)
		 records[i]= scan.nextInt();
	 System.out.println("\nПишу в файл");
	 XlsWriter xlsWriter= new XlsWriter(extractor.headers.toStringNamesArr(), n);
	 for(int i=0; i<n; i++)
		 extractor.printRecord(xlsWriter,records[i], i);
	 xlsWriter.close();

	 System.out.println("Есть, готово. Введи что-то для выхода из программы или просто закрой ее");
	 getch();

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
