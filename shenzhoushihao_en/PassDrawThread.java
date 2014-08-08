 
package com.example.shenzhoushihao;                                            //���������

//���������
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
 * �������ڵ��͵�DrawThread�࣬�뱾��Ϸ�е�WelcomeDrawThread��TouchThread����ȫһ����ʵ�ֵĹ���Ҳ�ǿ���һ���̺߳�ͨ��
 * ����doDraw���������ػ�������Ļ������ǣ�浽��ͬ��View���̵߳�������ֹͣ��Ϊ�˷����̣��������û���������ϲ�
 *
 */

public class PassDrawThread extends Thread                                     //�̳�Thread
{                      
	PassView father;                                                           //GameView��������
	SurfaceHolder surfaceHolder;                                               //SurfaceHolder��������
	boolean flag;                                                              //ѭ�����Ʊ���
	boolean isDrawOn;                                                          //�ڲ�ѭ������
	
	public PassDrawThread(PassView father,SurfaceHolder surfaceHolder)         //���췽��
	{          
		super.setName("==PassDrawThread");                                     //��ʼ������
		this.father = father;                                                  //��ʼ����Ա����
		this.surfaceHolder = surfaceHolder;                                    //��ʼ����Ա����      
		flag = true;                                                           //��ʼ��ѭ�����Ʊ���Ϊtrue
		isDrawOn = true;                                                       //��ʼ���ڲ�ѭ������Ϊtrue
		}	
	
	public void run()                                                          //�߳�ִ�з���
	{                                                                          //�����߳�
		while(flag)                                                            //���ѭ��
		{                                                                     
			Canvas canvas = null;
			while(isDrawOn)                                                    //�ڲ�ѭ��
			{                    
				try
				{
					canvas = surfaceHolder.lockCanvas(null);                   //��������
					synchronized(surfaceHolder)
					{
						father.doDraw(canvas);                                 //������Ļ
						}				
				}
				catch(Exception e)
				{
					e.printStackTrace();
					}
				finally
				{                                                              //�ͷŻ���
					if(canvas != null)
					{                                                          //������ΪNULLʱ
						surfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
		}
	}


