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
	 int dbfFields[];
	 int dbfFieldsUpdated[];
	 int xlsFields[];

	 public static final int CONVERTER_TYPE_IST=0;
	 public static final int CONVERTER_TYPE_IT=1;
	 public static final int CONVERTER_TYPE_IZT=2;

	 abstract class InputConverter
		{
		 abstract ArrayList<Object> convert(Object[] record);
		 }
	 class Converter_ist extends InputConverter
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
	 class Converter_it extends InputConverter
		{
		 @Override
		 ArrayList<Object> convert(Object[] record)
			{
			 ArrayList<Object> result= new ArrayList<>();
			 for(int i=0; i<dbfFields.length; i++)
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
				 }
			 dbfFieldsUpdated=dbfFields;
			 return result;
			 }
		 }
	 class Converter_izt extends InputConverter
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
			 double sum25= extractor.extractFieldAs_double(record, dbfFields[separator+1] );
			 double sumpen= extractor.extractFieldAs_double(record, dbfFields[separator+2] );
			 if(sum25==DbfExtractor.EMPTY_NUM && sumpen==DbfExtractor.EMPTY_NUM)
				 result.add(DbfExtractor.EMPTY_NUM);
			 else
				{
				 if(sum25==DbfExtractor.EMPTY_NUM)
					 sum25=0;
				 if(sumpen==DbfExtractor.EMPTY_NUM)
					 sumpen=0;
				 result.add(sum25+sumpen);
				 }
			 updatedDbfIndexArr[separator]= dbfFields[separator+1];
			 dbfFieldsUpdated=updatedDbfIndexArr;
			 return result;
			 }
		 }

	 Doer(String _filePath,int[] _dbfFields,int[] _xlsFields,int converterType)
		{
		 filePath=_filePath;
		 dbfFields=_dbfFields;
		 xlsFields=_xlsFields;
		 extractor= new DbfExtractor(_filePath);
		 switch(converterType)
			{
			 case CONVERTER_TYPE_IST:
				 setConverter(new Converter_ist() );
				 break;
			 case CONVERTER_TYPE_IT:
				 setConverter(new Converter_it() );
				 break;
			 case CONVERTER_TYPE_IZT:
				 setConverter(new Converter_izt() );
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
	 void writeToXls(XlsWriter xlsWriter) throws IOException
		{
		 ArrayList< ArrayList<Integer> > indexLists= new ArrayList<>(indexMap.values() );
		 int i=0;
		 for(ArrayList<Integer> indexList : indexLists)
			{
			 for(int index : indexList)
				{
				 Object record[]= extractor.extractRecord(index);
				 ArrayList<Object> fieldsData;
				 fieldsData= converter.convert(record);
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
				 i++;
				 }
			 }
		 }
	 }
