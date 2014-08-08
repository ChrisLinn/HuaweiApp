 
package com.example.shenzhoushihao;                        //���������

//���������
import java.util.HashMap;   
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView3 extends SurfaceView implements SurfaceHolder.Callback//�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{    
	TankActivity father;					               //TankActivity������
	Bitmap menu_help1;                                     //������һҳ��ͼƬ
	Bitmap menu_help2;                                     //�����ڶ�ҳ��ͼƬ
	Bitmap menu_help3;                                     //��������ҳ��ͼƬ
	public static int pageNumber=1;                        //Ĭ��HelpView��ҳ��Ϊ1
	
	Rect rectLeft    =new Rect(5,270,80,315);              //���ͷ��Ӧ��Χ��Ϊ���Σ�����Ϊ���ϽǺ����½ǵ����ꡣ��Ļ���Ͻǵ�����Ϊ��0,0��
	Rect rectRight   =new Rect(180,280,235,315);           //�Ҽ�ͷ��Ӧ��Χ��ͬ��
	
	SoundPool soundPool;		                           //SoundPool��������
	HashMap<Integer,Integer> soundMap;	                   //���������Դ��Map
	
	
	public HelpView3(TankActivity father)                  //���췽��
	{   
		super(father);                                     //��ʼ������
		this.father = father;                              //��ʼ����Ա����
		getHolder().addCallback(this);
		initSound(father);                                 //��ʼ��������Դ�ķ���
		menu_help1 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_help13);//���ص�һҳ��ͼƬ��Դ
		menu_help2 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_help23);//���صڶ�ҳ��ͼƬ��Դ
		menu_help3 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_help33);//���ص���ҳ��ͼƬ��Դ
	}
	
	
	public void doDraw(Canvas canvas)                      //doDraw����������������Ļ
	{                  
		Paint paint = new Paint();	                       //��������
		paint.setAlpha(255);                               //����͸����
		if(pageNumber==1)                                  //��Ϊ��һҳ
		    canvas.drawBitmap(menu_help1, 0, 0,paint);     //���Ƶ�һ��ͼƬ
		else if(pageNumber==2)                             //��Ϊ�ڶ�ҳ
			canvas.drawBitmap(menu_help2, 0, 0,paint);     //���Ƶڶ���ͼƬ
		else if(pageNumber==3)                             //��Ϊ����ҳ
			canvas.drawBitmap(menu_help3, 0, 0,paint);     //���Ƶ�����ͼƬ
	}
	
	//��������ʼ������
	public void initSound(TankActivity father)             //initSound�ķ�����
	{         
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);//��ʼ��soundPool,�����ֱ�Ϊ����ͬʱ���ŵ��������Ϊһ�����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();         //��ʼ�����������Դ�Ĺ�ϣ��
		int id = soundPool.load(father, R.raw.game_press, 2);//���ذ���������Դ��idΪ2
		soundMap.put(2, id);	                           //����soundMap��ָ��id��
	}
		
	//��������������
	public void playSound(int sound,int loop)              //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id��
	                                                       //�ڶ����ǲ��Ŵ�����-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
	{                    
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		float volume = streamVolumeCurrent / streamVolumeMax;   	    
		soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 2);//ϵͳ�Ĳ��������ķ���
	}
	
    //������ֹͣ����
	public void stopSound(int sound)                       //��дֹͣ�����ķ�������Ҫĸ���ǿ���ͨ������������Դ��id
	{                  
		soundPool.stop(soundMap.get(sound));               //ϵͳֹͣ��������
	}	
		
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//��д�ӿڷ���
	{}

	
	public void surfaceCreated(SurfaceHolder holder)       //����Viewʱ����ִ��
	{             
		Canvas canvas=null;
		try 
		{                            
			canvas = holder.lockCanvas(null);              //��������
			doDraw(canvas);                                //����doDraw����������������Ļ
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas != null)
			{
				holder.unlockCanvasAndPost(canvas);        //����
			}
		}		
	}
	

	public void surfaceDestroyed(SurfaceHolder holder)     //View������ʱ���õķ���
	{}
		
	//************HelpView3�и�������Ӧ***********************************************
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)               //�˷�����ShenzhoushihaoActivity���ã���������Ļʱϵͳ����TheKingOfMedicineActivity
	{	                                                   //�е�onTouchEvent,��ȡ���������Ȼ�󴫵ݲ������˷���
		if(father.currentView==this)                       //��ǰ��Ļͣ��HelpView3��ʱ
		{
			if(rectLeft.contains(x,y))                     //�������ļ�ͷ��������������涨�ľ�����ʱ
			{	
				if(WelcomeView3.wantSound)                 //����������Ϊ��
					playSound(2,0);                        //���Ű�����Ч
			    father.vib.vibrate(new long[]{100,10,100,70},-1); //��
				if(pageNumber==1)                          //����ڵ�һҳ
				{
					father.setContentView(father.wv);      //���õ�ǰ��ĻΪWelcomeView3����
					father.currentView = father.wv;        //��¼��ǰView
					father.wv.status=7;                    //ת��WelcomeView3�����˵�ҳ��
				}
				else
				{                                          //�����ǵ�һҳ
					pageNumber--;                          //ҳ����1     
					father.myHandler.sendEmptyMessage(3);  //����3��myHandler��sendEmptyMessage������ִ�����е�case3��
				}                                          //���������View��ֻ����ҳ������һ
			}
			else if(rectRight.contains(x,y))               //������Ҽ�ͷ������ԭ��ͬ�����ͷ
			{
				if(pageNumber==1||pageNumber==2)           //��ǰΪ��һҳ��ڶ�ҳ
				{           
					if(WelcomeView3.wantSound)             //��������Ϊ���򲥷�����
						playSound(2,0);
					father.vib.vibrate(new long[]{100,10,100,70},-1); //��
					pageNumber++;                          //ҳ��+1   
					father.myHandler.sendEmptyMessage(3);  //����3��myHandler��sendEmptyMessage������ִ�����е�case3��
				}                                          //���������View��ֻ����ҳ������һ
			}
				
		}
		return true;
	}
}

