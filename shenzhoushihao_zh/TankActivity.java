
package com.example.shenzhoushihao;                                    //声明包语句  部分代码参考《药王前传》

//引入相关类
import com.example.shenzhoushihao.BitmapManager3;
import com.example.shenzhoushihao.WelcomeView3;
import com.example.shenzhoushihao.GameView3;
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
import android.content.SharedPreferences;
import android.graphics.Color;

public class TankActivity extends Activity implements OnGestureListener//继承Activity类并实现OnGestureListener接口	
{        
	View currentView;					                               //记录当前View
	@SuppressWarnings("unchecked")
	WelcomeView3 wv;						                           //WelcomeView3对象引用
	GameView3 gv;						                               //GameView3类引用
	HelpView3 hv;						                               //HelpView3对象引用            
	HeroRecordView3 hrv;			                                   //HeroRocordView3对象引用
	PassView3 pv;                                                      //PassView3对象引用
	AboutView3 av;                                                     //AboutView3对象引用
	ProgressView3 prv;                                                 //ProgressView3对象引用
	ProgressDialog progress;                                           //ProgressDialog对象引用，设置进度条
	Vibrator vib;                                                      //Vibrator对象引用，设置触屏震动
	String heroName;                                                   //英雄榜中用于储存读取和保存的文件名  
	String archiveName="空";                                            //存档中用于储存读取和保存的文件名
	Button []button=new Button[6];                                     //Button数组，用于显示保存的文档名
	String []name=new String[6];           
	private GestureDetector detector;                                  //手势监听器
	AlertDialog.Builder builder;                                       //对话框的一个对象 
	User []user=new User[6];                                           //User对象数组，用于储存英雄榜
	boolean oneFlag=true;
	boolean twoFlag=true;
	boolean threeFlag=true;
	static int[] currentScore={0,0,0,0,0,0};                           //计算本关得分
	int n=0;
	
	//创建自定义的Handler
	Handler myHandler = new Handler()                                  //创建自定义的Handler
	{					
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg)                         //重写处理消息的方法
		{			
			switch(msg.what)                                           //判断Message对象的类型
			{							
				case 0:	
					gv= new GameView3(TankActivity.this);              //初始化GameView3
					gv.playSound(1,-1);                                //循炸弹播放游戏背景音
					setContentView(gv);						           //切换屏幕到GameView3
					currentView = gv;                                  //记录当前View
					break;
				case 1:                                                //继续游戏
					saveItem();                                        //调用此类的saveItem方法，弹出当前存档的对话框
					break;
				case 2:  
					hrv = new HeroRecordView3(TankActivity.this);      //创建HeroRecordView3对象
					setContentView(hrv);						       //切换屏幕到HeroRecordView3
					currentView = hrv;						           //记录当前View
					break;
				
				case 3:									               //显示帮助界面
					hv = new HelpView3(TankActivity.this);			   //创建HelpView3对象
					setContentView(hv);						           //切换屏幕到HelpView3
					currentView = hv;						           //记录当前View
					break;	
				case 4:									               //显示关于界面
					av = new AboutView3(TankActivity.this);			   //创建AboutView3对象
					setContentView(av);						           //切换屏幕到AboutView3
					currentView = av;						           //记录当前View
					break;
					
				case 5:									               //退出游戏
					if(currentView==wv&&WelcomeView3.wantSound)        //当前View为WelcomeView3且声音开关为开
					{
						wv.stopSound(1);						       //停止声音的播放
					    wv.soundPool.release();					       //释放WelcomeView3的SoundPool对象
					    wv.soundPool=null;
					}
					else if(currentView==gv&&WelcomeView3.wantSound)   //若当前View为WelcomeView3的一个对象且音效选择为开启
					{
						gv.stopSound(1);						       //停止声音的播放
						gv.soundPool.release();					       //释放WelcomeView3的SoundPool对象
						gv.soundPool=null;
					}
					android.os.Process.killProcess(android.os.Process.myPid()); //结束进程
					System.exit(0);							           //退出程序
					break;
				case 6:									               //过关或失败	
					pv = new PassView3(TankActivity.this);	           //创建PassView3对象
					setContentView(pv);						           //切换屏幕到PassView3
					currentView = pv;						           //记录当前View
					if(WelcomeView3.wantSound)                         //音效选择是开启
					{
						if(gv.failFlag&&!gv.nextStage)                 //若失败    
						    pv.playSound(3, 0);                        //播放失败背景音效
						else
					    {                                              //成功
							if(GameView3.stage==0)
							    pv.playSound(4, 0);                    //播放成功背景音效
						    else
						        pv.playSound(2, 0);                    //播放成功背景音效
					    }
					}
					if(gv.nextStage)
						saveRecordDialog();
					break;
				case 7:
					prv = new ProgressView3(TankActivity.this,progress); //初始化
					setContentView(prv);						       //切换屏幕到ProgressView3
					currentView = prv;                                 //记录当前View
					break;
				case 8:                                                //点击当前存档对话框上的存档后，读取存档
					load(archiveName);                                 //载入
					
					if(gv.bombNumber==0)
					{                                                  //如果炸弹的个数为零 
						pv=new PassView3(TankActivity.this);           //声明一个PassView3对象
						setContentView(pv);	                           //设置当前View为PassView3
						currentView = pv;                              //记录当前的View
					}
					else                                               //炸弹数不是零，则进入GameView3
					{
						setContentView(gv);	                           //设置当前View为GameView3
						if(WelcomeView3.wantSound)
						  gv.playSound(1,-1);                          //循环播放游戏背景音，id为1
						currentView = gv;                              //记录当前的View
					}
					break;	
				default:
						break;
			}
		}
	};
	
		
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)                    //Activity的子类时率先自动执行的方法
	{  		  
		super.onCreate(savedInstanceState);	        
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);      //设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
		for(int i=0;i<6;i++)
		{
			user[i]=new User();
		    currentScore[i]=0;
		}
		progress=new ProgressDialog(TankActivity.this);                //初始化进度条
		vib=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);//振动的初始化
		detector=new GestureDetector(this);                            //初始化手势监听器
		BitmapManager3.loadWelcomePublic(getResources());	           //加载欢迎界面的图片资源
		wv = new WelcomeView3(TankActivity.this);                      //初始化WelcomeView3
		setContentView(wv);						                       //设置当前屏幕为WelcomeView3对象
	    currentView = wv;	  
			    	        
	}
	
	
	public boolean onTouchEvent(MotionEvent event)                     //点击屏幕
	{      
		if(currentView == wv)                                          //欢迎界面响应
		{	
			if(event.getAction() == MotionEvent.ACTION_UP)             //当点击屏幕的手指离开屏幕时响应
			{               
				int x = (int)event.getX();			                   //获取屏幕点击处的X坐标
				int y = (int)event.getY();			                   //获取屏幕点击处的Y坐标
				return wv.myTouchEvent(x, y);                          //调用WelcomeView3中的myTouchEvent方法
			}
		}
		else if(currentView==hv)                                       //帮助界面响应
		{     
			if(event.getAction() == MotionEvent.ACTION_UP)             //当点击屏幕的手指离开屏幕时响应
			{                  
				int x = (int)event.getX();			                   //获取屏幕点击处的X坐标
				int y = (int)event.getY();	                           //获取屏幕点击处的Y坐标
				return hv.myTouchEvent(x, y);                          //调用HelpView3中的myTouchEvent方法
			}
		}
		else if(currentView==av)                                       //关于界面响应
		{                  
			if(event.getAction() == MotionEvent.ACTION_UP)             //当点击屏幕的手指离开屏幕时响应
			{                
				int x = (int)event.getX();			                   //获取屏幕点击处的X坐标
				int y = (int)event.getY();	                           //获取屏幕点击处的Y坐标
				return av.myTouchEvent(x, y);                          //调用AboutView3中的myTouchEvent方法
			}
		}
		else if(currentView==pv)                                       //过关界面响应
		{                  
			if(event.getAction() == MotionEvent.ACTION_UP)             //当点击屏幕的手指离开屏幕时响应
			{           
				int x = (int)event.getX();			                   //获取屏幕点击处的X坐标
				int y = (int)event.getY();	                           //获取屏幕点击处的Y坐标
				return pv.myTouchEvent(x, y);                          //调用PassView3中的myTouchEvent方法      
						
			}
		}
		else if(currentView==hrv)                                      //英雄榜界面响应
		{                   
			if(event.getAction() == MotionEvent.ACTION_UP)             //当点击屏幕的手指离开屏幕时响应
			{           
				int x = (int)event.getX();			                   //获取屏幕点击处的X坐标
				int y = (int)event.getY();	                           //获取屏幕点击处的Y坐标
				return hrv.myTouchEvent(x, y);                         //调用PassView3中的myTouchEvent方法      
			}
		}
		else if(currentView == gv&&gv.status!=1)                       //如果当前View为GameView3并且炸弹的状态不是在飞行
		{			
			detector.onTouchEvent(event);                              //监听器监听
			return false;	
		}
		return true;
	}
	
	
	public boolean onDown(MotionEvent e)                               //实现OnGestureListener接口重写的方法，点击屏幕按下的事件响应
	{     
		return false;
	}
	
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY)//滑动抛出炸弹的响应方法，前两个参数分别代表起始点和终点，
	                                                                   //后两个参数是滑动屏幕时在x,y两个方向上的加速度
	{	
		int x = (int)e1.getX();			                               //获取屏幕点击处的X坐标
		int y = (int)e1.getY();	                                       //获取屏幕点击处的y坐标，用来判断起始点是否在炸弹所在的区域
		if(GameView3.stage==0)
		{
			if(gv.bombNumber==gv.initBomb-1)
			    Disp1("注意左下角,您剩余的炸弹数有更新");
			else if(gv.bombNumber==4)
		    {
				Disp1("注意左下角,您剩余的炸弹数已不多");
				
		    }
			if(gv.taskArray[0][0]==4&&oneFlag)                         //taskarray :此数组标志右下角的任务值
				{
					oneFlag=false;
				    Disp2("右下角的任务进度有更新,您须根据任务选择轰炸敌方坦克");
				}
				if(gv.taskArray[0][1]==3&&twoFlag)
				{
					twoFlag=false;
					Disp2("右下角的任务进度有更新,您须根据任务选择轰炸敌方坦克");
				}
				if(gv.taskArray[0][2]==2&&threeFlag)
				{
					threeFlag=false;
					Disp2("右下角的任务进度有更新,您须根据任务选择轰炸敌方坦克");
				}
			}     
			if(gv.status!=1&&x>95&&x<145&&y>270&&y<320)                //若此时没有炸弹在空中飞，即上一次抛出的炸弹已经落地，并且起始点位于元炸弹所在的区域
				{ 
					gv.playSound(2, 0);                                //调用GameView3的playSound方法，播放圆炸弹飞出的音效，若音效选择的是关闭，此方法会自动不播放
					if(Math.abs(velocityX)>1150)                       //给x方向的加速度设置一个上限
				    {
						velocityX=velocityX>0?1150:(-1150);            //当达到上限时取上限
				     }
					if(Math.abs(velocityY)>2700)                       //给y方向的加速度设置一个上限
				    {
					    velocityY=velocityY>0?2700:(-2700);            //当达到上限时取上限
				    }
					//下面这样设计的目的是，当不小心触到屏幕时，炸弹不飞出，在x,y方向上起始点的距离的绝对值必须大于FLING_MIN_DISTANCE
					if (e1.getX() - e2.getX() > GameView3.FLING_MIN_DISTANCE)      //向左滑动
			        {	    
						gv.xx=velocityX/11;                            //加速度在x方向上成比例缩小
					    gv.yy=velocityY/11;		                       //加速度在y方向上成比例缩小
					    gv.status=1;                                   //将圆炸弹状态改为在空中飞行的状态
					    return true;
		
					} 
					else if (e1.getX() - e2.getX() < -GameView3.FLING_MIN_DISTANCE)//向右滑动
					{   
						gv.xx=velocityX/11;                            //加速度在x方向上成比例缩小
						gv.yy=velocityY/11;	                           //加速度在y方向上成比例缩小
						gv.status=1;                                   //将圆炸弹状态改为在空中飞行的状
                        return true;
                    } 
					else if (e1.getY() - e2.getY() > GameView3.FLING_MIN_DISTANCE)//向上滑动
					{   
						gv.xx=velocityX/11;                            //加速度在x方向上成比例缩小                                          
						gv.yy=velocityY/11;		                       //加速度在y方向上成比例缩小
						gv.status=1;                                   //将圆炸弹状态改为在空中飞行的状态
					    return true;
		
					} 
					else if (e1.getY() - e2.getY() > -GameView3.FLING_MIN_DISTANCE)//向下滑动
					{    
						gv.xx=velocityX/11;                            //加速度在x方向上成比例缩小
						gv.yy=velocityY/11;		                       //加速度在y方向上成比例缩小	
						gv.status=1;                                   //将圆炸弹状态改为在空中飞行的状态
					    return true;
		
					}
		        }
			return false;
		}
	
	
	public void onLongPress(MotionEvent arg0)    
	{ 
		// TODO Auto-generated method stub       //实现OnGestureListener接口重写的方法，长按屏幕的事件响应
	}
	
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) 
	{                 
		// TODO Auto-generated method stub       //实现OnGestureListener接口重写的方法，在屏幕上移动的事件响应
		return false;
	}
	
	
	public void onShowPress(MotionEvent e)       //实现OnGestureListener接口重写的方法
	{      
		// TODO Auto-generated method stub
				
	}
	
	
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(currentView==gv||currentView==pv)      //当前View为GameView3或PassView3时才有效
			{            
				AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this);//同上，声明
				builder.setIcon(R.drawable.icon);     //同上，设置图像
				builder.setTitle("是否保存？");          //同上，设置标题
				builder.setPositiveButton("是", new DialogInterface.OnClickListener()     //同上
				{    
					public void onClick(DialogInterface dialog, int which)               //同上
					{     
						saveDialog();                 //弹出“存档对话框”
				    }
			    });
				builder.setNegativeButton("否", new DialogInterface.OnClickListener()    //同上
				{                   
				    public void onClick(DialogInterface dialog, int which)               //同上
				    {                            
				    	myHandler.sendEmptyMessage(5);//执行Handler中case 5的语句，不存档直接退出游戏
				    }
				});
				builder.create().show();              //创建并显示对话框
			}
			else                                      //弹出“退出对话框”，注释同上
			{          
				AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this);       
				builder.setIcon(R.drawable.icon);
				builder.setTitle("    确定退出？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{	
						if(WelcomeView3.wantSound)
					    {
							wv.playSound(2,0);
							wv.playSound(2,0);
					    }
						myHandler.sendEmptyMessage(5);//执行Handler中case 5的语句，直接退出
					}
			    });
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int which)//不作任何操作，对话框消失
				    {}
				});
			    builder.create().show();              //创建并显示对话框
			}
			return true;      	
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	
	public boolean onSingleTapUp(MotionEvent e)       //实现OnGestureListener接口重写的方法
	{   
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)     //为手机的菜单键设置响应，设置菜单项为  “菜单”，“音效”，“退出”
	{             
		menu.add("菜单");                              //菜单选项一共有菜单，存档，音效，退出四个选项
		menu.add("存档");
		menu.add("音效");
	    menu.add("退出");
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)//对手机上的菜单键设置响应的函数
	{        
		if(item.getTitle().equals("菜单"))              //点击菜单的响音
		{                   
			if(WelcomeView3.wantSound&&currentView==gv)//若音效选择的是开启，并且从游戏进行中回到主功能页面
			{
				gv.stopSound(1);
				gv.soundPool.release();
				gv.soundPool=null;
			}
			setContentView(wv);						  //设置当前屏幕为WelcomeView3对象
			currentView = wv;	                      //记录当前View
			wv.status =7;                             //显示游戏的主功能页面
					
		}
		else if(item.getTitle().equals("存档"))         //点击菜单的响音
		{                   
			if(currentView==gv||currentView==pv)      //当前View为GameView3或PassView3时才有效
				if(GameView3.stage!=3)               
			        saveDialog();                     //弹出存档的对话框
		}
		else if(item.getTitle().equals("音效"))         //点击了音效这个菜单项
		{            
			AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this);  //声明一个对话框
			builder.setIcon(R.drawable.icon);         //设置图像为raw文件中文件名为icon的图片
			builder.setTitle("音效开关");                //对话框标题
			builder.setPositiveButton("开启", new DialogInterface.OnClickListener()    //监听"开启"按钮
			{    
				public void onClick(DialogInterface dialog, int which)                //被点击时 
				{   
					wv.musicDialogOpen();             //调用WelcomeView3中的musicDialogOpen（）方法
				}
			});
			builder.setNegativeButton("关闭", new DialogInterface.OnClickListener()    //监听关闭按钮
			{     
				public void onClick(DialogInterface dialog, int which)                //被点击时
				{           
				    wv.musicDialogClose();            //调用WelcomeView3中的musicDialogClose（）方法
				}
			});
			builder.create().show();                  //显示对话框
		}
		else if(item.getTitle().equals("退出"))        //点击退出时
		{             
			if(currentView==gv||currentView==pv)      //当前View为GameView3或PassView3时才有效
			{            
				AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this);//同上，声明
				builder.setIcon(R.drawable.icon);     //同上，设置图像
				builder.setTitle("是否保存？");          //同上，设置标题
				builder.setPositiveButton("是", new DialogInterface.OnClickListener()     //同上
				{    
					public void onClick(DialogInterface dialog, int which)               //同上
					{     
						saveDialog();                 //弹出“存档对话框”
				    }
			    });
					
			    builder.setNegativeButton("否", new DialogInterface.OnClickListener()   //同上
			    {                   
			    	public void onClick(DialogInterface dialog, int which)              //同上
				    {                            
				    	myHandler.sendEmptyMessage(5);//执行Handler中case 5的语句，不存档直接退出游戏
				    }
				});
			    builder.create().show();              //创建并显示对话框
			}
			else                                      //弹出“退出对话框”，注释同上
			{          
				AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this);       
				builder.setIcon(R.drawable.icon);
				builder.setTitle("    确定退出？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{	
						if(WelcomeView3.wantSound)
					    {
							wv.playSound(2,0);
							wv.playSound(2,0);
					    }
						myHandler.sendEmptyMessage(5);//执行Handler中case 5的语句，直接退出
					}
			    });
			    builder.setNegativeButton("否", new DialogInterface.OnClickListener() 
			    {
				    public void onClick(DialogInterface dialog, int which)   //不作任何操作，对话框消失
				    {}
				});
			    builder.create().show();                                     //创建并显示对话框
			}
		}
		return super.onOptionsItemSelected(item);
				
	}
	
	
    public void saveDialog()                          //弹出存档的对话框的方法，同上的注释不再重复写
    {                    
		AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this); //声明，
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.main1, null);          //显示xml中的布局
		builder.setIcon(R.drawable.icon);
		builder.setTitle("       游戏存档");
		builder.setView(textEntryView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				EditText userName = (EditText) textEntryView.findViewById(R.id.etUserName); //得到文本框对象
				heroName=userName.getText().toString();   //将文本框中输入的内容转换成String类型
				save(gv, heroName);                       //存档
				int i=loaddata();                         //读取现在已经存档已经存到了第几个
				saveName(heroName,i);                     //存入姓名
				i++;                                      //+1
				savedata(i);                              //储存i
				myHandler.sendEmptyMessage(5);            //执行Handler中case 5的语句，退出游戏
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() //不作任何操作，对话框消失
		{     
			public void onClick(DialogInterface dialog, int whichButton) 
			{}
		});
		builder.create().show();                          //创建并显示对话框
				
	}
    
    
	@SuppressWarnings({ "static-access" })
	//游戏的保存
	public  void save(GameView3 gv,String s)
	{
		SharedPreferences.Editor e=getBaseContext().getSharedPreferences(s,Activity.MODE_PRIVATE).edit();
		e.putInt("stage", GameView3.stage);             //存储游戏的关数
		e.putInt("bombNumber", gv.bombNumber);          //存储所剩的炸弹数
		for (int i=0;i<gv.bitmapData[4].flag.length;i++)//存储图片的是否显示
		{     
			e.putBoolean( "flag"+i,gv.bitmapData[4].flag[i]);
		}
		e.putInt("get1",gv.taskArray[gv.stage][2]);     //存储任务数组
	    e.putInt("get2", gv.taskArray[gv.stage][1]);
		e.putInt("get3", gv.taskArray[gv.stage][0]);
		e.putBoolean("sound", WelcomeView3.wantSound);  //存储是否播放声音
		e.putBoolean("fail",gv.failFlag);               //存储是否游戏失败
		e.putBoolean("nextStage", gv.nextStage);        //存储是否进行下一关
		e.putString("x1",gv.s3);                        //存储大坦克的已炸中个数
		e.putString("x2",gv.s2);                        //存储中坦克的已炸中个数
		e.putString("x3",gv.s1);                        //存储小坦克的已炸中个数
		e.commit();                                     //提交
	}
	
	
	@SuppressWarnings({ })
	//游戏的读取
	public void load(String s)
	{
		SharedPreferences p=getBaseContext().getSharedPreferences(s,Activity.MODE_PRIVATE);
		int n=p.getInt("stage", 0); 
		GameView3.stage=n;
		gv= new GameView3(TankActivity.this);           //声明一个新的GameView3对象
		gv.bombNumber=p.getInt("bombNumber",20);        //读取剩余炸弹数，默认为20
		for (int i=0;i<gv.bitmapData[4].flag.length;i++)//读取每一张图片的状态，即是否在屏幕上绘制它的标志
		{           
			gv.bitmapData[4].flag[i]= p.getBoolean( "flag"+i,true);     //默认false
		}
		gv.taskArray[n][2] =p.getInt("get1",0);         //读取任务进度
		gv.taskArray[n][1]= p.getInt("get2",0 );
		gv.taskArray[n][0]= p.getInt("get3",0 );
		gv.tt.flag=true;                                //启动线程标志为true
		gv.tdt.flag=true;
		gv.tdt.isDrawOn=true;
		gv.failFlag=p.getBoolean("fail", false);        //是否失败的标志
		gv.nextStage=p.getBoolean("nextStage", false);  //是否进入下一关的标志
		gv.s3=p.getString("x1", "0");                   //任务进度的字符串，屏幕右下角需要绘制的
		gv.s2=p.getString("x2", "0");
		gv.s1=p.getString("x3", "0");
					 
	}
	
	
	//保存对应游戏存档的用户名
	public void saveName(String s,int i)
	{
		SharedPreferences.Editor e=getBaseContext().getSharedPreferences("name",Activity.MODE_PRIVATE).edit();
		e.putString("name"+i,s );	                    //以传入的i来标记
		e.commit();                                     //提交
	}
	
	
	//读取对应游戏存档的用户名
	public String loadName(int i)
	{
		SharedPreferences p=getBaseContext().getSharedPreferences("name",Activity.MODE_PRIVATE);
	    return p.getString("name"+i, "空");              //读取，默认为空
	}
	
	
	//保存已存档的标记，若已满，再覆盖以前的
	public void savedata(int i)
	{
		SharedPreferences.Editor e=getBaseContext().getSharedPreferences("data",Activity.MODE_PRIVATE).edit();
		if(i==6)                                        //储存六个，当满了后从第一个覆盖
			i=0;
		e.putInt("data1", i);
		e.commit();                                     //提交
	}
	
			
	//读取存档标记
	public int loaddata()                               //读取，同上注释不再重复
	{
		SharedPreferences p=getBaseContext().getSharedPreferences("data",Activity.MODE_PRIVATE);
		return p.getInt("data1", 0);
	}
	
	
	//英雄榜的保存
	public void saveHero(User user[])
	{
		SharedPreferences.Editor e=getBaseContext().getSharedPreferences("hero",Activity.MODE_PRIVATE).edit();
		for(int i=0;i<6;i++)
		{
			e.putInt("grade"+i, user[i].grade);         //保存分数
			e.putString("userName"+i,user[i].userName); //保存用户名
		}
		e.commit();                                     //提交
	}
	
	
	//读取英雄榜
	public void loadHero(User user[])
	{
		SharedPreferences p=getBaseContext().getSharedPreferences("hero",Activity.MODE_PRIVATE);
	    for(int i=0;i<6;i++)
	    {
			user[i].grade= p.getInt("grade"+i, 0);               //读取分数
			user[i].userName=p.getString("userName"+i, "空");     //读取用户名
		}
	}
	
	
    //是否存档对话框
	public void saveItem()//同上的注释不再重复
	{                      
		builder = new AlertDialog.Builder(TankActivity.this); 
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.archive, null);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("        当前存档");
		int n = 1;
		for(int i=0;i<6;i++)                                     //获取各个按钮的id
		{                    
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
			button[i] = (Button) textEntryView.findViewById(n); //得到button对象
			String s=loadName(i);                               //读入存档姓名       
			button[i].setLinkTextColor(Color.RED);              //点击后按钮颜色为红色
			button[i].setTextSize(18);                          //字体大小
			button[i].setText(s);                               //设置按钮名称
		} 
		button[0].setOnClickListener(new file1());              //为六个按钮设置监听器
	    button[1].setOnClickListener(new file2());
		button[2].setOnClickListener(new file3());
		button[3].setOnClickListener(new file4());
		button[4].setOnClickListener(new file5());
		button[5].setOnClickListener(new file6());
		builder.setView(textEntryView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) //不作任何操作，关闭对话框
			{}
		});
		builder.create().show();                                   //创建并显示
				
    }
	
	
	//响应存档中button的点击事件
	class file1 implements OnClickListener
	{
		public void onClick(View v) 
		{
			archiveName=button[0].getText().toString();           //取得按钮名称，从而可以通过这个标记读取存档
			Disp("选择了"+archiveName+"存档");                       //输出提示
			if(archiveName!="空")                                 //存档名不为空时，即有存档时，
				executeHandler();                                 //执行此函数，目的是读取存档
		}
	}
	
	
	class file2 implements OnClickListener                       //同上
	{                   
		public void onClick(View v)
		{
			archiveName=button[1].getText().toString();
			Disp("选择了"+archiveName+"存档");
			if(archiveName!="空")
				executeHandler();
		}
	}
	
	
	class file3 implements OnClickListener                       //同上
	{                  
		public void onClick(View v) 
		{
			archiveName=button[2].getText().toString();
			Disp("选择了"+archiveName+"存档");
			if(archiveName!="空")
				executeHandler();
		}
	}
	
	
	class file4 implements OnClickListener                       //同上
	{                  
		public void onClick(View v)
		{
	      archiveName=button[3].getText().toString();
	      Disp("选择了"+archiveName+"存档");
			if(archiveName!="空")
				executeHandler();
		}
	}
	
	
	class file5 implements OnClickListener                       //同上
	{                  
		public void onClick(View v) 
		{
			archiveName=button[4].getText().toString();
			Disp("选择了"+archiveName+"存档");
			if(archiveName!="空")
				executeHandler();
		}
	}
	
	
	class file6 implements OnClickListener                       //同上
	{                  
		public void onClick(View v) 
		{
			archiveName=button[5].getText().toString();
			Disp("选择了"+archiveName+"存档"); 
			if(archiveName!="空")
			   executeHandler();
		}
	}
	
	
	//用于存档中点击button后调用
	public void executeHandler()
	{   
		if(WelcomeView3.wantSound)
		{
			wv.playSound(2, 0);                                  //播放声音
			wv.playSound(2, 0);
		}
			myHandler.sendEmptyMessage(8);	                     //执行myHandler中的case 8，即读取存档
	}
	
	
	//保存分数对话框
	public void saveRecordDialog()                               //同上的注释不再重复
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(TankActivity.this); //声明
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.main, null);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("    保存得分纪录");  
		builder.setView(textEntryView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				EditText userName = (EditText) textEntryView.findViewById(R.id.etUserName);//得到EditText的一个对象
		        heroName=userName.getText().toString();           //将在文本框中输入的内容转换为字符串型
		        User heros=new User(heroName,currentScore[0]);    //声明一个User对象，用于下面的与所存英雄的分数比较
		        loadHero(user);                                   //读取英雄榜
		        for(int m=0;m<6;m++)                              //比较分数
		        {
		        	if(heros.grade>=user[m].grade)                //从第一个开始比较
		        	{                 
		        		for(int n=5;n>m;n--)                      //得到m后，m下面的往后移，当前的存档m这个位置，
					    user[n]=user[n-1];
		        		user[m]=heros;               
				        break;
				     }
		        }
		        saveHero(user);                                   //保存当前的英雄
		       }
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()//不做任何操作，取消对话框
		{                
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		builder.create().show();
	}
	
	
	public void Disp(String s)                                    //输出方法
	{
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();        //打印S字符串	
	}
	public void Disp1(String s)                                   //输出方法
	{
		Toast toast = Toast.makeText(this,s, Toast.LENGTH_LONG);   
		toast.setGravity(Gravity.CENTER, 300, 10);   
		toast.show(); 
		
	}

	
	public void Disp2(String s)                                   //输出方法
	{
		Toast toast = Toast.makeText(this,s, Toast.LENGTH_LONG);   
		toast.setGravity(Gravity.CENTER, 300, 100);   
		toast.show(); 
	}
		
}
