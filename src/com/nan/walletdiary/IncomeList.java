package com.nan.walletdiary;

import java.util.Calendar;
import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class IncomeList extends Activity {
	ListView income_list;
	CPDataBase mydb;
	public void onCreate(Bundle icicle) 
	{
		try{
		super.onCreate(icicle);
		setContentView(R.layout.income_list);	
		
		mydb=new CPDataBase(this);
		
		income_list=(ListView)findViewById(R.id.income_list);
		
		fillIncomeList();
		
		
		}
		catch(Exception e)
		{
			//Toast.makeText(getApplicationContext(), "IncomeList.java onCreate():  "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		setResult(RESULT_OK);
	}
	public void fillIncomeList()
	{
		Cursor cursor;
		
		mydb.open();
		cursor=mydb.ifetchAllNotes();
		startManagingCursor(cursor);

		/*String[] from = new String[] {CPDataBase.INCOME_KEY_CATEGORY_NAME,CPDataBase.INCOME_KEY_NO,CPDataBase.INCOME_KEY_AMOUNT,
				CPDataBase.INCOME_KEY_DESCRIPTION,CPDataBase.INCOME_KEY_DATE};
		int[] to = new int[] { R.id.income_list_category,R.id.income_list_refno,R.id.income_list_amount,R.id.income_list_descrip,R.id.income_list_time};*/
		
		String[] from=new String[] {CPDataBase.INCOME_KEY_DATE,CPDataBase.INCOME_KEY_CATEGORY_NAME,CPDataBase.INCOME_KEY_NO,CPDataBase.INCOME_KEY_AMOUNT,CPDataBase.INCOME_KEY_DESCRIPTION};
		int [] to=new int[] {R.id.income_list_time,R.id.income_list_category,R.id.income_list_refno,R.id.income_list_amount,R.id.income_list_descrip};
		
		income_list.setAdapter(new IncomeListAdapter(from, to, cursor));
		
		int income_total_value=mydb.iTotalIncome();
		TextView income_total=(TextView)findViewById(R.id.income_total);
		income_total.setText(String.valueOf(income_total_value));
		
		mydb.close();
	}
	class IncomeListAdapter extends SimpleCursorAdapter
    {
    	Cursor cursor;
    	public IncomeListAdapter(String from[],int to[],Cursor cursor) {
    		super(IncomeList.this, R.layout.income_list_row, cursor, from, to);
    		this.cursor=cursor;
		}
    	public View getView(int position,View convertView,ViewGroup parent)
    	{
    		TextView category,refno,amount,descrip,dateTime;
    		View row=null;

    		row=convertView;
    		if(row==null)
    		{
    			LayoutInflater inflater=getLayoutInflater();
    			row=inflater.inflate(R.layout.income_list_row, parent, false);
    		}
    		
    		category=(TextView) row.findViewById(R.id.income_list_category);
    		refno=(TextView)row.findViewById(R.id.income_list_refno);
    		amount=(TextView)row.findViewById(R.id.income_list_amount);
    		descrip=(TextView)row.findViewById(R.id.income_list_descrip);
    		dateTime=(TextView)row.findViewById(R.id.income_list_time);
    		
			cursor.moveToPosition(position);
			
    		int category_name_index=cursor.getColumnIndex(CPDataBase.INCOME_KEY_CATEGORY_NAME);
    		String category_name=cursor.getString(category_name_index);
    		category.setText(category_name);
    		
    		int refno_index=cursor.getColumnIndex(CPDataBase.INCOME_KEY_NO);
    		int refno_value=cursor.getInt(refno_index);
    		refno.setText(String.valueOf(refno_value));
    		
    		int amount_value_index=cursor.getColumnIndex(CPDataBase.INCOME_KEY_AMOUNT);
    		int amount_value=cursor.getInt(amount_value_index);
    		amount.setText(String.valueOf(amount_value));
    		
    		int descrip_value_index=cursor.getColumnIndex(CPDataBase.INCOME_KEY_DESCRIPTION);
    		String descrip_value=cursor.getString(descrip_value_index);
    		descrip.setText(descrip_value);
    		
    		int date_index=cursor.getColumnIndex(CPDataBase.INCOME_KEY_DATE);
    		int date=cursor.getInt(date_index); 
    		if((date/100)%100<10)
    		{
    			if((date%100)<10)
    			{
    				dateTime.setText(String.valueOf((date/10000)+"-"+"0"+((date/100)%100)+"-"+"0"+(date%100)));
    			}
    			else
    				dateTime.setText(String.valueOf((date/10000)+"-"+"0"+((date/100)%100)+"-"+(date%100)));
    		}
    		else
    		{
    			if((date%100)<10)
    			{
    				dateTime.setText(String.valueOf((date/10000)+"-"+((date/100)%100)+"-"+"0"+(date%100)));
    			}
    			else
    				dateTime.setText(String.valueOf((date/10000)+"-"+((date/100)%100)+"-"+(date%100)));
    		}
    		

    		return(row);
    		
    	}
    }
}