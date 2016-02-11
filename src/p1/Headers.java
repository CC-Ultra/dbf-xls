package p1;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFHeader;
import com.linuxense.javadbf.DBFReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by snayper on 04.02.2016.
 */
public class Headers
	{
	 DBFField fields[];

	 Headers(String filepath) throws IOException
		{
		 DBFReader dbfIn= new DBFReader(new FileInputStream(filepath) );
		 fields= new DBFField[ dbfIn.getFieldCount() ];
		 for(int i=0; i < fields.length; i++)
			 fields[i]= dbfIn.getField(i);
		 }

	 public String[] toStringNamesArr()
		{
		 String result[];
		 int l= fields.length;
		 result= new String[l];
		 for(int i=0; i<l; i++)
			 result[i]=fields[i].getName();
		 return result;
		 }
	 }
