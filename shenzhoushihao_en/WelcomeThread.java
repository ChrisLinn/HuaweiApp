 
package com.example.shenzhoushihao;                                            //���������

//���������
import android.graphics.Color; 

@SuppressWarnings("unused")
public class WelcomeThread extends Thread                                      //�̳���Thread��    
{                       
	@SuppressWarnings("unchecked")
	WelcomeView father;                                                        //WelcomeView�Ķ�������
	boolean flag;                                                              //�߳�ִ�б�־
	int sleepSpan = 50;                                                        //����ʱ��
	int characterCounter=0;	                                                   //ִ��3��ѭ�����Ż��һ����
	int charNumber=0;                                                          //���������ļ�����
	
	@SuppressWarnings("unchecked")
	public WelcomeThread(WelcomeView father)                                   //���췽��
	{          
		this.father = father;                                                  //��ʼ����Ա����
		flag = true;                                                           //Ĭ��True���߳�ִ�б�־
		}
	
	@SuppressWarnings("unchecked")
	public void run()                                                          //�̵߳�ִ�з���
	{                                                                          //run����
		while(flag)
		{                                                                      //��־Ϊtrueʱ
			switch(father.status)                                              //switch  WelcomeView�е�status 
			{        
			case 0:                                                            //������ʾ����ͼƬ
				father.alpha -=2;                                              //�𽥱䰵��ʧ
				if(father.alpha <0)                                            //��ȫ��ʧʱ
				{             
					father.status =8;                                          //����WelcomeView�е���ʾ��������ѡ�����
					father.alpha=255;                                          //��Ļ���ȵ�����
					}
				break;
		     case 8:                                                           //��ִ���κβ���
				break;
			 case 1:                                                           //���ڹ�����Ļ
				father.screenX -=20;                                           //ÿִ��һ��������20
				if(father.screenX<=0)                                          //�Ƶ�һ��λ��ʱ
				{           
					father.status = 3;                                         //��ʼ��ʾ����
					}
			    break;
			 case 3:                                                           //��ʾ����
				 characterCounter++;                                           //ÿִ��һ�μ�������һ
				 if(characterCounter == 3)                                     //������ʱ
				 {                  
					 characterCounter = 0;                                     //����
					 father.characterNumber++;                                 //WelcomeView����ʾ��������1
					 charNumber++;                                             //д�ּ�������1
					 if(charNumber == father.str.length)                       //����ȫ����ʾ���ʱ
					 {	  			
						 father.status = 7;                                    //����WelconeView��case 7�����������ܽ���
						 }					
					 }
				 break;
			 default:
				 break;					
				 }
			
				try
				{
					Thread.sleep(sleepSpan);     //�߳�����
					}										
				catch(Exception e)
				{
					e.printStackTrace();
					}                            //���񲢴�ӡ�쳣
				}
		}
	}

