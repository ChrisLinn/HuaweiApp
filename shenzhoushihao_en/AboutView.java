 
package com.example.shenzhoushihao;                                            //���������

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


public class AboutView extends SurfaceView implements SurfaceHolder.Callback   //�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{    
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity������
	Bitmap menu_about1;                                                        //���ڽ���ĵ�һҳͼƬ
	Bitmap menu_about2;                                                        //���ڽ���ĵڶ�ҳͼƬ
	public static int pageNumber=1;                                            //ҳ�룬Ĭ��Ϊ1
	Rect rectLeft    =new Rect(5,270,80,315);                                  //���ͷ��Ӧ��Χ��Ϊ���Σ�����Ϊ���ϽǺ����½ǵ����ꡣ��Ļ���Ͻǵ�����Ϊ��0,0��
	Rect rectRight   =new Rect(180,280,235,315);                               //�Ҽ�ͷ��Ӧ��Χ��ͬ��
	SoundPool soundPool;		                                               //SoundPool��������
	HashMap<Integer,Integer> soundMap;	                                       //���������Դ��Map
	
	
	public AboutView(ShenzhoushihaoActivity father)                            //���췽��
	{         
		super(father);                                                         //��ʼ������
		this.father = father;                                                  //��Ա������ʼ��
		getHolder().addCallback(this);            
		menu_about1 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_about1);      //���ص�һҳ���ڽ���ͼƬ��Դ
		menu_about2 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_about2);      //���صڶ�ҳ���ڽ���ͼƬ��Դ
		initSound(father);                                                     //��ʼ��������Դ�ķ���
	}
	
	
	public void doDraw(Canvas canvas)                                          //doDraw����������������Ļ
	{                      
		Paint paint = new Paint();	                                           //��������
		paint.setAlpha(255);		                                           //����͸����
		if(pageNumber==1)                                                      //��ҳ��Ϊһ
		    canvas.drawBitmap(menu_about1, 0, 0,paint);                        //���Ƶ�һҳ����
		else 
			canvas.drawBitmap(menu_about2, 0, 0,paint);                        //���Ƶڶ�ҳ����
		}
	   
	
	public void initSound(ShenzhoushihaoActivity father)                       //��������ʼ������
	{                                                                          //initSound�ķ�����
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);            //��ʼ��soundPool,�����ֱ�Ϊ����ͬʱ���ŵ��������Ϊһ�����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();                             //��ʼ�����������Դ�Ĺ�ϣ��
		int id = soundPool.load(father, R.raw.game_press, 2);                  //���ذ���������Դ��idΪ2
		soundMap.put(2, id);	                                               //����soundMap������id��
		}
		
			
	public void playSound(int sound,int loop)                                  //��������������
	{                                                                          //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id���ڶ����ǲ��Ŵ�����-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		float volume = streamVolumeCurrent / streamVolumeMax;   	    
		soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 2);       //ϵͳ�Ĳ��������ķ���
		}
		
		
	public void stopSound(int sound)                                           //������ֹͣ����
	{                                                                          //��дֹͣ�����ķ�������Ҫĸ���ǿ���ͨ������������Դ��id
			soundPool.stop(soundMap.get(sound));                               //ϵͳֹͣ��������
		}	

	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{}                                                                         //��д�ӿڷ���
		

	public void surfaceCreated(SurfaceHolder holder)
	{                                                                          //����Viewʱ����ִ��
			Canvas canvas=null;
			try 
			{                            
				canvas = holder.lockCanvas(null);                              //��������
				doDraw(canvas);                                                //����doDraw����������������Ļ
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				if(canvas != null)
				{
					holder.unlockCanvasAndPost(canvas);                        //����
				}
				}		
		}
	
	
	public void surfaceDestroyed(SurfaceHolder holder) 
    {}                                                                         //View������ʱ���õķ���

	
   //AboutView�и�������Ӧ
		@SuppressWarnings("unchecked")
		public boolean myTouchEvent(int x,int y)                               //�˷�����ShenzhoushihaoActivity���ã���������Ļʱϵͳ����ShenzhoushihaoActivity
	    {	                                                                   //�е�onTouchEvent,��ȡ���������Ȼ����ô��ݲ������˷���
			if(father.currentView==this)
			{
				if(rectLeft.contains(x,y))                                     //�������ļ�ͷ��������������涨�ľ�����ʱ
				{	
					
					if(WelcomeView.wantSound)                                  //����������Ϊ��
					     playSound(2,0);                                       //���Ű�����Ч
					father.vib.vibrate(new long[]{100,10,100,70},-1);          //��
					if(pageNumber==1)                                          //����ڵ�һҳ
					{
					father.setContentView(father.wv);						   //���õ�ǰ��ĻΪWelcomeView����
					father.currentView = father.wv;                            //��¼��ǰView
					father.wv.status=7;                                        //ת��WelcomeView�����˵�ҳ��
					}
					else
					{                                                          //�����ǵ�һҳ
						pageNumber--;                                          //ҳ����1
						father.myHandler.sendEmptyMessage(4);                  //����4��myHandler��sendEmptyMessage������ִ�����е�case3��
					     }                                                     //���������View��ֻ����ҳ������һ
				}
				else if(rectRight.contains(x,y))                               //ͬ�ϣ�ֻ�������Ҽ�ͷ
				{
					if(pageNumber==1)
					{                                                          //���ڵ�һҳ
						
						if(WelcomeView.wantSound)                              //����������Ϊ��
							playSound(2,0);                                    //���Ű�����
						father.vib.vibrate(new long[]{100,10,100,70},-1);      //��
						pageNumber++;                                          //ҳ���1
						father.myHandler.sendEmptyMessage(4);                  //����4��myHandler��sendEmptyMessage������ִ�����е�case3��
						}                                                      //���������View��ֻ����ҳ����1
					}
				}
		 return true;
		}			
}
