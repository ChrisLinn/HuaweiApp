
package com.example.shenzhoushihao;                 //声明包语句

//引入相关类
import android.graphics.Bitmap;


public class BitmapData                             //管理图片的类
{                  
  Bitmap[] bit;                                     //每一关的图片
  float[] X;                                        //每幅图片的x坐标
  float[] Y;                                        //每幅图片的y坐标
  boolean[] flag;                                   //图片是否显示
  
public BitmapData(Bitmap[] bit1,float[] X1,float[] Y1,boolean[] flag1)//类的构造方法
{ 
	 this.bit=bit1;                                 //初始化成员变量
	 this.X=X1;
	 this.Y=Y1;
	 this.flag=flag1;
	 }
}