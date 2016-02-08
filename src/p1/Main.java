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
 static boolean userChoice()
	{
	 System.out.println("Еще раз? (Y/N)");
	 if(Objects.equals(getch().toLowerCase(),"y") )
		{
		 System.out.println("\n*************************************\n");
		 return true;
		 }
	 else
		 return false;
	 }
 static void iteration(String[] fileList) throws IOException
	{
	 Scanner scan = new Scanner(System.in);
	 System.out.println("Итак, в нашем распоряжении такие файлы:");
	 for(int i=0; i<fileList.length; i++)
		 System.out.println( (i+1) +". "+ fileList[i] );
	 System.out.print("\nВыбери из какого читать (введи его номер)\nn=");
	 int fileNumber= scan.nextInt()-1;
	 DbfExtractor extractor= new DbfExtractor(fileList[fileNumber] );
	 System.out.print("В файле "+ extractor.n +" записей. Какую напечатать? (0.."+ (extractor.n-1) +")\ni=");
	 int index= scan.nextInt();
	 System.out.println();
	 extractor.printRecord(index);
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
	 String fileList[]= getDirFilesList(fileadr);
	 boolean iterate=true;

	 while(iterate)
		{
		 iteration(fileList);
		 iterate=userChoice();
		 }
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
