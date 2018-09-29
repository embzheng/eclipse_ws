package com.example.webviewhtml3;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView beginLoading,endLoading,loading,mtitle;
	WebView mWebView;
	Button btnGo;
    EditText edtUrl,edtDiff;
    Timer timer;
    int noticeid = 0;
    String htmlOld, htmlNow;
    ArrayList<String> diffAll;
    int new_html = 0;
    int start = 0;
    
    public int getNew_html() {
		return new_html;
	}

	public void setNew_html(int new_html) {
		this.new_html = new_html;
	}
	HtmlAnalyse analyse = new HtmlAnalyse();
    
    //news begin
    private List<News> newsList;
    private NewsAdapter adapter;
    private Handler handler, setTextHandler;
    private ListView lv;
    
    
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mWebView = (WebView) findViewById(R.id.webView1);
		beginLoading = (TextView) findViewById(R.id.text_beginLoading);
        endLoading = (TextView) findViewById(R.id.text_endLoading);
        loading = (TextView) findViewById(R.id.text_Loading);
        mtitle = (TextView) findViewById(R.id.title);
        edtUrl = (EditText) findViewById(R.id.edtUrl);
        edtDiff = (EditText) findViewById(R.id.edtDiff);
        btnGo = (Button) findViewById(R.id.btnGo);
        
        htmlOld = new String("string-htmlOld");
        htmlNow = new String("string-htmlNow");
        diffAll = new ArrayList<String>();
        
        //textview����
        edtDiff.setMovementMethod(ScrollingMovementMethod.getInstance());
        
        //get from file
        newsList = new ArrayList();
        //lv = (ListView) findViewById(R.id.news_lv);
        //getNews();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    adapter = new NewsAdapter(MainActivity.this,newsList);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);
                            Intent intent = new Intent(MainActivity.this,NewsDisplayActvivity.class);
                            Log.e("news.getNewsUrl()", news.getNewsUrl());
                            intent.putExtra("news_url",news.getNewsUrl());
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        
        //getBlog
        //getBlog();
        
        
        
        Log.e("aaa", "hello");
        
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        
		//����WebChromeClient��
		mWebView.setWebChromeClient(new WebChromeClient() {
            //��ȡ��վ����
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("����������");
                mtitle.setText(title);
            }
            //��ȡ���ؽ���
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    loading.setText(progress);
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                    loading.setText(progress);  
                }
            }
        });
        //����WebViewClient��
        mWebView.setWebViewClient(new WebViewClient() {
        	@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //���ü���ǰ�ĺ���
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("��ʼ������");
                beginLoading.setText("��ʼ������");

            }

            //���ý������غ���
            @Override
            public void onPageFinished(WebView view, String url) {
            	System.out.println("����������");
            	endLoading.setText("����������");
            	Log.e("process:", ""+view.getProgress());
                //sendSimplestNotificationWithAction("����������");
            	if(view.getProgress() == 100) {
            		view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                            "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            		super.onPageFinished(view, url);
            	}                
                
                
//                try   
//                {   
//                	Thread.currentThread().sleep(5000);//����   
//                }   
//                catch(Exception e){}  
                
            }
        });
		
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//����js����ֱ�Ӵ򿪴��ڣ���window.open()��Ĭ��Ϊfalse
        mWebView.getSettings().setJavaScriptEnabled(true);//�Ƿ�����ִ��js��Ĭ��Ϊfalse������trueʱ�������ѿ������XSS©��
        mWebView.getSettings().setSupportZoom(true);//�Ƿ�������ţ�Ĭ��true
        mWebView.getSettings().setBuiltInZoomControls(true);//�Ƿ���ʾ���Ű�ť��Ĭ��false
        mWebView.getSettings().setUseWideViewPort(true);//���ô����ԣ�������������š�����ͼģʽ
        mWebView.getSettings().setLoadWithOverviewMode(true);//��setUseWideViewPort(true)һ������ҳ����Ӧ����
        mWebView.getSettings().setAppCacheEnabled(true);//�Ƿ�ʹ�û���
        mWebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        
        mWebView.loadUrl("http://blog.sina.com.cn/s/articlelist_1216826604_0_1.html");		
        
        //��ý���
        mWebView.requestFocus();
        
        btnGo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mWebView.loadUrl(edtUrl.getText().toString());
						
				//edtUrl.setText("go go go");
				//SendBigNotification();
				//sendBigNotificationInboxStyle();
				//sendBigNotificationBigTextStyle("sendBigNotificationBigTextStyle text");
				
				//ArrayList<String> diff = new ArrayList<String>();
		        //diff.add("aa");
				//OpenDiffDisplayActvivity(diffAll);
				edtUrl.setText(mWebView.getUrl());
				
				if(start == 0) {
					// ��ʼ����ʱ��
			        timer = new Timer();
					start = 1;
					btnGo.setText("start");
					timer.schedule(new TimerTask() {
			            @Override
			            public void run() {
			                //Log.e("+++ mtitle", ""+new_html);
			            	List<String> lispP1 = null,lispP2 = null;
			            	//Log.e("new_html:", ""+new_html);
			            	if(new_html == 1)
			            	{
								if(htmlNow == null) {
									sendSimplestNotificationWithAction("htmlNow is null!");
									return;
								} 
								//System.out.println("***htmlNow:"+htmlNow);
				                //System.out.println("+++htmlOld:"+htmlOld); 	                	
								
			                	
				        		getDiffProc2(htmlOld, htmlNow);
				        		
				        		//update textView
				        		Message message = new Message();  
			                    message.what = 1;  
			                    setTextHandler.sendMessage(message);	        		
				        		htmlOld = htmlNow; 
			                	new_html = 0;
			            	}         
			            	//loadurl
			            	mWebView.post(new Runnable() {
			                    @Override
			                    public void run() {
			                    	mWebView.loadUrl(edtUrl.getText().toString());
			                    }
			            	});
			            }
			        }, 1000, 30000);/* �ӳ�1�뿪ʼִ�У�ÿx��ִ��һ�� */
				} else if(start == 1) {
					btnGo.setText("stop");
					start = 0;
					timer.cancel();
				}
				
			}
		});
     // Handler�첽��ʽ����ͼƬ  
        setTextHandler = new Handler() {  
            public void handleMessage(android.os.Message msg) { 
                switch (msg.what) {  
                case 1:  
                    // ���سɹ�  
                	for(int i = 0;i < diffAll.size();i++){
                		//System.out.println(diff.get(i));  
                		edtDiff.getText().append(diffAll.get(i));
                		edtDiff.setSelection(edtDiff.getText().length(), edtDiff.getText().length());  
                    } 
                	//mtitle.setText(StringUtil.ListToString(diffAll));
                	//((EditText) mtitle).setSelection(mtitle.getText().length(), mtitle.getText().length());  
                    break;  
                }  
            };  
        };  
        
        
	}
	
	/**
    * ����һ�������ת��MainActivity����Ϣ
    */
    private void sendSimplestNotificationWithAction(String text) {
       //��ȡNotificationManagerʵ��
 	   NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       //��ȡPendingIntent
       Intent mainIntent = new Intent(this, MainActivity.class);
       PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       //���� Notification.Builder ����
       NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
               .setSmallIcon(R.drawable.ic_launcher)
               //���֪ͨ���Զ����
               .setAutoCancel(true)
               .setContentTitle("���Ǵ�Action��Notification")
               .setContentText(Integer.toString(noticeid) + ":" +text)
               .setContentIntent(mainPendingIntent)
               .setDefaults(Notification.DEFAULT_ALL);
		       
       //����֪ͨ
       mNotifyManager.notify(noticeid++, builder.build());
    }
    
    private void sendBigNotificationInboxStyle() {
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle��ʾʾ��");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app.NotificationCompat.InboxStyle();
        style.setBigContentTitle("BigContentTitle")
                .addLine("��һ�У���һ�У���һ�У���һ�У���һ�У���һ�У���һ��")
                .addLine("�ڶ���")
                .addLine("������")
                .addLine("������")
                .addLine("������")
                .setSummaryText("SummaryText");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);
    }
    private void sendBigNotificationBigTextStyle(String text) {
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this); 
    	builder.setContentTitle("setContentTitle"); 
    	builder.setContentText(text); 
    	builder.setSmallIcon(R.drawable.ic_launcher); 
    	builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher)); 
    	NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(); 
    	style.bigText(text); 
    	style.setBigContentTitle("setBigContentTitle"); 
    	//SummaryTextûʲô�� ���Բ����� 
    	style.setSummaryText("setSummaryText"); 
    	builder.setStyle(style); 
    	builder.setAutoCancel(true); 
    	//Intent intent = new Intent(this,MainActivity.class); 
    	//PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0); 
    	//builder.setContentIntent(pIntent); 
    	builder.setDefaults(Notification.DEFAULT_ALL); 
    	Notification notification = builder.build(); 
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);
    }
    private void getNews(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //��ȡ��������20ҳ�����ݣ���ַ��ʽΪ��https://voice.hupu.com/nba/�ڼ�ҳ
                    for(int i = 1;i<=2;i++) {

                        Document doc = Jsoup.connect("https://voice.hupu.com/nba/" + Integer.toString(i)).get();
                        Elements titleLinks = doc.select("div.list-hd");    //��������ȡÿ�����ŵı��������ӵ�ַ
                     // Elements descLinks = doc.select("div.list-content");//��������ȡÿ�����ŵļ��
                        Elements timeLinks = doc.select("div.otherInfo");   //��������ȡÿ�����ŵ�ʱ������Դ
                        Log.e("title",Integer.toString(titleLinks.size()));
                        for(int j = 0;j < titleLinks.size();j++){
                            String title = titleLinks.get(j).select("a").text();
                            String uri = titleLinks.get(j).select("a").attr("href");
                       //   String desc = descLinks.get(j).select("span").text();
                            String time = timeLinks.get(j).select("span.other-left").select("a").text();
                            News news = new News(title,uri,null,time);
                            newsList.add(news);
                        }
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
	}
    private void getBlog() {
    	new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                	HtmlAnalyse analyse = new HtmlAnalyse();
                	List<String> diff = null; 
        
                	try {
                		diff = analyse.getListPFromUrl("https://blog.csdn.net/lixpjita39/article/details/76204379");
            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                	
                	if(diff == null) {
                		System.out.println("getDiffF faile,is null!"); 
                		sendSimplestNotificationWithAction("getDiffF faile,is null!");
                		return;
                	}
                	sendSimplestNotificationWithAction("getDiffF:" + diff.size());
                	for(int i = 0;i < diff.size();i++){
                		System.out.println(diff.get(i)); 
                		sendSimplestNotificationWithAction("diff:"+diff.get(i));
                		if(diff.get(i).indexOf("��λ") != -1) {
                			System.out.println("spec:"+diff.get(i)); 
                			//sendSimplestNotificationWithAction("************spec:"+diff.get(i));
                		}
                    }     
                    System.out.println("Thank God!"); 

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    	
    }
    
    private void getDiffProc(final String htmlOld, final String html) {
    	new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                	
                	List<String> diff = null; 
                	//sendSimplestNotificationWithAction("getDiffProc run");
                	try {
                		diff = analyse.getDiffFromHtml(htmlOld, html);
            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                	
                	if(diff == null) {
                		System.out.println("getDiffF faile,is null!"); 
                		sendSimplestNotificationWithAction("getDiffF faile,is null!");
                		return;
                	}
                	//sendSimplestNotificationWithAction("getDiffF:" + diff.size());
                	for(int i = 0;i < diff.size();i++){
                		System.out.println(diff.get(i)); 
                		sendSimplestNotificationWithAction("diff:"+diff.get(i));
                		if(diff.get(i).indexOf("��λ") != -1) {
                			System.out.println("spec:"+diff.get(i)); 
                			sendSimplestNotificationWithAction("��λ��Ϣ��"+diff.get(i));
                		}
                    }  
                	
                    System.out.println("Thank God!"); 

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    	
    }
    private void getDiffProc2(final String htmlOld, final String htmlNew) {
    	List<String> diff = null; 
    	
    	if(htmlOld.equals(htmlNew)) {
    		System.out.println("have not update!"); 
    		return;
    	}
    	try {
			diff = analyse.getDiffFromHtmlByBr(htmlOld, htmlNew);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sendSimplestNotificationWithAction("Exception e:"+e.getMessage());
			e.printStackTrace();
		}
    	if(diff == null) {
    		System.out.println("getDiffF faile,is null!"); 
    		sendSimplestNotificationWithAction("getDiffF faile,is null!");
    		return;
    	}
    	if(diff.size() == 0) {
    		System.out.println("getDiffF size is 0!"); 
    		sendSimplestNotificationWithAction("getDiffF size is 0!");
    		return;
    	}
    	//sendSimplestNotificationWithAction("getDiffF:" + diff.size());
    	for(int i = 0;i < diff.size();i++){
    		System.out.println(diff.get(i)); 
    		sendBigNotificationBigTextStyle(diff.get(i));
    		
    		diffAll.add(diff.get(i));
    		if(diff.get(i).indexOf("��λ") != -1) {
    			System.out.println("spec:"+diff.get(i)); 
    			//sendSimplestNotificationWithAction("��λ��Ϣ��"+diff.get(i));
    		}
        }  
    	
    	
        System.out.println("Thank God!");    	
    }
    /**
     * �߼�����JS����
     */
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
        	//sendSimplestNotificationWithAction("getSource");
        	new_html = 1;
        	//htmlNow = new String(html);
        	try {
        		htmlNow = analyse.getHtmlByClass(html, "item_hide");						
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//mtitle.setText(html);  
        	//Log.e("mtitle:", mtitle.getText().toString());
        	//Log.e("mtitle size:", ""+mtitle.getTextSize());
            //htmlNow = new String(html);
        	//System.out.println("****** InJavaScriptLocalObj htmlNow:"+html); 
        	//Log.e("****** InJavaScriptLocalObj htmlNow:", html);

        }
    }
    public void OpenDiffDisplayActvivity(ArrayList<String> diff){
        //�½�һ����ʽ��ͼ����һ������Ϊ��ǰActivity����󣬵ڶ�������Ϊ��Ҫ�򿪵�Activity��
        Intent intent =new Intent(MainActivity.this, DiffDisplayActvivity.class);
        //��BundleЯ������
        Bundle bundle=new Bundle();
        //����name����Ϊtinyphp
        
        bundle.putStringArrayList("diff_list", diff);
        intent.putExtras(bundle);
        startActivity(intent);       
    }
}