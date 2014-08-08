 
package com.example.shenzhoushihao;                                          //���������

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

public class PassView3 extends SurfaceView implements SurfaceHolder.Callback //PassView3��̳���SurfaceView�࣬
                                                                             //ʵ����SurfaceHolder��Callback�ӿ�
{    
	TankActivity father;                                     //TankActivity������
	PassThread3 pt;                                          //PassThread3������                           
	PassDrawThread3 pdt;                                     //PassDrawThread3������
	int stage;                                               //��¼��ǰ״̬��0�ؿ���10Ϊ��Ӧ�Ĺؿ���Ӧ��״̬
	int alpha=255;                                           //�������
	int leftX=-130;                                          //��¼���ؾ���ͼƬ���Ͻ�����
	int rightX=300;                                          //������¼ը��ͼƬ���Ͻ�����
	Bitmap bmpBomb;                                          //���ը����ͼƬ
	Bitmap gamePass;                                         //��Ź��ؽ����ͼƬ 
	Bitmap gameOver;                                         //�����Ϸʧ�ܵ�ͼƬ
	Bitmap gameSuccess1;                                     //ͨ�ؽ���ļ���ͼƬ
	BitmapData[] bitmapData=new BitmapData[6];               //��Źؿ�
	Rect save        =new Rect(5,270,80,315);                //���½ǰ�ť��Ӧ��Χ
	Rect movetonextlevel   =new Rect(180,280,235,315);       //���½ǰ�ť��Ӧ��Χ
    SoundPool soundPool;		                             //SoundPool��������
	HashMap<Integer,Integer> soundMap;                       //���������Դ��Map
	
	
	//����������ʼ����Ҫ��Ա����
	public PassView3(TankActivity father)                    //���췽��
	{                    
		super(father);                                       //��ʼ������
		this.father = father;                                //��ʼ����Ա����
		stage=GameView3.stage;                               //�õ��ؿ���
		getHolder().addCallback(this);
		pt=new PassThread3(this);                            //�����߳�
		pdt=new PassDrawThread3(this,getHolder());
		initSound(father);                                   //��Ա��������ʼ������
		bmpBomb = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);                //����ը�� ��ͼƬ
		gameSuccess1= BitmapFactory.decodeResource(this.getResources(), R.drawable.game_success_1);  //����ͨ�ص�ͼƬ
		gamePass = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_pass);        //���ع��� ��ͼƬ
		gameOver = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_over);        //����ը��ʧ�ܵ�ͼƬ
		BitmapManager3.loadCurrentStageResources(getResources(),stage);                              //���ض�Ӧ�ؿ���ͼƬ��Դ
		initBitmap();                                                                                //���ó�ʼ��ͼƬ�ĳ�Ա����
	}
	
	//������������Ļ
	@SuppressWarnings("static-access")
	public void doDraw(Canvas canvas)                        //doDraw��������PassDrawThread3�з�������
	{                     
		String currentScore=Integer.toString(father.currentScore[GameView3.stage]);    //��ʾ���ؿ��÷ֵ��ַ���
		Paint paint = new Paint();	                         //��������paint
		paint.setAlpha(alpha);                               //��������
		paint.setTextSize(22);                               //���������С
	    paint.setTypeface(Typeface.DEFAULT_BOLD);	         //���ô���
	    paint.setColor(Color.RED);                           //������ɫ
		paint.setAntiAlias(true);					         //���ÿ����
		
		if(father.gv.failFlag&&!father.gv.nextStage)         //���ʣ��ը����Ϊ0��failFlag��־Ϊtrue�ҹ��ر�־Ϊfalse
		{
		playSound(3,0);                                      //����ʧ������
		canvas.drawBitmap(gameOver,0,0,null);                //����ʧ��ͼƬ
		}                            
	else                                                     //��֮�ɹ�
	{   
		playSound(2,0);                                      //���Ź�������                              
        switch(stage)                        
        {                      
        case 0:                                              //������Ϊ0��10
        	canvas.drawBitmap(gameSuccess1, 0,0,null);       //���ƹ��ر���
			canvas.drawBitmap(bitmapData[4].bit[7], rightX+10,165,paint);//��ը���С̹��
			canvas.drawBitmap(bitmapData[4].bit[9], rightX+5,204,paint); //��ը�����̹��
			canvas.drawBitmap(bitmapData[4].bit[11], rightX,240,paint);  //��ը��Ĵ�̹��
			break;			
		case 10:                                            //��ʾ��ը���ͼƬλ��
			leftX=-130;                         
			rightX=300; 
            break;			
        default :
			break;
		}
		if(stage>3&&GameView3.stage!=3)
		{                                                        //�����ص÷����
			canvas.drawText("X"+father.gv.s3, 150, 183, paint);  //д�����������
		    canvas.drawText("X"+father.gv.s2, 150, 234, paint);
		    canvas.drawText("X"+father.gv.s1, 150, 275, paint);
		    canvas.drawText("Score in this misson:", 60, 140, paint);        //��������λ��д�Ϲ涨����
		    canvas.drawText(currentScore, 165, 140, paint);      //���Ƶ�ǰ�÷�      
		    Paint paint3 = new Paint();	                         //��������
		    paint3.setAlpha(alpha);                              //�������
		    paint3.setTextSize(28);                              //�����С
	        paint3.setTypeface(Typeface.DEFAULT_BOLD);	         //���ô���
	        paint3.setColor(Color.RED);                          //������ɫ
		    paint3.setAntiAlias(true);					         //���ÿ����
		    paint3.setTextSize(20);                              //�������������С
		    canvas.drawText("Complete", 195, 315, paint3);            //��������λ��д�Ϲ涨����
		    }
	}
	}
		

	public void initBitmap()                                     //�����ĳ�ʼ��
	{
		switch(stage)                                            //�����عؿ�����ͼƬ
		{             
		    case 0:
			    bitmapData[4]=new BitmapData(BitmapManager3.currentStageResources, BitmapManager3.level0X, BitmapManager3.level0Y,BitmapManager3.flag0);
		        break;		   
		    default:
			    break;
		}
	}
	
	//��������ʼ������
	public void initSound(TankActivity father)                     //initSound�ķ�����
	{     
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);//��ʼ��soundPool,�����ֱ�Ϊ����ͬʱ���ŵ��������Ϊ1�����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();                 //��ʼ�����������Դ�Ĺ�ϣ��
		int id1 = soundPool.load(father, R.raw.game_press, 1);     //������������id��
		int id2 = soundPool.load(father, R.raw.game_pass, 2);      //��������
		int id3 = soundPool.load(father, R.raw.game_fail, 3);      //����ʧ������
		int id4 = soundPool.load(father, R.raw.game_success, 4);   //ͨ������
		soundMap.put(1, id1);	                                   //�����������������,��id�Ŷ�Ӧ  
		soundMap.put(2, id2);	
		soundMap.put(3, id3);	
		soundMap.put(4, id4);			
		
	}
	
	//��������������
	public void playSound(int sound,int loop)                      //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id��
	                                                               //�ڶ����ǲ��Ŵ�����-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
	{                    
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);   //ϵͳ�Ĳ��������ķ���
	}
	
	//������ֹͣ����
	public void stopSound(int sound)                                       //����id��
	{                                
		soundPool.stop(soundMap.get(sound));                               //��soundMap���ҵ�idΪsound������
	}	

	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//��д�ӿ�surfaceChanged����
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                       //��дsurfaceCreated����������PassView3ʱ����
	{
		pdt.isDrawOn = true;                                               //���Ʊ�־Ϊtrue
		if(!pdt.isAlive())                                                 //���û����������
		{         
			pdt.start();
		}
		pt.flag = true;                                                    //���Ʊ�־Ϊtrue
		if(!pt.isAlive())                                                  //���û����������
		{            
			pt.start();
	     }		
	}
	

	public void surfaceDestroyed(SurfaceHolder holder)                     //��дsurfaceDestroyed�������뿪PassView3ʱ�Զ�����
	{                          
		pdt.isDrawOn = false;                                              //�ı��߳�pdt��־����PassDrawThread3���в����ظ�������Ļ
	}
	
	//************PassView3�и�������Ӧ******************************************************
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)                               //������Ӧ��������TankActivity�е�OnTouchEvent���е���
	{
			if(save.contains(x,y)&&(!father.gv.nextStage))                 //������½����������ʧ����֮�������½ǵ�λ��
			{
				
				if(GameView3.stage==0)                                     //����
				{
                  if(WelcomeView3.wantSound)                               //��������Ϊ��ʱ���Ű�����
				     playSound(1,0);
                  father.vib.vibrate(new long[]{100,10,100,70},-1);        //��
				   father.saveRecordDialog();                              //��������÷ּ�¼�ĶԻ���
				   pdt.isDrawOn=false;                                     //ֹͣ�̻߳��ƺ�ֹͣ���߳���ı������ֵ
			       pt.flag=false;                                          //ͬ��
				}
			}
			
		else if(movetonextlevel.contains(x,y)&&(GameView3.stage!=5&&GameView3.stage!=0||(!father.gv.nextStage)))
			{                                                              //��Ϸͨ�ػص������صĿ�ʼ��Ӣ�۰�...����
				
				if(WelcomeView3.wantSound)                                 //����Ϊ��ʱ
					playSound(1,0);                                        //���Ű�������
				father.vib.vibrate(new long[]{100,10,100,70},-1);          //��
				if(father.gv.nextStage)                                    //������ر�־Ϊtrue
				{  
					GameView3.stage++;                                     //�ؿ��ż�һ
				}
				
				else                                                       //��֮������Ϸʧ�ܣ����ǽ��е������������֮ǰ�Ļ�������
				{                            
				for(int i=0;i<6;i++)                                       //��������
				     TankActivity.currentScore[i]=0;                       //���صĵ÷�����
			        }
				father.myHandler.sendEmptyMessage(7);                      //����TankActivity�е�handler�е�case 7��������������
				pdt.isDrawOn=false;                                        //ֹͣ�̻߳��ƺ�ֹͣ���߳���ı������ֵ
			     pt.flag=false;                                            //ͬ��
	        } 
		else if(movetonextlevel.contains(x,y)&&GameView3.stage==0)         //������½ǵ�����ͨ�غ󣬼������ﵽ���ޣ�Ҳ���Ƕ����Բ������
			{                          
			       if(WelcomeView3.wantSound)                              //�����������Ϊ��
					   playSound(1,0);                                     //���Ű�����
					father.setContentView(father.wv);			           //���õ�ǰ��ĻΪWelcomeView3����
					father.currentView = father.wv;	                       //��¼��ǰView
					father.wv.status =1;                                   //��ʾ��Ϸ��ͨ�ؽ���
				}	
			return true;
    }                                                                      //��������
}


