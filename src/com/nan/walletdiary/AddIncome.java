package com.nan.walletdiary;

import java.util.Calendar;
import java.lang.String;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddIncome extends Activity implements OnClickListener {
	private ImageButton DatePicker, TimePicker;
	private Button save, back;
	private EditText amount_disp, number_disp, descp_disp;
	private TextView account_disp, date_disp, category_disp, time_disp;
	private Spinner cat_spin, pay_spin;
	private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
	private String db_date, db_account, db_cat_name, db_pay_name, db_number,
			db_descp, db_time;
	private int db_amount;
	private long db_cat_id, db_pay_id;

	private static final int DATE_PICK = 1002;
	private static final int CATEGORY_PICK = 1003;
	private static final int EDIT_CATEGORY = 1004;
	private static final int EDIT_PAYMENT = 1005;
	private static final int TIME_PICK = 1006;

	// /////////////////////DATABASE HELPER ELEMENTS/////////////////
	private CPDataBase mdbhelper;
	private Cursor ccursor, pcursor;// p stands for payment database cursor and

	// similarly c stands for category cursor

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_income);
		buttonInti();// Initialises all buttons and sets then on click listener
	}

	private void buttonInti() {
		DatePicker = (ImageButton) findViewById(R.id.date_picker_income);
		TimePicker = (ImageButton) findViewById(R.id.time_picker_income);

		save = (Button) findViewById(R.id.Save_income);
		back = (Button) findViewById(R.id.back_income);

		date_disp = (TextView) findViewById(R.id.date_disp_income);
		time_disp = (TextView) findViewById(R.id.time_disp_income);

		cat_spin = (Spinner) findViewById(R.id.category_spinner_income);
		// pay_spin = (Spinner) findViewById(R.id.payment_spinner);

		amount_disp = (EditText) findViewById(R.id.amount_disp_income);
		number_disp = (EditText) findViewById(R.id.number_disp_income);
		descp_disp = (EditText) findViewById(R.id.description_disp_income);

		// /////////////INTIALISING DATABASE AND POPULATING SPINNER/////////////

		mdbhelper = new CPDataBase(this);
		mdbhelper.open();
		populate_category();

		// /setting date to current date////////////////

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);

		String str_time = mHour + ":" + mMinute + ":" + mSecond;// fetching
																// current time
		time_disp.setText(str_time);
		db_time = str_time;

		String str_date = mYear + "-" + (mMonth + 1) + "-" + mDay;
		date_disp.setText(str_date);
		if (mMonth < 9) {
			if (mDay <= 9) {
				db_date = mYear + "" + "0" + (mMonth + 1) + "" + "0" + mDay;
			} else {
				db_date = mYear + "" + "0" + (mMonth + 1) + "" + mDay;
			}
		} else {
			if (mDay <= 9) {
				db_date = mYear + "" + (mMonth + 1) + "" + "0" + mDay;
			} else {
				db_date = mYear + "" + (mMonth + 1) + "" + mDay;
			}
		}// setting db_date i.e

		// the date to be
		// inserted into
		// database

		// ///setting buttons on click listener
		DatePicker.setOnClickListener(this);
		TimePicker.setOnClickListener(this);
		save.setOnClickListener(this);
		back.setOnClickListener(this);
		cat_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			
			public void onItemSelected(AdapterView arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				String selected_category=null;
				Cursor sel_cat = mdbhelper.cfetchNote(id);
				startManagingCursor(sel_cat);
				if(sel_cat!=null && sel_cat.getCount()!=0){
				selected_category = sel_cat.getString(sel_cat
						.getColumnIndexOrThrow(CPDataBase.C_KEY_CATEGORY));
				String str = "selected string " + selected_category;
				Toast
						.makeText(getApplicationContext(), str,
								Toast.LENGTH_SHORT).show();
				}
				stopManagingCursor(sel_cat);
				// setting value for database variables
				db_cat_name = selected_category;
				db_cat_id = id;

			}

			
			public void onNothingSelected(AdapterView arg0) {
				Cursor sel_cat = mdbhelper.cfetchNote(1);
				String selected_category=null;
				startManagingCursor(sel_cat);
				if(sel_cat!=null && sel_cat.getCount()!=0){
				selected_category = sel_cat.getString(sel_cat
						.getColumnIndexOrThrow(CPDataBase.C_KEY_CATEGORY));
				String str = "selected string " + selected_category;
				Toast
						.makeText(getApplicationContext(), str,
								Toast.LENGTH_SHORT).show();
				}
				stopManagingCursor(sel_cat);
				// setting value for database variables
				db_cat_name = selected_category;
				db_cat_id = 1;
				// TODO Auto-generated method stub

			}
		});
	}

	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.date_picker_income:
			// /creates a date picker dialog
			showDialog(DATE_PICK);
			break;
		case R.id.time_picker_income:
			showDialog(TIME_PICK);
			break;

		case R.id.Save_income:
			add_expenses_to_table();
			stopManagingCursor(ccursor);
			stopManagingCursor(pcursor);
			mdbhelper.close();
			setResult(RESULT_OK);
			finish();
			break;

		case R.id.back_income:
			stopManagingCursor(ccursor);
			stopManagingCursor(pcursor);
			mdbhelper.close();
			setResult(RESULT_CANCELED);
			finish();
			break;

		}

	}

	// ////Date picker and time picker part/////////////////////////////////////
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			String str_time = mHour + ":" + mMinute + ":" + mSecond;// fetching
																	// current
																	// time
			time_disp.setText(str_time);
			db_time = str_time;
			// TODO Auto-generated method stub

		}
	};
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			// updateDisplay();
			String str1 = mYear + "-" + (mMonth + 1) + "-" + mDay;
			date_disp.setText(str1);
			//Toast.makeText(getApplicationContext(), str1, Toast.LENGTH_SHORT)
			//		.show();
			if (mMonth < 9) {
				if (mDay <= 9) {
					db_date = mYear + "" + "0" + (mMonth + 1) + "" + "0" + mDay;
				} else {
					db_date = mYear + "" + "0" + (mMonth + 1) + "" + mDay;
				}
			} else {
				if (mDay <= 9) {
					db_date = mYear + "" + (mMonth + 1) + "" + "0" + mDay;
				} else {
					db_date = mYear + "" + (mMonth + 1) + "" + mDay;
				}
			}
			// i.e the date to
			// be inserted into
			// database

		}
	};

	
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DATE_PICK:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_PICK:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;

	}

	// ////////////End of Date picker part///////////////////////////

	// ////////////////////OPTIONS MENU PART/////////////////////////////
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(menu.NONE, EDIT_CATEGORY, menu.NONE, "Edit Category");
		// menu.add(menu.NONE, EDIT_PAYMENT, menu.NONE, "Edit payment");
		return super.onCreateOptionsMenu(menu);
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case EDIT_CATEGORY:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void populate_category() {
		// TODO Auto-generated method stub

		int ccheck = mdbhelper.iccheck();
		if (ccheck == 0) {
			mdbhelper.iccreateNote("Salary", 1);
			mdbhelper.iccreateNote("Personal Savings", 1);
			mdbhelper.iccreateNote("Rental", 1);
			mdbhelper.iccreateNote("Other", 1);

		}
		ccursor = mdbhelper.icfetchAllNotes();
		startManagingCursor(ccursor);
		if(ccursor!=null && ccursor.getCount()!=0){
		SimpleCursorAdapter category_adapter_income = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, ccursor,
				new String[] { CPDataBase.IC_KEY_CATEGORY },
				new int[] { android.R.id.text1 });
		category_adapter_income
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cat_spin.setAdapter(category_adapter_income);
		}
		stopManagingCursor(ccursor);

	}

	// //////////////ADDING EXPENSES TO TABLE/////////////////
	private void add_expenses_to_table() {
		// TODO Auto-generated method stub
		db_amount = Integer.parseInt(amount_disp.getText().toString());
		db_number = number_disp.getText().toString();
		db_descp = descp_disp.getText().toString();
		db_account = "default";
		int db_date_int = Integer.parseInt(db_date);
		mdbhelper.icreateNote(db_account, db_date_int, db_time, db_cat_name,
				db_cat_id, db_amount, db_number, db_descp);

	}

}