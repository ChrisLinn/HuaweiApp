 
package com.example.shenzhoushihao;                                            //���������

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


public class GameView extends SurfaceView implements SurfaceHolder.Callback    //�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{      
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity������
	static final int FLING_MIN_DISTANCE = 5;                                   //��С�������볣�������ж��Ƿ�С�Ĵ�����Ļʱʹ�á�
	float startX=40,startY=17;                                                 //дintroduce����ʼ���      
    String introduce="HUAWEI APP CONTEST";//����Ϸ���涯̬��ʾ�Ĳ�����Ʒ����
	int taskArray[][]=new int [6][3];                                          //��������
	float x=95,y=270;                                                          //����ʼλ��
	float xx=0,yy=0;                                                           //x��y������ٶ�
	int status=0;                                                              //���ƻ��ɳ�ȥ�ͻ����ڷɵ�����״̬
	Bitmap bmpCircle;                                                          //��
	int circleNumber;                                                          //��������      
	boolean isDrawCircle=true;                 
	public static int stage=0;                                                 //��ǰ�ؿ�Ĭ��Ϊ0
	boolean nextStage;                                                         //�ɹ����
	boolean failFlag;                                                          //����ʧ�ܣ�Ҳ�����þ������л���δ���
    BitmapData[] bitmapData=new BitmapData[6];                                 //BitmapData���͵����飬��Źؿ���ͼƬ��Դ��������
    
    
	String sCircle;                                                            //���ĸ����String���͵ı���
	String s1;                                                                 //��ҩ   ���Ѽ�����
	String s2;                                                                 //ȼ��   ���Ѽ�����
	String s3;                                                                 //ը��   ���Ѽ�����	
	
	//�߳�
    TouchThread tt;
    TouchDrawThread tdt;
    
    float sca=0;                                                               //�����ǻ�����Ļ����Ǳ����˵Ļ���Ϊ0ʱ������Ļ� 
    float w=1.0f,h=1.0f;                                                       //���ƻ������õĲ���Ĭ����1������»���w��h����͸ߵķ�������
    
    int initCircle=13;                                                         //�������и���ʱ������
    SoundPool soundPool;		                                               //SoundPool��������
 	HashMap<Integer,Integer> soundMap;	                                       //���������Դ��Map
 	
 	
	public GameView(ShenzhoushihaoActivity father)                             //���췽��
	{                 
		super(father);                                                         //��ʼ������
		failFlag=false;                                                        //û����Ϸʧ�ܣ�ʧ�ܱ�־Ĭ��Ϊfalse
		nextStage=false;                                                       //�Ƿ�����һ�صı�־Ĭ��Ϊfalse
		this.father = father;                                                  //��ʼ����Ա����
		getHolder().addCallback(this);                                         //��ʼ��
		initSound(father);                                                     //����initSound��ʼ��������Դ
		tt = new TouchThread(this);                                            //�߳�
		tdt = new TouchDrawThread(this,getHolder());                           //�߳�
		bmpCircle = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle);//���ػ���ͼƬ
		BitmapManager.loadCurrentStageResources(getResources(),stage);         //��ݹؿ��ż���ͼƬ
		for(int i=0;i<6;i++)                                                   //���������ʼ������ʼ��Ϊ��
			for(int j=0;j<3;j++)
				taskArray[i][j]=0;
		initBitmap();	                                                       //��ʼ����������ͼƬ�������Լ��涨�Ļ�������
		}
	public void initBitmap()                                                   //��ʼ����������ͼƬ�������Լ��涨�Ļ��������ķ���   
	{
		switch(stage)
		{                                                                      //�ؿ���
		case 0:
			circleNumber=13;                                                   //��һ�ع涨�Ļ���,��ʼ����Ϊ13��ԭ�򣺱��ؼƷֻ���Ϊ����ɱ���������ȡ̹�ˣ���ҩ��ȼ�ϸ�һ������ǰ���£��ԣ�ʣ�໷��*10��Ϊ�������ʼ��Ϊ13��ʹ��óɼ�Ϊ100�֣���ϳ��� 
			for(int i=0;i<10;i++)                                              //���Ʊ���ͼƬ��û�����е�ͼƬ
				BitmapManager.flag0[i]=true;                                   //��Ϊtrue�����Ի���
			for(int i=10;i<19;i++)                                             //��Ҫ��ʱ�л���ͼƬ
				BitmapManager.flag0[i]=false;                                  //һ��ʼ�����ƣ���Ϊfalse
			bitmapData[0]=new BitmapData(BitmapManager.currentStageResources,                    
					                           BitmapManager.level0X, BitmapManager.level0Y,BitmapManager.flag0);      //��ʼ��ͼƬ
			break;
			default:
			break;
			}
		}
	
	
		//������������Ļ
		@SuppressWarnings("static-access")
		public void doDraw(Canvas canvas)
		{                                                                      //touchDrawThread��������doDraw���������ػ�
			Paint paint = new Paint();	                                       //��������
			paint.setAlpha(255);                                               //�������
			if(circleNumber>0)
			{
				switch(stage)
				{                                                              //�ؿ���
				case 0:                                                        //��һ��
					for(int i=0;i<bitmapData[0].X.length;i++)                  //�������飬����ÿһ��Ӧ�û��Ƶ�ͼƬ
						{
						if(bitmapData[0].flag[i])                              //���Ʊ�־Ϊtrueʱ�����
							{
							canvas.drawBitmap(bitmapData[0].bit[i], bitmapData[0].X[i],bitmapData[0].Y[i],paint);   //����ΪBitmapͼƬ��x��꣬y��꣬����
							}
						}
			     break;
			     default:
			    	 break;
			    	 }                                                         //����switch	
				}                                                              //��������������

			drawAll(canvas);                                                   //����drawAll�������������ȣ�ʣ�໷������һ���ؿ��������Ϸ��Ĵ���涨����
		
			if(failFlag||nextStage)
			{                                                                  //ʧ���˻��߳ɹ���
				father.currentScore[stage]=(circleNumber-1)*10;                //���㵱ǰ�ؿ��ĵ÷�
				if(WelcomeView.wantSound)                                      //������־Ϊtrueʱ
				    stopSound(1);                                              //ֹͣ��Ϸ������
				father.myHandler.sendEmptyMessage(6);                          //��ת��ShenzhoushihaoActivity�е�handler�е�case 6�����ػ�ʧ�ܽ���
				soundPool.release();                                           //�ͷ����ֳ�
				soundPool=null;                  
				tdt.isDrawOn = false;                                          //ֹͣ�̻߳���ͼƬ
			    tt.flag=false;	                                               //ֹͣ�̻߳���ͼƬ
			}
			isSuccess();                                                       //�ж��Ƿ��������
		}
		
		
		public void drawAll(Canvas canvas)
		{
			Paint paint2 = new Paint();                                        //��������pant2
			paint2.setAlpha(255);                                              //����
			paint2.setTextSize(16);                                            //�����С
		    paint2.setTypeface(Typeface.DEFAULT_BOLD);	                       //���ô���
		    paint2.setColor(Color.RED);                                        //������ɫ
			paint2.setAntiAlias(true);					                       //���ÿ����
			if(circleNumber!=0&&!nextStage)                                    //�����Ϊ�����û�й��
		        canvas.drawText(introduce, startX, startY, paint2);            //������ƴ����������
		    startX-=0.8;                                                       //����ûִ��һ��doDraw����������0.8
		    if(startX<-750)                                                    //���Գ�������ݣ�����ʾ�������ֺ���һ�δ�ͷ����ʾ
			    startX=240;
			Paint paint = new Paint();	                                       //��������paint
			paint.setAlpha(255);	                                           //����
			if(sca==1&&!tt.isRedraw)
			{                                                                  //���ڷ������һ�û��ʧ֮ǰ
				Paint paint1=new Paint();                                      //��������
				Matrix m=new Matrix();                                         //��������
				m.preTranslate(x,y);                                           //������о���任
				m.preScale(w,h);
			    canvas.drawBitmap(bmpCircle, m ,paint1);                       //���Ʊ����˵Ļ�
			    if(circleNumber>1)                                             //�������1ʱ
			    canvas.drawBitmap(bmpCircle, 95,270, paint);                   //�����м����·��Ļ�
			}
			else if(sca==0&&tt.isRedraw)                                       //��û��������ʧ��־��ʾ������ʧ��
			{                                                                  //�����κβ���
			}
			else if(sca==1&&tt.isRedraw)                                       //���ڷ��У���ʱ����Ե��ǻ��Ѿ���ʧ�ˣ������̻߳�û��sca��ֵ�Ļ���
			{
				if(circleNumber>1)                                             //�������1ʱ
			         canvas.drawBitmap(bmpCircle, 95,270, paint);              //����
			}
			else                                                               //����
			{
				if(circleNumber>0)                                             //���������ʱ����Ҫ����Ի������1ʱ
			         canvas.drawBitmap(bmpCircle, 95,270, paint);              //����
			}
			
			//��ʾ��������
			Paint paintCircle = new Paint();	                               //�������ʣ�ʹ�õ���Ĭ������
			paintCircle.setTypeface(Typeface.DEFAULT_BOLD);	                   //���ô���
			paintCircle.setColor(Color.RED);                                   //������ɫ
			paintCircle.setAntiAlias(true);					                   //���ÿ����
			sCircle=Integer.toString(circleNumber);                            //��ʾʣ�໷�ĸ���
			canvas.drawText("Circles remianed :x"+sCircle, 30, 317, paintCircle);
			if(stage==0)
			{                                                                  //����������
			s1=Integer.toString(taskArray[stage][2]);                          //ת�����ַ����ͺ󣬻������½ǵ���Ʒ�Ѽ���  ����ͬ
			s2=Integer.toString(taskArray[stage][1]);
			s3=Integer.toString(taskArray[stage][0]);
			
			//��ʾ
			canvas.drawText("x"+s1, 159, 317, paintCircle);                    //�ڲ�ͬ�������ƣ���ͬ
			canvas.drawText("x"+s2, 188, 317, paintCircle);
			canvas.drawText("x"+s3, 216, 317, paintCircle);
			}
			//canvas.drawText("No.", 75, 285, paintCircle);                        //�����ǵڼ��ص�����
			//canvas.drawText("   "+Integer.toString(stage+1), 70, 300, paintCircle);//stage+1Ϊ��ǰ�ؿ���1
			//canvas.drawText("Misson", 75, 315, paintCircle);
		}
		
		
		public void isSuccess()                                                //�˹ص��������
		{
			switch(stage)
			{
			case 0:
				if(taskArray[0][0]>=1&&taskArray[0][1]>=1&&taskArray[0][2]>=1) //��һ�ص���������������ﵽ��ʱ
					{
					nextStage=true;//��ر�־Ϊtrue
					//failFlag=true;
					}
				break;
				default:
					break;
			}
		}
		
		
		public void initSound(ShenzhoushihaoActivity father)                   //��������ʼ������
		{                                                                      //initSound�ķ�����
			soundPool = new SoundPool(1000,AudioManager.STREAM_MUSIC,100);     //��ʼ��soundPool,����ֱ�Ϊ����ͬʱ���ŵ��������Ϊ�ĸ����ڶ��������ͣ����������
		    soundMap = new HashMap<Integer,Integer>();                         //��ʼ�����������Դ�Ĺ�ϣ��
			int id1 = soundPool.load(father, R.raw.game_background, 1);        //������������Ϸ������
			int id2 = soundPool.load(father, R.raw.game_cast, 2);              //����������
			int id3 = soundPool.load(father, R.raw.game_hit, 3);               //���е�����	
			int id4 = soundPool.load(father, R.raw.game_landing, 4);           //�������
			int id5= soundPool.load(father, R.raw.game_sink, 5);                 
			int id6= soundPool.load(father, R.raw.game_hit_sixth,6);           
			int id7= soundPool.load(father, R.raw.game_press, 7);              //��������
			soundMap.put(1, id1);	                                           //����soundMap��ָ��id��
			soundMap.put(2, id2);	
			soundMap.put(3, id3);	
			soundMap.put(4, id4);
			soundMap.put(5, id5);
			soundMap.put(6, id6);
			soundMap.put(7, id7);
			}
		
		
		public void playSound(int sound,int loop)                              //��������������
		{                                                                      //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id���ڶ����ǲ��Ŵ���-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
			if(WelcomeView.wantSound)
			{
				AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
				float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
				float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
				float volume = streamVolumeCurrent / streamVolumeMax;   	    
				soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);//ϵͳ�Ĳ��������ķ���
				}
			}
		
		
		public void stopSound(int sound)                                       //������ֹͣ����
		{                                                                      //����id��           
			soundPool.stop(soundMap.get(sound));                               //��soundMap���ҵ�idΪsound������
			}
		
		
		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
		{                                                                      //��д�ӿ�surfaceChanged����
		    }
		
		
		public void surfaceCreated(SurfaceHolder holder)
		{                                                                      //��дsurfaceCreated����������GameViewʱ����
			tdt.isDrawOn = true;                                               //���Ʊ�־Ϊtrue
			if(!tdt.isAlive())
			{                                                                  //���û����������
				tdt.start();
				}
			tt.flag = true;                                                    //���Ʊ�־Ϊtrue
			if(!tt.isAlive())
			{                                                                  //���û����������
				tt.start();
				}
			}
		
		
		public void surfaceDestroyed(SurfaceHolder holder)
		{                                                                      //��дsurfaceDestroyed�������뿪GameViewʱ�Զ�����
			tdt.isDrawOn = false;                                              //�ı��߳�tdt��־����TouchDrawThread���в����ظ�������Ļ
			}
		}
	
