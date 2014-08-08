 
package com.example.shenzhoushihao;                                     //���������

//���������
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@SuppressWarnings("unused")
public class WelcomeView3 <MyActivity> extends SurfaceView implements SurfaceHolder.Callback //�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{    
	TankActivity father;					                            //TankActivity������
	WelcomeThread3 wt;                                                  //��̨�޸�����߳�
	WelcomeDrawThread3 wdt;                                             //��̨�ػ��߳�
	boolean ifsound;                                                    //�жϱ�־����,Ϊtrueʱ�����Ϸ����������Чѡ����棬��ʱ��Ч�Ŀ����͹ر����������¼���Ч
	boolean skip;                                                       //�жϱ�־����,Ϊtrueʱ�����Ϸ�������ڳ�����Ļ�Ľ��棬��ʱ�����ش����¼���Ч
	public static boolean sounder;                                      //���ֻ�Ĳ˵�������ѡ����Ч������ж��Ƿ��������ı�־����������ĸı��ʹwantSoundҲ�ı䣬�Ӷ�ﵽ����������Ч��
	public static boolean wantSound;                                    //WelcomeView3�ж��Ƿ��������ı�־����
	
	int status=-1;		                                                //��¼��ǰ״̬,0��������������ʧ��8����Ч���棬7����Ϸ�˵���1����ʾipad��Ļ��3����ʾ����
	int alpha = 0;                                                      //��¼����ͼƬ�ı���ɫ
	int screenX=320;                                                    //��¼��Ļ���Ͻǵ�x���
	int screenY=60;                                                     //��¼��Ļ���Ͻǵ�y���
	float textSize = 13f;                                               //����Ĵ�С
	int characterSpanX=24;                                              //������֮���ƫ��
	int characterSpanY=15;                                              //ÿ���ֵĴ�С
	int characterNumber=1;                                              //������Ļ����ʾ���ֵ���Ŀ
	int textStartX = 164;                                               //���ֵ���ʼx��� 
	int textStartY = 75;                                                //���ֵ���ʼy���
	int characterEachLine =13;                                          //ÿһ����ʾ�ֵĸ���
	int startChar=0;                                                    //�ַ��д����￪ʼ�����Ϊ�˹���ʹ��
	int maxChar=66;                                                     //��Ļ���������ʾ������
	
	//�����ʾ�ֵ��㷨����ڴ�����ϵ�GreatRun��Ϸ
	char [] str={'W','e','l','l',' ','d','o','n','e',
				'!','Y','o','u',' ','h','a','v','e',
				' ','c','o','m','p','l','e','t','e',
				' ','a','l','l',' ','t','h','e',' ','m',
				'i','s','s','i','o','n','s','!'};                //��ʾ����������
	Rect rectStart    =new Rect( 0,  0,  90, 32);                       //��ʼ�˵�
	Rect rectContinue =new Rect( 30,67,120,  99);                       //����˵�
	Rect rectHero     =new Rect( 80,134,170,166);                       //Ӣ�۰�˵�
	Rect rectHelp     =new Rect(120,201,210,233);                       //����˵�
	Rect rectExit     =new Rect(150,267,240,300);                       //�˳��˵�
	
	Rect rectSoundMenu = new Rect(94,445,226,475);                      //�����˵�
	Rect rectSoundMenu1 = new Rect(20,280,60,320);                      //�����˵�
	Rect rectSoundMenu2 = new Rect(200,300,240,320);                    //�����˵�
	Rect rectSkip = new Rect(90,290,240,320);                           //���˵�
	
	SoundPool soundPool;		                                        //SoundPool��������
	HashMap<Integer,Integer> soundMap;	                                //���������Դ��Map
	
	boolean play;
	
	
	//����������ʼ����Ҫ��Ա����
	public WelcomeView3(TankActivity father)                            //���췽��
	{  
		super(father);                                                  //��ʼ������
		this.father = father;                                           //��ʼ����Ա����	
		getHolder().addCallback(this);                                  //��ʼ��
		wt = new WelcomeThread3(this);                                  //��ʼ���߳�
		wdt = new WelcomeDrawThread3(this,getHolder());                 //��ʼ���ػ��߳�
		initSound(father);                                              //��ʼ�������ķ�����������
		status = 0;	                                                    //��ʼ��Ϊ��
	}
	
	
	//������Ļ
    public void doDraw(Canvas canvas)                                   //doDraw������WelcomeDrawThread3�������ô˷���������Ļ
    {            
		Paint paint = new Paint();	                                    //��������
		paint.setAlpha(alpha);		                                    //����
		switch(status)
		{
		case 0:
			canvas.drawColor(Color.BLACK);                              //����
			BitmapManager3.drawWelcomePublic(0, canvas, 0, 0,paint);    //���������汳��
			ifsound=true;                                               //����һ������ѡ�����Ĵ����¼���Ϊ��Ч
			break;
		case 8:                                                         //״̬����ʾ��Ч
			canvas.drawColor(Color.BLACK);                              //����
			BitmapManager3.drawWelcomePublic(3, canvas, 0, 0,paint);    //��������ѡ�����
			break;
		case 7:                                                         //״̬����ʾ��Ϸ�˵�����
			canvas.drawColor(Color.BLACK);	   //����
			BitmapManager3.drawWelcomePublic(2, canvas, 0, 0,paint);    //���˵�����
			BitmapManager3.drawWelcomePublic(7, canvas, 0,0,paint);     //����Ķ��ǡ���ʼ������Ӣ�۰񡱵Ȳ˵������
			BitmapManager3.drawWelcomePublic(8, canvas, 30,67,paint);   //�ڲ�ͬ����괦����
			BitmapManager3.drawWelcomePublic(9, canvas, 80,134,paint);
			BitmapManager3.drawWelcomePublic(10, canvas, 120,201,paint);
			BitmapManager3.drawWelcomePublic(12, canvas, 150, 267,paint);
			break;
		case 1:                                                           //״̬����ʾipad��Ļ
			canvas.drawColor(Color.BLACK);                                //����
			alpha=255;                                                    //������������
			paint.setAlpha(alpha);                                        //���û���
			BitmapManager3.drawWelcomePublic(2, canvas, 0, 0,paint);      //����
			BitmapManager3.drawWelcomePublic(1, canvas, screenX, 0,paint);//��ʾ��̬��ipad��Ļ
			break;
        case 3:                                                           //״̬����ʾ����
			canvas.drawColor(Color.BLACK);                                //����
			BitmapManager3.drawWelcomePublic(2, canvas, 0, 0,paint);      //������ҳ�汳��
			BitmapManager3.drawWelcomePublic(1, canvas, screenX, 0,paint);//����ipad��ĻͼƬ
			paint.setTextSize(textSize);			                      //���������С
			paint.setColor(Color.RED);                                    //����������ɫ
			paint.setTypeface(Typeface.DEFAULT_BOLD);	                  //���ô���
			paint.setAntiAlias(true);					                  //���ÿ����
			if(characterNumber > maxChar)                                 //���Ҫ��ʾ���������ipad��Ļ�����ʾ����
			{
				startChar += characterEachLine;		                      //�޸��ַ������п�ʼ���Ƶ�λ��
				characterNumber -= characterEachLine;	
			}	
				
			
	        //���ֳ��ֵ��㷨����ںܶ�android����̳��ϵ�GreatRun��Ϸ�����ֳ����㷨
	        int lines = characterNumber/characterEachLine + (characterNumber%characterEachLine==0?0:1);//���� һ����Ҫ����
		    Paint paint1 = new Paint();	                                  //��������
		    Typeface font = Typeface.create(Typeface.SERIF, Typeface.BOLD);
			paint1.setStrokeWidth(20);
			paint1.setARGB(200, 200, 80, 60);
			paint1.setTypeface(font);

			for(int i=0;i<lines;i++)
			{
				if(i == lines-1)                                          //���һ��
				{                           
					int n = characterNumber-characterEachLine*(lines-1);  //ʣ�������
					for(int j=0;j<n-1;j++)                                //����д��
					{              
						canvas.drawText(str[startChar+i*characterEachLine+j]+"", textStartX-i*characterSpanX, textStartY+j*characterSpanY, paint);
					}
				}
				else                                                      //���������һ��
				{                           
					for(int j=0;j<characterEachLine;j++)                  //ÿһ�е�����
					{                       
						canvas.drawText(str[startChar+i*characterEachLine+j]+"", textStartX-i*characterSpanX, textStartY+j*characterSpanY, paint);
					}					
				}				
			}			
			break;
		default:
			break;
		}
	}
    
    
	//��������ʼ������
	public void initSound(TankActivity father)                            //initSound�������ڹ��췽���б�����
	{                        
		soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,100);       //��ʼ��soundPool,����ֱ�Ϊ����ͬʱ���ŵ��������Ϊһ�����ڶ��������ͣ����������
		soundMap = new HashMap<Integer,Integer>();                        //��ʼ�����������Դ�Ĺ�ϣ��
		int id1= soundPool.load(father, R.raw.welcome_background, 1);     //Ϊÿһ����Ҫ�������趨id
		int id2 = soundPool.load(father, R.raw.game_press, 2);
		int id3= soundPool.load(father, R.raw.game_background, 3);
		soundMap.put(1, id1);                                             //���������أ���id�Ŷ�Ӧ���������ڵ���
		soundMap.put(2, id2);	
		soundMap.put(3, id3);	
	}
    //��������������
	public void playSound(int sound,int loop)                           //��дϵͳ�Ĳ��������ķ�������һ�����������ֵ�id��
	                                                                    //�ڶ����ǲ��Ŵ���-1Ϊ����ѭ����0Ϊһ�Σ�1Ϊ���Σ��Դ�����
	{                    
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		float volume = streamVolumeCurrent / streamVolumeMax;   	    
		soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);//ϵͳ�Ĳ��������ķ���
	}
	
				
    //������ֹͣ����
	public void stopSound(int sound)                                    //��дֹͣ�����ķ�������Ҫĸ���ǿ���ͨ���������Դ��id
	{                   
		soundPool.stop(soundMap.get(sound));                            //ϵͳֹͣ��������
	}	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//��д�ӿڷ���
	{}
	
				
    public void surfaceCreated(SurfaceHolder holder)                    //��дsurfaceCreated���� 
    {
    	wdt.isDrawOn = true;                                            //���Ʊ�־��Ϊtrue
		if(!wdt.isAlive())                                              //����߳�û������
		{                  
			wdt.start();                                                //����
		}
		wt.flag = true;                                                 //WelcomeThread3��ѭ����־��Ϊtrue
		if(!wt.isAlive())                                               //����߳�û������
		{                  
			wt.start();                                                 //������
		}		
	}
    
				
	public void surfaceDestroyed(SurfaceHolder holder)                  //��дsurfaceDestroyed����
	{
		wdt.isDrawOn = false;
	}
	
	
	//************WelcomeView�и�������Ӧ*************************************************************
	public boolean myTouchEvent(int x,int y)                            //��������Ϸ��ʱ�������ѡ��
	{
		if(ifsound)                                                     //�˽���Ĵ�����־Ϊtrueʱ����Ч
		{
			if(rectSoundMenu1.contains(x,y))                            //������ǡ�������
			{	
				playSound(2,0);                                         //���Ű�����
				playSound(1,-1);                                        //ѭ��������
				wantSound=true;                                         //�������ر�־Ϊtrue
			    father.vib.vibrate(new long[]{100,10,100,70},-1);       //��
				status=7;                                               //����WelcomeView3�е�case 7
			}
			else if(rectSoundMenu2.contains(x,y))                       //�����������
			{
				wantSound=false;                                        //�������ر�־Ϊfalse
				father.vib.vibrate(new long[]{100,10,100,70},-1);       //��
				status=7;                                               //ͬ��Ҳ����WelcomeView3�е�case 7
			}
		}
		//�����Ļѡ��
		if(skip)                                                        //�˽���Ĵ�����־Ϊtrueʱ����Ч
		{
			if(rectSkip.contains(x,y))                                  //�����˹涨�ġ��������
			{	
				if(wantSound)                                           //���������־Ϊtrue
				{ 
					playSound(2,0);                                     //���Ű�����
				}
				father.vib.vibrate(new long[]{100,10,100,70},-1);       //��
				status=7;                                               //��WelcomeView3�е�case  7
			}	
		}	
					 
		//���˵�ѡ��
		if(rectStart.contains(x, y)&&status==7)                         //����ˡ���ʼ�������ҵ�ǰ�Ļ���ʵ�������ܽ��棬��ʼ�µ���Ϸ
		{
			if(wantSound)                                               //����������ر�־Ϊtrue��
			{
				playSound(2,0);                                         //���Ű�����
				playSound(2,0);
			}
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //��
			GameView3.stage=0;                                          //������ʼ��Ϸ�� ����һ�ؿ���ʼ  
			father.myHandler.sendEmptyMessage(7);                       //��TankActivity�е�handler�е�case 7
		}
		else if(rectContinue.contains(x, y)&&status==7)                 //����ˡ����������ҵ�ǰ�Ļ���ʵ�������ܽ��棬������Ϸ
		{
			if(wantSound)                                               //�����ȷ
				playSound(2,0);                                         //���Ű�����
			father.myHandler.sendEmptyMessage(1);                       //��TankActivity�е�handler�е�case 1
		}
		else if(rectHero.contains(x, y)&&status==7)                     //����ˡ�Ӣ�۰������ҵ�ǰ�Ļ���ʵ�������ܽ��棬Ӣ�۰����
		{
			if(wantSound)                                               //�����ȷ
				playSound(2,0);                                         //���Ű�����
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //��
			father.myHandler.sendEmptyMessage(2);                       //��TankActivity�е�handler�е�case 2
						
		}
		else if(rectHelp.contains(x, y)&&status==7)                     //����ˡ����������ҵ�ǰ�Ļ���ʵ�������ܽ��棬�������
		{
			HelpView3.pageNumber=1;                                     //ҳ���ʼΪ1
			if(wantSound)                                               //�����ȷ
				playSound(2,0);                                         //���Ű�����
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //��
			father.myHandler.sendEmptyMessage(3);                       //��TankActivity�е�handler�е�case 3
		}
		else if(rectExit.contains(x, y)&&status==7)                     //����ˡ��˳��������ҵ�ǰ�Ļ���ʵ�������ܽ��棬�˳�����
		{	
			if(wantSound)                                               //�����ȷ
				playSound(2,0);                                         //���Ű�����
			father.vib.vibrate(new long[]{100,10,100,70},-1);           //��
			AlertDialog.Builder builder = new AlertDialog.Builder(father);//����һ���Ի���
			builder.setIcon(R.drawable.icon);                           //����Сͼ��
			builder.setTitle("    Quit?");                             //����
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() //��ť���ǡ�
			{            
				public void onClick(DialogInterface dialog, int which)  //������ǡ�
				{	      					
					father.myHandler.sendEmptyMessage(5);               //��TankActivity�е�handler�е�case 5
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() //��ť����
			{             
				public void onClick(DialogInterface dialog, int which)  //�������"
				{            
					status=7;                                           //�ص������ܽ��棬Ҳ����WelcomeView3�е�cas 7
				}
			});
			builder.create();                                           //��������ʾ
			builder.show();
			ifsound=false;                                              //����Ϊfalse
			skip=false;
		}
		return true;
	}
	
	
	public void musicDialogOpen()                                       //��������ѡ��Ի����еĿ�����ťʱ����
	{
		playSound(2,0);                                                 //���Ű�����
		if(!wantSound&&!play)                                           //����������������ǹرյ���play��־Ϊfalse����һ�ιرհ�ť��play���Ϊfalse��
		{
			play=true;                                                  //����������Ϊtrue
			if(father.currentView==this)                                //�����WelcomeView3
		    {
				stopSound(2);                                           //ֹͣ������
				stopSound(2);
				playSound(1,0);                                         //���Ż�ӭ�����
			}
			else if(father.currentView==father.gv)                      //ת����String���ͺ󣬽�ȡ���й�һ�Σ������GameView3
				playSound(3, 0);                                        //������Ϸ������
		}
		wantSound=true;
	}
	
	
    public void musicDialogClose()                                      //��������ѡ��Ի����еĹرհ�ťʱ����            
	{
    	if(wantSound)                                                   //������������ǿ�����
		{
    		playSound(2,0);                                             //�������ΰ�����
			playSound(2,0);
			if(father.currentView==this)                                //��ǰView�����WelcomeView3
				father.wv.stopSound(1);                                 //ֹͣ��ӭ����ı�����Ч
			else if(father.currentView==father.gv)                      //��ǰView�����GameView3
				father.gv.stopSound(1);                                 //ֹͣ��Ϸ������
		}
    	wantSound=false;                                                //��Ϸ���ر�־��ΪFalse
		play=false;                                                     //play��Ϊfalse
	}

}
