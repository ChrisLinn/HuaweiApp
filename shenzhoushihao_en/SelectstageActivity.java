 
package com.example.shenzhoushihao;                //���������

//���������
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * ѡ�ذ�ť��
 *
 */

public class SelectstageActivity extends Activity
{
	private Button bt_toShenzhoushihaoActivity;    //��һ��ѡ��BUTTON
	private Button bt_toPlaneActivity;             //�ڶ���ѡ��BUTTON
	private Button bt_toTankActivity;              //������ѡ��BUTTON
	
  
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.selectstage);      //���ò���Ϊselectstage
      
        bt_toShenzhoushihaoActivity=(Button)findViewById(R.id.bt_toShenzhoushihaoActivity);//ǿ������ת��
        bt_toPlaneActivity=(Button)findViewById(R.id.bt_toPlaneActivity);
        bt_toTankActivity=(Button)findViewById(R.id.bt_toTankActivity);
      
        bt_toShenzhoushihaoActivity.setOnClickListener(new ClickEvent());//��BUTTON��������
        bt_toPlaneActivity.setOnClickListener(new ClickEvent());         //ͬ��
        bt_toTankActivity.setOnClickListener(new ClickEvent());          //ͬ��
    }
	

	class ClickEvent implements OnClickListener    //��������
	{
		public void onClick(View v)
		{
			//TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.bt_toShenzhoushihaoActivity: //ѡ���һ�أ����ƴ���
				// TODO Auto-generated method stub
  			    Intent intent1=new Intent();       //����һ��Intent
  		        intent1=intent1.setClass(SelectstageActivity.this,ShenzhoushihaoActivity.class);//����Intent
  		        SelectstageActivity.this.startActivity(intent1);//Activity����ת
  		        break;
  		                                           //ע����ͬ
  		    case R.id.bt_toPlaneActivity:          //ѡ��ڶ��أ�̫������
  			     // TODO Auto-generated method stub
  			     Intent intent2=new Intent();
  		         intent2=intent2.setClass(SelectstageActivity.this,PlaneActivity.class);
  		         SelectstageActivity.this.startActivity(intent2);
  			     break;
  		    case R.id.bt_toTankActivity:           //ѡ������أ�̫�վѻ�
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
