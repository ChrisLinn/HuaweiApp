 
package com.example.shenzhoushihao;                  //���������

//���������
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView3 extends SurfaceView implements SurfaceHolder.Callback//�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{      
	TankActivity father;					         //TankActivity������
	static final int FLING_MIN_DISTANCE = 5;         //��С�������볣�������ж��Ƿ�С�Ĵ�����Ļʱʹ�á�
	float startX=40,startY=17;                       //дintroduce����ʼ���      
    String introduce="HUAWEI APP CONTEST";//����Ϸ���涯̬��ʾ�Ĳ�����Ʒ����
	int taskArray[][]=new int [6][3];                //��������
	float x=95,y=270;                                //ը����ʼλ��
	float xx=0,yy=0;                                 //x��y������ٶ�
	int status=0;                                    //����ը���ɳ�ȥ��ը�����ڷɵ�����״̬
	Bitmap bmpBomb;                                  //ը��
	int bombNumber;                                  //ը��������      
	boolean isDrawCircle=true;                 
	public static int stage=0;                       //��ǰ�ؿ�Ĭ��Ϊ0
	boolean nextStage;                               //�ɹ����
	boolean failFlag;                                //����ʧ�ܣ�Ҳ�����þ�������ը����δ���
    BitmapData[] bitmapData=new BitmapData[6];       //BitmapData���͵����飬��Źؿ���ͼƬ��Դ��������
    
    String sBomb;                                    //ը���ĸ����String���͵ı���
	String s1;                                       //С̹��   ��ը�и���
	String s2;                                       //��̹��   ��ը�и���
	String s3;                                       //��̹��   ��ը�и���	
	
	//�߳�
    TouchThread3 tt;
    TouchDrawThread3 tdt;
    
    float sca=0;                                     //�����ǻ������ը�����Ǳ����˵�ը����Ϊ0ʱ�������ը�� 
    float w=1.0f,h=1.0f;                             //����ը�������õĲ���Ĭ����1�������ը����w��h����͸ߵķ�������
    
    int initBomb=50;                                 //��ը�����и���ʱ������
    SoundPool soundPool;		                     //SoundPool��������
 	HashMap<Integer,Integer> soundMap;	             //���������Դ��Map
 	
 	
 	public GameView3(TankActivity father)            //���췽��
 	{                
		super(father);                               //��ʼ������
		failFlag=false;                              //û����Ϸʧ�ܣ�ʧ�ܱ�־Ĭ��Ϊfalse
		nextStage=false;                             //�Ƿ�����һ�صı�־Ĭ��Ϊfalse
		this.father = father;                        //��ʼ����Ա����
		getHolder().addCallback(this);               //��ʼ��
		initSound(father);                           //����initSound��ʼ��������Դ
		tt = new TouchThread3(this);                 //�߳�
		tdt = new TouchDrawThread3(this,getHolder());//�߳�
		bmpBomb = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);//����ը����ͼƬ
		BitmapManager3.loadCurrentStageResources(getResources(),stage);              //����ͼƬ
		for(int i=0;i<6;i++)                         //���������ʼ������ʼ��Ϊ��
			for(int j=0;j<3;j++)
				taskArray[i][j]=0;
		initBitmap();	                             //��ʼ����������ͼƬ�������Լ��涨��ը��������
	}
 	
 	//��ʼ����������ͼƬ�������Լ��涨�Ļ��������ķ���
 	public void initBitmap()    
 	{
 		switch(stage)                                //�ؿ���
 		{
 		case 0:
 			bombNumber=50;                           //���ع涨��ը����
 			for(int i=0;i<7;i++)                                                  
 				BitmapManager3.flag0[i]=true;
 			for(int i=7;i<13;i++)
 				BitmapManager3.flag0[i]=false;
 			BitmapManager3.level0X[1]=240;           //����̹�˵ĳ�ʼx���                                           
 			BitmapManager3.level0X[2]=385; 
 			BitmapManager3.level0X[3]=110; 
 			BitmapManager3.level0X[4]=240; 
 			BitmapManager3.level0X[5]=40; 
 		    BitmapManager3.level0X[6]=210; 
 			bitmapData[4]=new BitmapData(BitmapManager3.currentStageResources,
 						        BitmapManager3.level0X, BitmapManager3.level0Y,BitmapManager3.flag0);
 			break; 					
 		default:
 			break;
 		}
 	}
 	
     //������������Ļ
 	@SuppressWarnings("static-access")
 	public void doDraw(Canvas canvas)                //touchDrawThread��������doDraw���������ػ�
 	{  
 		Paint paint = new Paint();	                 //��������
 		paint.setAlpha(255);                         //�������
 		if(bombNumber>0)
 		{
 			switch(stage)
 			{                                        //�ؿ���
 			case 0:                                                                  
 				canvas.drawBitmap(bitmapData[4].bit[0], bitmapData[4].X[0],bitmapData[4].Y[0],paint);            //�ȵ������Ʊ���ͼƬ
 				for(int i=1;i<bitmapData[4].X.length;i++)//��������ͼƬ
 				{
 					if(bitmapData[4].flag[i])        //��־Ϊtrue�Ķ�����
 						{
 							canvas.drawBitmap(bitmapData[4].bit[i], bitmapData[4].X[i],bitmapData[4].Y[i],paint);//����
 							if(!tt.ifKeep)           //���û��ը��̹��
 							moveTank(i);             //�����ǵ���moveTank����ʹ̹���˹�
 						 }
 				}
 				break;
 		   default:
 				break;
 				                                     //����switch	
 			}                                        //ը��������������
 					
 		}
 		drawAll(canvas);                             //����drawAll�������������ȣ�ʣ��ը����ؿ��������Ϸ��Ĵ���涨����
 					
 		if(failFlag||nextStage)                      //ʧ�ܻ��߳ɹ�
 		{                                   
 			father.currentScore[stage]=(bombNumber-1)*10;//���㵱ǰ�ؿ��ĵ÷�
 			if(WelcomeView3.wantSound)               //������־Ϊtrueʱ
 				stopSound(1);                        //ֹͣ��Ϸ������
 			father.myHandler.sendEmptyMessage(6);    //��ת��TankActivity�е�handler�е�case 6�����ػ�ʧ�ܽ���
 			soundPool.release();                     //�ͷ����ֳ�
 			soundPool=null;                  
 			tdt.isDrawOn = false;                    //ֹͣ�̻߳���ͼƬ
 			tt.flag=false;	                         //ֹͣ�̻߳���ͼƬ
 		}
 		isSuccess();                                 //�ж��Ƿ��������
 	}
 	
 	
 	public void drawAll(Canvas canvas)
 	{
 		Paint paint2 = new Paint();                  //��������pant2
 		paint2.setAlpha(255);                        //����
 		paint2.setTextSize(16);                      //�����С
 		paint2.setTypeface(Typeface.DEFAULT_BOLD);	 //���ô���
 		paint2.setColor(Color.BLUE);                 //������ɫ
 		paint2.setAntiAlias(true);					 //���ÿ����
 		if(bombNumber!=0&&!nextStage)                //���ը����Ϊ�����û�й��
 			canvas.drawText(introduce, startX, startY, paint2);//������ƴ����������
 		startX-=0.8;                                 //����ûִ��һ��doDraw����������0.8
 	    if(startX<-750)                              //���Գ�������ݣ�����ʾ�������ֺ���һ�δ�ͷ����ʾ
 			startX=240;
 		Paint paint = new Paint();	                 //��������paint
 		paint.setAlpha(255);	                     //����
 		if(sca==1&&!tt.isRedraw)                     //ը���ڷ������һ�û��ʧ֮ǰ
 		{               
 			Paint paint1=new Paint();                //��������
 			Matrix m=new Matrix();                   //��������
 			m.preTranslate(x,y);                     //������о���任
 			m.preScale(w,h);
 			canvas.drawBitmap(bmpBomb, m ,paint1);   //���Ʊ����˵�ը��
 			if(bombNumber>1)                         //ը�������1ʱ
 				canvas.drawBitmap(bmpBomb, 95,270, paint);//�����м����·���ը��
 		}
 		else if(sca==0&&tt.isRedraw)                 //ը��û��������ʧ��־��ʾ������ʧ��
 		{}       
 			                                         //�����κβ���
 		else if(sca==1&&tt.isRedraw)                 //ը���ڷ��У���ʱ����Ե���ը���Ѿ���ʧ�ˣ������̻߳�û��sca��ֵ�Ļ���
 		{
 			if(bombNumber>1)                         //ը�������1ʱ
 				canvas.drawBitmap(bmpBomb, 95,270, paint);//����
 		}
 		else                                         //����
 		{
 			if(bombNumber>0)                         //ը���������ʱ����Ҫ�����ը�������1ʱ
 				canvas.drawBitmap(bmpBomb, 95,270, paint);//����
 		}
 		//��ʾը��������
 		Paint paintCircle = new Paint();	         //�������ʣ�ʹ�õ���Ĭ������
 		paintCircle.setTypeface(Typeface.DEFAULT_BOLD);//���ô���
 		paintCircle.setColor(Color.RED);             //������ɫ
 		paintCircle.setAntiAlias(true);			     //���ÿ����
 		sBomb=Integer.toString(bombNumber);          //��ʾʣ��ը���ĸ���
 		canvas.drawText("Bombs:x"+sBomb, 30, 317, paintCircle);
 		if(stage!=5)
 		{                                            //����������
 			s1=Integer.toString(taskArray[stage][2]);       //ת�����ַ����ͺ󣬻������½ǵĽ��  ����ͬ
 		    s2=Integer.toString(taskArray[stage][1]);
 		    s3=Integer.toString(taskArray[stage][0]);
 		    //��ʾ
 		    canvas.drawText("x"+s1, 159, 317, paintCircle); //�ڲ�ͬ�������ƣ���ͬ
 		    canvas.drawText("x"+s2, 188, 317, paintCircle);
 		    canvas.drawText("x"+s3, 216, 317, paintCircle);
 	     }
 		//canvas.drawText("No.", 75, 285, paintCircle);         //�����ǵڼ��ص�����
 		//canvas.drawText("   "+Integer.toString(stage+3), 70, 300, paintCircle);//�����
 		//canvas.drawText("Misson", 75, 315, paintCircle);
 	}
 	
 	
 	public void isSuccess()                                 //���ص��������
 	{
 		switch(stage)
 		{
 		case 0:
 			if(taskArray[0][0]>=4&&taskArray[0][1]>=3&&taskArray[0][2]>=2)//���ص�����
 				nextStage=true;
 			break;
 		default:
 			break;
 		}
 	}
 	
 	//��������ʼ������
	public void initSound(TankActivity father)//initSound�ķ�����
	{
		soundPool = new SoundPool(1000,AudioManager.STREAM_MUSIC,100);          //��ʼ��soundPool,����ֱ�Ϊ����ͬʱ���ŵ��������Ϊ�ĸ����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();                              //��ʼ�����������Դ�Ĺ�ϣ��
		int id1 = soundPool.load(father, R.raw.game_background, 1);             //������������Ϸ������
		int id2 = soundPool.load(father, R.raw.bulletsound1, 2);                //����ը��������
		int id3 = soundPool.load(father, R.raw.explode, 3);                     //��ը������	
		int id4= soundPool.load(father, R.raw.game_landing, 4);                 //�������
		int id5= soundPool.load(father, R.raw.game_sink, 5);                    //��ˮ������
		int id6= soundPool.load(father, R.raw.game_hit_sixth, 6);               //��������е�����
		int id7= soundPool.load(father, R.raw.game_press, 7);                   //��������
		soundMap.put(1, id1);	                                                //����soundMap��ָ��id��
		soundMap.put(2, id2);	
		soundMap.put(3, id3);	
		soundMap.put(4, id4);
		soundMap.put(5, id5);	
		soundMap.put(6, id6);
		soundMap.put(7, id7);	
	}
	
	//��������������
	public void playSound(int sound,int loop)               //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id��
	                                                        //�ڶ����ǲ��Ŵ���-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
	{                                                                           
		if(WelcomeView3.wantSound)
		{
			AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
			float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
			float volume = streamVolumeCurrent / streamVolumeMax;   	    
			soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);//ϵͳ�Ĳ��������ķ���
		}
	}
	
	//������ֹͣ����
	public void stopSound(int sound)                       //����id��
	{                               
		soundPool.stop(soundMap.get(sound));               //��soundMap���ҵ�idΪsound������
	}	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//��д�ӿ�surfaceChanged����
	{}                                                   
	
	
	public void surfaceCreated(SurfaceHolder holder)
	{                                                      //��дsurfaceCreated����������GameView3ʱ����
		tdt.isDrawOn = true;                               //���Ʊ�־Ϊtrue
		if(!tdt.isAlive())                                 //���û����������
		{         
			tdt.start();
		}
		tt.flag = true;                                    //���Ʊ�־Ϊtrue
		if(!tt.isAlive())
		{                                                  //���û����������
			tt.start();
		}		
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) 
	{                                                      //��дsurfaceDestroyed�������뿪GameView3ʱ�Զ�����
		tdt.isDrawOn = false;                              //�ı��߳�tdt��־����TouchDrawThread���в����ظ�������Ļ
	}
	
	
	public void moveTank(int i)                            //̹���ƶ������ķ�����
	{
		if(i==1||i==2)                                     //����С̹��
		{
			bitmapData[4].X[i]-=0.3;                       //�ƶ�����һ��0.2
		}
		else if(i==3||i==4)
		{                                                  //��������̹��
			bitmapData[4].X[i]-=0.6;                       //�ƶ��ٶ�Ҳ���еȵ�
		}
		else if(i==5||i==6)
		{                                                  //��̹���ƶ����
			bitmapData[4].X[i]-=1;                         //һ�μ�һ
		}
		bitmapData[4].X[i+6]=bitmapData[4].X[i];           //��ը��̹��ͬʱ�ı�x��꣬��ը��ʱ�л�ͼƬʱ����
		if(bitmapData[4].X[i]<-44)                         //�ߵ��������̹��ȫ�?��ʧʱ
			bitmapData[4].X[i]=260;                        //�����Ҷ˳���
    }

}
	
