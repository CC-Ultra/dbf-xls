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
//	 String dirPath="Исходники\\exp";
	 String dirPath="";
//	 Scanner scan= new Scanner(System.in);
	 Doer stFragment=null, tFragment=null, ztFragment=null;
	 String fileList[];

	 fileList= getDirFilesList(dirPath);
	 System.out.println("Итак, в нашем распоряжении такие файлы:");
	 for(int i=0; i<fileList.length; i++)
		{
		 System.out.println( (i+1) +". "+ fileList[i] );
		 if(fileList[i].contains("_st") )
			 stFragment= new Doer(fileList[i], new int[]{2,1,4,5,6}, new int[]{2,3,4,5,6}, Doer.CONVERTER_TYPE_ST);
		 if(fileList[i].contains("_t") )
			 tFragment= new Doer(fileList[i], new int[]{4,6,7,8,9}, new int[]{7,8,9,10,11}, Doer.CONVERTER_TYPE_T);
		 if(fileList[i].contains("_zt") )
			 ztFragment= new Doer(fileList[i], new int[]{5,11,21,-1,5,10,15}, new int[]{12,13,16}, Doer.CONVERTER_TYPE_ZT);
		}
	 if(stFragment==null || tFragment==null || ztFragment==null)
		{
		 System.out.println("\nНе найдены все файлы. Продолжение невозможно");
		 System.exit(1);
		 }
	 System.out.println("\nКлючевое поле выборки: NKV\n");
	 int keyFieldIndex=2;

	 System.out.println("Анализ...");
	 for(int i=0; i<stFragment.extractor.n; i++)
		{
		 Object istRecord[]= stFragment.extractor.extractRecord(i);
		 for(int j=0; j<tFragment.extractor.n; j++)
			{
			 Object itRecord[]= tFragment.extractor.extractRecord(j);
			 for(int k=0; k<ztFragment.extractor.n; k++)
				{
				 Object iztRecord[]= ztFragment.extractor.extractRecord(k);
				 if(istRecord[keyFieldIndex].equals(itRecord[keyFieldIndex] ) && istRecord[keyFieldIndex].equals(iztRecord[keyFieldIndex] ) )
					{
					 if(!stFragment.indexMap.containsKey(istRecord[keyFieldIndex] ) )
						{
						 stFragment.indexMap.put(istRecord[keyFieldIndex], new ArrayList<>() );
						 tFragment.indexMap.put(itRecord[keyFieldIndex], new ArrayList<>() );
						 ztFragment.indexMap.put(iztRecord[keyFieldIndex], new ArrayList<>() );
						 }
					 stFragment.updateIndexMap(istRecord[keyFieldIndex],i);
					 tFragment.updateIndexMap(itRecord[keyFieldIndex],j);
					 ztFragment.updateIndexMap(iztRecord[keyFieldIndex],k);
					 }
				 }
			 }
		 }
	 int xlsN=0;
	 for(Map.Entry<Object,ArrayList<Integer> > x : stFragment.indexMap.entrySet() )
		 xlsN+= x.getValue().size();
	 String headers[]=
			{
				"Номер", "Тип операции", "Номер договора", "Дата договора",
				"Фамилия", "Имя", "Отчество", "Наименование изделия",
				"Проба", "Цена за грамм", "Вес", "Вес чистый",
				"Сумма кредита", "Дата выдачи", "Сумма возвращенная",
				"Процент + пеня", "Изделие в ломбарде (дней)"
			 };

	 System.out.println("Запись...");
	 XlsWriter resultXls= new XlsWriter(headers,xlsN,"Result.xls");
	 stFragment.writeToXls(resultXls);
	 tFragment.writeToXls(resultXls);
	 ztFragment.writeToXls(resultXls);
	 resultXls.close();
	 System.out.println("Готово");
	 }
 }
