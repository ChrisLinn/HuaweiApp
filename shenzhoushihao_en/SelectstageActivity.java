 
package com.example.shenzhoushihao;                //声明包语句

//引入相关类
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * 选关按钮类
 *
 */

public class SelectstageActivity extends Activity
{
	private Button bt_toShenzhoushihaoActivity;    //第一关选关BUTTON
	private Button bt_toPlaneActivity;             //第二关选关BUTTON
	private Button bt_toTankActivity;              //第三关选关BUTTON
	
  
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.selectstage);      //设置布局为selectstage
      
        bt_toShenzhoushihaoActivity=(Button)findViewById(R.id.bt_toShenzhoushihaoActivity);//强制类型转换
        bt_toPlaneActivity=(Button)findViewById(R.id.bt_toPlaneActivity);
        bt_toTankActivity=(Button)findViewById(R.id.bt_toTankActivity);
      
        bt_toShenzhoushihaoActivity.setOnClickListener(new ClickEvent());//绑定BUTTON到监听器
        bt_toPlaneActivity.setOnClickListener(new ClickEvent());         //同上
        bt_toTankActivity.setOnClickListener(new ClickEvent());          //同上
    }
	

	class ClickEvent implements OnClickListener    //监听器类
	{
		public void onClick(View v)
		{
			//TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.bt_toShenzhoushihaoActivity: //选择第一关：蓄势待发
				// TODO Auto-generated method stub
  			    Intent intent1=new Intent();       //声明一个Intent
  		        intent1=intent1.setClass(SelectstageActivity.this,ShenzhoushihaoActivity.class);//发送Intent
  		        SelectstageActivity.this.startActivity(intent1);//Activity的跳转
  		        break;
  		                                           //注释下同
  		    case R.id.bt_toPlaneActivity:          //选择第二关：太空遨游
  			     // TODO Auto-generated method stub
  			     Intent intent2=new Intent();
  		         intent2=intent2.setClass(SelectstageActivity.this,PlaneActivity.class);
  		         SelectstageActivity.this.startActivity(intent2);
  			     break;
  		    case R.id.bt_toTankActivity:           //选择第三关：太空狙击
  			     // TODO Auto-generated method stub
  			     Intent intent3=new Intent();
  		         intent3=intent3.setClass(SelectstageActivity.this,TankActivity.class);
  		         SelectstageActivity.this.startActivity(intent3);
  		         break;
  		    default:
  				break;
  			}
		}
	}
}
