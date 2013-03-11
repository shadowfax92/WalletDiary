package com.nan.walletdiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class CPDataBase {

	// /expenses payment database
	public static final String P_KEY_PAYMENT = "payment";
	public static final String P_KEY_ROWID = "_id";
	public static final String P_KEY_DEFAULT = "dflt";
	// /expenses category database
	public static final String C_KEY_CATEGORY = "category";
	public static final String C_KEY_ROWID = "_id";
	public static final String C_KEY_DEFAULT = "dflt";
	// /add expense database
	public static final String EXP_KEY_ROWID = "_id";
	public static final String EXP_KEY_ACCOUNT = "account";
	public static final String EXP_KEY_DATE = "date_exp";
	public static final String EXP_KEY_CATEGORY_NAME = "cat_name";
	public static final String EXP_KEY_CATEGORY_ID = "cat_id";
	public static final String EXP_KEY_PAYMENT_NAME = "pay_name";
	public static final String EXP_KEY_PAYMENT_ID = "pay_id";
	public static final String EXP_KEY_AMOUNT = "amount";
	public static final String EXP_KEY_NO = "num";
	public static final String EXP_KEY_DESCRIPTION = "descp";
	public static final String EXP_KEY_TIME = "time";
	
	//add income database
	public static final String INCOME_KEY_ROWID = "_id";
	public static final String INCOME_KEY_ACCOUNT = "account";
	public static final String INCOME_KEY_DATE = "date_exp";
	public static final String INCOME_KEY_CATEGORY_NAME = "cat_name";
	public static final String INCOME_KEY_CATEGORY_ID = "cat_id";
	public static final String INCOME_KEY_PAYMENT_NAME = "pay_name";
	public static final String INCOME_KEY_PAYMENT_ID = "pay_id";
	public static final String INCOME_KEY_AMOUNT = "amount";
	public static final String INCOME_KEY_NO = "num";
	public static final String INCOME_KEY_DESCRIPTION = "descp";
	public static final String INCOME_KEY_TIME = "time";
	
	// /income category database
	public static final String IC_KEY_CATEGORY = "category";
	public static final String IC_KEY_ROWID = "_id";
	public static final String IC_KEY_DEFAULT = "dflt";
	
	private static final String TAG = "DbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;

	private static final String PAYMENT_TABLE_CREATE = "create table payment_list (_id integer primary key autoincrement, "
			+ "payment text not null,dflt integer);";
	private static final String CATEGORY_TABLE_CREATE = "create table category_list (_id integer primary key autoincrement, "
			+ "category text not null,dflt integer);";
	private static final String EXP_TABLE_CREATE = "create table expenses (_id integer primary key autoincrement,account text,"
			+ "date_exp integer,time text,cat_name text,cat_id integer,pay_name text,pay_id integer,amount int,num int,descp text);";
	
	private static final String INCOME_CATEGORY_TABLE_CREATE = "create table income_category (_id integer primary key autoincrement, "
		+ "category text not null,dflt integer);";
	private static final String INCOME_TABLE_CREATE = "create table income (_id integer primary key autoincrement,account text,"
		+ "date_exp integer,time text,cat_name text,cat_id integer,amount int,num int,descp text);";
	
	private static final String DATABASE_NAME = "CPDatabase";
	private static final String PAYMENT_TABLE_NAME = "payment_list";
	private static final String CATEGORY_TABLE_NAME = "category_list";
	private static final String INCOME_CATEGORY_TABLE_NAME = "income_category";
	private static final String EXP_TABLE_NAME = "expenses";
	private static final String INCOME_TABLE_NAME = "income";
	
	private static final int DATABASE_VERSION = 1;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		 
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(PAYMENT_TABLE_CREATE);
			db.execSQL(CATEGORY_TABLE_CREATE);
			db.execSQL(EXP_TABLE_CREATE);
			db.execSQL(INCOME_TABLE_CREATE);
			db.execSQL(INCOME_CATEGORY_TABLE_CREATE);

		}

		 
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS payment_list");
			db.execSQL("DROP TABLE IF EXISTS category_list");
			db.execSQL("DROP TABLE IF EXISTS expenses");
			db.execSQL("DROP TABLE IF EXISTS income_category");
			db.execSQL("DROP TABLE IF EXISTS income");
			onCreate(db);
		}
	}

	public CPDataBase(Context ctx) {
		this.mCtx = ctx;
	}

	public CPDataBase open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	// ///PAYMENT TABLE PART//////////////////////////////////
	public long pcreateNote(String pay, int def) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(P_KEY_PAYMENT, pay);
		initialValues.put(P_KEY_DEFAULT, String.valueOf(def));

		return mDb.insert(PAYMENT_TABLE_NAME, null, initialValues);
	}

	public void pdeleteNote(long rowId) {
		mDb.execSQL("delete from payment_list where _id=" + rowId + ";");
		// return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor pfetchAllNotes() {

		return mDb.query(PAYMENT_TABLE_NAME, new String[] { P_KEY_ROWID,
				P_KEY_PAYMENT }, null, null, null, null, null);
	}

	public Cursor pfetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, PAYMENT_TABLE_NAME, new String[] { P_KEY_ROWID,
				P_KEY_PAYMENT }, P_KEY_ROWID + "=" + rowId, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	

	public int pcheck() {
		// Cursor
		// check=mDb.rawQuery("select count(*) from category_list where dflt=1",
		// null);
		SQLiteStatement s1 = mDb
				.compileStatement("select count(*) from payment_list where dflt=1");
		long count = s1.simpleQueryForLong();
		if (count == 4) {
			return 1;
		} else {
			return 0;
		}

	}

	// ///////END OF PAYMENT TABLE//////////////////////

	// ////////CATEGORY TABLE START///////////////////////////
	public long ccreateNote(String cat, int def) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(C_KEY_CATEGORY, cat);
		initialValues.put(C_KEY_DEFAULT, String.valueOf(def));

		return mDb.insert(CATEGORY_TABLE_NAME, null, initialValues);
	}

	public void cdeleteNote(long rowId) {
		mDb.execSQL("delete from category_list where _id=" + rowId + ";");
		// return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor cfetchAllNotes() {

		return mDb.query(CATEGORY_TABLE_NAME, new String[] { C_KEY_ROWID,
				C_KEY_CATEGORY }, null, null, null, null, null);
	}

	public Cursor cfetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, CATEGORY_TABLE_NAME, new String[] { C_KEY_ROWID,
				C_KEY_CATEGORY }, C_KEY_ROWID + "=" + rowId, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public int ccheck() {
		// Cursor
		// check=mDb.rawQuery("select count(*) from category_list where dflt=1",
		// null);
		SQLiteStatement s = mDb
				.compileStatement("select count(*) from category_list where dflt=1");
		long count = s.simpleQueryForLong();
		if (count == 13) {
			return 1;
		} else {
			return 0;
		}

	}

	// /////////////////END OF CATEGORY TABLE/////////////////////

	// ///////////////EXPENSES TABLE PART//////////////////////////
	public long ecreateNote(String acc, int expense_date,String exp_time, String cat_name,
			long cat_id, String pay_name, long pay_id, int amount, String number,
			String descp) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(EXP_KEY_ACCOUNT, acc);
		initialValues.put(EXP_KEY_TIME,exp_time);
		initialValues.put(EXP_KEY_DATE, String.valueOf(expense_date));
		initialValues.put(EXP_KEY_CATEGORY_NAME, cat_name);
		initialValues.put(EXP_KEY_CATEGORY_ID, String.valueOf(cat_id));
		initialValues.put(EXP_KEY_PAYMENT_NAME, pay_name);
		initialValues.put(EXP_KEY_PAYMENT_ID, String.valueOf(pay_id));
		initialValues.put(EXP_KEY_AMOUNT, String.valueOf(amount));
		initialValues.put(EXP_KEY_NO, number);
		initialValues.put(EXP_KEY_DESCRIPTION, descp);

		return mDb.insert(EXP_TABLE_NAME, null, initialValues);
	}

	public void edeleteNote(long rowId) {
		mDb.execSQL("delete from expenses where _id=" + rowId + ";");
		// return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor efetchAllNotes() {

		return mDb.query(EXP_TABLE_NAME, new String[] { EXP_KEY_ROWID,
				EXP_KEY_ACCOUNT, EXP_KEY_DATE,EXP_KEY_TIME , EXP_KEY_CATEGORY_ID,
				EXP_KEY_CATEGORY_NAME, EXP_KEY_PAYMENT_NAME,
				EXP_KEY_PAYMENT_ID, EXP_KEY_AMOUNT, EXP_KEY_NO,
				EXP_KEY_DESCRIPTION }, null, null, null, null, null);
	}

	public Cursor efetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, EXP_TABLE_NAME, new String[] { EXP_KEY_ROWID,
				EXP_KEY_ACCOUNT, EXP_KEY_DATE,EXP_KEY_TIME , EXP_KEY_CATEGORY_ID,
				EXP_KEY_CATEGORY_NAME, EXP_KEY_PAYMENT_NAME,
				EXP_KEY_PAYMENT_ID, EXP_KEY_AMOUNT, EXP_KEY_NO,
				EXP_KEY_DESCRIPTION }, EXP_KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	public Cursor eYearlyExpenses(String year) {
		Cursor cursor=null; 
		cursor=mDb.rawQuery("select *,month_id,sum(amount) as sum_month_expenses"+
				" from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id from expenses) "+
				"group by year,month_id having year="+year+" order by month_id", null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
		}
		return(cursor);
	}
	public int eYearlyExpensesTotal(int year)
	{
		int res=0;
		Cursor cursor=mDb.rawQuery("select *,sum(amount) as sum_total_yearly_expenses "+
				"from (select *,(date_exp/10000) as year from expenses) "+
				"group by year having year="+year, null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			String sum_total_yearly_expenses_string=cursor.getString(cursor.getColumnIndex("sum_total_yearly_expenses"));
			if(sum_total_yearly_expenses_string!=null)
				res=Integer.parseInt(sum_total_yearly_expenses_string);
		}
		return(res);		
	}
	public Cursor eMonthlyExpenses(int year,int month_id)
	{
		Cursor cursor=mDb.rawQuery("select *,year,month_id,day,sum(amount) as sum_daily_expenses "+
				"from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id,(date_exp%100) as day from expenses) "+
				"group by year,month_id,day "+
				"having year="+year+" and month_id="+month_id,null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
		}
		return(cursor);
	}
	public int eMonthlyExpensesTotal(int year,int month_id)
	{
		int res=0;
		Cursor cursor=mDb.rawQuery("select *,year,sum(amount) as sum_total_monthly_expenses "+
				"from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id from expenses) "+
				"group by year,month_id "+
				"having year="+year+" and month_id="+month_id,null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			String sum_total_monthly_expenses_string=cursor.getString(cursor.getColumnIndex("sum_total_monthly_expenses"));
			if(sum_total_monthly_expenses_string!=null)
				res=Integer.parseInt(sum_total_monthly_expenses_string);
		}
		return(res);		
	}
	public Cursor eWeeklyExpenses(String startDate,String endDate)
	{
		Cursor cursor=mDb.rawQuery("select *,date_exp,year,month_id,day,sum(amount) as sum_weekly_expenses "+
				"from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id,(date_exp%100) as day from expenses) "+
				"group by year,month_id,day "+
				"having date_exp between "+startDate+" and "+endDate, null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			
		}
		return(cursor);
	}
	public int eWeeklyExpensesTotal(String startDate,String endDate)
	{
		int res=0;
		Cursor cursor=mDb.rawQuery("select *,sum(amount) as sum_total_weekly_expenses "+
				"from expenses where date_exp between "+startDate+" and "+endDate, null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			String sum_total_weekly_expenses_string=cursor.getString(cursor.getColumnIndex("sum_total_weekly_expenses"));
			if(sum_total_weekly_expenses_string!=null)
				res=Integer.parseInt(sum_total_weekly_expenses_string);
		}
		return(res);		
	}
	public Cursor eDailyExpenses(int year,int month_id,int day)
	{
		Cursor cursor=mDb.rawQuery("select *,year,month_id,day,amount "+
				"from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id,(date_exp%100) as day from expenses) "+
				"where year="+year+" and month_id="+month_id+" and day="+day+" order by time", null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
		}
		return(cursor);
	}
	public int eDailyExpensesTotal(int year,int month_id,int day)
	{
		int res=0;
		/*Cursor cursor=mDb.rawQuery("select *,sum(amount) as sum_total_daily_expenses "+
				"from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id,(date_exp%100) as day from expenses) "+
				"group by year,month_id,day "+
				"having year="+year+" and month_id="+month_id+" and day="+day, null);*/
		/*Cursor cursor=mDb.rawQuery("select *,sum(amount) as sum_total_daily_expenses "+
				"from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id,(date_exp%100) as day from expenses) "+
				"group by year,month_id,day "+
				"having year="+year+" and month_id="+month_id+" and day="+day,null);*/
		Cursor cursor=mDb.rawQuery("select *,sum(amount) as sum_total_daily_expenses from (select *,(date_exp/10000) as year,(date_exp/100)%100 as month_id,(date_exp%100) as day from expenses) "+
				"where year="+year+" and month_id="+month_id+" and day="+day, null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			String sum_total_daily_expenses_string=cursor.getString(cursor.getColumnIndex("sum_total_daily_expenses"));
			if(sum_total_daily_expenses_string!=null)
				res=Integer.parseInt(sum_total_daily_expenses_string);
		}
		
		
		return(res);		
	}
	public Cursor eCustomExpenses(String startDate,String endDate)
	{
		Cursor cursor=mDb.rawQuery("select * from expenses "+
				"where date_exp between "+startDate+" and "+endDate, null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
		}
		return(cursor);
	}
	public Cursor eAllExpenses() {
		Cursor cursor=mDb.rawQuery("select * from expenses order by date_exp,time", null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
		}
		return(cursor);
	}
	public int eAllExpensesTotal(){
		int res=0;
		Cursor cursor=mDb.rawQuery("select sum(amount) as total_expenses from expenses",null);
		if (cursor != null && cursor.getCount()!=0) {
			cursor.moveToFirst();
			String total_expenses_string=cursor.getString(cursor.getColumnIndex("total_expenses"));
			if(total_expenses_string!=null)
				res=Integer.parseInt(total_expenses_string);
		}
		return(res);
	}
	// /////////////////END OF EXPENSES TABLE//////////////////

	/*public void ecreateNote(String dbAccount, String dbDate, String dbCatName,
			long dbCatId, String dbPayName, long dbPayId, int dbAmount,
			String dbNumber, String dbDescp) {
		// TODO Auto-generated method stub
		
	}*/
	// ////////INCOME CATEGORY TABLE START///////////////////////////
	public long iccreateNote(String cat, int def) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(IC_KEY_CATEGORY, cat);
		initialValues.put(IC_KEY_DEFAULT, String.valueOf(def));

		return mDb.insert(INCOME_CATEGORY_TABLE_NAME, null, initialValues);
	}

	public void icdeleteNote(long rowId) {
		mDb.execSQL("delete from income_category where _id=" + rowId + ";");
		// return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor icfetchAllNotes() {

		return mDb.query(INCOME_CATEGORY_TABLE_NAME, new String[] { IC_KEY_ROWID,
				IC_KEY_CATEGORY }, null, null, null, null, null);
	}

	public Cursor icfetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, INCOME_CATEGORY_TABLE_NAME, new String[] { IC_KEY_ROWID,
				IC_KEY_CATEGORY }, IC_KEY_ROWID + "=" + rowId, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public int iccheck() {
		// Cursor
		// check=mDb.rawQuery("select count(*) from income_category where dflt=1",
		// null);
		SQLiteStatement s = mDb
				.compileStatement("select count(*) from income_category where dflt=1");
		long count = s.simpleQueryForLong();
		if (count == 4) {
			return 1;
		} else {
			return 0;
		}

	}

	// /////////////////END OF INCOME CATEGORY TABLE/////////////////////
	
	// ///////////////INCOME TABLE PART//////////////////////////
	public long icreateNote(String acc, int expense_date,String income_time, String cat_name,
			long cat_id,int amount, String number,
			String descp) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(INCOME_KEY_ACCOUNT, acc);
		initialValues.put(INCOME_KEY_TIME,income_time);
		initialValues.put(INCOME_KEY_DATE, String.valueOf(expense_date));
		initialValues.put(INCOME_KEY_CATEGORY_NAME, cat_name);
		initialValues.put(INCOME_KEY_CATEGORY_ID, String.valueOf(cat_id));
		initialValues.put(INCOME_KEY_AMOUNT, String.valueOf(amount));
		initialValues.put(INCOME_KEY_NO, number);
		initialValues.put(INCOME_KEY_DESCRIPTION, descp);

		return mDb.insert(INCOME_TABLE_NAME, null, initialValues);
	}

	public void ideleteNote(long rowId) {
		mDb.execSQL("delete from income where _id=" + rowId + ";");
		// return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor ifetchAllNotes() {

		return mDb.query(INCOME_TABLE_NAME, new String[] { INCOME_KEY_ROWID,
				INCOME_KEY_ACCOUNT, INCOME_KEY_DATE,INCOME_KEY_TIME , INCOME_KEY_CATEGORY_ID,
				INCOME_KEY_CATEGORY_NAME, INCOME_KEY_AMOUNT, INCOME_KEY_NO,
				INCOME_KEY_DESCRIPTION }, null, null, null, null, "date_exp,time");
	}

	public Cursor ifetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, INCOME_TABLE_NAME, new String[] { INCOME_KEY_ROWID,
				INCOME_KEY_ACCOUNT, INCOME_KEY_DATE,INCOME_KEY_TIME , INCOME_KEY_CATEGORY_ID,
				INCOME_KEY_CATEGORY_NAME,INCOME_KEY_AMOUNT, INCOME_KEY_NO,
				INCOME_KEY_DESCRIPTION }, INCOME_KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public int iTotalIncome()
	{
		int res=0;
		
		Cursor mcursor = mDb.rawQuery("select sum(amount) as total_income from "+INCOME_TABLE_NAME, null);
		if (mcursor != null) {
			mcursor.moveToFirst();
			String total_income_string=mcursor.getString(mcursor.getColumnIndex("total_income"));
			if(total_income_string!=null)
				res=Integer.parseInt(total_income_string);
		}
		return(res);
	}


	// /////////////////END OF INCOME TABLE//////////////////

}