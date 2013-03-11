package com.nan.walletdiary;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class ExpenseList extends Activity {
	
	public static final int FIRST_DAY_OF_WEEK=2;
	
	TabHost tabs;
	ListView yearly_expenses,monthly_expenses,weekly_expenses,daily_expenses,all_expenses;
	TextView yearly_expenses_total,yearly_expenses_header;
	TextView monthly_expenses_total,monthly_expenses_header;
	TextView weekly_expenses_total,weekly_expenses_header;
	TextView daily_expenses_total,daily_expenses_header;
	TextView custom_expenses_total,all_expenses_total;
	
	TextView weekly_expenses_header_new;
	
	CPDataBase mydb;
	Button prev_year,next_year,graph_year;
	Button prev_month,next_month;
	Button prev_week,next_week;
	Button prev_day,next_day;
	//my part
	private Button from_custom, to_custom;
	private ListView custom_expenses;

	private int mYear, mMonth, mDay, mMonth1, mYear1, mDay1;
	private String from_date, to_date, str_to, str_from;
	private TextView from_text, to_text;
	
	static final int DATE_PICK_FROM = 1100;
	static final int DATE_PICK_TO = 1101;
	private static final int MENU_DEL=1014;
	
	public void onCreate(Bundle icicle) {
		try{
			super.onCreate(icicle);
			setContentView(R.layout.expense_list);
			
			mydb=new CPDataBase(this);
			
			yearly_expenses=(ListView)findViewById(R.id.yearly_expenses);
			monthly_expenses=(ListView)findViewById(R.id.monthly_expenses);
			weekly_expenses=(ListView)findViewById(R.id.weekly_expenses);
			daily_expenses=(ListView)findViewById(R.id.daily_expenses);
			all_expenses=(ListView)findViewById(R.id.all_expenses);
			
			custom_expenses=(ListView)findViewById(R.id.custom_expenses);
			
			//mypart
			registerForContextMenu(all_expenses);
			registerForContextMenu(daily_expenses);
			registerForContextMenu(custom_expenses);
			
			graph_year = (Button) findViewById(R.id.year_graph);
			graph_year.setOnClickListener(new OnClickListener() {

				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//plotYearGraph();
					Intent plot_graph=new Intent(ExpenseList.this, PlotGraphYear.class);
					String year_string=(String)yearly_expenses_header.getText();
					plot_graph.putExtra("year", year_string);
					startActivity(plot_graph);

				}
			});
			//end of mypart
			

			
			yearly_expenses_total=(TextView)findViewById(R.id.yearly_expenses_total);
			yearly_expenses_header=(TextView)findViewById(R.id.yearly_expenses_header);
			monthly_expenses_total=(TextView)findViewById(R.id.monthly_expenses_total);
			monthly_expenses_header=(TextView)findViewById(R.id.monthly_expenses_header);			
			weekly_expenses_total=(TextView)findViewById(R.id.weekly_expenses_total);
			weekly_expenses_header=(TextView)findViewById(R.id.weekly_expenses_header);
			daily_expenses_total=(TextView)findViewById(R.id.daily_expenses_total);
			daily_expenses_header=(TextView)findViewById(R.id.daily_expenses_header);
			custom_expenses_total=(TextView)findViewById(R.id.custom_expenses_total);
			all_expenses_total=(TextView)findViewById(R.id.all_expenses_total);
			
			weekly_expenses_header_new=(TextView)findViewById(R.id.weekly_expenses_header_new);
			//my custom expenses part
			from_custom = (Button) findViewById(R.id.custom_from);
			to_custom = (Button) findViewById(R.id.custom_to);

			from_text = (TextView) findViewById(R.id.custom_from_text);
			to_text = (TextView) findViewById(R.id.custom_to_text);
			
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			from_date = convert_to_date_string(mMonth, mDay, mYear);
			to_date = from_date;
			str_from = mDay + "-" + (mMonth + 1) + "-" + mYear;
			str_to = str_from;
			from_text.setText(str_from);
			to_text.setText(str_to);
			from_custom.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(DATE_PICK_FROM);

				}
			});
			to_custom.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(DATE_PICK_TO);

				}
			});
			custom_expenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), "Item Selected", Toast.LENGTH_SHORT).show();


					
					
				}
			});
			//end of my custom expenses part
			yearly_expenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), "Item Selected", Toast.LENGTH_SHORT).show();
			   		Calendar c=Calendar.getInstance();
		    		TextView tag;
		    		tag=(TextView)v.findViewById(R.id.expense_list_row_tag);
		    		
		    		String month_name=(String) tag.getText();
		    		//Toast.makeText(getApplicationContext(), month_name, Toast.LENGTH_SHORT).show();
		    		int month_id = 1;
	    			String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
					int i;
					for(i=0;i<12;i++)
					{
						if(month_name.equals(months[i]))
						{
							month_id=i+1;
							break;
						}
					}


		    		//Toast.makeText(getApplicationContext(),String.valueOf(month_id), Toast.LENGTH_SHORT).show();
		    		
		    		//------------------------------ The following is the source for Error-----------
		    		
		    		fill_monthly_expenses(c.get(Calendar.YEAR),month_id);
		    		

		    		//-------------------------------------------------------------------------------
		    		
		    		//Toast.makeText(getApplicationContext(),String.valueOf(month_id), Toast.LENGTH_SHORT).show();
		    		tabs.setCurrentTab(1);
					
				}
			});
			
			monthly_expenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					
					int year=-1;
					int month_id=-1;
					int day=-1;
					Calendar c=Calendar.getInstance();
		    		
					String monthly_expenses_header_string=(String)monthly_expenses_header.getText();
					year=Integer.parseInt(monthly_expenses_header_string.substring(monthly_expenses_header_string.length()-4, monthly_expenses_header_string.length()));
					//Toast.makeText(getApplicationContext(), String.valueOf(year), Toast.LENGTH_SHORT).show();
					String month_name=monthly_expenses_header_string.substring(0, monthly_expenses_header_string.length()-6);
					//Toast.makeText(getApplicationContext(),"**"+month_string+"**", Toast.LENGTH_SHORT).show();
					String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
					int i;
					for(i=0;i<12;i++)
					{
						if(month_name.equals(months[i]))
						{
							month_id=i+1;
							break;
						}
					}
					
					TextView tag;
		    		tag=(TextView)v.findViewById(R.id.expense_list_row_tag);
		    		String day_string=(String)tag.getText();
		    		//day=Integer.parseInt(day_string.substring(0, day_string.length()-1));
		    		day=Integer.parseInt(day_string);
		    		
		    		fill_weekly_expenses(year,month_id,day);
		    		
		    		tabs.setCurrentTab(2);

					//Toast.makeText(getApplicationContext(),"xx"+String.valueOf(day)+"xx", Toast.LENGTH_SHORT).show();
					
				}
			});
			
			weekly_expenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {	
					
					int year=-1;
					int month_id=-1;
					int day=-1;
		    		int date;
					
					TextView tag;
		    		tag=(TextView)v.findViewById(R.id.expense_list_row_tag);
		    		String date_string=(String)tag.getText();
		    		date_string=date_string.replace("-", "0");
		    		date=Integer.parseInt(date_string);
		    		
		    		year=(date/1000000);
		    		month_id=(date/1000)%100;
		    		day=date%100;
		    		//day=Integer.parseInt(day_string.substring(day_string.length()-3, day_string.length()-1));
		    		
		    		fill_daily_expenses(year,month_id, day);
		    				
		    		tabs.setCurrentTab(3);


				}
			});
			daily_expenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {

					
				}
			});
			
			prev_year=(Button)findViewById(R.id.prev_year);
			
			prev_year.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					String year_string=(String)yearly_expenses_header.getText();
					int year_value=Integer.parseInt(year_string);
					year_value=year_value-1;

					fill_yearly_expenses(year_value);

				}
			});
			next_year=(Button)findViewById(R.id.next_year);

			next_year.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					String year_string=(String)yearly_expenses_header.getText();
					int year_value=Integer.parseInt(year_string);
					year_value=year_value+1;
					
					
					fill_yearly_expenses(year_value);

				}
			});

			prev_month=(Button)findViewById(R.id.prev_month);
			prev_month.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					
					int year=-1,month_id=-1;
					String monthly_expenses_header_string=(String)monthly_expenses_header.getText();
					year=Integer.parseInt(monthly_expenses_header_string.substring(monthly_expenses_header_string.length()-4, monthly_expenses_header_string.length()));
					
					String month_name=monthly_expenses_header_string.substring(0, monthly_expenses_header_string.length()-6);
					
					String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
					int i;
					for(i=0;i<12;i++)
					{
						if(month_name.equals(months[i]))
						{
							month_id=i+1;
							break;
						}
					}

					
					//Toast.makeText(getApplicationContext(), "prev_month: "+month_id, Toast.LENGTH_SHORT).show();
					month_id=month_id-1;
					if(month_id==0)
					{
						month_id=12;
						year=year-1;
					}
					fill_monthly_expenses(year, month_id);
				}
			});
			
			next_month=(Button)findViewById(R.id.next_month);
			next_month.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
				
					int year=-1,month_id=-1;
					String monthly_expenses_header_string=(String)monthly_expenses_header.getText();
					year=Integer.parseInt(monthly_expenses_header_string.substring(monthly_expenses_header_string.length()-4, monthly_expenses_header_string.length()));
					
					String month_name=monthly_expenses_header_string.substring(0, monthly_expenses_header_string.length()-6);
					
					String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
					int i;
					for(i=0;i<12;i++)
					{
						if(month_name.equals(months[i]))
						{
							month_id=i+1;
							break;
						}
					}
					
					if(month_id==12)
					{
						month_id=1;
						year=year+1;
					}
					else
						month_id=month_id+1;
					fill_monthly_expenses(year, month_id);
				}
			});
			
			prev_week=(Button)findViewById(R.id.prev_week);
			prev_week.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					String weekly_expenses_header_string=(String)weekly_expenses_header.getText();
					String endDate=weekly_expenses_header_string.substring(weekly_expenses_header_string.length()-8, weekly_expenses_header_string.length());
					int date=Integer.parseInt(endDate);
					int day=-1;
					int month_id=-1;
					int year=-1;
		    		year=(date/10000);
		    		month_id=(date/100)%100;
		    		day=date%100;
					Calendar c=Calendar.getInstance();
					c.set(year,month_id-1, day);
					//Toast.makeText(getApplicationContext(), "prev_week:  "+
					//		"Date after Set:  "+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
					
					c.add(Calendar.DAY_OF_MONTH, -7);
					//Toast.makeText(getApplicationContext(), "prev_week:  "+
					//		"Date after Add:  "+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
					day=c.get(Calendar.DAY_OF_MONTH);
					month_id=c.get(Calendar.MONTH);
					month_id=month_id+1;
					year=c.get(Calendar.YEAR);
					fill_weekly_expenses(year, month_id, day);
					
				}
			});
			next_week=(Button)findViewById(R.id.next_week);
			next_week.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					String weekly_expenses_header_string=(String)weekly_expenses_header.getText();
					String endDate=weekly_expenses_header_string.substring(weekly_expenses_header_string.length()-8, weekly_expenses_header_string.length());
					int date=Integer.parseInt(endDate);
					int day=-1;
					int month_id=-1;
					int year=-1;
		    		year=(date/10000);
		    		month_id=(date/100)%100;
		    		day=date%100;
					Calendar c=Calendar.getInstance();
					c.set(year,month_id-1, day);
					//Toast.makeText(getApplicationContext(), "prev_week:  "+
					//		"Date after Set:  "+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
					
					c.add(Calendar.DAY_OF_MONTH, 1);
					//Toast.makeText(getApplicationContext(), "prev_week:  "+
					//		"Date after Add:  "+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
					day=c.get(Calendar.DAY_OF_MONTH);
					month_id=c.get(Calendar.MONTH);
					month_id=month_id+1;
					year=c.get(Calendar.YEAR);
					fill_weekly_expenses(year, month_id, day);
					
				}
			});
			prev_day=(Button)findViewById(R.id.prev_day);
			prev_day.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					String date_string=(String)daily_expenses_header.getText();
					date_string=date_string.replace("-", "0");
		    		int date=-1,year=-1,month_id=-1,day=-1;
					date=Integer.parseInt(date_string);
		    		year=(date/1000000);
		    		month_id=(date/1000)%100;
		    		day=date%100;
		    		
					Calendar c=Calendar.getInstance();
					c.set(year,month_id-1, day);
		    		c.add(Calendar.DAY_OF_MONTH, -1);
		    		
		    		year=c.get(Calendar.YEAR);
		    		month_id=c.get(Calendar.MONTH);
		    		month_id++;
		    		day=c.get(Calendar.DAY_OF_MONTH);
		    		
		    		fill_daily_expenses(year, month_id, day);
		    		
					
				}
			});
			next_day=(Button)findViewById(R.id.next_day);
			next_day.setOnClickListener(new View.OnClickListener() {
				
				
				public void onClick(View v) {
					String date_string=(String)daily_expenses_header.getText();
					date_string=date_string.replace("-", "0");
		    		int date=-1,year=-1,month_id=-1,day=-1;
					date=Integer.parseInt(date_string);
		    		year=(date/1000000);
		    		month_id=(date/1000)%100;
		    		day=date%100;
		    		
					Calendar c=Calendar.getInstance();
					c.set(year,month_id-1, day);
		    		c.add(Calendar.DAY_OF_MONTH, 1);
		    		
		    		year=c.get(Calendar.YEAR);
		    		month_id=c.get(Calendar.MONTH);
		    		month_id++;
		    		day=c.get(Calendar.DAY_OF_MONTH);
		    		
		    		fill_daily_expenses(year, month_id, day);
					
				}
			});
			fillData();
			
			
//---------------------------------------------------------------------------------------------------------------------------------------------------
			/*LayoutInflater inflater=getLayoutInflater();
			View year_tab=inflater.inflate(R.layout.tabs_view,null);
			View month_tab=inflater.inflate(R.layout.tabs_view,null);
			View week_tab=inflater.inflate(R.layout.tabs_view,null);
			View day_tab=inflater.inflate(R.layout.tabs_view,null);
			TextView tab_label;*/
			
			
			tabs=(TabHost)findViewById(R.id.tabhost);
			tabs.setup();
			
			TabHost.TabSpec spec=tabs.newTabSpec("tag1");
			spec.setContent(R.id.tab1);
			//tab_label=(TextView) year_tab.findViewById(R.id.tab_label);
			//tab_label.setText("Year's");
			//spec.setIndicator(year_tab);
			spec.setIndicator("Year's");
			tabs.addTab(spec);
			
			spec=tabs.newTabSpec("tag2");
			spec.setContent(R.id.tab2);
			//tab_label=(TextView) month_tab.findViewById(R.id.tab_label);
			//tab_label.setText("Month's");
			//spec.setIndicator(month_tab);
			spec.setIndicator("Month's");
			tabs.addTab(spec);
			
			spec=tabs.newTabSpec("tag3");
			spec.setContent(R.id.tab3);
			//tab_week_label=(TextView) tab_view.findViewById(R.id.tab_label);
			//tab_week_label.setText("Week's");
			//spec.setIndicator(tab_view);
			spec.setIndicator("Week's");
			tabs.addTab(spec);
			
			spec=tabs.newTabSpec("tag4");
			spec.setContent(R.id.tab4);
			spec.setIndicator("Day's");
			tabs.addTab(spec);

			spec=tabs.newTabSpec("tag5");
			spec.setContent(R.id.tab5);
			spec.setIndicator("Custom");
			tabs.addTab(spec);
			
			spec=tabs.newTabSpec("tag6");
			spec.setContent(R.id.tab6);
			spec.setIndicator("All Expenses");
			
			tabs.addTab(spec);
			
			Bundle extras=getIntent().getExtras();
			switch(extras.getInt("tab_index"))
			{
			case 0:
				tabs.setCurrentTab(0);
				break;
			case 1:
				tabs.setCurrentTab(1);
				break;
			case 2:
				tabs.setCurrentTab(2);
				break;
			case 3:
				tabs.setCurrentTab(3);
				break;
			default:
				tabs.setCurrentTab(0);
				break;
			}
//----------------------------------------------------------------------------------------------------------------------------------------------
			
			}
			catch(Exception e)
			{
				//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		
		private void fillData() {
			
			fill_all_expenses();
			fill_yearly_expenses(-1);
			fill_monthly_expenses(-1, -1);
			fill_weekly_expenses(-1, -1, -1);
			fill_daily_expenses(-1, -1, -1);
			fill_custom_expenses();
			

		}
		void fill_yearly_expenses(int year)
		{
			Cursor cursor;
			mydb.open();
			final Calendar c = Calendar.getInstance();
			if(year==-1)
				year= c.get(Calendar.YEAR);
			cursor=mydb.eYearlyExpenses(String.valueOf(year));
			startManagingCursor(cursor);

			String[] from = new String[] {"month_id","sum_month_expenses"};
			int[] to = new int[] { R.id.expense_list_row_tag,R.id.expense_list_row_value };
			
			yearly_expenses_header.setText(String.valueOf(year));
			yearly_expenses_total.setText(String.valueOf(mydb.eYearlyExpensesTotal(year)));			
			yearly_expenses.setAdapter(new ExpensesListAdapter(from, to,0,cursor));
			mydb.close();
		}
		void fill_monthly_expenses(int year,int month_id)
		{
			try{
			mydb.open();
			Cursor cursor;
			String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
			final Calendar c = Calendar.getInstance();
			if(year==-1)
			{
				year= c.get(Calendar.YEAR);
			}
			if(month_id==-1)
			{
				//Year is supplied and month_id should be set to the current month_id.
				month_id= c.get(Calendar.MONTH);
				month_id++;
			}

			cursor=mydb.eMonthlyExpenses(year,month_id);
			startManagingCursor(cursor);
			
			
			String[] from = new String[] {"day","sum_daily_expenses"};
			int[] to = new int[] { R.id.expense_list_row_tag,R.id.expense_list_row_value };
			
			monthly_expenses_header.setText(months[month_id-1]+", "+String.valueOf(year));
			monthly_expenses_total.setText(String.valueOf(mydb.eMonthlyExpensesTotal(year,month_id)));	
			monthly_expenses.setAdapter(new ExpensesListAdapter(from, to,1,cursor));
			mydb.close();
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "fill_monthly_expenses:  "+e.getMessage()+" AND "+e.getStackTrace()+" AND "+e.getCause(), Toast.LENGTH_SHORT).show();
			}
		}
		void fill_weekly_expenses(int year,int month_id,int day)
		{
			mydb.open();
			Cursor cursor;
			Calendar c = Calendar.getInstance();
			if(year==-1)
			{
				year= c.get(Calendar.YEAR);
			}
			if(month_id==-1)
			{
				month_id= c.get(Calendar.MONTH);
				month_id++;
			}
			if(day==-1)
			{
				day = c.get(Calendar.DAY_OF_MONTH);
			}
			
			c.set(year,month_id-1, day);
			
			int startDay,startMonth,startYear;
			int endDay,endMonth,endYear;
			int dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
			//Toast.makeText(getApplicationContext(), String.valueOf(dayOfWeek)+"month&year"+String.valueOf(month_id)+":"+String.valueOf(year), Toast.LENGTH_SHORT).show();
			//System.out.println("Day of Week:"+dayOfWeek+"\n");
			
			int last_day_of_week;
			last_day_of_week=(FIRST_DAY_OF_WEEK+6)%7;
			if(last_day_of_week==0)
				last_day_of_week+=7;
			//Toast.makeText(getApplicationContext(), String.valueOf(last_day_of_week), Toast.LENGTH_SHORT).show();
			if(dayOfWeek!=last_day_of_week)
			{
				c.add(Calendar.DAY_OF_MONTH, -1*(dayOfWeek-FIRST_DAY_OF_WEEK));
				//printDate(c,"Start Date");
				startDay=c.get(Calendar.DAY_OF_MONTH);
				startMonth=c.get(Calendar.MONTH)+1;
				startYear=c.get(Calendar.YEAR);
				
				//c=Calendar.getInstance();
				c.set(year,month_id-1, day);
				c.add(Calendar.DAY_OF_MONTH,(8-dayOfWeek));
				//printDate(c,"End Date");
				endDay=c.get(Calendar.DAY_OF_MONTH);
				endMonth=c.get(Calendar.MONTH)+1;
				endYear=c.get(Calendar.YEAR);
			}
			else
			{
				c.add(Calendar.DAY_OF_MONTH, -1*(6));
				//printDate(c,"Start Date");
				startDay=c.get(Calendar.DAY_OF_MONTH);
				startMonth=c.get(Calendar.MONTH)+1;
				startYear=c.get(Calendar.YEAR);
				
				//c=Calendar.getInstance();
				c.set(year,month_id-1, day);
				//printDate(c,"End Date");
				endDay=c.get(Calendar.DAY_OF_MONTH);
				endMonth=c.get(Calendar.MONTH)+1;
				endYear=c.get(Calendar.YEAR);	
			}
			
			String startDate,endDate;
			if(startMonth<10)
			{
				if(startDay<=9)
				{
					startDate=startYear+""+"0"+(startMonth)+""+"0"+startDay;
				}
				else
				{
					startDate=startYear+""+"0"+(startMonth)+""+startDay;
				}
			}
			else
			{
				if(startDay<=9)
				{
					startDate=startYear+""+(startMonth)+""+"0"+startDay;
				}
				else
				{
					startDate=startYear+""+(startMonth)+""+startDay;
				}
			}
			if(endMonth<10)
			{
				if(endDay<=9)
				{
					endDate=endYear+""+"0"+(endMonth)+""+"0"+endDay;
				}
				else
				{
					endDate=endYear+""+"0"+(endMonth)+""+endDay;
				}
			}
			else
			{
				if(endDay<=9)
				{
					endDate=endYear+""+(endMonth)+""+"0"+endDay;
				}
				else
				{
					endDate=endYear+""+(endMonth)+""+endDay;
				}
			}
			int sDate=Integer.parseInt(startDate);
			int eDate=Integer.parseInt(endDate);
			//Toast.makeText(getApplicationContext(),"StarDate:"+startDate+" EndDate:"+endDate, Toast.LENGTH_SHORT).show();
			cursor=mydb.eWeeklyExpenses(startDate,endDate);
			startManagingCursor(cursor);
			
			String[] from = new String[] {CPDataBase.EXP_KEY_DATE,"sum_weekly_expenses"};
			int[] to = new int[] { R.id.expense_list_row_tag,R.id.expense_list_row_value };
			//weekly_expenses_header.setText((sDate/10000)+"-"+((sDate%100)/100)+"-"+(sDate%100)+"  -  "
			//		+(eDate/10000)+"-"+((eDate%100)/100)+"-"+(eDate%100));
			weekly_expenses_header_new.setText(startYear+"-"+startMonth+"-"+startDay+" to "+endYear+"-"+endMonth+"-"+endDay);
			weekly_expenses_header.setText(startDate+" - "+endDate);
			try{
				int weekly_expenses_total_value=mydb.eWeeklyExpensesTotal(startDate,endDate);
				//Toast.makeText(getApplicationContext(), "ExpenseList.java fill_weekly_expenses:  "+String.valueOf(weekly_expenses_total_value), Toast.LENGTH_SHORT).show();
				weekly_expenses_total.setText(String.valueOf(weekly_expenses_total_value));
			}
			catch(Exception e)
			{
				//Toast.makeText(getApplicationContext(), "ExpenseList.java fill_weekly_expenses:  "+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			weekly_expenses.setAdapter(new ExpensesListAdapter(from, to,2,cursor));
			mydb.close();
		}
		void fill_daily_expenses(int year,int month_id,int day)
		{
			mydb.open();
			Cursor cursor;
			final Calendar c = Calendar.getInstance();
			if(year==-1)
			{
				year= c.get(Calendar.YEAR);
			}
			if(month_id==-1)
			{
				month_id= c.get(Calendar.MONTH);
				month_id++;
			}
			if(day==-1)
			{
				day = c.get(Calendar.DAY_OF_MONTH);
			}
			c.set(year,month_id-1,day);
			
			cursor=mydb.eDailyExpenses(year,month_id,day);
			startManagingCursor(cursor);
			String[] from = new String[] {CPDataBase.EXP_KEY_TIME,CPDataBase.EXP_KEY_CATEGORY_NAME,CPDataBase.EXP_KEY_PAYMENT_NAME,CPDataBase.EXP_KEY_AMOUNT,
					CPDataBase.EXP_KEY_DESCRIPTION};
			int[] to = new int[] {R.id.delr_time, R.id.delr_category,R.id.delr_payment_type,R.id.delr_amount,R.id.delr_descrip};
			
			String month_id_string,day_string;
			
			if(month_id<10)
				month_id_string="0"+String.valueOf(month_id);
			else
				month_id_string=String.valueOf(month_id);
			
			if(day<10)
				day_string="0"+String.valueOf(day);
			else
				day_string=String.valueOf(day);
			
			daily_expenses_header.setText(year+"-"+month_id_string+"-"+day_string);
			try{
				int daily_expenses_total_value=mydb.eDailyExpensesTotal(year, month_id, day);
				//Toast.makeText(getApplicationContext(), "ExpenseList.java fill_daily_expenses:  "+String.valueOf(daily_expenses_total_value), Toast.LENGTH_SHORT).show();
				daily_expenses_total.setText(String.valueOf(daily_expenses_total_value));
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "ExpenseList.java fill_daily_expenses:  "+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			daily_expenses.setAdapter(new ExpensesListAdapter(from, to,3,cursor));
			mydb.close();
		}
		
		void fill_all_expenses()
		{
			Cursor cursor;
			mydb.open();
			cursor=mydb.eAllExpenses();
			startManagingCursor(cursor);
			//String[] from = new String[] {CPDataBase.EXP_KEY_DESCRIPTION};
	
			//int[] to = new int[] { R.id.text1 };
	
			//SimpleCursorAdapter expenses = new SimpleCursorAdapter(this, R.layout.row,
			//		cursor, from, to);
			String[] from = new String[] {CPDataBase.EXP_KEY_TIME,CPDataBase.EXP_KEY_CATEGORY_NAME,CPDataBase.EXP_KEY_PAYMENT_NAME,CPDataBase.EXP_KEY_AMOUNT,
					CPDataBase.EXP_KEY_DESCRIPTION};
			int[] to = new int[] {R.id.delr_time, R.id.delr_category,R.id.delr_payment_type,R.id.delr_amount,R.id.delr_descrip};
			all_expenses.setAdapter(new ExpensesListAdapter(from, to,4,cursor));
			int all_expenses_total_value=mydb.eAllExpensesTotal();
			all_expenses_total.setText(String.valueOf(all_expenses_total_value));
			mydb.close();
		}

	    class ExpensesListAdapter extends SimpleCursorAdapter
	    {
	    	TextView tag,value;
	    	int code;
	    	Cursor cursor;
	    	public ExpensesListAdapter(String from[],int to[],int code,Cursor cursor) {
	    		super(ExpenseList.this, R.layout.expenses_list_row, cursor, from, to);
	    		this.code=code;
	    		this.cursor=cursor;
			}
	    	public View getView(int position,View convertView,ViewGroup parent)
	    	{
	    		View row=null;
	    		if(code==0)
	    		{
	    			row=setGetViewForYearlyExpenses(position, convertView, parent);
	    		}
	    		else if(code==1)
	    		{
	    			row=setGetViewForMonthlyExpenses(position, convertView, parent);
	    		}
	    		else if(code==2)
	    		{
	    			row=setGetViewForWeeklyExpenses(position, convertView, parent);
	    		}
	    		else if(code==3)
	    		{
	    			row=setGetViewForDailyExpenses(position, convertView, parent);
	    		}
	    		else if(code==4)
	    		{
	    			row=setGetViewForAllExpenses(position, convertView, parent);
	    		}
	    		else
	    		{
	    			//Do Nothing
	    		}
	    		return(row);
	    		
	    	}
	    	public View setGetViewForYearlyExpenses(int position,View convertView,ViewGroup parent)
	    	{
	    		String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
	    		View row;
	    		row=convertView;
	    		if(row==null)
	    		{
	    			LayoutInflater inflater=getLayoutInflater();
	    								row=inflater.inflate(R.layout.expenses_list_row, parent, false);
	    		}
				tag=(TextView)row.findViewById(R.id.expense_list_row_tag);
				value=(TextView)row.findViewById(R.id.expense_list_row_value);
				
				cursor.moveToPosition(position);
	    		int month_id_index=cursor.getColumnIndex("month_id");
	    		int month_id=cursor.getInt(month_id_index);
	    		tag.setText(months[month_id-1]);
	    		
	    		int sum_month_expenses_index=cursor.getColumnIndex("sum_month_expenses");
	    		int sum_month_expenses=cursor.getInt(sum_month_expenses_index);
	    		value.setText(String.valueOf(sum_month_expenses));
	    		
	    		return(row);
	    	}
	    	public View setGetViewForMonthlyExpenses(int position,View convertView,ViewGroup parent)
	    	{
	    		View row;
	    		row=convertView;
	    		if(row==null)
	    		{
	    			LayoutInflater inflater=getLayoutInflater();
	    			row=inflater.inflate(R.layout.expenses_list_row, parent, false);
	    		}
				tag=(TextView)row.findViewById(R.id.expense_list_row_tag);
				value=(TextView)row.findViewById(R.id.expense_list_row_value);
				
				cursor.moveToPosition(position);
	    		int day_index=cursor.getColumnIndex("day");
	    		int day=cursor.getInt(day_index);
	    		tag.setText(String.valueOf(day));
	    		
	    		int sum_daily_expenses_index=cursor.getColumnIndex("sum_daily_expenses");
	    		int sum_daily_expenses=cursor.getInt(sum_daily_expenses_index);
	    		value.setText(String.valueOf(sum_daily_expenses));
	    		
	    		return(row);
	    	}
	    	public View setGetViewForWeeklyExpenses(int position,View convertView,ViewGroup parent)
	    	{
	    		View row;
	    		row=convertView;
	    		if(row==null)
	    		{
	    			LayoutInflater inflater=getLayoutInflater();
	    			row=inflater.inflate(R.layout.expenses_list_row, parent, false);
	    		}
				tag=(TextView)row.findViewById(R.id.expense_list_row_tag);
				value=(TextView)row.findViewById(R.id.expense_list_row_value);
				
				cursor.moveToPosition(position);
				
	    		int date_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_DATE);
	    		int date=cursor.getInt(date_index);
	    		if((date/100)%100<10)
	    		{
	    			if((date%100)<10)
	    			{
	    				tag.setText(String.valueOf((date/10000)+"-"+"0"+((date/100)%100)+"-"+"0"+(date%100)));
	    			}
	    			else
	    				tag.setText(String.valueOf((date/10000)+"-"+"0"+((date/100)%100)+"-"+(date%100)));
	    		}
	    		else
	    		{
	    			if((date%100)<10)
	    			{
	    				tag.setText(String.valueOf((date/10000)+"-"+((date/100)%100)+"-"+"0"+(date%100)));
	    			}
	    			else
	    				tag.setText(String.valueOf((date/10000)+"-"+((date/100)%100)+"-"+(date%100)));
	    		}
	    		//tag.setText(String.valueOf((date/10000)+"-"+((date/100)%100)+"-"+(date%100)));
	    		//tag.setText(String.valueOf(date));
	    		
	    		int sum_weekly_expenses_index=cursor.getColumnIndex("sum_weekly_expenses");
	    		int sum_weekly_expenses=cursor.getInt(sum_weekly_expenses_index);
	    		value.setText(String.valueOf(sum_weekly_expenses));
	    		
	    		return(row);
	    	}
	    	public View setGetViewForDailyExpenses(int position,View convertView,ViewGroup parent)
	    	{	
	    		TextView category,payment_type,amount,descrip,time;
	    		View row;
	    		row=convertView;
	    		if(row==null)
	    		{
	    			LayoutInflater inflater=getLayoutInflater();
	    			row=inflater.inflate(R.layout.day_expenses_list_row, parent, false);
	    		}

	    		category=(TextView) row.findViewById(R.id.delr_category);
	    		payment_type=(TextView)row.findViewById(R.id.delr_payment_type);
	    		amount=(TextView)row.findViewById(R.id.delr_amount);
	    		descrip=(TextView)row.findViewById(R.id.delr_descrip);
	    		time=(TextView)row.findViewById(R.id.delr_time);
	    		
				cursor.moveToPosition(position);
				
	    		int category_name_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_CATEGORY_NAME);
	    		String category_name=cursor.getString(category_name_index);
	    		category.setText(category_name);
	    		
	    		int payment_name_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_PAYMENT_NAME);
	    		String payment_name=cursor.getString(payment_name_index);
	    		payment_type.setText(payment_name);
	    		
	    		int amount_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_AMOUNT);
	    		int amount_value=cursor.getInt(amount_value_index);
	    		amount.setText(String.valueOf(amount_value));
	    		
	    		int descrip_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_DESCRIPTION);
	    		String descrip_value=cursor.getString(descrip_value_index);
	    		descrip.setText(descrip_value);
	    		
	    		int time_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_TIME);
	    		String time_value=cursor.getString(time_value_index);
	    		time.setText(time_value);
	    		
	    		
	    		return(row);
	    	}
	    	public View setGetViewForAllExpenses(int position,View convertView,ViewGroup parent)
	    	{	
	    		TextView category,payment_type,amount,descrip,time;
	    		View row;
	    		row=convertView;
	    		if(row==null)
	    		{
	    			LayoutInflater inflater=getLayoutInflater();
	    			row=inflater.inflate(R.layout.day_expenses_list_row, parent, false);
	    		}

	    		category=(TextView) row.findViewById(R.id.delr_category);
	    		payment_type=(TextView)row.findViewById(R.id.delr_payment_type);
	    		amount=(TextView)row.findViewById(R.id.delr_amount);
	    		descrip=(TextView)row.findViewById(R.id.delr_descrip);
	    		time=(TextView)row.findViewById(R.id.delr_time);
	    		
				cursor.moveToPosition(position);
				
	    		int category_name_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_CATEGORY_NAME);
	    		String category_name=cursor.getString(category_name_index);
	    		category.setText(category_name);
	    		
	    		int payment_name_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_PAYMENT_NAME);
	    		String payment_name=cursor.getString(payment_name_index);
	    		payment_type.setText(payment_name);
	    		
	    		int amount_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_AMOUNT);
	    		int amount_value=cursor.getInt(amount_value_index);
	    		amount.setText(String.valueOf(amount_value));
	    		
	    		int descrip_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_DESCRIPTION);
	    		String descrip_value=cursor.getString(descrip_value_index);
	    		descrip.setText(descrip_value);
	    		
	    		int time_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_TIME);
	    		String time_value=cursor.getString(time_value_index);
	    		int date_value_index=cursor.getColumnIndex(CPDataBase.EXP_KEY_DATE);
	    		int date_value=cursor.getInt(date_value_index);
	    		String date=(date_value/10000)+"-"+(date_value/100)%100+"-"+(date_value%100);
	    		time.setText(date+", "+time_value);
	    		
	    		
	    		return(row);
	    	}
	    	
	    }
	    //my part
	    public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.add(0, MENU_DEL, 0, "Delete");
		}
		public boolean onContextItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case MENU_DEL:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
						.getMenuInfo();
				//Enter the query to delete the node
				//mydbhelper.deleteNote(info.id);
				Toast.makeText(getApplicationContext(), "info.id="+info.id, Toast.LENGTH_LONG).show();
			try{
				mydb.open();
				mydb.edeleteNote(info.id);
				mydb.close();
				fillData();
			}
			catch(Exception e)
			{
				//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
				break;

			}
			return super.onContextItemSelected(item);
		}
		
		//custom expenses part
			// /DATE PICKER DIAlOG
			private DatePickerDialog.OnDateSetListener mDateSetListener_from = new DatePickerDialog.OnDateSetListener() {

				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mYear1 = year;
					mMonth1 = monthOfYear;
					mDay1 = dayOfMonth;
					str_from = mDay1 + "-" + (mMonth1 + 1) + "-" + mYear1;
					from_text.setText(str_from);
					from_date = convert_to_date_string(mMonth1, mDay1, mYear1);
					//Toast.makeText(getApplicationContext(), "from_date=" + from_date,
					//		Toast.LENGTH_SHORT).show();
					fill_custom_expenses();

				}
			};
			private DatePickerDialog.OnDateSetListener mDateSetListener_to = new DatePickerDialog.OnDateSetListener() {

				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mYear1 = year;
					mMonth1 = monthOfYear;
					mDay1 = dayOfMonth;
					// updateDisplay();
					str_to = mDay1 + "-" + (mMonth1 + 1) + "-" + mYear1;
					to_text.setText(str_to);

					to_date = convert_to_date_string(mMonth1, mDay1, mYear1);
					//Toast.makeText(getApplicationContext(), "to_date=" + to_date,
					//		Toast.LENGTH_SHORT).show();
					fill_custom_expenses();

				}
			};

			
			protected Dialog onCreateDialog(int id) {
				// TODO Auto-generated method stub
				switch (id) {
				case DATE_PICK_FROM:
					return new DatePickerDialog(this, mDateSetListener_from, mYear,
							mMonth, mDay);
				case DATE_PICK_TO:
					return new DatePickerDialog(this, mDateSetListener_to, mYear,
							mMonth, mDay);
				}
				return null;

			}

			// This methods takes the date which was picked from date picker dialog and
			// converts it into
			// format which is used to store the date in database
			private String convert_to_date_string(int month, int day, int year) {

				String date = null;
				if (month <= 9) {
					if (day <= 9) {
						date = year + "" + "0" + (month+1 ) + "" + "0" + day;
					} else {
						date = year + "" + "0" + (month+1) + "" + day;
					}
				} else {
					if (day <= 9) {
						date = year + "" + (month +1) + "" + "0" + day;
					} else {
						date = year + "" + (month +1) + "" + day;
					}
				}
				return date;

			}
			// ----------------------------------------------End of date picker and
			// related stuff-------------------------------------------------------///
			void fill_custom_expenses()
			{
				
				try{
				mydb.open();
				Cursor cursor;
				cursor=mydb.eCustomExpenses(from_date, to_date);
				startManagingCursor(cursor);
				String[] from = new String[] {CPDataBase.EXP_KEY_CATEGORY_NAME,CPDataBase.EXP_KEY_PAYMENT_NAME,CPDataBase.EXP_KEY_AMOUNT,
						CPDataBase.EXP_KEY_DESCRIPTION};
				int[] to = new int[] { R.id.delr_category,R.id.delr_payment_type,R.id.delr_amount,R.id.delr_descrip};
				//daily_expenses_header.setText(String.valueOf(day));
				//daily_expenses_total.setText(String.valueOf(mydb.eDailyExpensesTotal(year,month_id,day)));
				custom_expenses.setAdapter(new ExpensesListAdapter(from, to,3,cursor));
				
				int custom_expenses_total_value=mydb.eWeeklyExpensesTotal(from_date, to_date);
				custom_expenses_total.setText(String.valueOf(custom_expenses_total_value));
				mydb.close();
				}
				catch (Exception e) {
					// TODO: handle exception
					//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
				
			}
			private void plotYearGraph() {
				try {
					// fetching values part
					Cursor cursor;
					int year=-1, month_count = 0,tmp;
					float[] exp = new float[12];
					String[] month = new String[12];
					String[] ver_values=new String[3];

					mydb.open();
					final Calendar c = Calendar.getInstance();
					String year_string=(String)yearly_expenses_header.getText();
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
					float max=exp[0];
					float sum=0;
					for(int i=0;i<12;++i)
					{
						if(exp[i]>max)
							max=exp[i];
						sum=sum+exp[i];
						
						
					}
					float avg=sum/12;
					ver_values[0]=String.valueOf((int)max);
					ver_values[1]=String.valueOf((int)avg);
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
