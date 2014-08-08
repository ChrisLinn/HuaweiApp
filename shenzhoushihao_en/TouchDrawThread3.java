/****************************************************************************************************
*******************��ʮ����³�����ƴ���������Ʒ*****************************************************
********************          ����ʮ��                           *************************************
***********************  �й�ʯ�ʹ�ѧ��������     ****************************************************
************************* С �� �� ����С��     ******************************************************
************   �鳤����ѧ��        ��Ա�����յ�,���ټ�,�����,����      ********************************
******************************************************************************************************/
package com.example.shenzhoushihao;                                      //���������

//���������
import android.graphics.Canvas;           
import android.view.SurfaceHolder;

/*
 * �������ڵ��͵�DrawThread�࣬�뱾��Ϸ�е�WelcomeDrawThread3��PassDrawThread3����ȫһ����
 * ʵ�ֵĹ���Ҳ�ǿ���һ���̺߳�ͨ������doDraw���������ػ�������Ļ������ǣ�浽��ͬ��View��
 * �̵߳�������ֹͣ��Ϊ�˷����̣��������û���������ϲ�
 * 
 */

public class TouchDrawThread3 extends Thread                             //�̳�Thread
{                      
	GameView3 father;                                                    //GameView3��������
	SurfaceHolder surfaceHolder;                                         //SurfaceHolder��������
	boolean flag;                                                        //ѭ�����Ʊ���
	boolean isDrawOn;                                                    //�ڲ�ѭ������
	
	public TouchDrawThread3(GameView3 father,SurfaceHolder surfaceHolder)//���췽��
	{          
		super.setName("==TouchDrawThread3");                             //��ʼ������
		this.father = father;                                            //��ʼ����Ա����
		this.surfaceHolder = surfaceHolder;                              //��ʼ����Ա����      
		flag = true;                                                     //��ʼ��ѭ�����Ʊ���Ϊtrue
		isDrawOn = true;                                                 //��ʼ���ڲ�ѭ������Ϊtrue
	}
	//�߳�ִ�з���
	public void run()
	{                                                                    //�����߳�
		while(flag)
		{                                                                //���ѭ��
			Canvas canvas = null;
			while(isDrawOn)
			{                                                            //�ڲ�ѭ��
				try
				{
					canvas = surfaceHolder.lockCanvas(null);             //��������
					synchronized(surfaceHolder)
					{
						father.doDraw(canvas);                           //������Ļ
					}				
				}
				catch(Exception e)
				{
					e.printStackTrace();
			    }
				finally
				{                                                        //�ͷŻ���
					if(canvas != null)
					{                                                    //������ΪNULLʱ
						surfaceHolder.unlockCanvasAndPost(canvas);       //�ͷŻ���
					}
				}
			}
		}
	}
}
