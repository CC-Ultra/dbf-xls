package p1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by snayper on 08.03.2016.
 */
public class Doer
	{
	 String filePath;
	 HashMap<Object, ArrayList<Integer> > indexMap= new HashMap<>();
	 DbfExtractor extractor;
	 InputConverter converter;
	 int converterType;
	 int dbfFields[];
	 int dbfFieldsUpdated[];
	 int xlsFields[];

	 public static final int CONVERTER_TYPE_ST=0;
	 public static final int CONVERTER_TYPE_T=1;
	 public static final int CONVERTER_TYPE_ZT=2;

	 abstract class InputConverter
		{
		 abstract ArrayList<Object> convert(Object[] record);
		 }
	 class Converter_st extends InputConverter
		{
		 @Override
		 ArrayList<Object> convert(Object[] record)
			{
			 ArrayList<Object> result= new ArrayList<>();
			 int separator=0;
			 for(int i=0; i<dbfFields.length; i++)
				 if(dbfFields[i]==-1)
					{
					 separator=i;
					 break;
					 }
			 int updatedDbfIndexArr[]= new int[separator+1];
			 for(int i=0; i<separator; i++)
				{
				 char fieldType= (char)extractor.headers.fields[dbfFields[i] ].getType().getCode();
				 switch(fieldType)
					{
					 case 'C':
						 result.add(extractor.extractFieldAs_String(record, dbfFields[i] ) );
						 break;
					 case 'N':
						 result.add(extractor.extractFieldAs_double(record, dbfFields[i] ) );
						 break;
					 case 'D':
						 result.add(extractor.extractFieldAs_Date(record, dbfFields[i] ) );
						 break;
					 default:
						 result.add(record[dbfFields[i] ] );
					 }
				  updatedDbfIndexArr[i]= dbfFields[i];
				  }
			 String fullName= extractor.extractFieldAs_String(record, dbfFields[separator+1] ).trim();
			 fullName+= " "+ extractor.extractFieldAs_String(record, dbfFields[separator+2] ).trim();
			 fullName+= " "+ extractor.extractFieldAs_String(record, dbfFields[separator+3] ).trim();
			 result.add(fullName);
			 updatedDbfIndexArr[separator]= dbfFields[separator+1];
			 dbfFieldsUpdated=updatedDbfIndexArr;
			 return result;
			 }
		 }
	 class Converter_t extends InputConverter
		{
		 @Override
		 ArrayList<Object> convert(Object[] record)
			{
			 ArrayList<Object> result= new ArrayList<>();
			 for(int dbfFieldNum : dbfFields)
				{
				 char fieldType=(char) extractor.headers.fields[dbfFieldNum].getType().getCode();
				 switch(fieldType)
					{
					 case 'C':
						 result.add(extractor.extractFieldAs_String(record,dbfFieldNum));
						 break;
					 case 'N':
						 result.add(extractor.extractFieldAs_double(record,dbfFieldNum));
						 break;
					 case 'D':
						 result.add(extractor.extractFieldAs_Date(record,dbfFieldNum));
						 break;
					 default:
						 result.add(record[dbfFieldNum]);
					 }
				 }
			 dbfFieldsUpdated=dbfFields;
			 return result;
			 }
		 }
	 class Converter_zt extends InputConverter
		{
		 @Override
		 ArrayList<Object> convert(Object[] record)
			{
			 ArrayList<Object> result= new ArrayList<>();
			 int separator=0;
			 for(int i=0; i<dbfFields.length; i++)
				 if(dbfFields[i]==-1)
					{
					 separator=i;
					 break;
					 }
			 int updatedDbfIndexArr[]= new int[separator];
			 for(int i=0; i<separator; i++)
				{
				 char fieldType= (char)extractor.headers.fields[dbfFields[i] ].getType().getCode();
				 switch(fieldType)
					{
					 case 'C':
						 result.add(extractor.extractFieldAs_String(record, dbfFields[i] ) );
						 break;
					 case 'N':
						 result.add(extractor.extractFieldAs_double(record, dbfFields[i] ) );
						 break;
					 case 'D':
						 result.add(extractor.extractFieldAs_Date(record, dbfFields[i] ) );
						 break;
					 default:
						 result.add(record[dbfFields[i] ] );
					 }
				 updatedDbfIndexArr[i]= dbfFields[i];
				 }
			 double sums= extractor.extractFieldAs_double(record, dbfFields[separator+1] );
			 double sum25= extractor.extractFieldAs_double(record, dbfFields[separator+2] );
			 double sumpen= extractor.extractFieldAs_double(record, dbfFields[separator+3] );
			 double proc= sumpen+sum25;
			 double returnSum= proc+sums;
			 result.add(returnSum);
			 result.add(proc);
			 result.add(record[3] ); //OP
			 dbfFieldsUpdated=updatedDbfIndexArr;
			 return result;
			 }
		 }

	 Doer(String _filePath,int[] _dbfFields,int[] _xlsFields,int _converterType)
		{
		 converterType=_converterType;
		 filePath=_filePath;
		 dbfFields=_dbfFields;
		 xlsFields=_xlsFields;
		 extractor= new DbfExtractor(_filePath);
		 switch(converterType)
			{
			 case CONVERTER_TYPE_ST:
				 setConverter(new Converter_t() );
				 break;
			 case CONVERTER_TYPE_T:
				 setConverter(new Converter_t() );
				 break;
			 case CONVERTER_TYPE_ZT:
				 setConverter(new Converter_zt() );
			 }
		 }
	 void updateIndexMap(Object key,int newIndex)
		{
		 ArrayList<Integer> updatedList= indexMap.get(key);
		 updatedList.add(newIndex);
		 indexMap.put(key,updatedList);
		 }
	 void setConverter(InputConverter _converter)
		{
		 converter=_converter;
		 }
	 void putSpecialFields(XlsWriter xlsWriter,ArrayList<Object> specials,int i)
		{
		 if(converterType==CONVERTER_TYPE_ZT)
			{
			 double op= (double)specials.get(2);
			 String opStr;
			 double returnSumm,proc;
			 if(op==1)
				{
				 opStr="залог";
				 returnSumm=proc=DbfExtractor.EMPTY_NUM;
				 }
			 else
				{
				 opStr="выкуп";
				 returnSumm= (double)specials.get(0);
				 proc= (double)specials.get(1);
				 }
			 xlsWriter.writeAs_String(opStr, i, 1);
			 xlsWriter.writeAs_double(returnSumm, i, 14);
			 xlsWriter.writeAs_double(proc, i, 15);
			 }
		 }
	 void writeToXls(XlsWriter xlsWriter) throws IOException
		{
		 ArrayList< ArrayList<Integer> > indexLists= new ArrayList<>(indexMap.values() );
		 int i=0;
		 for(ArrayList<Integer> indexList : indexLists)
			 for(int index : indexList)
				{
				 Object record[]= extractor.extractRecord(index);
				 ArrayList<Object> fieldsData;
				 fieldsData= converter.convert(record);
				 ArrayList<Object> specials=new ArrayList<>(fieldsData.subList(xlsFields.length, fieldsData.size() ) );
				 for(int j=0; j<xlsFields.length; j++)
					{
					 char fieldType= (char)extractor.headers.fields[dbfFieldsUpdated[j] ].getType().getCode();
					 switch(fieldType)
						{
						 case 'C':
							 xlsWriter.writeAs_String(fieldsData.get(j).toString(), i, xlsFields[j] );
							 break;
						 case 'N':
							 xlsWriter.writeAs_double( (double)fieldsData.get(j), i, xlsFields[j] );
							 break;
						 case 'D':
							 xlsWriter.writeAs_Date( (Date)fieldsData.get(j), i, xlsFields[j] );
							 break;
						 default:
							 xlsWriter.writeAs_String("null", i, xlsFields[j] );
						 }
					 }
				 putSpecialFields(xlsWriter,specials,i);
				 i++;
				 }
		 }
	 }
