 
package com.example.shenzhoushihao;                 //���������

public class PassThread extends Thread              //�̳�Thread��
{                   
	PassView father;                                //PassView�Ķ�������
	boolean flag;                                   //ѭ����־
	int sleepSpan = 50;                             //����ʱ��,50����
	
	public PassThread(PassView father)              //���췽��
	{                   
		this.father = father;                       //��ʼ����Ա����
		flag = true;                                //ѭ����־Ĭ��Ϊtrue
		}
	
	public void run()                               //�̵߳�ִ�з���
	{                                               //�����߳�
		while(flag)                                 //��־Ϊtrueʱ
		{                    
			if(GameView.stage==0)
			{                                       //���ؽ���ʱ
				father.leftX +=5;                   //����Ļ�������仯��+5���γ�����������������м�Ʈ
			    father.rightX-=5;                   //ÿѭ��һ��-5
		    }
			if(father.father.gv.nextStage)          //����ǹ��ؽ��������ʧ�ܽ���
			{
				switch(father.stage)                //PassView�е�stage
				{
				case 0:                             //��һ��
					if(father.leftX==95)            //��ߵ���Ʒ�����м�ʱ
						father.stage=6;             //ִ��PassView�е�case 6��Ҳ���ǻ������к����Ʒ
					break;
				default:
					break;
					}
				try
				{
					Thread.sleep(sleepSpan);
					}								//�߳�����
			    catch(Exception e)
			    {
			    	e.printStackTrace();
			    	}							    //���񲢴�ӡ�쳣
			    }                                   //����if���
			}                                       //ѭ��
		}                                           //����
	}                                               //��
