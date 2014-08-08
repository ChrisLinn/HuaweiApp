 
package com.example.shenzhoushihao;                   //���������

//���������
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * ��������
 *
 */

public class HelpView2 extends SurfaceView implements SurfaceHolder.Callback 
{
	PlaneActivity activity;                           //PlaneActivity������
	private TutorialThread thread;                    //ˢ֡���߳�
	Paint paint;                                      //����
	Bitmap background;                                //����
	Bitmap help3;                                     //д����Ϸ�淨��ipad��Ļ
	Bitmap ok;                                        //ȷ����ť
	
	
	public HelpView2(PlaneActivity activity)          //������ 
	{
		super(activity);
		this.activity = activity;                     //�õ�activity������
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();                                 //��ʼ��ͼƬ��Դ
	}
	
	
	public void initBitmap()                          //��ʼ��ͼƬ��Դ�ķ���
	{
		paint = new Paint();                          //���ɻ��� 
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		help3 = BitmapFactory.decodeResource(getResources(), R.drawable.help3);
		ok = BitmapFactory.decodeResource(getResources(), R.drawable.ok);
	}
	
	
	public void onDraw(Canvas canvas)                 //���Ʒ���
	{
		//����������z��ģ��󻭵ĻḲ��ǰ�滭��
		canvas.drawColor(Color.BLACK);                //����Ļˢ�ɺ�ɫ 
		canvas.drawBitmap(background, 0,0, paint);
		canvas.drawBitmap(help3, 0, 10, paint);
		canvas.drawRect(0, 0, 320, 10, paint);        //�������µĺڿ�
		canvas.drawRect(0, 230, 320, 240, paint); 
		canvas.drawBitmap(ok, 256, 198, paint);  
    }
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)  //����ʱ������  
	{
        this.thread.setFlag(true);                    //�����̱߳�־λ
        this.thread.start();                          //�����߳�
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)//�ݻ�ʱ������ 
	{
        boolean retry = true;                         //ѭ����־λ
        thread.setFlag(false);                        //����ѭ����־λ
        while (retry) 
        {
            try {
                thread.join();                        //�ȴ��߳̽���
                retry = false;
            } 
            catch (InterruptedException e)            //���ϵ�ѭ����ֱ��ˢ֡�߳̽���
            {}
        }
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)    //��Ļ����
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(event.getX()>256 && event.getX()<256+ok.getWidth()
					&& event.getY()>198 && event.getY()<198+ok.getHeight())    //�����ȷ����ť
			{
				Message msg1 = activity.myHandler.obtainMessage(7); 
				activity.myHandler.sendMessage(msg1);                          //����activity����Handler��Ϣ
			}  
		}
		return super.onTouchEvent(event);
	}
	
	
	
	class TutorialThread extends Thread               //ˢ֡�߳�
	{
		private int span = 100;                       //˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;
		private HelpView2 helpView;                   //HelpView2������
		private boolean flag = false;                 //ѭ����־λ
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, HelpView2 helpView) //������
        {
            this.surfaceHolder = surfaceHolder;
            this.helpView = helpView;                 //�õ���������
        }
        
        
        public void setFlag(boolean flag)             //���ñ�־λ 
        {
        	this.flag = flag;
        }
        
        
		@Override
		public void run()                             //��д��run����  
		{
			Canvas c;                                 //����
            while (this.flag) 
            {
                c = null;
                try 
                {
                    c = this.surfaceHolder.lockCanvas(null);                   // ������������
                    synchronized (this.surfaceHolder) 
                    {
                    	helpView.onDraw(c);           //���û��Ʒ���
                    }
                } 
                finally                               //ʹ��finally��䱣֤�������һ����ִ��
                {
                    if (c != null) 
                    {
                    	this.surfaceHolder.unlockCanvasAndPost(c);	           //������Ļ��ʾ����
                    }
                }
                try
                {
                	Thread.sleep(span);               //˯��ָ��������
                }
                catch(Exception e)                    //�����쳣��Ϣ
                {
                	e.printStackTrace();              //��ӡ�쳣��Ϣ
                }
            }
		}
	}
}