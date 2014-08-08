 
package com.example.shenzhoushihao;              //���������

//���������
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* 
 * ����Ϸ����
 *
 */

public class GameView2 extends SurfaceView implements SurfaceHolder.Callback
{
	int selectMap = 1;                           //�ڶ���
	PlaneActivity activity;                      //PlaneActivity������
	private TutorialThread thread;               //ˢ֡���߳�
	GameViewBackGroundThread gameThread;         //���������߳�
	KeyThread keyThread;                         //���̼����߳�
	MoveThread moveThread;                       //�ƶ�������߳� 
	ExplodeThread explodeThread;                 //��ը��֡���߳� 
	
    int backGroundIX = 0;                        //����ͼ��x����
    int i = 0;                                   //����ͼ������
	int cloudX = 470;                            //�Ʋʵ�X����
	
	Bitmap battleback;                           //�����Ĵ�ͼ
	Bitmap[] battlebacks = new Bitmap[ConstantUtil.pictureCount];//װ�ָ��Ժ��ͼƬ
	Bitmap cloud;                                //�Ʋ�
	Bitmap enemyPlane1;                          //�л�1
	Bitmap enemyPlane2;                          //�л�2
	Bitmap enemyPlane3;                          //�л�3
	Bitmap enemyPlane4;                          //�л�4
	
	Bitmap[] number = new Bitmap[10];            //��������
	int[] explodesID = new int[]                 //��ը������֡
	{
		R.drawable.explode1,
		R.drawable.explode2,
		R.drawable.explode3,
		R.drawable.explode4,
		R.drawable.explode5,
		R.drawable.explode6,
	};
	Bitmap[] explodes = new Bitmap[explodesID.length];//��ը������
	
	Bitmap hullBackground;                       //��ʾ�����ı���ͼƬ
	Bitmap hull;                                 //������ͼƬ
	Bitmap life;                                 //Ѫ���ͼƬ
	Bitmap changebullet;
	
	int status = 1;                              //��Ϸ��״̬:1��ʾ��Ϸ��,2��ʾ��Ϸʧ�ܼ��ҷ��ɻ�û��������
	
	Paint paint;//����
	Plane plane = new Plane(50, 140, 1, 0, ConstantUtil.life, this,activity);  //��ʼ���ҷ��ɻ� 
	
    ArrayList<Bullet> badBollets = new ArrayList<Bullet>();                    //�з��ɻ��������ӵ�
	ArrayList<Bullet> goodBollets = new ArrayList<Bullet>();                   //�ҷ��ɻ��������ӵ�
	ArrayList<Explode> explodeList = new ArrayList<Explode>();                 //��ը
	ArrayList<ChangeBullet> changeBollets = new ArrayList<ChangeBullet>();     //���˸ı��ӵ�������
	ArrayList<EnemyPlane> enemyPlanes;           //�з��ķɻ�
	ArrayList<Life> lifes;                       //��Ų�Ѫ����
	SoundPool soundPool;                         //����
	HashMap<Integer, Integer> soundPoolMap; 
	MediaPlayer mMediaPlayer; 
	
	
	public GameView2(PlaneActivity activity)     //������ 
	{
		super(activity);
		this.activity = activity;                //activity������
		initSounds();
		mMediaPlayer = MediaPlayer.create(activity, R.raw.gamestart);
		mMediaPlayer.setLooping(true);
		
        getHolder().addCallback(this);           //ע��ӿ�
        this.thread = new TutorialThread(getHolder(), this);                   //��ʼ��ˢ֡�߳�
        this.gameThread = new GameViewBackGroundThread(this);                  //��ʼ�����������߳�
        this.keyThread = new KeyThread(activity);                              //�����߳�
        this.moveThread = new MoveThread(this);                                //�ƶ��߳�
        this.explodeThread = new ExplodeThread(this);
        
        
        if(activity.processView != null)                                       //�߼��ؽ���������
        {
        	activity.processView.process += 20;
        }
        
        
        if(this.selectMap == 1)
        { 
        	enemyPlanes = Maps.getFirst();                                     //ȡ��һ�صĵл�
        	lifes = Maps.getFirstLife();                                       //ȡ��һ�ص�Ѫ��
        	changeBollets = Maps.getFirstBollet();                             //ȡ��һ�س��˸ı��ӵ��������б�
        }
        initBitmap();                                                          //��ʼ������ͼƬ
        if(activity.isSound)
        {
        	mMediaPlayer.start();
        }
        if(activity.processView != null)                                       //�߼��ؽ���������
        {
        	activity.processView.process += 20;
        } 
	}
	
	
	public void initSounds()                                                   //��ʼ������
	{
	     soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     soundPoolMap.put(1, soundPool.load(getContext(), R.raw.bulletsound1, 1));
	     soundPoolMap.put(2, soundPool.load(getContext(), R.raw.explode, 1));
	     if(activity.processView != null)                                      //�߼��ؽ���������
	     {
	        activity.processView.process += 20;
	     }
	     soundPoolMap.put(3, soundPool.load(getContext(), R.raw.dead, 1));
	     if(activity.processView != null)                                      //�߼��ؽ���������
	     {
	        activity.processView.process += 20;
	     }
	} 
	
	
	public void playSound(int sound, int loop)                                 //�������� 
	{
	    AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
	
	
	public void initBitmap()                                                   //��ʼ������ͼƬ
	{
        if(activity.processView != null)                                       //�߼��ؽ���������
        {
        	activity.processView.process += 20;
        }
		paint = new Paint();
		paint.setColor(Color.RED);
		battleback = BitmapFactory.decodeResource(getResources(), R.drawable.battleback);          //�󱳾�ͼƬ
		cloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);                    //�Ʋ�
		for(int i=0; i<battlebacks.length; i++)                                                    //�г�СͼƬ
		{
			battlebacks[i] = Bitmap.createBitmap(battleback, ConstantUtil.pictureWidth*i, 0, ConstantUtil.pictureWidth, ConstantUtil.pictureHeight);
		}
		battleback = null;                                                                         //�ͷŵ���ͼ
		enemyPlane1 = BitmapFactory.decodeResource(getResources(), R.drawable.plane4);             //�л�1��ͼƬ
		enemyPlane2 = BitmapFactory.decodeResource(getResources(), R.drawable.plane5);             //�л�2��ͼƬ
		enemyPlane3 = BitmapFactory.decodeResource(getResources(), R.drawable.plane6);             //�л�3��ͼƬ
		enemyPlane4 = BitmapFactory.decodeResource(getResources(), R.drawable.plane7);             //�л�4��ͼƬ
	    hullBackground = BitmapFactory.decodeResource(getResources(),R.drawable.hullbackground);   //����ֵ������ͼƬ
		hull = BitmapFactory.decodeResource(getResources(), R.drawable.hull);                      //����ֵͼƬ
		life = BitmapFactory.decodeResource(getResources(), R.drawable.life);                      //��������ֵ������ͼƬ 
		changebullet = BitmapFactory.decodeResource(getResources(), R.drawable.changebullet);      //�ı��ӵ�������ͼƬ
		number[0] = BitmapFactory.decodeResource(getResources(), R.drawable.number0);              //��¼ʱ�������0-9ͼƬ
		number[1] = BitmapFactory.decodeResource(getResources(), R.drawable.number1);
		number[2] = BitmapFactory.decodeResource(getResources(), R.drawable.number2);
		number[3] = BitmapFactory.decodeResource(getResources(), R.drawable.number3);
		number[4] = BitmapFactory.decodeResource(getResources(), R.drawable.number4);
		number[5] = BitmapFactory.decodeResource(getResources(), R.drawable.number5);
		number[6] = BitmapFactory.decodeResource(getResources(), R.drawable.number6);
		number[7] = BitmapFactory.decodeResource(getResources(), R.drawable.number7);
		number[8] = BitmapFactory.decodeResource(getResources(), R.drawable.number8);
		number[9] = BitmapFactory.decodeResource(getResources(), R.drawable.number9);
		
		for(int i=0; i<explodes.length; i++)     //��ʼ����ըͼƬ
		{
			explodes[i] = BitmapFactory.decodeResource(getResources(), explodesID[i]);
			activity.vib.vibrate(new long[]{100,10,100,70},-1);                //��
		}
		
		for(EnemyPlane ep : enemyPlanes)         //��ʼ���������͵ĵл�ͼƬ
		{
			if(ep.type == 1)
			{
				ep.bitmap = enemyPlane1;
			}
			else if(ep.type == 2)
			{
				ep.bitmap = enemyPlane2;
			}
			else if(ep.type == 3)
			{
				ep.bitmap = enemyPlane3;
			}
			else if(ep.type == 4)
			{
				ep.bitmap = enemyPlane4;
			}
		}
		for(Life l : lifes)                      //��ʼ��Ѫ�����ͼƬ
		{
			l.bitmap = life;
		}
		
		for(ChangeBullet cb : changeBollets)     //��ʼ�����˸ı��ӵ�������ͼƬ
		{
			cb.bitmap = changebullet;
		}
	}
	
	
	public void onDraw(Canvas canvas)            //���Ʒ���
	{
		//����������z��ģ��󻭵ĻḲ��ǰ�滭��
		//�Ƚ�������п������Է�����ʱ����ͬ������
		int backGroundIX=this.backGroundIX;
		int i=this.i;
		int cloudX = this.cloudX;
		
		//���i��������
		if(backGroundIX>0)
		{
			int n=(backGroundIX/ConstantUtil.pictureWidth)+((backGroundIX%ConstantUtil.pictureWidth==0)?0:1);     //����i�����м���ͼ
			for(int j=1;j<=n;j++)
			{
				canvas.drawBitmap(
			      battlebacks[(i-j+ConstantUtil.pictureCount)%ConstantUtil.pictureCount], 
			      backGroundIX-ConstantUtil.pictureWidth*j, 
			      ConstantUtil.top, 
			      paint
			     );
			}
		}

		//���i�Լ�
		canvas.drawBitmap(battlebacks[i], backGroundIX, ConstantUtil.top, paint);
		
		//���i�Ҳ������
		if(backGroundIX<ConstantUtil.screenWidth-ConstantUtil.pictureWidth)
		{
			int k=ConstantUtil.screenWidth-(backGroundIX+ConstantUtil.pictureWidth);
			int n=(k/ConstantUtil.pictureWidth)+((k%ConstantUtil.pictureWidth==0)?0:1);//����i�����м���ͼ
			for(int j=1;j<=n;j++)
			{
				canvas.drawBitmap(
						battlebacks[(i+j)%ConstantUtil.pictureCount], 
						backGroundIX+ConstantUtil.pictureWidth*j, 
						ConstantUtil.top, 
						paint
				);
			}
		}	

		plane.draw(canvas);                      //����ҷɻ�
		if(status == 1 || status == 3)           //��Ϸ��ʱ,�ؿ���ʱ
		{
			try
			{
				for(Bullet b:goodBollets)
				{
					b.draw(canvas);              //�����ҷ��ӵ�
				}			
			}
			catch(Exception e)                   //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			{}
			try
			{
				for(EnemyPlane ep:enemyPlanes)
				{
					if(ep.status == true)
					{
						ep.draw(canvas);         //���Ƶз��ɻ�
					}
				}
			}
			catch(Exception e)                   //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			{} 
			try
			{ 
				for(Bullet b:badBollets)
				{
					b.draw(canvas);              //���Ƶз��ӵ� 
				}
			}
			catch(Exception e)                   //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			{}	
			try
			{
				for(ChangeBullet cb : changeBollets)
				{
					if(cb.status == true)
					{
						cb.draw(canvas);         //���Ƴ��˸ı�ǹ������
					}
				}
			}
			catch(Exception e)                   //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			{}
			try
			{
				for(Life l : lifes)
				{
					if(l.status == true)
					{
						l.draw(canvas);          //����Ѫ��
					}
				}
			}
			catch(Exception e)                   //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			{
			}
			try
			{
				for(Explode e : explodeList)
				{
					e.draw(canvas);              //���Ʊ�ը
				}
			}
			catch(Exception e)                   //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			{}
		}
		
		if(cloudX>-cloud.getWidth() && cloudX<ConstantUtil.screenWidth)
		{
			canvas.drawBitmap(cloud, cloudX, ConstantUtil.top, paint);         //�����Ʋ�
		}
		
		canvas.drawBitmap(hullBackground, -10, 200, paint);                    //���������ı���
		canvas.drawText("HP", 20, 215, paint);
		for(int j=0; j<((5-plane.life)<0?5:plane.life); j++)
		{
			canvas.drawBitmap(hull, 95+11*j, 202, paint);                      //���Ʊ�ʾ������С����
		}
		activity.currentScore=plane.life*20;                                   //���㵱ǰ�ؿ��ĵ÷�
		canvas.drawText("Misson 2", 150, 215, paint); 
		String timeStr =gameThread.touchTime/10+"";                            //ת�����ַ���
    	for(int c=0;c<timeStr.length();c++)
    	{
    		int tempScore=timeStr.charAt(c)-'0';
    		canvas.drawBitmap(number[tempScore], 280+c*22, 202, paint);        //ѭ������ʱ��
    	}
    	canvas.drawText("Time", 250, 215, paint);
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                           //����ʱ������Ӧ����
	{
        this.thread.setFlag(true);                                             //����ˢ֡�߳�
        this.thread.start();
        this.gameThread.setFlag(true);
        this.gameThread.start();                                               //�������������߳�
        this.keyThread.setFlag(true);
        keyThread.start();                                                     //�������̼����߳�
        this.moveThread.setFlag(true);
        moveThread.start();                                                    //���������ƶ�����ƶ��߳�
        this.explodeThread.setFlag(true);
        explodeThread.start();
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                         //�ݻ�ʱ�ͷ���Ӧ����
	{                                        
        boolean retry = true;
        this.thread.setFlag(false);
        this.gameThread.setFlag(false);
        this.keyThread.setFlag(false);
        this.moveThread.setFlag(false);
        this.explodeThread.setFlag(false);
        while (retry) 
        {
            try 
            {
            	keyThread.join();
                thread.join();
                gameThread.join();
                moveThread.join();
                explodeThread.join();
                retry = false;
            } 
            catch (InterruptedException e)       //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
            {}
        }
	}
	
	
	
	class TutorialThread extends Thread          //ˢ֡�߳�
	{
		private int sleepSpan = 67;              //˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;
		private GameView2 gameView;
		private boolean flag = false;
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, GameView2 gameView)                     //������
        {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }
        
        
        public void setFlag(boolean flag)        //����ѭ�����λ
        {
        	this.flag = flag;
        }
        
        
		@Override
		public void run()                        //��д��run����  
		{
			Canvas c;
            while (this.flag) 
            {
                c = null;
                try 
                {
                    c = this.surfaceHolder.lockCanvas(null);                   // ������������
                    synchronized (this.surfaceHolder) 
                    {
                    	gameView.onDraw(c);                                    //���û��Ʒ���
                    }
                } 
                finally 
                {
                    if (c != null) 
                    {
                        this.surfaceHolder.unlockCanvasAndPost(c);             //������Ļ��ʾ����
                    }
                }
                try
                {
                	Thread.sleep(sleepSpan);                                   //˯��ָ��������
                }
                catch(Exception e)                                             //�����쳣��Ϣ
                {
                	e.printStackTrace();                                       //��ӡ��ջ��Ϣ
                }
            }
		}
	}
}