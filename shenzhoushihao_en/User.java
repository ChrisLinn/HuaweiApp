 
package com.example.shenzhoushihao;              //声明包语句

/*
 * 该类为用户类
 *
 */

public class User                                //自定义的用户类
{                	
	String userName;                             //用户名
	int grade;                                   //分数
	User(){}                                     //无参空构造方法
	User(String s,int n)                         //带所有参数的构造方法
	{
		userName=s;
		grade=n;
	}
}
