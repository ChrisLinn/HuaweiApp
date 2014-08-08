 
package com.example.shenzhoushihao;              //声明包语句 部分代码参考《太空保卫战》

//引入相关类
import android.app.Activity;
import android.view.MotionEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.view.Gravity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.os.Vibrator;
import android.app.Service;

public class PlaneActivity extends Activity
{
	View  currentView = null;                    //初始为空
	int action = 0;                              //键盘的状态,二进制表示 从左往右表示上下左右
	GameView2 gameView;                          //GameView2的引用
	WelcomeView2 welcomeView;                    //WelcomeView2的引用
	FailView failView;                           //FailView的引用
	HelpView2 helpView;                          //HelpView2的引用
	WinView winView;                             //WinView的引用
	ProcessView processView;                     //ProcessView的引用
	boolean isSound = true;                      //是否播放声音
	static int currentScore=0;                   //计算本关得分
	Vibrator vib;                                //Vibrator对象引用，设置触屏震动
	Button mybutton=null;                        //声明mybutton对象，初始为空
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub   
		//弹出“退出”对话框
	    AlertDialog.Builder builder = new AlertDialog.Builder(PlaneActivity.this);       
	    builder.setIcon(R.drawable.icon);
	    builder.setTitle("    确定退出？");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{	
				System.exit(0);	                 //直接退出

			}
	    });
	    builder.setNegativeButton("否", new DialogInterface.OnClickListener() 
	    {
	    	public void onClick(DialogInterface dialog, int which)             //不作任何操作，对话框消失
	    	{}
		});
	    builder.create().show();                 //创建并显示对话框
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)//菜单选项有退出选项
	{
		// TODO Auto-generated method stub
        menu.add("退出");
		return super.onCreateOptionsMenu(menu);
	}
	
	
	Handler myHandler = new Handler()            //用来更新UI线程中的控件
	{
        public void handleMessage(Message msg) 
        {
        	if(msg.what == 1)                    //游戏失败，玩家飞机坠毁
        	{
        		if(gameView != null){
        			gameView.keyThread.setFlag(false);//停止键盘监听
        			gameView.gameThread.setFlag(false);
        			gameView.moveThread.setFlag(false);
        			gameView = null;
        		}
        		initFailView();                  //切换到FialView
        	}
        	else if(msg.what == 2)               //切换到GameView2
        	{  
        	  
        		if(welcomeView != null)
        		{
        			welcomeView = null;          //释放欢迎界面
        		}
        		if(processView != null)
        		{
        			processView = null;          //释放加载界面
        		}
            	processView = new ProcessView(PlaneActivity.this,2);//初始化进度条并切换到进度条View
            	PlaneActivity.this.setContentView(processView);
            	new Thread()                     //线程
            	{
            		public void run()
            		{
            			Looper.prepare();
            			gameView = new GameView2(PlaneActivity.this);//初始化GameView2
            			Looper.loop();
            		}
            	}.start();                       //启动线程
        	}
        	else if(msg.what == 3)               //WelcomeView2发来的消息，切换到HelpView2
        	{
        		initHelpView();                  //切换到HelpView2界面
        	}
        	else if(msg.what == 4)
        	{
        		if(helpView != null)             //释放帮助界面
        		{
        			helpView = null;
        		}
        		toWelcomeView();                 //切换到WelcomeView2界面 
        	}
        	else if(msg.what == 5)
        	{
        		if(gameView != null)
        		{
        			gameView.gameThread.setFlag(false);
        			gameView.keyThread.setFlag(false);
        			gameView.moveThread.setFlag(false);
        			gameView.explodeThread.setFlag(false);
        			gameView = null;
        		}        		
        		initWinView();                   //切换到WinView界面     		 			   
        	}
        	else if(msg.what == 6)
        	{
        		toGameView();                    //去游戏界面
        	}
        	else if(msg.what == 7)
        	{
        		if(welcomeView != null)          //释放欢迎界面
        		{
        			welcomeView = null;
        		}
        		if(processView != null)          //释放加载界面
        		{
        			processView = null;
        		}
        		processView = new ProcessView(PlaneActivity.this,1);//初始化进度条并切换到进度条View
        		PlaneActivity.this.setContentView(processView);
            	new Thread()                     //线程
            	{
            		public void run()            //重写的run方法
            		{
            			Looper.prepare();
            			welcomeView = new WelcomeView2(PlaneActivity.this);//初始化WelcomeView2
            			Looper.loop();
            		}
            	}.start();                       //启动线程
        	}
        }
	};
	
	
    public void onCreate(Bundle savedInstanceState)                            //创建时被创建
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);                         //全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	processView = new ProcessView(this,1);                                 //初始化进度条并切换到进度条View
    	vib=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);                 //振动的初始化
    	this.setContentView(processView);                                      //设置加载界面
    	new Thread()                             //线程
    	{
    		public void run()
    		{
    			Looper.prepare();
    			welcomeView = new WelcomeView2(PlaneActivity.this);            //初始化WelcomeView2
    		}
    	}.start();                               //启动线程
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event)                             //屏幕监听
	{      
		if(this.currentView==winView&&event.getAction() == MotionEvent.ACTION_DOWN)
		{
				setContentView(R.layout.activity_shenzhoushihao);              //设置页面布局为下一关界面 
			    mybutton=(Button)findViewById(R.id.myButton);                  //findViewById方法找到Button
			    mybutton.setOnClickListener(new ClickEvent());                 //绑定监听器
		}
		return super.onTouchEvent(event);
	}
	
    
    
    public void toWelcomeView()                  //切换到欢迎界面
    {     	
    	this.setContentView(welcomeView);
    	this.currentView = welcomeView;
    }
    
    
    public void toGameView()                     //初始游戏界面
    {
    	this.setContentView(gameView);
    	Disp1("游戏过程中请注意下方生命值和时间的变化。                    中途会有道具奖励(H标志道具可增加一点生命值，P标志道具可加强子弹)！");
    }
    
    
    public void initHelpView()                   //初始帮助界面
    {
    	helpView = new HelpView2(this);
    	this.setContentView(helpView);
    }
    
    
    public void initFailView()                   //初始游戏失败界面
    {
    	failView = new FailView(this);
    	this.setContentView(failView);
    } 
    
    
    public void initWinView()                    //初始胜利界面
    {   
    	winView = new WinView(this);
    	this.setContentView(winView);
    	this.currentView=winView;
    }
    
    
    public boolean onKeyUp(int keyCode, KeyEvent event)//键盘抬起
    {
    	if(keyCode == 19)                        //上
    	{
    		action = action & 0x1F;
    	}
    	if(keyCode == 20)                        //下
    	{
    		action = action & 0x2F;
    	}    	
    	if(keyCode == 21)                        //左
    	{
    		action = action & 0x37;
    	}    	
    	if(keyCode == 22)                        //右
    	{
    		action = action & 0x3B;
    	}
    	if(keyCode == KeyEvent.KEYCODE_A)         //A
    	{
    		action = action & 0x3D;
    	}
		return false;
	}
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event)//键盘按下监听
    {
    	if(keyCode == 19)                        //上
    	{
    		action = action | 0x20;
    	}
    	if(keyCode == 20)                        //下
    	{
    		action = action | 0x10;
    	}
    	if(keyCode == 21)                        //左
    	{
    		action = action | 0x08;
    	}
    	if(keyCode == 22)                        //右
    	{
    		action = action | 0x04;
    	}
    	if(keyCode == KeyEvent.KEYCODE_A)         //A 
    	{
    		action = action | 0x02;
    	}
    	if(keyCode==KeyEvent.KEYCODE_BACK)        //弹出“退出”对话框
    	{ 
			AlertDialog.Builder builder = new AlertDialog.Builder(PlaneActivity.this);       
			builder.setIcon(R.drawable.icon);
			builder.setTitle("    确定退出？");
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{	
					System.exit(0);               //直接退出
					
				}
	    	});
	       builder.setNegativeButton("否", new DialogInterface.OnClickListener() 
	       {
		    	public void onClick(DialogInterface dialog, int which) 
		    	{}                                //不做任何操作，对话框消失
		    });
	       builder.create().show();               //创建并显示对话框
			}
		return false;		
    }
    
    
    public void Disp1(String s)                   //输出方法
 	{
 		Toast toast = Toast.makeText(this,   
 			   s, Toast.LENGTH_LONG);   
 				  toast.setGravity(Gravity.TOP, 100, 80);   
 			 toast.show();  		
 	}
    class ClickEvent implements OnClickListener   //监听器类
	{
		public void onClick(View v)
		{ 
			Intent intent1=new Intent();          //声明一个Intent
	        intent1=intent1.setClass(PlaneActivity.this,TankActivity.class);//发送Intent
	        PlaneActivity.this.startActivity(intent1);//Activity的跳转
	        PlaneActivity.this.finish();          //结束当前Activity
		}
	}
}