package com.nan.walletdiary;

import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class PlotGraphYear extends Activity{
	private CPDataBase mydb;
	private String year_string=null;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mydb=new CPDataBase(this);
		Bundle extra=getIntent().getExtras();
		year_string=extra.getString("year");
		try {
			// fetching values part
			Cursor cursor;
			int year=-1, month_count = 0,tmp;
			float[] exp = new float[12];
			String[] month = new String[12];
			String[] ver_values=new String[3];

			mydb.open();
			final Calendar c = Calendar.getInstance();
			//String year_string=(String)yearly_expenses_header.getText();
			int year_value=Integer.parseInt(year_string);
			if(year==-1)
			{
			year = c.get(Calendar.YEAR);
			}
			cursor = mydb.eYearlyExpenses(String.valueOf(year_value));
			startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					//month[month_count] = cursor.getString(cursor.getColumnIndex("month_id"));
					String temp2 = cursor.getString(cursor.getColumnIndex("month_id"));
					tmp=Integer.parseInt(temp2);
					String temp = cursor.getString(cursor
							.getColumnIndex("sum_month_expenses"));

					exp[tmp-1] = (Integer.parseInt(temp));
					
				} while (cursor.moveToNext());
			}
			mydb.close();
			float max=exp[0];
			float sum=0;
			for(int i=0;i<12;++i)
			{
				if(exp[i]>max)
					max=exp[i];
				sum=sum+exp[i];
				
				
			}
			float avg=sum/12;
			ver_values[0]=String.valueOf((int)max)+"  (Max)";
			ver_values[1]=String.valueOf((int)avg)+"  (Avg)";
			ver_values[2]=String.valueOf(0);
			mydb.close();
			float[] values = new float[] { 2.0f, 1.5f, 2.5f, 1.0f, 3.0f };
			String[] verlabels = new String[] {"" };
			 String[] horlabels = new String[] { "1",
			 "2","3","4","5","6","7","8","9","10","11","12"
			 };
			GraphView graphView = new GraphView(this, exp, "Expenses vs Month",
					horlabels,ver_values, GraphView.BAR);
			setContentView(graphView);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

}
