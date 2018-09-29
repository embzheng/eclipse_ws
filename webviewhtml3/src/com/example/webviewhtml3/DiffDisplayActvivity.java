package com.example.webviewhtml3;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;

public class DiffDisplayActvivity extends Activity{
	private String newsUrl;
	private EditText edtDiff;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff);
        //newsUrl = getIntent().getStringArrayListExtra("diff_list")
        //��ҳ���������
        Bundle bundle = this.getIntent().getExtras();
        //����nameֵ
        //String name = bundle.getString("name");
        ArrayList<String> diff = bundle.getStringArrayList("diff_list");
        //Log.e("��ȡ����nameֵΪ",diff.get(0));  
        
        edtDiff = (EditText) findViewById(R.id.edtNewDiff);
        //textview����
        edtDiff.setMovementMethod(ScrollingMovementMethod.getInstance());
        for(int i = 0;i < diff.size();i++){
    		//System.out.println(diff.get(i));  
    		edtDiff.getText().append(diff.get(i));
    		edtDiff.setSelection(edtDiff.getText().length(), edtDiff.getText().length());  
        }
    }
}
