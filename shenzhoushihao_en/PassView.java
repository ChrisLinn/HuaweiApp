 
package com.example.shenzhoushihao;                                            //���������

//���������
import java.util.HashMap;                                                     
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;


public class PassView extends SurfaceView implements SurfaceHolder.Callback    //PassView��̳���SurfaceView�࣬ʵ����SurfaceHolder��Callback�ӿ�
{    
	ShenzhoushihaoActivity father;                                             //ShenzhoushihaoActivity������
	PassThread pt;                                                             //PassThread������                           
	PassDrawThread pdt;                                                        //PassDrawThread������
	int stage;                                                                 //��¼��ǰ״̬��0Ϊ�ؿ���6Ϊ��Ӧ�Ĺؿ���Ӧ��״̬
	int alpha=255;                                                             //�������
	int leftX=-130;                                                            //��¼���ؾ���ͼƬ���Ͻ�����
	int rightX=300;                                                            //������¼��ͼƬ���Ͻ�����
	Bitmap bmpCircle;                                                          //��Ż���ͼƬ
	Bitmap gamePass;                                                           //��Ź��ؽ����ͼƬ
	Bitmap gameOver;                                                           //�����Ϸʧ�ܵ�ͼƬ
	BitmapData[] bitmapData=new BitmapData[6];                                 //��Źؿ�����
	
	Rect save=new Rect(5,270,80,315);                                          //���½ǰ�ť��Ӧ��Χ
	Rect movetonextlevel=new Rect(180,280,235,315);                            //���½ǰ�ť��Ӧ��Χ
    Button myButton=null;
    
	SoundPool soundPool;		                                               //SoundPool��������
	HashMap<Integer,Integer> soundMap;                                         //���������Դ��Map
	public PassView(ShenzhoushihaoActivity father)                             //����������ʼ����Ҫ��Ա����
	{                                                                          //���췽��
		super(father);                                                         //��ʼ������
	    this.father = father;                                                  //��ʼ����Ա����
	    stage=GameView.stage;                                                  //�õ��ؿ���
	    getHolder().addCallback(this);
	    pt=new PassThread(this);                                               //�����߳�
		pdt=new PassDrawThread(this,getHolder());
		initSound(father);                                                     //��Ա��������ʼ������
		bmpCircle = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle);               //���ػ� ��ͼƬ
		gamePass = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_pass);           //���ع��� ��ͼƬ
		gameOver = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_over);           //���ػ�ʧ�ܵ�ͼƬ
		BitmapManager.loadCurrentStageResources(getResources(),stage);         //���ض�Ӧ�ؿ���ͼƬ��Դ
		initBitmap();                                                          //���ó�ʼ��ͼƬ�ĳ�Ա����
		}
	
	
	//������������Ļ
	@SuppressWarnings("static-access")
	public void doDraw(Canvas canvas)                                          //doDraw��������PassDrawThread�з�������
	{                      
		String currentScore=Integer.toString(father.currentScore[GameView.stage]);                      //��ʾ���ؿ��÷ֵ��ַ���
		Paint paint = new Paint();	                                           //��������paint
		paint.setAlpha(alpha);                                                 //��������
		paint.setTextSize(22);                                                 //���������С
	    paint.setTypeface(Typeface.DEFAULT_BOLD);	                           //���ô���
	    paint.setColor(Color.RED);                                             //������ɫ
		paint.setAntiAlias(true);					                           //���ÿ����
		Paint paint1 = new Paint();	                                           //��������paint
		paint1.setAlpha(alpha);                                                //��������
		paint1.setTextSize(15);                                                //���������С
	    paint1.setTypeface(Typeface.DEFAULT_BOLD);	                           //���ô���
	    paint1.setColor(Color.YELLOW);                                         //������ɫ
		paint1.setAntiAlias(true);					                           //���ÿ����
		if(father.gv.failFlag&&!father.gv.nextStage)                           //���ʣ�໷��Ϊ0��failFlag��־Ϊtrue�ҹ��ر�־Ϊfalse
			{
			playSound(3,0);                                                    //����ʧ������
			canvas.drawBitmap(gameOver,0,0,null);                              //����ʧ��ͼƬ
			}                            
		else                                                                   //��֮�ɹ���
		{   
			playSound(2,0);                                                    //���Ź�������                              
		    canvas.drawBitmap(gamePass, 0,0,null);                             //���ƹ��ر���
			switch(stage)                                                      //switch�ؿ���
			{                      
			case 0:                                                            //��һ��Ϊ0�� 6
				canvas.drawBitmap(bitmapData[0].bit[1], leftX,155,paint);      //�����Ѽ���ͼƬ�����ƶ����γɶ�����leftX��PassThread�иı�
				canvas.drawBitmap(bitmapData[0].bit[7], leftX,207,paint);
				canvas.drawBitmap(bitmapData[0].bit[4], leftX,248,paint);
				canvas.drawBitmap(bmpCircle, rightX,150,paint);                //����������Ʈ����rightX��PassThread�иı�
				canvas.drawBitmap(bmpCircle, rightX,200,paint);
				canvas.drawBitmap(bmpCircle, rightX,248,paint);
				break;
		   case 6:                                                             //������������ʾ������Ʒ��ͼƬ
			    canvas.drawBitmap(bitmapData[0].bit[10], 81,155,paint);        //ը��
				canvas.drawBitmap(bitmapData[0].bit[16], 83,207,paint);        //ȼ��
				canvas.drawBitmap(bitmapData[0].bit[13], 75,248,paint);        //��ҩ
				leftX=-130;                                                    //�ָ���ֵ
				rightX=300;                        
				break;
				default : 
					break;
						}
			if(stage==6)//���غ���ʾ�÷�
			{  				
				canvas.drawText("X"+father.gv.s3, 118, 183, paint);            //д�����������
				canvas.drawText("X"+father.gv.s2, 118, 234, paint);
				canvas.drawText("X"+father.gv.s1, 117, 293, paint);
				canvas.drawText("Score in this misson:", 40, 140, paint);                  //��������λ��д�Ϲ涨����
				canvas.drawText(currentScore, 145, 140, paint);                //���Ƶ�ǰ�÷�				
				canvas.drawText("Press to start next misson", 27, 310, paint1);         //��ʾ��ʾ��Ϣ
				}
			}
		}
	public void initBitmap()       //�����ĳ�ʼ��
	{
		switch(stage)              //���ر��ؿ�ͼƬ
		{            
		case 0:
			bitmapData[0]=new BitmapData(BitmapManager.currentStageResources, BitmapManager.level0X, BitmapManager.level0Y,BitmapManager.flag0);    //�������BitmapData�Ĺ��캯��
			break;
			default:
				break;
				}
		}
			
			
	public void initSound(ShenzhoushihaoActivity father)                       //��������ʼ������
	{                                                                          //initSound�ķ�����
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);            //��ʼ��soundPool,�����ֱ�Ϊ����ͬʱ���ŵ��������Ϊ1�����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();                             //��ʼ�����������Դ�Ĺ�ϣ��
		int id1 = soundPool.load(father, R.raw.game_press, 1);                 //������������id��
		int id2 = soundPool.load(father, R.raw.game_pass, 2);                  //��������
		int id3 = soundPool.load(father, R.raw.game_fail, 3);                  //����ʧ������
		int id4 = soundPool.load(father, R.raw.game_success, 4);               //ͨ������
		soundMap.put(1, id1);	                                               //����������������أ���id�Ŷ�Ӧ  
		soundMap.put(2, id2);	
		soundMap.put(3, id3);
		soundMap.put(4, id4);
		}
	
	
	public void playSound(int sound,int loop)                                  //��������������
	{                                                                          //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id���ڶ����ǲ��Ŵ�����-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);       //ϵͳ�Ĳ��������ķ���
	    }
	
	
	public void stopSound(int sound)                                           //������ֹͣ����
	{                                                                          //����id�� 
		soundPool.stop(soundMap.get(sound));                                   //��soundMap���ҵ�idΪsound������
		}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
	{}                                                                         //��д�ӿ�surfaceChanged����
	
	
	public void surfaceCreated(SurfaceHolder holder)                           //��дsurfaceCreated����������PassViewʱ����
	{
		pdt.isDrawOn = true;                                                   //���Ʊ�־Ϊtrue
		if(!pdt.isAlive())                                                     //���û����������
		{         
			pdt.start();
			}
		pt.flag = true;                                                        //���Ʊ�־Ϊtrue
		if(!pt.isAlive())                                                      //���û����������
		{           
			pt.start();
			}
		}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                         //��дsurfaceDestroyed�������뿪PassViewʱ�Զ�����
	{                          
		pdt.isDrawOn = false;                                                  //�ı��߳�pdt��־����PassDrawThread���в����ظ�������Ļ
		}
	
	
	//PassView�и�������Ӧ
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)                                   //������Ӧ��������ShenzhoushihaoActivity�е�OnTouchEvent���е���
	{
		if(save.contains(x,y)&&(!father.gv.nextStage))                         //�����ʧ����֮�������½ǵ�λ��
			{
			if(GameView.stage==0)
			{
				if(WelcomeView.wantSound)                                      //��������Ϊ��ʱ���Ű�����
					playSound(1,0);
				father.vib.vibrate(new long[]{100,10,100,70},-1);              //��
				father.saveRecordDialog();                                     //��������÷ּ�¼�ĶԻ���
				pdt.isDrawOn=false;                                            //ֹͣ�̻߳��ƺ�ֹͣ���߳���ı������ֵ
			    pt.flag=false;                                                 //ͬ��
			    }
			}
		else if(movetonextlevel.contains(x,y)&&(!father.gv.nextStage))         //������Ϸʧ�ܺ������½ǵ�����
		{ 
			if(WelcomeView.wantSound)                                          //����Ϊ��ʱ
				playSound(1,0);                                                //���Ű�������
			father.vib.vibrate(new long[]{100,10,100,70},-1);                  //��
			ShenzhoushihaoActivity.currentScore[0]=0;                          //������������ص÷�����	
			father.myHandler.sendEmptyMessage(7);                              //����ShenzhoushihaoActivity�е�handler�е�case 7��������������
			pdt.isDrawOn=false;                                                //ֹͣ�̻߳��ƺ�ֹͣ���߳���ı������ֵ
			pt.flag=false;                                                     //ͬ��
			}
		return true;
		}                                                                      //��������
	}

