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
	{//
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
//	 String dirPath="Исходники\\exp";
	 String dirPath="";
//	 Scanner scan= new Scanner(System.in);
	 Doer istFragment=null, itFragment=null, iztFragment=null;
	 String fileList[];

	 fileList= getDirFilesList(dirPath);
	 System.out.println("Итак, в нашем распоряжении такие файлы:");
	 for(int i=0; i<fileList.length; i++)
		{
		 System.out.println( (i+1) +". "+ fileList[i] );
		 if(fileList[i].contains("ist") )
			 istFragment= new Doer(fileList[i], new int[]{2,1,-1,4,5,6}, new int[]{1,2,3}, Doer.CONVERTER_TYPE_IST);
		 if(fileList[i].contains("it") )
			 itFragment= new Doer(fileList[i], new int[]{4,6,7,8,9,7}, new int[]{4,5,6,7,8,9}, Doer.CONVERTER_TYPE_IT);
		 if(fileList[i].contains("izt") )
			 iztFragment= new Doer(fileList[i], new int[]{5,11,12,-1,10,15}, new int[]{10,11,12,13}, Doer.CONVERTER_TYPE_IZT);
		 }
	 if(istFragment==null || itFragment==null || iztFragment==null)
		{
		 System.out.println("\nНе найдены все файлы. Продолжение невозможно");
		 System.exit(1);
		 }
	 System.out.println("\nКлючевое поле выборки: NKV\n");
	 int keyFieldIndex=2;

	 System.out.println("Анализ...");
	 for(int i=0; i<istFragment.extractor.n; i++)
		{
		 Object istRecord[]= istFragment.extractor.extractRecord(i);
		 for(int j=0; j<itFragment.extractor.n; j++)
			{
			 Object itRecord[]= itFragment.extractor.extractRecord(j);
			 for(int k=0; k<iztFragment.extractor.n; k++)
				{
				 Object iztRecord[]= iztFragment.extractor.extractRecord(k);
				 if(istRecord[keyFieldIndex].equals(itRecord[keyFieldIndex] ) && istRecord[keyFieldIndex].equals(iztRecord[keyFieldIndex] ) )
					{
					 if(istFragment.indexMap.containsKey(istRecord[keyFieldIndex] ) )
						{
						 istFragment.updateIndexMap(istRecord[keyFieldIndex],i);
						 itFragment.updateIndexMap(itRecord[keyFieldIndex],j);
						 iztFragment.updateIndexMap(iztRecord[keyFieldIndex],k);
						 }
					 else
						{
						 istFragment.indexMap.put(istRecord[keyFieldIndex], new ArrayList<>() );
						 itFragment.indexMap.put(itRecord[keyFieldIndex], new ArrayList<>() );
						 iztFragment.indexMap.put(iztRecord[keyFieldIndex], new ArrayList<>() );
						 istFragment.updateIndexMap(istRecord[keyFieldIndex],i);
						 itFragment.updateIndexMap(itRecord[keyFieldIndex],j);
						 iztFragment.updateIndexMap(iztRecord[keyFieldIndex],k);
						 }
					 }
				 }
			 }
		 }
	 int xlsN=0;
	 for(Map.Entry<Object,ArrayList<Integer> > x : istFragment.indexMap.entrySet() )
		 xlsN+= x.getValue().size();
	 String headers[]=
			{
				"п/п", "№ договора", "Дата договора",
				"Ф.И.О.", "Наименование изделия", "Проба",
				"Цена 1 грамма", "Вес изделия", "Вес чистый",
				"Оценочная стоимость", "Сумма кредита выданного", "Дата выдачи кредита",
				"Сумма кредита возвращенного", "Сумма процентов и пени", "Сумма общая"
			 };

	 System.out.println("Запись...");
	 XlsWriter resultXls= new XlsWriter(headers,xlsN,"Result.xls");
	 istFragment.writeToXls(resultXls);
	 itFragment.writeToXls(resultXls);
	 iztFragment.writeToXls(resultXls);
	 resultXls.close();
	 System.out.println("Готово");
	 }
 }
