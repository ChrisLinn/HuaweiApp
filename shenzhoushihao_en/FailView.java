 
package com.example.shenzhoushihao;              //���������

//���������
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* 
 * ��Ϸʧ�ܽ���
 *
 */

public class FailView extends SurfaceView implements SurfaceHolder.Callback   
{
	PlaneActivity activity;                      //PlaneActivity������
	private TutorialThread thread;               //ˢ֡���߳�
	Bitmap fialBackground;                       //����
	Bitmap goon;                                 //������Ϸ��ť
	Bitmap exit2;                                //�˳���ť
	Paint paint;                                 //����
	
	
	public FailView(PlaneActivity activity)      //������ 
	{
		super(activity);
		this.activity = activity;                //�õ�activity������
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();                            //��ʼ��ͼƬ��Դ 
	}
	
	
	public void initBitmap()                     //��ʼ��ͼƬ��Դ�ķ���
	{
		paint = new Paint();
		fialBackground = BitmapFactory.decodeResource(getResources(), R.drawable.fialbackground);
		goon = BitmapFactory.decodeResource(getResources(), R.drawable.goon);
		exit2 = BitmapFactory.decodeResource(getResources(), R.drawable.exit2);
	}
	
	
	public void onDraw(Canvas canvas)            //���Ʒ���
	{
		//����������z��ģ��󻭵ĻḲ��ǰ�滭��
		canvas.drawBitmap(fialBackground, 0, 0, paint);
		canvas.drawBitmap(goon, 0, 10, paint);
		canvas.drawBitmap(exit2, 230, 209, paint);
		
		canvas.drawRect(0, 0, 320, 10, paint);   //�������µĺڿ�
		canvas.drawRect(0, 230, 320, 240, paint);
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                             //����ʱ������  
	{
        this.thread.setFlag(true);               //�����̱߳�־λ
        this.thread.start();                     //�����߳�
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                           //�ݻ�ʱ������ 
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
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)                               //��Ļ����
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(event.getX() > 0 && event.getX() < 0+goon.getWidth()
					&& event.getY() > 10 && event.getY() < 10+goon.getHeight())  //�����������Ϸ��ť
			{
				Message msg1 = activity.myHandler.obtainMessage(2);
				activity.myHandler.sendMessage(msg1);                            //����activity����Handler��Ϣ			
			}
			else if(event.getX() > 230 && event.getX() < 230+exit2.getWidth()
					&& event.getY() > 209 && event.getY() < 209+exit2.getHeight()) //������˳���Ϸ��ť
			{
				System.exit(0);                                                  //ֱ���˳�
			}
		}
		return super.onTouchEvent(event);
	}
	
	
	
	class TutorialThread extends Thread          //ˢ֡�߳�
	{
		private int span = 500;                  //˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;
		private FailView fialView;
		private boolean flag = false;
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, FailView fialView)    //������
        {
            this.surfaceHolder = surfaceHolder;
            this.fialView = fialView;
        }
        
        
        public void setFlag(boolean flag)        //���ñ�־λ
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
                try                              // ������������
                {
                    c = this.surfaceHolder.lockCanvas(null);                     
                    synchronized (this.surfaceHolder) 
                    {
                    	fialView.onDraw(c);      //���û��Ʒ���
                    }
                }
                finally 
                {
                    if (c != null)               //������Ļ��ʾ����
                    {
                    	this.surfaceHolder.unlockCanvasAndPost(c);
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