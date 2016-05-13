package com.example.wubingzhang.week9.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DBclass extends SQLiteOpenHelper {
private List<News> li=new ArrayList<News>();
private final static String DATABASE_NAME="d2.db3";
private static String DB_PATH="/data/data/com.example.wubingzhang.week9/databases/";
private static String DB_NAME=DATABASE_NAME;
SQLiteDatabase myDataBase;
Context myContext;
final static int DATABASE_VERSION=3;
public String TABLE_NAME="account";

public DBclass(Context context){
	super(context,DATABASE_NAME,null,DATABASE_VERSION+1);
	this.myContext=context;
}

@Override
public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	String sql="DROP TABLE IF EXISTS " +TABLE_NAME;
	db.execSQL(sql);
	onCreate(db);
}
public Cursor doSQL(String i_sql){
	SQLiteDatabase sqliteDB=this.getReadableDatabase();
	Cursor cursor=sqliteDB.rawQuery(i_sql, null);
	return cursor;
}
public Cursor select(){
	SQLiteDatabase db=myDataBase;
	Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
	return cursor;
}
public void opDropTable(){
	SQLiteDatabase db=this.getWritableDatabase();
	String sql="DROP TABLE IF EXISTS "+TABLE_NAME;
	db.execSQL(sql);
	onCreate(db);
}
public void setTableName(String itablename){
	TABLE_NAME=itablename;
}
public void createDataBase()throws IOException {
	boolean dbExist=checkDataBase();
	if(dbExist){
		
	}else{
		this.getReadableDatabase();
		Log.d("createdatabase", "not exist");
		try{
			copyDataBase();
			dbExist=checkDataBase();
			//this.openDataBase();
		}catch(IOException e){
			throw new Error("error copying database");
		}
	}
	Log.d("createdatabase", "created");
}
boolean checkDataBase(){
	SQLiteDatabase checkDB=null;
	try{
		String mypath=DB_PATH+DB_NAME;
		checkDB= SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
	}catch(SQLiteException e){
		
	}
	if(checkDB!=null){
		checkDB.close();
	}
	boolean b= checkDB!=null?true:false;
	if(b)
	Log.d("createdatabase", "exist");
	else
		Log.d("createdatabase", "notexist");
	return b;
}
void deleteDb(){
	String outfilename=DB_PATH+DB_NAME;
	File f=new File(outfilename);
	if(f.isFile()){
		f.delete();
	}
}
void copyDataBase()throws IOException {
	InputStream myinput=myContext.getAssets().open(DB_NAME);
	String outfilename=DB_PATH+DB_NAME;
	OutputStream myoutput=new FileOutputStream(outfilename);
	 
	byte[]buffer=new byte[1024];
	int length;
	while((length=myinput.read(buffer))>0){
		myoutput.write(buffer,0,length);
		
	}
	myoutput.flush();
	myoutput.close();
	myinput.close();
	Log.d("copydatabase", "copyed");
}
public void openDataBase()throws SQLiteException {
	String mypath=DB_PATH+DB_NAME;
	myDataBase= SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
	Log.d("opnedatabase", "poend");
}

@Override
public synchronized void close(){
	if(myDataBase!=null)
		myDataBase.close();
		super.close();
	
}
public long insert(float num,long time,String type){
	SQLiteDatabase db=myDataBase;
	ContentValues cv=new ContentValues();
	cv.put("number", num);
	cv.put("time", time);
	cv.put("type", type);
	long row=db.insert(TABLE_NAME, null, cv);
	return row;
}
public List<News> FunGetNewsInfo(Cursor myCursor){
	List<News> data2=new ArrayList<News>();
	String al="";
	if(myCursor.moveToFirst()){
		data2=new ArrayList<News>();
		do{
			News news=new News();
			news.number=myCursor.getFloat(myCursor.getColumnIndex("number"));
			news.time=myCursor.getLong(myCursor.getColumnIndex("time"));
			news.type=myCursor.getString(myCursor.getColumnIndex("type"));
			data2.add(news);
		}while(myCursor.moveToNext());
	}
	myCursor.close();
	return data2;
}
}










