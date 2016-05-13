package com.example.wubingzhang.week9.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.wubingzhang.week9.R;

import java.io.IOException;


public class AccountsActivity extends ActionBarActivity implements OnClickListener {
DBclass mDBClass;
ImageButton ibCloth1,ibEntertain,ibFee,ibRestau,ibTrans;
Button bSee,bDel;
EditText et;
ListView myListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        et=(EditText)findViewById(R.id.editText1);
        ibCloth1=(ImageButton)findViewById(R.id.imageButton1);
       
        ibEntertain=(ImageButton)findViewById(R.id.ImageButton01);
        ibFee=(ImageButton)findViewById(R.id.ImageButton05);
        
        ibRestau=(ImageButton)findViewById(R.id.ImageButton04);
        ibTrans=(ImageButton)findViewById(R.id.ImageButton03);
        bSee=(Button)findViewById(R.id.button1);
        bDel=(Button)findViewById(R.id.button2);
        ibCloth1.setOnClickListener(this);
        ibEntertain.setOnClickListener(this);
        ibFee.setOnClickListener(this);
        ibRestau.setOnClickListener(this);
        ibTrans.setOnClickListener(this);
        bSee.setOnClickListener(this);
        bDel.setOnClickListener(this);
        //myListview=(ListView)findViewById(R.id.myListView);
        funopen();
       
    }

    void insertAccount(float n,long time,String type){
    	mDBClass.insert(n, time, type);
    }
    
void funopen(){
	mDBClass=new DBclass(this);
	try{
		mDBClass.createDataBase();
		mDBClass.openDataBase();
	}catch(IOException e){
		
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
	@Override
	public void onClick(View v) {
		if(et.getText().length()!=0){
			float f= Float.parseFloat(et.getText().toString());
			long t= System.currentTimeMillis();
					
			if(v==ibCloth1){
				insertAccount(f,t,"cloth");
			}else if(v==ibFee){
				insertAccount(f,t,"Fee");
			}
			else if(v==ibEntertain){
				insertAccount(f,t,"Entertain");
			}
			else if(v==ibRestau){
				insertAccount(f,t,"Restaurant");
			}
			else if(v==ibTrans){
				insertAccount(f,t,"Traffic");
			}
		}
		if(v==bSee){
			 Intent intent=new Intent();
				intent.setClass(AccountsActivity.this, tabAc.class);
				
				Bundle bundle=new Bundle();
				
				intent.putExtras(bundle);
				
				startActivityForResult(intent,0);
		}else if(v==bDel){
			mDBClass.deleteDb();
			funopen();
		}
	}
}
