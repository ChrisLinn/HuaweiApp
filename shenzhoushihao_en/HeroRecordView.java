 
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

public class HeroRecordView extends SurfaceView implements SurfaceHolder.Callback                       //�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{   
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity������
	Bitmap bmpBack;                                                            //���Ӣ�۰�ı���ͼƬ
	Bitmap left;                                                               //������ͷ
	
    Rect rectLeft = new Rect(1,270,60,310);                                    //���ͷ��Ӧ��Χ
    
	SoundPool soundPool;		                                               //SoundPool��������
	HashMap<Integer,Integer> soundMap;	                                       //���������Դ��Map
	User[] user=new User[6];                                                   //��ŵ÷���ǰ������Ӣ�ۣ�User����
	
	public HeroRecordView(ShenzhoushihaoActivity father)                       //���췽��
	{              
		super(father);                                                         //��ʼ������
		this.father = father;                                                  //��ʼ����Ա����
		getHolder().addCallback(this);
		initSound(father);                                                     //��ʼ��������Դ�ķ���
		bmpBack = BitmapFactory.decodeResource(father.getResources(), R.drawable.welcome_menu);         //���ر���ͼƬ��Դ
		left = BitmapFactory.decodeResource(father.getResources(), R.drawable.lpoint);                  //�������ͷ
		for(int i=0;i<6;i++)                                                   //��ÿ��User�������NULL��
			user[i]=new User();
		}
	
	
	public void doDraw(Canvas canvas)                                          //doDraw��������HeroRecordView����������surfaceCreatedʱ����
	{                              
		Paint paint = new Paint();	                                           //��������
		paint.setAlpha(255);		                                           //��������
		canvas.drawBitmap(bmpBack, 0, 0,paint);                                //���Ʊ���
		canvas.drawBitmap(left, 5, 280, paint);                                //���Ƽ�ͷ
		
		Paint paint1 = new Paint();	                                           //ͬ��
		paint1.setAlpha(255);                                                  //ͬ��
		paint1.setTextSize(32);                                                //��������
	    paint1.setTypeface(Typeface.DEFAULT_BOLD);	                           //���ô���
	    paint1.setColor(Color.rgb(255, 50, 70));                               //����rgb��ɫ
		paint1.setAntiAlias(true);		                                       //���ÿ����       
		canvas.drawText("High Scores", 70, 50, paint1);		                       //д�ϴ���Ӣ�۰�������
		
		Paint paint2 = new Paint();            	                               //��������
		paint2.setAlpha(255);                                                  //��������
		paint2.setTextSize(22);                                                //���������С
	    paint2.setTypeface(Typeface.DEFAULT_BOLD);	                           //���ô���
	    paint2.setColor(Color.RED);                                            //������ɫ
	    
		Paint paint3 = new Paint();	                                           //��������
		paint3.setAlpha(255);                                                  //ͬ��
		paint3.setTextSize(18);                                                //ͬ��
	    paint3.setTypeface(Typeface.DEFAULT_BOLD);	                           //���ô���
	    paint3.setColor(Color.rgb(140, 50, 70));                               //������ɫ
	    father.loadHero(user);                                                 //����Ӣ��User���󸶸�user����
	    paint1.setTextSize(23);                                                //������paint1������������
	    canvas.drawText("NAME     SCORE", 18, 85, paint1);                       //д����˸��к��������ʱ
	    for(int i=0;i<6;i++)                                                   //�����õ���6������
	    	if(user[i].userName.length()>10)
	    		user[i].userName=user[i].userName.substring(0, 7);             //����̫���Ļ���ȡǰ�˸�
	        if(user[0].userName!="NULL"&&user[0].userName!=null)
	        {   //ΪNULL����ΪĬ��ֵnullnullʱ����ʾ
	        	canvas.drawText(user[0].userName, 26, 120, paint2);            //д����
                canvas.drawText(Integer.toString(user[0].grade) , 174, 120, paint2);//д����
                }
	    for(int i=1;i<6;i++)
	    {                                                                      //ͬ���ķ���д�ϵڶ���������
	    	if(user[i].userName!="NULL"&&user[i].userName!=null) 
	    	{                                                                  //ͬ��
	    		canvas.drawText(user[i].userName, 30, 120+i*30, paint3);       //ͬ��
		        canvas.drawText(Integer.toString(user[i].grade) , 177, 120+i*30, paint3);
		        }                                                              //ͬ��
	    	}
	    }
	
	
	public void initSound(ShenzhoushihaoActivity father)                       //��������ʼ������
	{                                                                          //initSound�ķ�����
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);            //��ʼ��soundPool,�����ֱ�Ϊ����ͬʱ���ŵ��������Ϊһ�����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();                             //��ʼ�����������Դ�Ĺ�ϣ��
		int id = soundPool.load(father, R.raw.game_press, 2);                  //���ذ���������Դ��idΪ2
		soundMap.put(2, id);	                                               //����soundMap��ָ��id��
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
		soundPool.stop(soundMap.get(sound));                                   //ϵͳֹͣ�������� 
		}	
		

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{                                                                          //��д�ӿڷ���
		}

	
	public void surfaceCreated(SurfaceHolder holder) 
	{                                                                          //��д�ӿڵķ���������ʱ�Զ�����
		Canvas canvas=null;
		try 
		{
			canvas = holder.lockCanvas(null);                                  //��������
			doDraw(canvas);                                                    //����doDraw����
			} 
		catch (Exception e) 
		{                                                                      //�����쳣
			e.printStackTrace();
			}
		finally
		{                                                                      //���Ҫ�����
			if(canvas != null)
			{
				holder.unlockCanvasAndPost(canvas);                            //�ͷŻ���
				}
			}
		}

	
    public void surfaceDestroyed(SurfaceHolder holder) 
    {                                                                          //��д�ӿڵķ���������ʱ�Զ�����
		}
		
	//HeroRecordView�и�������Ӧ
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)                                   //������Ӧ��������ShenzhoushihaoActivity�е�OnTouchEvent���е���
	{
		if(father.currentView==this&&rectLeft.contains(x,y))
		{
			if(WelcomeView.wantSound)                                          //��������Ϊ��ʱ 
				playSound(2,0);                                                //�ų�������
			father.vib.vibrate(new long[]{100,10,100,70},-1);                  //������
			father.setContentView(father.wv);						           //���õ�ǰ��ĻΪWelcomeView����
		    father.currentView = father.wv;                                    //��¼��ǰView
		    father.wv.status=7;                                                //ֱ�ӵ�WelcomeView��case 7
		    }
		return true;
		}
	}
