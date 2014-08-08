 
package com.example.shenzhoushihao;                                            //���������

//���������
import android.app.ProgressDialog;          
import android.content.DialogInterface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ProgressView extends SurfaceView implements SurfaceHolder.Callback//�̳�SurfaceView��ʵ��SurfaceHolder��Callback�ӿ�
{
    ProgressDialog progress;                                                   //����������
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity������
    boolean flag;                                                              //�߳�ִ�б�־
    String title,message;                                                      //�������ı������Ϣ
    
    public ProgressView(final ShenzhoushihaoActivity father, final ProgressDialog progress)             //���췽��
    {         
	    super(father);                                                         //��ʼ������
		this.father = father;                                                  //��ʼ����Ա����
		this.progress=progress;                                                //��ʼ����Ա����
		flag=true;                                                             //��־Ĭ��Ϊtrue
		getHolder().addCallback(this);    
		progress.setMax(100);                                                  //���������ֵΪ100
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);            //ˮƽģʽ
		getMessage();                                                          //���������getMessage������ȡ������Ϣ
		progress.setButton("Skip", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				progress.dismiss();                                            //���ٽ�����
				father.myHandler.sendEmptyMessage(0);                          //����������������Ϸ���棬ִ��ShenzhoushihaoActivity�е�handler�е�case 0			
				flag=false;                                                    //����ѭ��
				}
			});
		progress.setTitle(title);                                              //���ñ���
		progress.setMessage(message);                                          //��������
		progress.setIcon(R.drawable.icon);                                     //�������Ͻǵ�Сͼ��
		progress.show();                                                       //��ʾ
		progress.setCancelable(false);                                         //�Ƿ���԰����ؼ�ȡ������ΪfalseΪ������
		
		
		new Thread()                                                           //һ���µ��߳�
		{
			public void run()                                                  //��������
			{
				int p=1;                                                       //��ʼ����Ϊ1
				while(flag)                                                    //flagΪtrueʱѭ��
				{
					p+=1;                                                      //һ�μ�һ����������㹻��ʱ�����������Ϣ
					progress.setProgress(p);                                   //���ý���
					if(p>=100)                                                 //�ﵽ���ֵʱ
					{
						progress.dismiss();                                    //���ٽ�����
						father.myHandler.sendEmptyMessage(0);                  //����������������Ϸ���棬ִ��ShenzhoushihaoActivity�е�handler�е�case 0			
						flag=false;                                            //����ѭ��
						}
					try
					{                                                          //�����쳣������
						Thread.sleep(100);
						}
					catch(InterruptedException e)
					{
						e.printStackTrace();
						}
					}
				}
			}.start();
			}
    public void getMessage()                                                   //���������ص���ʾ��Ϣ
    {
   	 switch(GameView.stage)                                                    //���ص����ֽ��ܣ�д�ڽ������Ի�����
   	 {                   
   	 case 0:
   			title="Misson 1:READY TO SHOOT";
   			message="To realize the China's space dream,the astronaut began his first misson passionately: prepare res for the shoot! Slip the ring at the bottom of the screen and release to catch items.";
   			break;
   			}
   	 }
    
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
    {}                                                                         //��д�ӿڷ���
    
    
	public void surfaceCreated(SurfaceHolder holder)                           //��д�ӿڷ�����View����ʱ������
	{}             
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                         //��д�ӿڷ�����View������ʱ����
	{}           
	

}
