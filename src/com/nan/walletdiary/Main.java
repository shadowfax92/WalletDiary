package com.nan.walletdiary;

import java.util.Calendar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ListActivity {
	private static final int ADD_EXPENSE_REQUEST=1001;
	private static final int ADD_INCOME_REQUEST=1010;
	private static final int SHOW_INCOMES_REQUEST=2000;
	private static final int FIRST_DAY_OF_WEEK=2;
	
	String tags[]={"This Year:","This Month:","This Week:","This Day:"};
	int year_expense=0,month_expense=0,week_expense=0,day_expense=0;

   	Button add_income,add_expense,settings;
   	
   	TextView account_balance,main_screen_total_income,main_screen_total_expenses;
   	TextView selection;
 
   	CPDataBase mydb;
   	 
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(new ExpenseTypeListAdapter());
        selection=(TextView)findViewById(R.id.selection);

        mydb=new CPDataBase(this);
        
        add_expense=(Button)findViewById(R.id.add_expenses);
        add_expense.setOnClickListener(new View.OnClickListener() {
			

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aii=new Intent(Main.this,AddExpenses.class);//aii stands for add income intent
				startActivityForResult(aii, ADD_EXPENSE_REQUEST);
				
			}
		});
        add_income=(Button)findViewById(R.id.add_income);
        add_income.setOnClickListener(new View.OnClickListener() {
			
			 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ai=new Intent(Main.this,AddIncome.class);
				startActivityForResult(ai, ADD_INCOME_REQUEST);
				
			}
		});
        settings=(Button)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
			
			 
			public void onClick(View v) {
				try{
				
				Intent i=new Intent(Main.this,IncomeList.class);
				startActivityForResult(i,SHOW_INCOMES_REQUEST);
				}
				catch(Exception e)
				{
					//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
        

   		updateMainScreen();
   	
    }
   	protected void onResume()
   	{
   		super.onResume();
   		updateMainScreen();

   	}
   	public void updateMainScreen()
   	{
   		Calendar c=Calendar.getInstance();
		mydb.open();
		year_expense=mydb.eYearlyExpensesTotal(c.get(Calendar.YEAR));
		month_expense=mydb.eMonthlyExpensesTotal(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
		//Code to find week_expense
		{
			int year=c.get(Calendar.YEAR);
			int month_id=c.get(Calendar.MONTH);
			month_id++;
			int day=c.get(Calendar.DAY_OF_MONTH);
			
			int startDay,startMonth,startYear;
			int endDay,endMonth,endYear;
			int dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
			
			int last_day_of_week;
			last_day_of_week=(FIRST_DAY_OF_WEEK+6)%7;
			if(last_day_of_week==0)
				last_day_of_week+=7;
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
			if(startMonth<9)
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
			if(endMonth<9)
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
			week_expense=mydb.eWeeklyExpensesTotal(startDate, endDate);
		}
		c=Calendar.getInstance();
		day_expense=mydb.eDailyExpensesTotal(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
		
		setListAdapter(new ExpenseTypeListAdapter());
		
		account_balance=(TextView)findViewById(R.id.account_balance);
		
		main_screen_total_income=(TextView)findViewById(R.id.main_screen_total_income);
		int income_total_value=mydb.iTotalIncome();
		main_screen_total_income.setText(String.valueOf(income_total_value));
		
		main_screen_total_expenses=(TextView)findViewById(R.id.main_screen_total_expenses);
		int expenses_total_value=mydb.eAllExpensesTotal();
		main_screen_total_expenses.setText(String.valueOf(expenses_total_value));
		
		int account_balance_value=income_total_value-expenses_total_value;
		account_balance.setText(String.valueOf(account_balance_value));
		mydb.close();

   	}
	 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==ADD_EXPENSE_REQUEST)
		{
			if(resultCode==RESULT_OK)
			{
				Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
				
			}
			if(resultCode==RESULT_CANCELED)
			{
				Toast.makeText(this, "Expense not added", Toast.LENGTH_SHORT).show();
			}
		}
		if(requestCode==ADD_INCOME_REQUEST)
		{
			if(resultCode==RESULT_OK)
			{
				Toast.makeText(this, "Income added", Toast.LENGTH_SHORT).show();
			}
			if(resultCode==RESULT_CANCELED)
			{
				Toast.makeText(this, "Income not added", Toast.LENGTH_SHORT).show();
			}
		}
		if(requestCode==SHOW_INCOMES_REQUEST)
		{
			if(resultCode==RESULT_OK)
			{
				//Toast.makeText(this, "Income Shown", Toast.LENGTH_SHORT).show();
			}
			if(resultCode==RESULT_CANCELED)
			{
				//Toast.makeText(this, "Income not Shown", Toast.LENGTH_SHORT).show();
			}
		}
	}
    public void onListItemClick(ListView parent, View v,int position, long id)
    {
    	Intent i=new Intent(Main.this,ExpenseList.class);
    	switch (position) {
		case 0:
			selection.setText(tags[position]);			
			i.putExtra("tab_index", 0);
			startActivity(i);
			break;
		case 1:
			selection.setText(tags[position]);
			i.putExtra("tab_index", 1);
			startActivity(i);
			break;
		case 2:
			selection.setText(tags[position]);
			i.putExtra("tab_index", 2);
			startActivity(i);
			break;
		case 3:
			selection.setText(tags[position]);
			i.putExtra("tab_index", 3);
			startActivity(i);
			break;
		default:
			break;
		}
    }
    class ExpenseTypeListAdapter extends ArrayAdapter
    {

		ExpenseTypeListAdapter(){
			super(Main.this,R.layout.expense_type_list_row,tags);
			
		}
		public View getView(int position,View convertView,ViewGroup parent)
		{
			View row=convertView;
			TextView tag,value;
			if (row==null) 
			{
				LayoutInflater inflater=getLayoutInflater();
				row=inflater.inflate(R.layout.expense_type_list_row, parent, false);
			}
			
			tag=(TextView)row.findViewById(R.id.tag);
			tag.setText(tags[position]);
			value=(TextView)row.findViewById(R.id.value);
			value.setText("unknown_value");
			switch(position)
			{
				case 0:
					value.setText(String.valueOf(year_expense));
					break;
				case 1:
					value.setText(String.valueOf(month_expense));
					break;
				case 2:
					value.setText(String.valueOf(week_expense));
					break;
				case 3:
					value.setText(String.valueOf(day_expense));
					break;
			}
			return row;
		}
    	
    }
}