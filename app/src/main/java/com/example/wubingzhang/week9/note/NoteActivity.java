package com.example.wubingzhang.week9.note;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.example.wubingzhang.week9.R;


public class NoteActivity extends ActionBarActivity implements OnClickListener{

Button btn,b2;
TextView tv2,tv1;
ListView lv1;
FileOutputStream fos=null;
FileInputStream fis=null;
DataOutputStream dos=null;
DataInputStream dis=null;

String[] from={"name","id"};              //这里是ListView显示内容每一列的列名
int[] to={R.id.notename};   //这里是ListView显示每一列对应的list_item中控件的id

ArrayList<HashMap<String,String>> list=null;
HashMap<String,String> map=null;
int numberofnotes,innerLastId;
String s2;
private SimpleAdapter adapter = null; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
      
        btn=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);
        btn.setOnClickListener(this);
        b2 .setOnClickListener(this);
        tv1=(TextView)findViewById(R.id.textView1);
        tv2=(TextView)findViewById(R.id.textView2);
        //tv3=(TextView)findViewById(R.id.textView3);
        lv1=(ListView)findViewById(R.id.listView1);
        lv1.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>)adapter.getItem(position);  
				String name = map.get("id");  
				//tv2.setText(name);
				openNote(name);
			}
        });
        try {
			if(this.openFileInput("index")==null){
				writezero();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
			writezero();
		}
        loadnum();
        loaditems();
    }
    void loadnum(){
    	try {
			if(openFileInput("index")!=null){
				
				try {
					fis=openFileInput("index");
					dis=new DataInputStream(fis);
					
					int i=dis.readInt();
					tv2.setText("note数量："+i);
					numberofnotes=i;
					
					i=dis.readInt();
					innerLastId=i;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					
						try {
							if(fis!=null)
							fis.close();
							if(dis!=null)
								dis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    void writezero(){
    	try {
			fos=this.openFileOutput("index", Context.MODE_PRIVATE);
			dos=new DataOutputStream(fos);
			dos.writeInt(0);
			dos.writeInt(0);
			
			//dos.writeChars(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			
				try {
					if(fos!=null)
					fos.close();
					if(dos!=null)
						dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    	loadnum();
        loaditems();
    	
    }
    String getName(int id){
    	String sname="";
    	try {
			if(this.openFileInput("sav"+id)!=null){
				try {
					fis=this.openFileInput("sav"+id);
					dis=new DataInputStream(fis);
					int i=dis.readInt();
					char[] c = new char[i+1];
					for(int j=0;j<i;j++){
						c[j]=dis.readChar();
					}
					sname=String.valueOf(c);
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
						try {
							if(fis!=null)
							fis.close();
							if(dis!=null)
								dis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	return sname;
    }
    int tryOpenNext(int j){
    	try{
    		openFileInput("sav"+j);
    		return j;
    	}
    	catch(FileNotFoundException e) {
    		if(j<innerLastId)
    			return tryOpenNext(j+1);
    		else
    			return -1;
    	}
		
    }
    void loaditems() {
    	list=new ArrayList<HashMap<String,String>>();
    	int j=0;
        for(int i=0; i<numberofnotes; i++){
        	j=tryOpenNext(j);
        	
        if(j!=-1){
        	 map=new HashMap<String,String>();       
				map.put("name", getName(j));
				map.put("id", String.valueOf(j));
				list.add(map);
				
				
        }
        j++;
        }
       
        adapter=new SimpleAdapter(this,list,R.layout.list_note,from,to);
        lv1.setAdapter(adapter);
    }


void createNewNote(){
	Intent intent=new Intent();
	intent.setClass(NoteActivity.this, ACaddNote.class);
	
	Bundle bundle=new Bundle();
	bundle.putInt("intvalue", numberofnotes);
	bundle.putInt("intlastid", innerLastId);
	bundle.putString("Pattern", "create");
	intent.putExtras(bundle);
	
	//startActivity(intent);
	startActivityForResult(intent,0);
}
    
    void openNote(String name){
    	Intent intent=new Intent();
		intent.setClass(NoteActivity.this, ACaddNote.class);
		
		Bundle bundle=new Bundle();
		bundle.putInt("intvalue", numberofnotes);
		bundle.putInt("intlastid", innerLastId);
		bundle.putString("Pattern", "open");
		bundle.putString("Name", name);
		intent.putExtras(bundle);
		
		startActivityForResult(intent,0);
}
    
    @Override
public void onClick(View v){
	
	if(v==btn){
		
		createNewNote();
	
		
	}else if(v==b2){
		writezero();
	}
	
}
@Override
protected void onActivityResult(int requestCode,int resultCode ,Intent data){
	Log.d("CheckStartActivity","onActivityResult and resultCode ="+resultCode);
	super.onActivityResult(requestCode, resultCode,data);
	if(resultCode==RESULT_OK){
		Toast.makeText(this, "Pass", Toast.LENGTH_LONG).show();
		Bundle bundle2=data.getExtras();
		String tt=bundle2.getString("page2Result");
		
		//tv2.setText(tt);
		 loadnum();
	    loaditems();
	}
	else{
		Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
		
	}
		
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
