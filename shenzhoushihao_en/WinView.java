package com.example.shenzhoushihao;              //���������

//���������
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Color;
import android.graphics.Typeface;

/*
 * ��Ϸʤ����棬����ؽ���
 *
 */

public class WinView extends SurfaceView implements SurfaceHolder.Callback 
{
	PlaneActivity activity;                      //PlaneActivity������
	private TutorialThread thread;               //ˢ֡���߳�
	Bitmap winviewbackground;                    //����
    Paint paint;                                 //����
	Paint paint1;                                //����1
	
	int alpha=255;                               //�������
	
	
	public WinView(PlaneActivity activity)       //������
	{ 
		super(activity);
		this.activity = activity;                //�õ�activity������
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();                            //��ʼ��ͼƬ��Դ 
	}
	
	
	public void initBitmap()                     //��ʼ��ͼƬ��Դ�ķ���
	{ 
		paint = new Paint();                     //��ɻ���
		paint.setAlpha(alpha);                   //��������
		paint.setTextSize(22);                   //���������С
	    paint.setTypeface(Typeface.DEFAULT_BOLD);//���ô���
	    paint.setColor(Color.RED);               //������ɫ
		paint.setAntiAlias(true);				 //���ÿ����
		paint1 = new Paint();                    //��ɻ���
		paint1.setAlpha(alpha);                  //��������
		paint1.setTextSize(18);                  //���������С
	    paint1.setTypeface(Typeface.DEFAULT_BOLD);//���ô���
	    paint1.setColor(Color.YELLOW);           //������ɫ
		paint1.setAntiAlias(true);				 //���ÿ����
		
		winviewbackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
}
	
	
	public void onDraw(Canvas canvas)            //���Ʒ���
	{
		String currentScore=Integer.toString(activity.currentScore);                      //��ʾ��ǰ�ؿ��÷ֵ��ַ�
		//����������z��ģ��󻭵ĻḲ��ǰ�滭��
		canvas.drawBitmap(winviewbackground, 0, 10, paint);	
		canvas.drawText("Score in this misson:       ", 25, 185, paint);                                    //������λ��д�Ϲ涨����  
		canvas.drawText("Press to start next misson", 15, 110, paint1);                          //������λ��д�Ϲ涨����  
		canvas.drawText(currentScore, 135, 185, paint);                                   //���Ƶ�ǰ�÷�   
		canvas.drawRect(0, 0, 320, 10, paint);                                            //�������µĺڿ�
		canvas.drawRect(0, 230, 320, 240, paint);
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                                     //����ʱ������ 
	{
        this.thread.setFlag(true);               //�����̱߳�־λ
        this.thread.start();                     //�����߳�
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                                   //�ݻ�ʱ������ 
	{
        boolean retry = true;                    //ѭ����־λ
        thread.setFlag(false);                   //����ѭ����־λ
        while (retry) 
        {
            try 
            {
                thread.join();                   //�ȴ��߳̽���
                retry = false;
            } 
            catch (InterruptedException e)       //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
            {}
        }
	}	
	
	class TutorialThread extends Thread          //ˢ֡�߳�
	{
		private int span = 500;                  //˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;
		private WinView winView;                 //WinView������
		private boolean flag = false;            //ѭ����־λ
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, WinView winView)              //������
        {
            this.surfaceHolder = surfaceHolder;
            this.winView = winView;              //�õ���Ϸʤ�����            
        }        
        
        public void setFlag(boolean flag)        //���ñ�־λ 
        {
        	this.flag = flag;
        }        
        
		@Override
		public void run()                        //��д��run���� 
		{
			Canvas c;                            //����
            while (this.flag)                    //ѭ�� 
            {
                c = null;
                try 
                {
                	c = this.surfaceHolder.lockCanvas(null);                	         // ���������
                    synchronized (this.surfaceHolder) 
                    {
                    	winView.draw(c);       //���û��Ʒ���
                    }
                } 
                finally                          //ʹ��finally��䱣֤�������һ����ִ�� 
                {
                    if (c != null) 
                    {
                        this.surfaceHolder.unlockCanvasAndPost(c);                    	 //������Ļ��ʾ����
                    }
                }
                try
                {
                	Thread.sleep(span);          //˯��ָ��������
                }
                catch(Exception e)               //�����쳣��Ϣ
                {
                	e.printStackTrace();         //��ӡ�쳣��Ϣ
                }
            }
		}
	}
}