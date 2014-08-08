 
package com.example.shenzhoushihao;              //��������� ���ִ���ο���̫�ձ���ս��

//���������
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
	View  currentView = null;                    //��ʼΪNULL
	int action = 0;                              //���̵�״̬,�����Ʊ�ʾ �������ұ�ʾ��������
	GameView2 gameView;                          //GameView2������
	WelcomeView2 welcomeView;                    //WelcomeView2������
	FailView failView;                           //FailView������
	HelpView2 helpView;                          //HelpView2������
	WinView winView;                             //WinView������
	ProcessView processView;                     //ProcessView������
	boolean isSound = true;                      //�Ƿ񲥷�����
	static int currentScore=0;                   //���㱾�ص÷�
	Vibrator vib;                                //Vibrator�������ã����ô�����
	Button mybutton=null;                        //����mybutton���󣬳�ʼΪNULL
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub   
		//�������˳����Ի���
	    AlertDialog.Builder builder = new AlertDialog.Builder(PlaneActivity.this);       
	    builder.setIcon(R.drawable.icon);
	    builder.setTitle("    Quit?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{	
				System.exit(0);	                 //ֱ���˳�

			}
	    });
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
	    {
	    	public void onClick(DialogInterface dialog, int which)             //�����κβ������Ի�����ʧ
	    	{}
		});
	    builder.create().show();                 //��������ʾ�Ի���
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)//�˵�ѡ�����˳�ѡ��
	{
		// TODO Auto-generated method stub
        menu.add("Quit");
		return super.onCreateOptionsMenu(menu);
	}
	
	
	Handler myHandler = new Handler()            //��������UI�߳��еĿؼ�
	{
        public void handleMessage(Message msg) 
        {
        	if(msg.what == 1)                    //��Ϸʧ�ܣ���ҷɻ�׹��
        	{
        		if(gameView != null){
        			gameView.keyThread.setFlag(false);//ֹͣ���̼���
        			gameView.gameThread.setFlag(false);
        			gameView.moveThread.setFlag(false);
        			gameView = null;
        		}
        		initFailView();                  //�л���FialView
        	}
        	else if(msg.what == 2)               //�л���GameView2
        	{  
        	  
        		if(welcomeView != null)
        		{
        			welcomeView = null;          //�ͷŻ�ӭ����
        		}
        		if(processView != null)
        		{
        			processView = null;          //�ͷż��ؽ���
        		}
            	processView = new ProcessView(PlaneActivity.this,2);//��ʼ�����������л���������View
            	PlaneActivity.this.setContentView(processView);
            	new Thread()                     //�߳�
            	{
            		public void run()
            		{
            			Looper.prepare();
            			gameView = new GameView2(PlaneActivity.this);//��ʼ��GameView2
            			Looper.loop();
            		}
            	}.start();                       //�����߳�
        	}
        	else if(msg.what == 3)               //WelcomeView2��������Ϣ���л���HelpView2
        	{
        		initHelpView();                  //�л���HelpView2����
        	}
        	else if(msg.what == 4)
        	{
        		if(helpView != null)             //�ͷŰ�������
        		{
        			helpView = null;
        		}
        		toWelcomeView();                 //�л���WelcomeView2���� 
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
        		initWinView();                   //�л���WinView����     		 			   
        	}
        	else if(msg.what == 6)
        	{
        		toGameView();                    //ȥ��Ϸ����
        	}
        	else if(msg.what == 7)
        	{
        		if(welcomeView != null)          //�ͷŻ�ӭ����
        		{
        			welcomeView = null;
        		}
        		if(processView != null)          //�ͷż��ؽ���
        		{
        			processView = null;
        		}
        		processView = new ProcessView(PlaneActivity.this,1);//��ʼ�����������л���������View
        		PlaneActivity.this.setContentView(processView);
            	new Thread()                     //�߳�
            	{
            		public void run()            //��д��run����
            		{
            			Looper.prepare();
            			welcomeView = new WelcomeView2(PlaneActivity.this);//��ʼ��WelcomeView2
            			Looper.loop();
            		}
            	}.start();                       //�����߳�
        	}
        }
	};
	
	
    public void onCreate(Bundle savedInstanceState)                            //����ʱ������
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);                         //ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	processView = new ProcessView(this,1);                                 //��ʼ�����������л���������View
    	vib=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);                 //�񶯵ĳ�ʼ��
    	this.setContentView(processView);                                      //���ü��ؽ���
    	new Thread()                             //�߳�
    	{
    		public void run()
    		{
    			Looper.prepare();
    			welcomeView = new WelcomeView2(PlaneActivity.this);            //��ʼ��WelcomeView2
    		}
    	}.start();                               //�����߳�
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event)                             //��Ļ����
	{      
		if(this.currentView==winView&&event.getAction() == MotionEvent.ACTION_DOWN)
		{
				setContentView(R.layout.activity_shenzhoushihao);              //����ҳ�沼��Ϊ��һ�ؽ��� 
			    mybutton=(Button)findViewById(R.id.myButton);                  //findViewById�����ҵ�Button
			    mybutton.setOnClickListener(new ClickEvent());                 //�󶨼�����
		}
		return super.onTouchEvent(event);
	}
	
    
    
    public void toWelcomeView()                  //�л�����ӭ����
    {     	
    	this.setContentView(welcomeView);
    	this.currentView = welcomeView;
    }
    
    
    public void toGameView()                     //��ʼ��Ϸ����
    {
    	this.setContentView(gameView);
    	Disp1("Be careful with your HP and time.There are rewards during the battle(H for recovering HP;P for strengthening firepower).");
    }
    
    
    public void initHelpView()                   //��ʼ��������
    {
    	helpView = new HelpView2(this);
    	this.setContentView(helpView);
    }
    
    
    public void initFailView()                   //��ʼ��Ϸʧ�ܽ���
    {
    	failView = new FailView(this);
    	this.setContentView(failView);
    } 
    
    
    public void initWinView()                    //��ʼʤ������
    {   
    	winView = new WinView(this);
    	this.setContentView(winView);
    	this.currentView=winView;
    }
    
    
    public boolean onKeyUp(int keyCode, KeyEvent event)//����̧��
    {
    	if(keyCode == 19)                        //��
    	{
    		action = action & 0x1F;
    	}
    	if(keyCode == 20)                        //��
    	{
    		action = action & 0x2F;
    	}    	
    	if(keyCode == 21)                        //��
    	{
    		action = action & 0x37;
    	}    	
    	if(keyCode == 22)                        //��
    	{
    		action = action & 0x3B;
    	}
    	if(keyCode == KeyEvent.KEYCODE_A)         //A
    	{
    		action = action & 0x3D;
    	}
		return false;
	}
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event)//���̰��¼���
    {
    	if(keyCode == 19)                        //��
    	{
    		action = action | 0x20;
    	}
    	if(keyCode == 20)                        //��
    	{
    		action = action | 0x10;
    	}
    	if(keyCode == 21)                        //��
    	{
    		action = action | 0x08;
    	}
    	if(keyCode == 22)                        //��
    	{
    		action = action | 0x04;
    	}
    	if(keyCode == KeyEvent.KEYCODE_A)         //A 
    	{
    		action = action | 0x02;
    	}
    	if(keyCode==KeyEvent.KEYCODE_BACK)        //�������˳����Ի���
    	{ 
			AlertDialog.Builder builder = new AlertDialog.Builder(PlaneActivity.this);       
			builder.setIcon(R.drawable.icon);
			builder.setTitle("    Quit?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{	
					System.exit(0);               //ֱ���˳�
					
				}
	    	});
	       builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
	       {
		    	public void onClick(DialogInterface dialog, int which) 
		    	{}                                //�����κβ������Ի�����ʧ
		    });
	       builder.create().show();               //��������ʾ�Ի���
			}
		return false;		
    }
    
    
    public void Disp1(String s)                   //�������
 	{
 		Toast toast = Toast.makeText(this,   
 			   s, Toast.LENGTH_LONG);   
 				  toast.setGravity(Gravity.TOP, 100, 80);   
 			 toast.show();  		
 	}
    class ClickEvent implements OnClickListener   //��������
	{
		public void onClick(View v)
		{ 
			Intent intent1=new Intent();          //����һ��Intent
	        intent1=intent1.setClass(PlaneActivity.this,TankActivity.class);//����Intent
	        PlaneActivity.this.startActivity(intent1);//Activity����ת
	        PlaneActivity.this.finish();          //������ǰActivity
		}
	}
}