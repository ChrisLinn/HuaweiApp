 
package com.example.shenzhoushihao;                 //���������

//���������
import android.graphics.Bitmap;


public class BitmapData                             //����ͼƬ����
{                  
  Bitmap[] bit;                                     //ÿһ�ص�ͼƬ
  float[] X;                                        //ÿ��ͼƬ��x����
  float[] Y;                                        //ÿ��ͼƬ��y����
  boolean[] flag;                                   //ͼƬ�Ƿ���ʾ
  
public BitmapData(Bitmap[] bit1,float[] X1,float[] Y1,boolean[] flag1)//��Ĺ��췽��
{ 
	 this.bit=bit1;                                 //��ʼ����Ա����
	 this.X=X1;
	 this.Y=Y1;
	 this.flag=flag1;
	 }
}