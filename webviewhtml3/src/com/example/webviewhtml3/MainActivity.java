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
        
        //textview滚动
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
        
		//设置WebChromeClient类
		mWebView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                mtitle.setText(title);
            }
            //获取加载进度
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
        //设置WebViewClient类
        mWebView.setWebViewClient(new WebViewClient() {
        	@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");
                beginLoading.setText("开始加载了");

            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
            	System.out.println("结束加载了");
            	endLoading.setText("结束加载了");
            	Log.e("process:", ""+view.getProgress());
                //sendSimplestNotificationWithAction("结束加载了");
            	if(view.getProgress() == 100) {
            		view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                            "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            		super.onPageFinished(view, url);
            	}                
                
                
//                try   
//                {   
//                	Thread.currentThread().sleep(5000);//毫秒   
//                }   
//                catch(Exception e){}  
                
            }
        });
		
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mWebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        mWebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        
        mWebView.loadUrl("http://blog.sina.com.cn/s/articlelist_1216826604_0_1.html");		
        
        //获得焦点
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
					// 初始化定时器
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
			        }, 1000, 30000);/* 延迟1秒开始执行，每x秒执行一次 */
				} else if(start == 1) {
					btnGo.setText("stop");
					start = 0;
					timer.cancel();
				}
				
			}
		});
     // Handler异步方式下载图片  
        setTextHandler = new Handler() {  
            public void handleMessage(android.os.Message msg) { 
                switch (msg.what) {  
                case 1:  
                    // 下载成功  
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
    * 发送一个点击跳转到MainActivity的消息
    */
    private void sendSimplestNotificationWithAction(String text) {
       //获取NotificationManager实例
 	   NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       //获取PendingIntent
       Intent mainIntent = new Intent(this, MainActivity.class);
       PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       //创建 Notification.Builder 对象
       NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
               .setSmallIcon(R.drawable.ic_launcher)
               //点击通知后自动清除
               .setAutoCancel(true)
               .setContentTitle("我是带Action的Notification")
               .setContentText(Integer.toString(noticeid) + ":" +text)
               .setContentIntent(mainPendingIntent)
               .setDefaults(Notification.DEFAULT_ALL);
		       
       //发送通知
       mNotifyManager.notify(noticeid++, builder.build());
    }
    
    private void sendBigNotificationInboxStyle() {
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle演示示例");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app.NotificationCompat.InboxStyle();
        style.setBigContentTitle("BigContentTitle")
                .addLine("第一行，第一行，第一行，第一行，第一行，第一行，第一行")
                .addLine("第二行")
                .addLine("第三行")
                .addLine("第四行")
                .addLine("第五行")
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
    	//SummaryText没什么用 可以不设置 
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
                    //获取虎扑新闻20页的数据，网址格式为：https://voice.hupu.com/nba/第几页
                    for(int i = 1;i<=2;i++) {

                        Document doc = Jsoup.connect("https://voice.hupu.com/nba/" + Integer.toString(i)).get();
                        Elements titleLinks = doc.select("div.list-hd");    //解析来获取每条新闻的标题与链接地址
                     // Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
                        Elements timeLinks = doc.select("div.otherInfo");   //解析来获取每条新闻的时间与来源
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
                		if(diff.get(i).indexOf("仓位") != -1) {
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
                		if(diff.get(i).indexOf("仓位") != -1) {
                			System.out.println("spec:"+diff.get(i)); 
                			sendSimplestNotificationWithAction("仓位信息："+diff.get(i));
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
    		if(diff.get(i).indexOf("仓位") != -1) {
    			System.out.println("spec:"+diff.get(i)); 
    			//sendSimplestNotificationWithAction("仓位信息："+diff.get(i));
    		}
        }  
    	
    	
        System.out.println("Thank God!");    	
    }
    /**
     * 逻辑处理JS操作
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
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent =new Intent(MainActivity.this, DiffDisplayActvivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        
        bundle.putStringArrayList("diff_list", diff);
        intent.putExtras(bundle);
        startActivity(intent);       
    }
}
