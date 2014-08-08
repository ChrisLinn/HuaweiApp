 
package com.example.shenzhoushihao;                                     //声明包语句

//引入相关类
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@SuppressWarnings("unused")
public class WelcomeView3 <MyActivity> extends SurfaceView implements SurfaceHolder.Callback //继承SurfaceView类实现SurfaceHolder和Callback接口
{    
	TankActivity father;					                            //TankActivity的引用
	WelcomeThread3 wt;                                                  //后台修改数据线程
	WelcomeDrawThread3 wdt;                                             //后台重绘线程
	boolean ifsound;                                                    //判断标志变量,为true时代表游戏画面正处于音效选择界面，此时音效的开启和关闭两个触屏事件生效
	boolean skip;                                                       //判断标志变量,为true时代表游戏画面正处于出现字幕的界面，此时“跳过”开关触屏事件生效
	public static boolean sounder;                                      //从手机的菜单键进入的选择音效界面的判断是否开启声音的标志变量，这个的改变会使wantSound也改变，从而达到控制声音的效果
	public static boolean wantSound;                                    //WelcomeView3判断是否开启声音的标志变量
	
	int status=-1;		                                                //记录当前状态,0：闪屏画面逐渐消失，8：音效界面，7：游戏菜单，1：显示ipad屏幕，3：显示文字
	int alpha = 0;                                                      //记录背景图片的背景色
	int screenX=320;                                                    //记录屏幕左上角的x坐标
	int screenY=60;                                                     //记录屏幕左上角的y坐标
	float textSize = 13f;                                               //字体的大小
	int characterSpanX=24;                                              //文字行之间的偏移
	int characterSpanY=15;                                              //每个字的大小
	int characterNumber=1;                                              //已在屏幕中显示的字的数目
	int textStartX = 164;                                               //文字的起始x坐标 
	int textStartY = 75;                                                //文字的起始y坐标
	int characterEachLine =13;                                          //每一行显示字的个数
	int startChar=0;                                                    //字符串中从哪里开始输出，为了滚屏使用
	int maxChar=66;                                                     //屏幕上最大能显示的字数
	
	//逐个显示字的算法借鉴于大多书上的GreatRun游戏
	char [] str={'神','舟','十','号','的','宇','航','员',
			'经','历','过','“','准','备','发','射','”',','
			,'“','太','空','遨','游','”','“','太','空','狙',
			'击','”','的','任','务','后',',','顺','利','完',
			'成','此','次','发','射','任','务',',','让',
			'世','界','见','证','了','中','国','创','造',
			'的','奇','迹',',','也','让','我','们','相',
			'信',',','哪','怕','身','处','在','小','小',
			'的','世','界',',','只','要','全','力','去',
			'做',',','也','能','实','现','自','己','大',
			'大','的','梦','想','.','.','.','.','.','.'};                //显示的文字内容
	Rect rectStart    =new Rect( 0,  0,  90, 32);                       //开始菜单
	Rect rectContinue =new Rect( 30,67,120,  99);                       //继续菜单
	Rect rectHero     =new Rect( 80,134,170,166);                       //英雄榜菜单
	Rect rectHelp     =new Rect(120,201,210,233);                       //帮助菜单
	Rect rectExit     =new Rect(150,267,240,300);                       //退出菜单
	
	Rect rectSoundMenu = new Rect(94,445,226,475);                      //声音菜单
	Rect rectSoundMenu1 = new Rect(20,280,60,320);                      //声音菜单
	Rect rectSoundMenu2 = new Rect(200,300,240,320);                    //声音菜单
	Rect rectSkip = new Rect(90,290,240,320);                           //跳过菜单
	
	SoundPool soundPool;		                                        //SoundPool对象引用
	HashMap<Integer,Integer> soundMap;	                                //存放声音资源的Map
	
	boolean play;
	
	
	//构造器，初始化主要成员变量
	public WelcomeView3(TankActivity father)                            //构造方法
	{  
		super(father);                                                  //初始化父类
		this.father = father;                                           //初始化成员变量	
		getHolder().addCallback(this);                                  //初始化
		wt = new WelcomeThread3(this);                                  //初始化线程
		wdt = new WelcomeDrawThread3(this,getHolder());                 //初始化重绘线程
		initSound(father);                                              //初始化声音的方法，下面有
		status = 0;	                                                    //初始化为零
	}
	
	
	//绘制屏幕
    public void doDraw(Canvas canvas)                                   //doDraw方法，WelcomeDrawThread3反复调用此方法绘制屏幕
    {            
		Paint paint = new Paint();	                                    //创建画笔
		paint.setAlpha(alpha);		                                    //亮度
		switch(status)
		{
		case 0:
			canvas.drawColor(Color.BLACK);                              //清屏
			BitmapManager3.drawWelcomePublic(0, canvas, 0, 0,paint);    //画闪屏界面背景
			ifsound=true;                                               //把下一个声音选择界面的触屏事件设为有效
			break;
		case 8:                                                         //状态：显示音效
			canvas.drawColor(Color.BLACK);                              //清屏
			BitmapManager3.drawWelcomePublic(3, canvas, 0, 0,paint);    //绘制声音选择界面
			break;
		case 7:                                                         //状态：显示游戏菜单界面
			canvas.drawColor(Color.BLACK);	   //清屏
			BitmapManager3.drawWelcomePublic(2, canvas, 0, 0,paint);    //主菜单背景
			BitmapManager3.drawWelcomePublic(7, canvas, 0,0,paint);     //下面的都是“开始“继续”“英雄榜”等菜单项的字
			BitmapManager3.drawWelcomePublic(8, canvas, 30,67,paint);   //在不同的坐标处绘制
			BitmapManager3.drawWelcomePublic(9, canvas, 80,134,paint);
			BitmapManager3.drawWelcomePublic(10, canvas, 120,201,paint);
			BitmapManager3.drawWelcomePublic(12, canvas, 150, 267,paint);
			break;
		case 1:                                                           //状态：显示ipad屏幕
			canvas.drawColor(Color.BLACK);                                //清屏
			alpha=255;                                                    //重新设置亮度
			paint.setAlpha(alpha);                                        //设置画笔
			BitmapManager3.drawWelcomePublic(2, canvas, 0, 0,paint);      //背景
			BitmapManager3.drawWelcomePublic(1, canvas, screenX, 0,paint);//显示动态的ipad屏幕
			break;
        case 3:                                                           //状态：显示文字
			canvas.drawColor(Color.BLACK);                                //清屏
			BitmapManager3.drawWelcomePublic(2, canvas, 0, 0,paint);      //主功能页面背景
			BitmapManager3.drawWelcomePublic(1, canvas, screenX, 0,paint);//绘制ipad屏幕图片
			paint.setTextSize(textSize);			                      //设置字体大小
			paint.setColor(Color.RED);                                    //设置字体颜色
			paint.setTypeface(Typeface.DEFAULT_BOLD);	                  //设置粗体
			paint.setAntiAlias(true);					                  //设置抗锯齿
			if(characterNumber > maxChar)                                 //如果要显示的字数超过了ipad屏幕最大显示个数
			{
				startChar += characterEachLine;		                      //修改字符数组中开始绘制的位置
				characterNumber -= characterEachLine;	
			}	
				
			
	        //文字出现的算法借鉴于很多android经典教程上的GreatRun游戏的文字出现算法
	        int lines = characterNumber/characterEachLine + (characterNumber%characterEachLine==0?0:1);//计算 一共需要几行
		    Paint paint1 = new Paint();	                                  //创建画笔
		    Typeface font = Typeface.create(Typeface.SERIF, Typeface.BOLD);
			paint1.setStrokeWidth(20);
			paint1.setARGB(200, 200, 80, 60);
			paint1.setTypeface(font);

			for(int i=0;i<lines;i++)
			{
				if(i == lines-1)                                          //最后一排
				{                           
					int n = characterNumber-characterEachLine*(lines-1);  //剩余的字数
					for(int j=0;j<n-1;j++)                                //遍历写字
					{              
						canvas.drawText(str[startChar+i*characterEachLine+j]+"", textStartX-i*characterSpanX, textStartY+j*characterSpanY, paint);
					}
				}
				else                                                      //还不是最后一排
				{                           
					for(int j=0;j<characterEachLine;j++)                  //每一列的字数
					{                       
						canvas.drawText(str[startChar+i*characterEachLine+j]+"", textStartX-i*characterSpanX, textStartY+j*characterSpanY, paint);
					}					
				}				
			}			
			break;
		default:
			break;
		}
	}
    
    
	//方法：初始化声音
	public void initSound(TankActivity father)                            //initSound方法体在构造方法中被调用
	{                        
		soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,100);       //初始化soundPool,参数分别为允许同时播放的音乐最多为一个，第二个是类型，最后是音质
		soundMap = new HashMap<Integer,Integer>();                        //初始化存放声音资源的哈希表
		int id1= soundPool.load(father, R.raw.welcome_background, 1);     //为每一个需要的音乐设定id
		int id2 = soundPool.load(father, R.raw.game_press, 2);
		int id3= soundPool.load(father, R.raw.game_background, 3);
		soundMap.put(1, id1);                                             //加入声音池，将id号对应起来，便于调用
		soundMap.put(2, id2);	
		soundMap.put(3, id3);	
	}
    //方法：播放声音
	public void playSound(int sound,int loop)                           //重写系统的播放声音的方法，第一个参数是音乐的id，
	                                                                    //第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
	{                    
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		float volume = streamVolumeCurrent / streamVolumeMax;   	    
		soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);//系统的播放声音的方法
	}
	
				
    //方法：停止声音
	public void stopSound(int sound)                                    //重写停止声音的方法，主要母的是可以通过传递音乐资源的id
	{                   
		soundPool.stop(soundMap.get(sound));                            //系统停止声音方法
	}	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//重写接口方法
	{}
	
				
    public void surfaceCreated(SurfaceHolder holder)                    //重写surfaceCreated方法 
    {
    	wdt.isDrawOn = true;                                            //绘制标志置为true
		if(!wdt.isAlive())                                              //如果线程没有启动
		{                  
			wdt.start();                                                //启动
		}
		wt.flag = true;                                                 //WelcomeThread3的循环标志置为true
		if(!wt.isAlive())                                               //如果线程没有启动
		{                  
			wt.start();                                                 //则启动
		}		
	}
    
				
	public void surfaceDestroyed(SurfaceHolder holder)                  //重写surfaceDestroyed方法
	{
		wdt.isDrawOn = false;
	}
	
	
	//************WelcomeView中个触屏响应*************************************************************
	public boolean myTouchEvent(int x,int y)                            //刚启动游戏的时候的音乐选项
	{
		if(ifsound)                                                     //此界面的触屏标志为true时才生效
		{
			if(rectSoundMenu1.contains(x,y))                            //点击”是“的区域
			{	
				playSound(2,0);                                         //播放按键音
				playSound(1,-1);                                        //循环背景音
				wantSound=true;                                         //声音开关标志为true
			    father.vib.vibrate(new long[]{100,10,100,70},-1);       //振动
				status=7;                                               //进入WelcomeView3中的case 7
			}
			else if(rectSoundMenu2.contains(x,y))                       //点击”否“区域
			{
				wantSound=false;                                        //声音开关标志为false
				father.vib.vibrate(new long[]{100,10,100,70},-1);       //振动
				status=7;                                               //同样也是跳到WelcomeView3中的case 7
			}
		}
		//跳过字幕选项
		if(skip)                                                        //此界面的触屏标志为true时才生效
		{
			if(rectSkip.contains(x,y))                                  //点在了规定的”跳过“区域
			{	
				if(wantSound)                                           //如果声音标志为true
				{ 
					playSound(2,0);                                     //播放按键音
				}
				father.vib.vibrate(new long[]{100,10,100,70},-1);       //振动
				status=7;                                               //跳到WelcomeView3中的case  7
			}	
		}	
					 
		//主菜单选项
		if(rectStart.contains(x, y)&&status==7)                         //点击了”开始“区域，且当前的画面实在主功能界面，开始新的游戏
		{
			if(wantSound)                                               //如果声音开关标志为true，
			{
				playSound(2,0);                                         //播放按键音
				playSound(2,0);
			}
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //振动
			GameView3.stage=0;                                          //触摸开始游戏后 从哪一关卡开始  
			father.myHandler.sendEmptyMessage(7);                       //跳到TankActivity中的handler中的case 7
		}
		else if(rectContinue.contains(x, y)&&status==7)                 //点击了”继续“区域，且当前的画面实在主功能界面，继续游戏
		{
			if(wantSound)                                               //如果正确
				playSound(2,0);                                         //播放按键音
			father.myHandler.sendEmptyMessage(1);                       //跳到TankActivity中的handler中的case 1
		}
		else if(rectHero.contains(x, y)&&status==7)                     //点击了”英雄榜“区域，且当前的画面实在主功能界面，英雄榜界面
		{
			if(wantSound)                                               //如果正确
				playSound(2,0);                                         //播放按键音
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //振动
			father.myHandler.sendEmptyMessage(2);                       //跳到TankActivity中的handler中的case 2
						
		}
		else if(rectHelp.contains(x, y)&&status==7)                     //点击了”帮助“区域，且当前的画面实在主功能界面，帮助界面
		{
			HelpView3.pageNumber=1;                                     //页码初始为1
			if(wantSound)                                               //如果正确
				playSound(2,0);                                         //播放按键音
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //振动
			father.myHandler.sendEmptyMessage(3);                       //跳到TankActivity中的handler中的case 3
		}
		else if(rectExit.contains(x, y)&&status==7)                     //点击了“退出”区域，且当前的画面实在主功能界面，退出机制
		{	
			if(wantSound)                                               //如果正确
				playSound(2,0);                                         //播放按键音
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //振动
			AlertDialog.Builder builder = new AlertDialog.Builder(father);//建立一个对话框
			builder.setIcon(R.drawable.icon);                           //设置小图像
			builder.setTitle("    确定退出？");                             //标题
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() //按钮“是”
			{            
				public void onClick(DialogInterface dialog, int which)  //点击“是”
				{	      					
					father.myHandler.sendEmptyMessage(5);               //跳到TankActivity中的handler中的case 5
				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() //按钮“否”
			{             
				public void onClick(DialogInterface dialog, int which)  //点击“否"
				{            
					status=7;                                           //回到主功能界面，也就是WelcomeView3中的cas 7
				}
			});
			builder.create();                                           //创建并显示
			builder.show();
			ifsound=false;                                              //重置为false
			skip=false;
		}
		return true;
	}
	
	
	public void musicDialogOpen()                                       //点下声音选择对话框中的开启按钮时调用
	{
		playSound(2,0);                                                 //播放按键音
		if(!wantSound&&!play)                                           //如果声音开关现在是关闭的且play标志为false，点一次关闭按钮，play会变为false，
		{
			play=true;                                                  //在这里面置为true
			if(father.currentView==this)                                //如果在WelcomeView3
		    {
				stopSound(2);                                           //停止按键音
				stopSound(2);
				playSound(1,0);                                         //播放欢迎界面的
			}
			else if(father.currentView==father.gv)                      //转换成String类型后，截取其中过一段，如果再GameView3
				playSound(3, 0);                                        //播放游戏背景音
		}
		wantSound=true;
	}
	
	
    public void musicDialogClose()                                      //点下声音选择对话框中的关闭按钮时调用            
	{
    	if(wantSound)                                                   //如果现在声音是开启的
		{
    		playSound(2,0);                                             //播放两次按键音
			playSound(2,0);
			if(father.currentView==this)                                //当前View如果是WelcomeView3
				father.wv.stopSound(1);                                 //停止欢迎界面的背景音效
			else if(father.currentView==father.gv)                      //当前View如果是GameView3
				father.gv.stopSound(1);                                 //停止游戏背景音
		}
    	wantSound=false;                                                //游戏开关标志赋为False
		play=false;                                                     //play置为false
	}

}
