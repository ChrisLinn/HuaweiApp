 
package com.example.shenzhoushihao;                          //���������

//���������
import android.graphics.Canvas;           
import android.view.SurfaceHolder;

/*
 * �������ڵ��͵�DrawThread�࣬�뱾��Ϸ�е�TouchDrawThread3��PassDrawThread3����ȫһ����
 * ʵ�ֵĹ���Ҳ�ǿ���һ���̺߳�ͨ������doDraw���������ػ�������Ļ������ǣ�浽��ͬ��View��
 * �̵߳�������ֹͣ��Ϊ�˷����̣��������û�������ϲ���
 * 
 */

public class WelcomeDrawThread3 extends Thread               //�̳�Thread
{
	@SuppressWarnings("unchecked")
	WelcomeView3 father;                                     //WelcomeView3��������
	SurfaceHolder surfaceHolder;                             //SurfaceHolder��������
	boolean flag;                                            //ѭ�����Ʊ���
	boolean isDrawOn;                                        //�ڲ�ѭ������
	int sleepSpan = 80;                                      //˯��ʱ��
	
	@SuppressWarnings("unchecked")
	public WelcomeDrawThread3(WelcomeView3 father,SurfaceHolder surfaceHolder)//���췽��
	{                 
		super.setName("==WelcomeDrawThread");                //��ʼ������
		this.father = father;                                //��ʼ����Ա����	
		this.surfaceHolder = surfaceHolder;                  //��ʼ����Ա����
		flag = true;                                         //��ʼ��ѭ�����Ʊ���Ϊtrue
		isDrawOn = true;                                     //��ʼ���ڲ�ѭ������Ϊtrue
	}
	
	
	//�߳�ִ�з���
	public void run()                                        //�����߳�
	{                  	
		while(flag)                                          //���ѭ��
		{                 
			Canvas canvas = null;
			while(isDrawOn)
			{                                                //�ڲ�ѭ��	
				try
				{
					canvas = surfaceHolder.lockCanvas(null); //��������
					synchronized(surfaceHolder)
					{
						father.doDraw(canvas);               //������Ļ
					}				
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{                                            //�ͷŻ���
					if(canvas != null)
					{                                        //������ΪNULLʱ
						surfaceHolder.unlockCanvasAndPost(canvas);//�ͷ�
					}
				}
				try
				{                                            //�߳�˯�߱������try catch
					Thread.sleep(sleepSpan);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			try
			{                                                //���ѭ��˯�ߣ��߳�˯�߱������try catch
				Thread.sleep(2000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
