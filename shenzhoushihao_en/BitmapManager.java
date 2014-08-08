 
package com.example.shenzhoushihao;                                            //���������

//���������
import android.content.res.Resources;		
import android.graphics.Bitmap;				
import android.graphics.BitmapFactory;		
import android.graphics.Canvas;				
import android.graphics.Paint;				


/*
 * �����ṩһЩ��̬�����ͳ�Ա����װ����Ϸ�õ�������ͼƬ��Դ����ֹ������й��ͳһ��������ά��
 *
 */

public class BitmapManager 
{
	private static Bitmap[] welcomePublic;                                     //���ϵͳ��Ҫ�õ���ͼƬ��Դ
	public static Bitmap[] currentStageResources;                              //�󱳾�����Ҫ�Ѽ�����Ʒ
	
	public static float[] level0X={0,40,200,45,170,90,150,160,20,90 ,40,200,45,170,90,150,160,20,90 };                 //��һ�صı�������ƷͼƬ��x����
	public static float[] level0Y={0, 160,80,30, 150,100,40 ,110,100,35 ,160,80,30, 150,100,40 ,110,100,35 };          //��һ�صı�������ƷͼƬ��y����
	public static boolean[] flag0={true,true,true,true,true,true,true,true,true,true,false,false,false,false,false,false,false,false,false};    //���Ʊ�־��true�����
	
	
	private BitmapManager(){}                                                  //�չ��췽��
	
	
	public static void loadCurrentStageResources(Resources r,int stage)        //����ͼƬ�ķ�������GameView�еĹ��췽���е���
	{
		switch(stage)
		{                                                                      //���ݹؿ��ż���ͼƬ
		case 0:                                                                //��һ��
					currentStageResources = new Bitmap[19];                    //�������ͼƬ�����鲢����ռ� 
					currentStageResources[0] = BitmapFactory.decodeResource(r, R.drawable.first_main);                 //��һ�س���
					currentStageResources[1] = BitmapFactory.decodeResource(r, R.drawable.bomb1);                      //ը��
					currentStageResources[2] = BitmapFactory.decodeResource(r, R.drawable.bomb1);                      //ը��
					currentStageResources[3] = BitmapFactory.decodeResource(r, R.drawable.bomb1);                      //ը��
					currentStageResources[4] = BitmapFactory.decodeResource(r, R.drawable.danyao);                     //��ҩ
					currentStageResources[5] = BitmapFactory.decodeResource(r, R.drawable.danyao);                     //��ҩ
					currentStageResources[6] = BitmapFactory.decodeResource(r, R.drawable.danyao);                     //��ҩ
					currentStageResources[7] = BitmapFactory.decodeResource(r, R.drawable.ranliao);                    //ȼ��
					currentStageResources[8] = BitmapFactory.decodeResource(r, R.drawable.ranliao);                    //ȼ��
					currentStageResources[9] = BitmapFactory.decodeResource(r, R.drawable.ranliao);                    //ȼ��
					
					currentStageResources[10] = BitmapFactory.decodeResource(r, R.drawable.first_fbomb);               //��Ȧ��ǰ���ը��
					currentStageResources[11] = BitmapFactory.decodeResource(r, R.drawable.first_mbomb);               //��Ȧ���м��ը��
					currentStageResources[12] = BitmapFactory.decodeResource(r, R.drawable.first_bbomb);               //��Ȧ������ը��
					currentStageResources[13] = BitmapFactory.decodeResource(r, R.drawable.first_fdanyao);             //��Ȧ��ǰ��ĵ�ҩ
					currentStageResources[14] = BitmapFactory.decodeResource(r, R.drawable.first_mdanyao);             //��Ȧ���м�ĵ�ҩ
					currentStageResources[15] = BitmapFactory.decodeResource(r, R.drawable.first_bdanyao);             //��Ȧ�����ĵ�ҩ
					currentStageResources[16] = BitmapFactory.decodeResource(r, R.drawable.first_mlranliao);           //��Ȧ���м��ȼ��
					currentStageResources[17] = BitmapFactory.decodeResource(r, R.drawable.first_mrranliao);           //��Ȧ������ȼ��
					currentStageResources[18] = BitmapFactory.decodeResource(r, R.drawable.first_branliao);            //��Ȧ������ȼ��
					break;
					default:
						break;
						}
			 }


public static void loadWelcomePublic(Resources r)//���ػ�ӭ����ͼƬ
{                                                                                                                      //���ػ�ӭ�����ͼƬ
	welcomePublic = new Bitmap[13];
	welcomePublic[0] = BitmapFactory.decodeResource(r, R.drawable.welcome_back);                                       //��ӭ���汳��
	welcomePublic[1] = BitmapFactory.decodeResource(r, R.drawable.ipad);                                               //ipad������Ϊ�������ֵı���
	welcomePublic[2] = BitmapFactory.decodeResource(r, R.drawable.welcome_menu);                                       //���˵�
	welcomePublic[3] = BitmapFactory.decodeResource(r, R.drawable.welcome_sound);                                      //������ر������Ľ��汳��
	welcomePublic[6] = BitmapFactory.decodeResource(r, R.drawable.skip);                                               //��������������
	welcomePublic[7] = BitmapFactory.decodeResource(r, R.drawable.txt_1);                                              //����ʼ��
	welcomePublic[8] = BitmapFactory.decodeResource(r, R.drawable.txt_2);                                              //��������
	welcomePublic[9] = BitmapFactory.decodeResource(r, R.drawable.txt_3);                                              //��Ӣ�۰�
	welcomePublic[10] = BitmapFactory.decodeResource(r, R.drawable.txt_4);                                             //��������
	welcomePublic[11] = BitmapFactory.decodeResource(r, R.drawable.txt_5);                                             //�����ڡ�
	welcomePublic[12] = BitmapFactory.decodeResource(r, R.drawable.txt_6);                                             //���˳���
}


public static void drawWelcomePublic(int imgId,Canvas canvas,int x,int y,Paint paint)                                  //����������ͼƬID����ϵͳ��ӭ����ͼƬ
{
	canvas.drawBitmap(welcomePublic[imgId], x, y, paint);
	}
}

