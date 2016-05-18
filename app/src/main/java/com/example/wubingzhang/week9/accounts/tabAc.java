package com.example.wubingzhang.week9.accounts;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.wubingzhang.week9.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class tabAc extends ActionBarActivity implements
		TabListener {
	DBclass mDBClass;static ListView myListview=null;

	ActionBar actionBar;
	
	List<HashMap<String,Object>> list;
	float[] percents;
	int[] amounts;
	float sum;
	String[] types={"book","cloth","Entertain","Fee","Restaurant","Traffic"};
@Override
protected void onCreate(Bundle sis){
	super.onCreate(sis);
	setContentView(R.layout.activity_statistics);
	
	actionBar=this.getSupportActionBar();
	actionBar.setDisplayShowTitleEnabled(true);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	actionBar.addTab(actionBar.newTab().setText("Day").setTabListener(this));
	actionBar.addTab(actionBar.newTab().setText("Month").setTabListener(this));
	actionBar.addTab(actionBar.newTab().setText("Year").setTabListener(this));
	actionBar.addTab(actionBar.newTab().setText("Detail").setTabListener(this));
	
	//funopen();

	//load();
}
void funopen(){
	mDBClass=new DBclass(this);
	try{
		mDBClass.createDataBase();
		mDBClass.openDataBase();
		//mDBClass.insert(456, "fda", "san");
		
		
	}catch(IOException e){
		
	}
}
 void load(String pattern){
	funopen();//funopen();
	
	Cursor tc=mDBClass.doSQL("select * from 'account' "+pattern);
	List<News> tl=mDBClass.FunGetNewsInfo(tc);
	int ts=tl.size();
	//DBClassMyAdapter tdcma;
	
	list=new ArrayList<HashMap<String,Object>>();
	for(int i=0;i<ts;i++){
		HashMap<String,Object> map;
		 map=new HashMap<String,Object>();
		 SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
			Date curDate    =   new Date(tl.get(i).time);
			String str    =    formatter.format(curDate);
			
			map.put("time", str);
			map.put("number", tl.get(i).number);
			map.put("type", tl.get(i).type);
			//map.put("id", j);
			list.add(map);
	}
	
	
}
float[] jisuan(){
	float[] f=new float[6];
	 sum=0;
	 amounts=new int[6];
	for(HashMap<String,Object> hm:list){
		float fi= Float.parseFloat(hm.get("number").toString());
		sum+=fi;
		String type=hm.get("type").toString();
		Log.d("jisuan", type);
		Log.d("jisuan", String.valueOf(fi));
		for(int i=0;i<6;i++){
			if(type.equals(types[i]))
			{
				amounts[i]+=1;
				f[i]+=fi;
				break;
			}
		}
		
	}
	for(int i=0;i<6;i++){
		f[i]/=sum;
		
	}
	
	return f;
}
 float[] jisuantoDegree(float[] f){
	 for(int i=0;i<6;i++){
			//f[i]/=sum;
			f[i]*=360;
			//Log.d("jisuan"+i,String.valueOf(f[i]) );
		}
	 return f;
 }
 List<HashMap<String,Object>> percentToListofMap(float[] f){
	 List<HashMap<String,Object>> l=new ArrayList<HashMap<String,Object>>();
	 HashMap<String,Object> map;
	 java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");   
	 for(int i=0;i<6;i++){
		 map=new HashMap<String,Object>();
			map.put("type", types[i]);
			 
			  double   d=f[i]*100;   
			map.put("percent",df.format(d)+"%");
			map.put("amount", amounts[i]);
			l.add(map);
		}
	 return l;
 }
@Override
public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	// TODO Auto-generated method stub
	
}
@Override
public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
	// TODO Auto-generated method stub
	DummyFragment fragment = new DummyFragment();fragment.ta=this;
	         Bundle argsBundle = new Bundle();
	        argsBundle.putInt(DummyFragment.ARG_SECTION_NUMBER, arg0.getPosition()+1);
        fragment.setArguments(argsBundle);
        
         //获取fragmenttransaction对象
	         FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
	         //使用fragment代替该activity中的container组件
	       fTransaction.replace(R.id.container, fragment);
        fTransaction.commit();

}
@Override
public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	// TODO Auto-generated method stub
	
}
public class DummyFragment extends Fragment {
	public static final String ARG_SECTION_NUMBER="section_number";
	tabAc ta;
	public DummyFragment(){
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		int position=getArguments().getInt(ARG_SECTION_NUMBER);
		View rootview=inflater.inflate(R.layout.fragment_main_dummy,container,false);
		// 
		 
		 
		long l=0;
		long day=1000*60*60*24;
		long interval=0;
		if(position==1){
			interval=day;
			  l= System.currentTimeMillis()-interval;
			ta.load("where time>"+l);
		}
		else if(position==2){
			interval=day*30;
			  l= System.currentTimeMillis()-interval;
				ta.load("where time>"+l);
		}
		else if(position==3){
			interval=day*365;
			  l= System.currentTimeMillis()-interval;
				ta.load("where time>"+l);
		}
		else if(position==4){
			 rootview=inflater.inflate(R.layout.layout1,container,false);
			myListview=(ListView)rootview.findViewById(R.id.listView1);
			
			ta.load("");
			String[] from={"time","number","type"};              //这里是ListView显示内容每一列的列名
		    int[] to={R.id.lname,R.id.textView1,R.id.textView2};   //这里是ListView显示每一列对应的list_item中控件的id
		   
		    	SimpleAdapter d=new SimpleAdapter(ta,list,R.layout.list_item,from,to);
			myListview.setAdapter(d);
		    
			return rootview;
		}
		 //rootview=inflater.inflate(R.layout.fragment_main_dummy,container,false);
		//TextView dt=(TextView)rootview.findViewById(R.id.section_lable);
		//dt.setVisibility(View.INVISIBLE);
		
		FrameLayout main=(FrameLayout)rootview.findViewById(R.id.ll1);
		//new float[]{4,(float) 355.9}
		percents=jisuan();
		String[] from={"type","percent","amount"};              //这里是ListView显示内容每一列的列名
	    int[] to={R.id.textView1,R.id.textView2,R.id.textView3};   //这里是ListView显示每一列对应的list_item中控件的id
		myListview=(ListView)rootview.findViewById(R.id.listView1);
		SimpleAdapter d=new SimpleAdapter(ta,percentToListofMap(percents),R.layout.list_item1,from,to);
		myListview.setAdapter(d);
		main.addView(new sampleview(ta,jisuantoDegree(percents), 
				new int[]{Color.BLACK, Color.BLUE, Color.YELLOW, Color.LTGRAY, Color.GREEN, Color.RED}));
		
		return rootview;
	}
}
static class sampleview extends View {
private ShapeDrawable[] mds;
	public sampleview(Context context,float[] drawf,int[] colora) {
		super(context);
		float f[]=drawf;
		int c[]=colora;
		setFocusable(true);
		mds=new ShapeDrawable[f.length];
		
		for(int i=0;i<f.length;i++){
			float ff=i==0?0:f[i-1];
			mds[i]=new msd(new ArcShape(ff,f[i]));
			f[i]+=ff;
			mds[i].getPaint().setColor(c[i]);
		}
		
	}
	@Override
	protected void onDraw(Canvas canvas){
		int x=10,y=10,width=400,height=400;
		
		for(Drawable dr:mds){
			if(dr!=null){
				dr.setBounds(x,y,x+width,y+height);
				dr.draw(canvas);
			}
			//y+=height+5;
		}
	}
}
static class msd extends ShapeDrawable {
	Paint msp=new Paint(Paint.ANTI_ALIAS_FLAG);
	public msd(Shape s){
		super(s);
		msp.setStyle(Paint.Style.STROKE);
	}
	public Paint getStrokePaint(){
		return msp;
	}
	@Override
	protected void onDraw(Shape s ,Canvas c,Paint p){
		s.draw(c, p);
		s.draw(c, msp);
	}
}


}
