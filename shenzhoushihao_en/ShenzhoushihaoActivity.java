 
package com.example.shenzhoushihao;                 //��������� ���ִ���ο���ҩ��ǰ����

//���������
import com.example.shenzhoushihao.BitmapManager;
import com.example.shenzhoushihao.WelcomeView;
import com.example.shenzhoushihao.GameView;
import com.example.shenzhoushihao.SelectstageActivity.ClickEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

public class ShenzhoushihaoActivity extends Activity implements OnGestureListener
{                                                   //�̳�Activity�ಢʵ��OnGestureListener�ӿ�	
	View currentView;					            //��¼��ǰView
	@SuppressWarnings("unchecked")
	WelcomeView wv;						            //WelcomeView��������
	GameView gv;						            //GameView������
	HelpView hv;						            //HelpView��������            
	HeroRecordView hrv;			                    //HeroRocordView��������
	PassView pv;                                    //PassView��������
	AboutView av;                                   //AboutView��������
	ProgressView prv;                               //ProgressView��������
	ProgressDialog progress;                        //ProgressDialog�������ã����ý�����
	Vibrator vib;                                   //Vibrator�������ã����ô�����
	String heroName;                                //Ӣ�۰������ڴ����ȡ�ͱ�����ļ���  
	String archiveName="NULL";                         //�浵�����ڴ����ȡ�ͱ�����ļ���
	Button []button=new Button[6];                  //Button���飬������ʾ������ĵ���
	String []name=new String[6];           
	private GestureDetector detector;               //���Ƽ�����
	AlertDialog.Builder builder;                    //�Ի����һ������ 
	User []user=new User[6];                        //User�������飬���ڴ���Ӣ�۰�
	boolean oneFlag=true;
	boolean twoFlag=true;
	boolean threeFlag=true;
	static int[] currentScore={0,0,0,0,0,0};        //��ʼ�����ص÷�
	int n=0;
	Button mybutton=null;
	
	
	
	Handler myHandler = new Handler()               //�����Զ����Handler
	 {					
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg)      //��д������Ϣ�ķ���
		{    
			switch(msg.what)                        //�ж�Message���������
			{						
			case 0:	
				gv= new GameView(ShenzhoushihaoActivity.this);//��ʼ��GameView
				gv.playSound(1,-1);                 //ѭ��������Ϸ������
				setContentView(gv);				    //�л���Ļ��GameView
				currentView = gv;                   //��¼��ǰView
				break;
			case 1:                                 //������Ϸ
				saveItem();                         //���ô����saveItem������������ǰ�浵�ĶԻ���
				break;
			case 2:  
				hrv = new HeroRecordView(ShenzhoushihaoActivity.this);			//����HelpView����
				setContentView(hrv);			    //�л���Ļ��HelpView
				currentView = hrv;				    //��¼��ǰView
				break;
			
			case 3:									//��ʾ��������
				hv = new HelpView(ShenzhoushihaoActivity.this);			       //����HelpView����
				setContentView(hv);				    //�л���Ļ��HelpView
				currentView = hv;				    //��¼��ǰView
				break;	
			case 4:									//��ʾ���ڽ���
				av = new AboutView(ShenzhoushihaoActivity.this);			   //����View����
				setContentView(av);					//�л���Ļ��HelpView
				currentView = av;					//��¼��ǰView
				break;
				
			case 5:									//�˳���Ϸ
				if(currentView==wv&&WelcomeView.wantSound)                     //��ǰViewΪWelcomeView����������Ϊ��
				{
				wv.stopSound(1);				    //ֹͣ�����Ĳ���
				wv.soundPool.release();				//�ͷ�WelcomeView��SoundPool����
				wv.soundPool=null;
				}
				else if(currentView==gv&&WelcomeView.wantSound)                //����ǰViewΪWelcomeView��һ����������Чѡ��Ϊ����
				{
					gv.stopSound(1);			    //ֹͣ�����Ĳ���
					gv.soundPool.release();			//�ͷ�WelcomeView��SoundPool����
					gv.soundPool=null;
				}
				android.os.Process.killProcess(android.os.Process.myPid());    //��������
				System.exit(0);					    //�˳�����
				break;
			case 6:									//���ػ�ʧ��	
				pv = new PassView(ShenzhoushihaoActivity.this);	               //����PassView����
				setContentView(pv);				    //�л���Ļ��PassView
				currentView = pv;					//��¼��ǰView
				if(WelcomeView.wantSound)           //��Чѡ���ǿ���
				{
				if(gv.failFlag&&!gv.nextStage)      //��ʧ��    
					pv.playSound(3, 0);             //����ʧ�ܱ�����Ч
				else{                               //�ɹ�
					    pv.playSound(2, 0);         //���ųɹ�������Ч
				}
				}
				if(gv.nextStage)
					saveRecordDialog();
				break;
			case 7:
				prv = new ProgressView(ShenzhoushihaoActivity.this,progress);  //��ʼ��
				setContentView(prv);				//�л���Ļ��ProgressView
				currentView = prv;                  //��¼��ǰView
				break;
			case 8:                                 //�����ǰ�浵�Ի����ϵĴ浵�󣬶�ȡ�浵
				load(archiveName);                  //����
				if(gv.circleNumber==0)              //������ĸ���Ϊ0
				{             
					pv=new PassView(ShenzhoushihaoActivity.this);              //����һ��PassView����
					setContentView(pv);	            //���õ�ǰViewΪPassView
					currentView = pv;               //��¼��ǰ��View
				}
				else                                //���������㣬�����GameView
				{
					setContentView(gv);	            //���õ�ǰViewΪGameView
					if(WelcomeView.wantSound)
					  gv.playSound(1,-1);           //ѭ��������Ϸ��������idΪ1
					currentView = gv;               //��¼��ǰ��View
				}
				break;	
			default:
					break;
			}
		}
	};
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)                            //Activity������ʱ�����Զ�ִ�еķ���	
	{  	  
		  super.onCreate(savedInstanceState);	        
	        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);          //����ȫ��
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
	        for(int i=0;i<6;i++)
	        {
				user[i]=new User();
				currentScore[i]=0;
			}
	        progress=new ProgressDialog(ShenzhoushihaoActivity.this);          //��ʼ��������
	        vib=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);                                        //�񶯵ĳ�ʼ��
	        detector=new GestureDetector(this);                                //��ʼ�����Ƽ�����
	        BitmapManager.loadWelcomePublic(getResources());	               //���ػ�ӭ�����ͼƬ��Դ
	        wv = new WelcomeView(ShenzhoushihaoActivity.this);                 //��ʼ��WelcomeView
	    	setContentView(wv);						                           //���õ�ǰ��ĻΪWelcomeView����
		    currentView = wv;	  
		    	        
	 }
	
	
		public boolean onTouchEvent(MotionEvent event)                         //�����Ļ
		{     
				if(currentView == wv)                                          //��ӭ������Ӧ
				{	
				if(event.getAction() == MotionEvent.ACTION_UP)                 //�������Ļ����ָ�뿪��Ļʱ��Ӧ
				{                
					int x = (int)event.getX();			                       //��ȡ��Ļ�������X����
					int y = (int)event.getY();			                       //��ȡ��Ļ�������Y����
					return wv.myTouchEvent(x, y);                              //����WelcomeView�е�myTouchEvent����
				}
				}
				else if(currentView==hv)                                       //"����"������Ӧ
				{                
					if(event.getAction() == MotionEvent.ACTION_UP)             //�������Ļ����ָ�뿪��Ļʱ��Ӧ
					{                 
					int x = (int)event.getX();			                       //��ȡ��Ļ�������X����
					int y = (int)event.getY();	                               //��ȡ��Ļ�������Y����
					return hv.myTouchEvent(x, y);                              //����HelpView�е�myTouchEvent����
					}
				}
				else if(currentView==av)                                       //"����"������Ӧ
				{                  
					if(event.getAction() == MotionEvent.ACTION_UP)             //�������Ļ����ָ�뿪��Ļʱ��Ӧ
					{                
						int x = (int)event.getX();			                   //��ȡ��Ļ�������X����
						int y = (int)event.getY();	                           //��ȡ��Ļ�������Y����
						return av.myTouchEvent(x, y);                          //����AboutView�е�myTouchEvent����
					}
				}
				else if(currentView==pv)                                       //"����"������Ӧ
				{    
					if(gv.nextStage&&event.getAction() == MotionEvent.ACTION_UP)             //�������Ļ����ָ�뿪��Ļ���Ѿ�����ʱ��Ӧ
					{ 
					  setContentView(R.layout.activity_shenzhoushihao);        //������ת����һ�ؽ���
					  mybutton=(Button)findViewById(R.id.myButton);            //ͨ��findViewById�ҵ�myButton
					  mybutton.setOnClickListener(new ClickEvent());  		   //���ü�����			
					}
					else if(event.getAction() == MotionEvent.ACTION_UP)                      //�������Ļ����ָ�뿪��Ļʱ��Ӧ
					{
						int x = (int)event.getX();			                   //��ȡ��Ļ�������X����
						int y = (int)event.getY();	                           //��ȡ��Ļ�������Y����
						return pv.myTouchEvent(x, y);                          //����PassView�е�myTouchEvent����      
					}
				}
				else if(currentView==hrv)                                      //"Ӣ�۰�"������Ӧ
				{                 
					if(event.getAction() == MotionEvent.ACTION_UP)             //�������Ļ����ָ�뿪��Ļʱ��Ӧ
					{          
						int x = (int)event.getX();			                   //��ȡ��Ļ�������X����
						int y = (int)event.getY();	                           //��ȡ��Ļ�������Y����
						return hrv.myTouchEvent(x, y);                         //����HeroRecordView�е�myTouchEvent����      
					}
				}
				else if(currentView == gv&&gv.status!=1)                       //�����ǰViewΪGameView���һ���״̬�����ڷ���
				{			
						detector.onTouchEvent(event);                          //����������
						return false;	
					}
				
				   return true;
			}
		public boolean onDown(MotionEvent e)                                   //ʵ��OnGestureListener�ӿ���д�ķ����������Ļ���µ��¼���Ӧ
		{
			return false;
			}
		
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY)//�����׳�������Ӧ������ǰ���������ֱ������ʼ����յ㣬�����������ǻ�����Ļʱ��x,y���������ϵļ��ٶ�
		{	
			int x = (int)e1.getX();			                                   //��ȡ��Ļ�������X����
			int y = (int)e1.getY();	                                           //��ȡ��Ļ�������y���꣬�����ж���ʼ���Ƿ��ڻ����ڵ�����
			if(GameView.stage==0){
			if(gv.circleNumber==gv.initCircle-1)
				Disp1("Num of circles updated in left bottom");
			else if(gv.circleNumber==4)
			{
			     Disp1("Circles shortage");
			}
			
			if(gv.taskArray[0][0]==1&&oneFlag)                                 //taskarray :�������־���½ǵ�����ֵ
			{
				oneFlag=false;
			    Disp2("Catch res according to missions updated in right bottom");
			}
			if(gv.taskArray[0][1]==1&&twoFlag)
			{
				twoFlag=false;
				Disp2("Catch res according to missions updated in right bottom");
			}
			if(gv.taskArray[0][2]==1&&threeFlag)
			{
				threeFlag=false;
				Disp2("Collect res according to missions updated in right bottom");
			}
			}     //������һ�صĽ�ѧ�ж�
			if(gv.status!=1&&x>95&&x<145&&y>270&&y<320)                        //����ʱû�л��ڿ��зɣ�����һ���׳��Ļ��Ѿ���أ�������ʼ��λ��Ԫ�����ڵ�����
			{ 
				gv.playSound(2, 0);                                            //����GameView��playSound����������Բ���ɳ�����Ч������Чѡ����ǹرգ��˷������Զ�������
			if(Math.abs(velocityX)>1150)                                       //��x����ļ��ٶ�����һ������
			{
				velocityX=velocityX>0?1150:(-1150);                            //���ﵽ����ʱȡ����
			}
			if(Math.abs(velocityY)>2700)                                       //��y����ļ��ٶ�����һ������
			{
				velocityY=velocityY>0?2700:(-2700);
				}
			
			//����������Ƶ�Ŀ���ǣ�����С�Ĵ�����Ļʱ��Բ�����ɳ�����x,y��������ʼ��ľ���ľ���ֵ�������FLING_MIN_DISTANCE
			if (e1.getX() - e2.getX() > GameView.FLING_MIN_DISTANCE)           //���󻬶� 
			{	    
				gv.xx=velocityX/11;                                            //���ٶ���x�����ϳɱ�����С
				gv.yy=velocityY/11;		                                       //���ٶ���y�����ϳɱ�����С
				gv.status=1;                                                   //��Բ��״̬��Ϊ�ڿ��з��е�״̬
				return true;
				}
			else if (e1.getX() - e2.getX() < -GameView.FLING_MIN_DISTANCE)     //���һ���
			{  
				gv.xx=velocityX/11;                                            //���ٶ���x�����ϳɱ�����С
			    gv.yy=velocityY/11;	                                           //���ٶ���y�����ϳɱ�����С
				gv.status=1;                                                   //��Բ��״̬��Ϊ�ڿ��з��е�״̬
	            return true;
			    }
			else if (e1.getY() - e2.getY() > GameView.FLING_MIN_DISTANCE)      //���ϻ���
			{   
					gv.xx=velocityX/11;                                        //���ٶ���x�����ϳɱ�����С                                          
					gv.yy=velocityY/11;		                                   //���ٶ���y�����ϳɱ�����С
					gv.status=1;                                               //��Բ��״̬��Ϊ�ڿ��з��е�״̬
				    return true;
	
				}
			else if (e1.getY() - e2.getY() > -GameView.FLING_MIN_DISTANCE)     //���»���
			{    
					gv.xx=velocityX/11;                                        //���ٶ���x�����ϳɱ�����С
					gv.yy=velocityY/11;		                                   //���ٶ���y�����ϳɱ�����С	
					gv.status=1;                                               //��Բ��״̬��Ϊ�ڿ��з��е�״̬
					return true;
			    }
	      }
			return false;
		}
		
		
		public void onLongPress(MotionEvent arg0)                              //ʵ��OnGestureListener�ӿ���д�ķ�����������Ļ���¼���Ӧ
		{ 
			// TODO Auto-generated method stub
		}
		
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY)        //ʵ��OnGestureListener�ӿ���д�ķ���������Ļ���ƶ����¼���Ӧ
		{                 
			// TODO Auto-generated method stub
			return false;
		}
		
		
		public void onShowPress(MotionEvent e)                                 //ʵ��OnGestureListener�ӿ���д�ķ���
		{      
			// TODO Auto-generated method stub
			
		}
		
		
		public boolean onKeyDown(int keyCode,KeyEvent event)
		{
		if(keyCode==KeyEvent.KEYCODE_BACK)                                     //�����·��ؼ�ʱ
		{
			if(currentView==gv||currentView==pv)                               //��ǰViewΪGameView��PassViewʱ����Ч
			{            
				AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);     //ͬ�ϣ�����
				builder.setIcon(R.drawable.icon);                              //ͬ�ϣ�����ͼ��
				builder.setTitle("Save?");                                   //ͬ�ϣ����ñ���
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{                                                              //ͬ��
					public void onClick(DialogInterface dialog, int which)
					{                                                          //ͬ��
						saveDialog();                                          //�������浵�Ի���              
			    	}
		    	});
		       builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		       {                                                               //ͬ��
			    	public void onClick(DialogInterface dialog, int which)
			    	{                                                          //ͬ��
			    		myHandler.sendEmptyMessage(5);                         //ִ��Handler��case 5����䣬���浵ֱ���˳���Ϸ
			    	}
			   });
		       builder.create().show();                                        //��������ʾ�Ի���
			}
			else                                                               //�������˳��Ի��򡱣�ע��ͬ��
			{          
				AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);       
				builder.setIcon(R.drawable.icon);
				builder.setTitle("    Quit?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						if(WelcomeView.wantSound)
						{
							wv.playSound(2,0);
						    wv.playSound(2,0);
						    }
						myHandler.sendEmptyMessage(5);                         //ִ��Handler��case 5����䣬ֱ���˳�
					}
		    	});
		       builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		       {
			    	public void onClick(DialogInterface dialog, int which)     //�����κβ������Ի�����ʧ
			    	{}
			   });
		       builder.create().show();                                        //��������ʾ�Ի���
				}
	        return true;      	
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
		}
		
		
		public boolean onSingleTapUp(MotionEvent e)                            //ʵ��OnGestureListener�ӿ���д�ķ���
		{   
			// TODO Auto-generated method stub
			return false;
		}
		
		
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu)                          //Ϊ�ֻ��Ĳ˵���������Ӧ�����ò˵���Ϊ  ���˵���������Ч�������˳���
	    {            
			menu.add("Menu");                                                    //�˵�ѡ��һ���в˵����浵����Ч���˳��ĸ�ѡ��
			menu.add("Save");
			menu.add("Sound");
			menu.add("Quit");
			return true;
		}
	    
	    
		@SuppressWarnings("unchecked")
		@Override
		public boolean onOptionsItemSelected(MenuItem item)                    //���ֻ��ϵĲ˵���������Ӧ�ĺ���
		{        
			if(item.getTitle().equals("Menu"))                                  //����˵�������
			{                   
				if(WelcomeView.wantSound&&currentView==gv)                     //����Чѡ����ǿ��������Ҵ���Ϸ�����лص�������ҳ��
				{
				gv.stopSound(1);
				gv.soundPool.release();
				gv.soundPool=null;
				}
				setContentView(wv);						                       //���õ�ǰ��ĻΪWelcomeView����
				currentView = wv;	                                           //��¼��ǰView
				wv.status =7;                                                  //��ʾ��Ϸ��������ҳ��
				
			}
			else if(item.getTitle().equals("Save"))                              //����˵�������
			{                   
				if(currentView==gv||currentView==pv)                           //��ǰViewΪGameView��PassViewʱ����Ч
					if(GameView.stage!=5)                                      //���һ�ز�����浵
				         saveDialog();                                         //�����浵�ĶԻ���
			}
			else if(item.getTitle().equals("Sound"))                              //�������Ч����˵���
			{           
				AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);     //����һ���Ի���
				builder.setIcon(R.drawable.icon);                              //����ͼ��Ϊraw�ļ����ļ���Ϊicon��ͼƬ
				builder.setTitle("Switch");                                     //�Ի������
				builder.setPositiveButton("On", new DialogInterface.OnClickListener()                   //����"����"��ť
				{   
					public void onClick(DialogInterface dialog, int which)     //�����ʱ
					{   
						wv.musicDialogOpen();                                  //����WelcomeView�е�musicDialogOpen��������
			    	}
		    	});
		       builder.setNegativeButton("Off", new DialogInterface.OnClickListener()                    //�����رհ�ť
		       {     
			    	public void onClick(DialogInterface dialog, int which)     //�����ʱ
			    	{          
			    		wv.musicDialogClose();                                 //����WelcomeView�е�musicDialogClose��������
			    	}
			   });
		       builder.create().show();                                        //��ʾ�Ի���
			}
		
			else if(item.getTitle().equals("Quit"))                              //���"�˳�"ʱ
			{            
				if(currentView==gv||currentView==pv)                           //��ǰViewΪGameView��PassViewʱ����Ч
				{            
				AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);     //ͬ�ϣ�����
				builder.setIcon(R.drawable.icon);                              //ͬ�ϣ�����ͼ��
				builder.setTitle("Save?");                                   //ͬ�ϣ����ñ���
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()                    //ͬ�� 
				{    
					public void onClick(DialogInterface dialog, int which)     //ͬ��
					{     
						saveDialog();                                          //�������浵�Ի���						
			    	}					
		    	});
				
		       builder.setNegativeButton("No", new DialogInterface.OnClickListener()                     //ͬ�� 
		       {                  
			    	public void onClick(DialogInterface dialog, int which)     //ͬ�� 
			    	{                            
			    		myHandler.sendEmptyMessage(5);                         //ִ��Handler��case 5����䣬���浵ֱ���˳���Ϸ
			    	}
			   });
		       builder.create().show();                                        //��������ʾ�Ի���
			}
			else                                                               //�������˳��Ի��򡱣�ע��ͬ��
			{         
				AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);       
				builder.setIcon(R.drawable.icon);
				builder.setTitle("    Quit?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which)
					{	
						           if(WelcomeView.wantSound)
						           {
						        	   wv.playSound(2,0);
						        	   wv.playSound(2,0);
						        	}
						    		myHandler.sendEmptyMessage(5);             //ִ��Handler��case 5����䣬ֱ���˳�
					}
		    	});
		       builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
		       {
			    	public void onClick(DialogInterface dialog, int which)     //�����κβ������Ի�����ʧ
			    	{}
			   });
		       builder.create().show();                                        //��������ʾ�Ի���
				}
				}
			return super.onOptionsItemSelected(item);
			
		}
		
		
		public void saveDialog()                                               //�����浵�ĶԻ���ķ�����ͬ�ϵ�ע�Ͳ����ظ�д
		{                    
			AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);         //����
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.main1, null);  //��ʾxml�еĲ���
			builder.setIcon(R.drawable.icon);
			builder.setTitle("       Save");
			builder.setView(textEntryView);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
			EditText userName = (EditText) textEntryView.findViewById(R.id.etUserName);                 //�õ��ı������
			heroName=userName.getText().toString();                            //���ı��������������ת����String���ͣ���ȡӢ�۰��ϵ�User���󣬷���user����                      
			save(gv, heroName);                                                //�浵
			int i=loaddata();                                                  //��ȡ�����Ѿ��浵�Ѿ��浽�˵ڼ���
			saveName(heroName,i);                                              //��������
			i++;                                                               //+1
			savedata(i);                                                       //����i
			myHandler.sendEmptyMessage(5);                                     //ִ��Handler��case 5����䣬�˳���Ϸ
			}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener()                       //�����κβ������Ի�����ʧ 
			{     
			public void onClick(DialogInterface dialog, int whichButton) {}
			});
			builder.create().show();                                           //��������ʾ�Ի���			
		}
		
		
		 @SuppressWarnings({ "static-access" })
		public  void save(GameView gv,String s)                                //��Ϸ�ı���
		 {
			 SharedPreferences.Editor e=getBaseContext().getSharedPreferences(s,Activity.MODE_PRIVATE).edit();
			 e.putInt("stage", GameView.stage);                                //�洢��Ϸ�Ĺ���
			 e.putInt("circleNumber", gv.circleNumber);
			 for (int i=0;i<gv.bitmapData[GameView.stage].flag.length;i++)     //�洢ͼƬ���Ƿ���ʾ
			 {                  
				 e.putBoolean( "flag"+i,gv.bitmapData[GameView.stage].flag[i]);
			}
			 e.putInt("get1",gv.taskArray[gv.stage][2]);                       //�洢��������
		     e.putInt("get2", gv.taskArray[gv.stage][1]);
		     e.putInt("get3", gv.taskArray[gv.stage][0]);
		     e.putBoolean("sound", WelcomeView.wantSound);                     //�洢�Ƿ񲥷�����
		     e.putBoolean("fail",gv.failFlag);                                 //�洢�Ƿ���Ϸʧ��
			 e.putString("x1",gv.s3);                                          //�洢"ը��"�����Ѽ�����
		     e.putString("x2",gv.s2);                                          //�洢"ȼ��"�����Ѽ�����
		     e.putString("x3",gv.s1);                                          //�洢"��ҩ"�����Ѽ�����
			 e.commit();                                                       //�ύ
				}
		 
		 
		 @SuppressWarnings({ })
		 public void load(String s)                                            //��Ϸ�Ķ�ȡ
		 {
			 SharedPreferences p=getBaseContext().getSharedPreferences(s,Activity.MODE_PRIVATE);
		     int n=p.getInt("stage", 0); 
		     GameView.stage=n;
		     gv= new GameView(ShenzhoushihaoActivity.this);                    //����һ���µ�GameView����
		     gv.circleNumber=p.getInt("circleNumber",20);                      //��ȡʣ�໷����Ĭ��Ϊ20
			 for (int i=0;i<gv.bitmapData[n].flag.length;i++)                  //��ȡÿһ��ͼƬ��״̬�����Ƿ�����Ļ�ϻ������ı�־
			 {           
				   gv.bitmapData[n].flag[i]= p.getBoolean( "flag"+i,true);     //Ĭ��false
				   }
			 gv.taskArray[n][2] =p.getInt("get1",0);                           //��ȡ�������
		     gv.taskArray[n][1]= p.getInt("get2",0 );
			 gv.taskArray[n][0]= p.getInt("get3",0 );
		     gv.tt.flag=true;                                                  //�����̱߳�־Ϊtrue
			 gv.tdt.flag=true;
			 gv.tdt.isDrawOn=true;
		     gv.failFlag=p.getBoolean("fail", false);                          //�Ƿ�ʧ�ܵı�־
			 gv.nextStage=p.getBoolean("nextStage", false);                    //�Ƿ������һ�صı�־
		     gv.s3=p.getString("x1", "0");                                     //������ȵ��ַ�������Ļ���½���Ҫ���Ƶ�
			 gv.s2=p.getString("x2", "0");
			 gv.s1=p.getString("x3", "0");
		}
		 
		 
		 public void saveName(String s,int i)                                  //�����Ӧ��Ϸ�浵���û���
		 {
			 SharedPreferences.Editor e=getBaseContext().getSharedPreferences("name",Activity.MODE_PRIVATE).edit();
			 e.putString("name"+i,s );	                                       //�Դ����i�����
			 e.commit();
			 }
		 
		 
		 public String loadName(int i)                                         //��ȡ��Ӧ��Ϸ�浵���û���
		 {
			 SharedPreferences p=getBaseContext().getSharedPreferences("name",Activity.MODE_PRIVATE);
			 return p.getString("name"+i, "NULL");                                //��ȡ��Ĭ��ΪNULL
		      }
		
		 
		 public void savedata(int i)                                           //�����Ѵ浵�ı�ǣ����������ٸ�����ǰ��
		 {
			 SharedPreferences.Editor e=getBaseContext().getSharedPreferences("data",Activity.MODE_PRIVATE).edit();
			 if(i==6)                                                          //���������������˺�ӵ�һ������
				i=0;
			 e.putInt("data1", i);
			 e.commit();                                                       //�ύ
			 }
		
		
		 public int loaddata()                                                 //��ȡ�浵���
		 {
			 SharedPreferences p=getBaseContext().getSharedPreferences("data",Activity.MODE_PRIVATE);
			 return p.getInt("data1", 0);
			 }
		
		
		 public void saveHero(User user[])                                     //Ӣ�۰�ı���
		 {
			 SharedPreferences.Editor e=getBaseContext().getSharedPreferences("hero",Activity.MODE_PRIVATE).edit();
			 for(int i=0;i<6;i++)
			 {
				 e.putInt("grade"+i, user[i].grade);                           //�������
			     e.putString("userName"+i,user[i].userName);                   //�����û���
			     }
			e.commit();                                                        //�ύ
		}
		
		
		 public void loadHero(User user[])                                     //��ȡӢ�۰�
		 {
			 SharedPreferences p=getBaseContext().getSharedPreferences("hero",Activity.MODE_PRIVATE);
		     for(int i=0;i<6;i++)
		     {
		    	 user[i].grade= p.getInt("grade"+i, 0);                        //��ȡ����
				 user[i].userName=p.getString("userName"+i, "NULL");              //��ȡ�û���
				 }
		 }	
		 
		 
		 public void saveItem()                                                //�Ƿ�浵�Ի���
		 {                                                                     //ͬ�ϵ�ע�Ͳ����ظ�
		     builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this); 
			 LayoutInflater factory = LayoutInflater.from(this);
			 final View textEntryView = factory.inflate(R.layout.archive, null);
			 builder.setIcon(R.drawable.icon);
			 builder.setTitle("        Current save");
			 int n = 1;
			 for(int i=0;i<6;i++)
			 {//��ȡ������ť��id
				 switch(i)
				 {
				 case 0:
					 n=R.id.button1;
					 break;
				 case 1:
					 n=R.id.button2;
					 break;
				 case 2:
					 n=R.id.button3;
					 break;
				 case 3:
					 n=R.id.button4;
					 break;
				 case 4:
					 n=R.id.button5;
					 break;
				 case 5:
					 n=R.id.button6;
					 break;
					 }
		        button[i] = (Button) textEntryView.findViewById(n);            //�õ�button����
		        String s=loadName(i);                                          //����浵����       
		        button[i].setLinkTextColor(Color.RED);                         //�����ť��ɫΪ��ɫ
		        button[i].setTextSize(18);                                     //�����С
				button[i].setText(s);                                          //���ð�ť����
				} 
			button[0].setOnClickListener(new file1());                         //Ϊ������ť���ü�����
			button[1].setOnClickListener(new file2());
			button[2].setOnClickListener(new file3());
			button[3].setOnClickListener(new file4());
			button[4].setOnClickListener(new file5());
			button[5].setOnClickListener(new file6());
			builder.setView(textEntryView);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{	                                                           //�����κβ������رնԻ���					
					    		
				}
	    	});
			builder.create().show();                                           //��������ʾ
		}
		 
		 
		 class file1 implements OnClickListener                                //��Ӧ�浵��button�ĵ���¼�
		 {
			 public void onClick(View v)
			 {
				 archiveName=button[0].getText().toString();                   //ȡ�ð�ť���ƣ��Ӷ�����ͨ�������Ƕ�ȡ�浵
				 Disp("Save "+archiveName+" chosen");                             //�����ʾ
		         if(archiveName!="NULL")                                         //�浵����ΪNULLʱ�����д浵ʱ��
		        	 executeHandler();                                         //ִ�д˺�����Ŀ���Ƕ�ȡ�浵
		         }
		}	
		 
		 
		 class file2 implements OnClickListener                                //ͬ��
		 {
			 public void onClick(View v)
			 {
				 archiveName=button[1].getText().toString();
		         Disp("Save "+archiveName+" chosen");
		         if(archiveName!="NULL")
		        	 executeHandler();
		         }
		}		 
		 
		 
		 class file3 implements OnClickListener                                //ͬ��
		 {
			 public void onClick(View v)
			 {
				 archiveName=button[2].getText().toString();
		         Disp("Save "+archiveName+" chosen");
		         if(archiveName!="NULL")
		        	 executeHandler();
		         }
		}		
		 
		 
		 class file4 implements OnClickListener                                //ͬ��
		 {
			 public void onClick(View v) 
			 {
				 archiveName=button[3].getText().toString();
                 Disp("Save "+archiveName+" chosen");
		         if(archiveName!="NULL")
		        	 executeHandler();
		         }
		}	
		 
		 
		 class file5 implements OnClickListener
		 {                                                                     //ͬ��
			 public void onClick(View v)
			 {
				 archiveName=button[4].getText().toString();
		         Disp("Save "+archiveName+" chosen");
		         if(archiveName!="NULL")
		        	 executeHandler();
		         }
		}	
		 
		 
		 class file6 implements OnClickListener                                //ͬ��
		 {
			 public void onClick(View v)
			 {
				 archiveName=button[5].getText().toString();
		         Disp("Save "+archiveName+" chosen"); 
		         if(archiveName!="NULL")
		        	 executeHandler();
		         }
		}
		 
		 
		 public void executeHandler()                                          //���ڴ浵�е��button�����
		 {
			 if(WelcomeView.wantSound)
			 {
				 wv.playSound(2, 0);                                           //��������
	             wv.playSound(2, 0);
	             }
			 myHandler.sendEmptyMessage(8);	                                   //ִ��myHandler�е�case 8������ȡ�浵
		}
		 
		 
		 public void saveRecordDialog()                                        //��������Ի���,ͬ�ϵ�ע�Ͳ����ظ�
		 {
			 AlertDialog.Builder builder = new AlertDialog.Builder(ShenzhoushihaoActivity.this);        //����
	         LayoutInflater factory = LayoutInflater.from(this);
	         final View textEntryView = factory.inflate(R.layout.main1, null);
	         builder.setIcon(R.drawable.icon);
	         builder.setTitle("    Save");
	         builder.setView(textEntryView);
	         builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
	         {
	        	 public void onClick(DialogInterface dialog, int whichButton)
	        	 {
	        		 EditText userName = (EditText) textEntryView.findViewById(R.id.etUserName);        //�õ�EditText��һ������
	        		 heroName=userName.getText().toString();                                            //�����ı��������������ת��Ϊ�ַ�����
	                 User heros=new User(heroName,currentScore[0]);                                     //����һ��User�������������������Ӣ�۵ķ����Ƚ�
	                 loadHero(user);                                                                    //��ȡӢ�۰�
	                 for(int m=0;m<6;m++)                                                               //�ȽϷ���
	                	 {
	                	 if(heros.grade>=user[m].grade)
	                	 {                                                                              //�ӵ�һ����ʼ�Ƚ�
	                		 for(int n=5;n>m;n--)                                                       //�õ�m��m����������ƣ���ǰ�Ĵ浵m���λ�ã�
	                			 user[n]=user[n-1];
	                		 user[m]=heros;
	                		 break;
	                		 }
	                	 }
	                 saveHero(user);                                           //���浱ǰ��Ӣ��
	                 }});
	         builder.setNegativeButton("No", new DialogInterface.OnClickListener()
	         {                                                                 //�����κβ�����ȡ���Ի���
	        	 public void onClick(DialogInterface dialog, int whichButton) {}
	        	 });
	         builder.create().show();
	     }
		 
		 
		 public void Disp(String s)                                            //�������
		 {
			 Toast.makeText(this, s, Toast.LENGTH_LONG).show();                //��ӡS�ַ���
		 }
		 
		 
		 public void Disp1(String s)                                           //�������
		 {
			 Toast toast = Toast.makeText(this,s, Toast.LENGTH_LONG);
	         toast.setGravity(Gravity.CENTER, 300, 10);   
             toast.show();
         }
		 
		 
		 public void Disp2(String s)                                           //�������
		 {
			 Toast toast = Toast.makeText(this,s, Toast.LENGTH_LONG);   
	         toast.setGravity(Gravity.CENTER, 300, 100);   
	         toast.show(); 
	     }
		 
		 
		 class ClickEvent implements OnClickListener                             //ʵ����ת�ļ�������
		 {
				public void onClick(View v)
				{ 
					Intent intent1=new Intent();                                 //����һ��Intent
  		            intent1=intent1.setClass(ShenzhoushihaoActivity.this,PlaneActivity.class);//����Intent
  		            ShenzhoushihaoActivity.this.startActivity(intent1);          //Activity����ת
  		            ShenzhoushihaoActivity.this.finish();                        //������ǰActivity  		     
			    }
				}
		 }
