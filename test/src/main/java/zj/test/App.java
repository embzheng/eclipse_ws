package zj.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.log.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import junit.framework.Assert;

/**
 * Hello world!
 *
 */
public class App 
{
	static List<String> listLast;
    public static void main( String[] args )
    {
    	//要切割的字符串
//    	 String  s  = "123.jpg,113.jpg,121.jpg,122.jpg,131.jpg"; 
//    	 String  sub =  "";
//    	 System.out.println("编译前："+s);
//    	 //调用方法
//    	 s = s.replaceAll( ",113.jpg|113.jpg,","");
//    	 System.out.println("编译后："+s);

        
        System.out.println( "Hello World!" );
        try {
        	getBlog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Thank God!");  
    }
    public static void getBloglist() throws Exception {  
        /**HtmlUnit请求web页面*/  
        WebClient wc = new WebClient();  
        wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
        wc.getOptions().setCssEnabled(false); //禁用css支持  
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
        HtmlPage page = wc.getPage("http://blog.sina.com.cn/s/articlelist_1216826604_0_1.html");  
        String pageXml = page.asXml(); //以xml的形式获取响应文本  
        //System.out.println(pageXml.toString());
  
        /**jsoup解析文档*/  
        Document doc = Jsoup.parse(pageXml, "http://blog.sina.com.cn/");   
      
        //System.out.println(doc.toString());  
        
        Element articleList = doc.select("div.articleList").get(0);
        Elements titleLinks = articleList.getElementsByClass("articleCell");
        //System.out.println(titleLinks.toString());  
        System.out.println(Integer.toString(titleLinks.size()));
    
        for(int j = 0;j < titleLinks.size();j++){
            String title = titleLinks.get(j).select("span.atc_title").get(0).select("a").text();
            System.out.println("title:" + title);
            String uri = titleLinks.get(j).select("span.atc_title").get(0).select("a").attr("href");
            System.out.println("uri:" + uri);
       //   String desc = descLinks.get(j).select("span").text();
            String time = titleLinks.get(j).select("span.atc_tm").text();
            System.out.println("time:" + time);          
        }
  
        System.out.println("Thank God!");  
    }
    public static void getBlog() throws Exception {  
  	
    	List<String> diff = new ArrayList<String>();    	
    	List<String> lisCurrent = getListPFromUrl("http://blog.sina.com.cn/s/blog_48874cec0102y0qm.html");
    	
    	listLast = lisCurrent;
    	if(listLast.size() == 0) {
    		System.out.println("listLast.size() is 0,begin!");  
    		return;
    	}
    	
    	
    	if(listLast.size() >= lisCurrent.size()) {
    		System.out.println("listLast.size() is err:"+Integer.toString(listLast.size()));  
    		System.out.println("lisCurrent.size():"+Integer.toString(lisCurrent.size())); 
    		return;
    	}
    	for(int j = 0;j < listLast.size();j++){
    		System.out.println("listLast:"+listLast.get(j)); 
    		System.out.println("lisCurrent:"+lisCurrent.get(j)); 
            if(!listLast.get(j).equals(lisCurrent.get(j)) ) {
            	diff.add(lisCurrent.get(j));
            	lisCurrent.remove(j);
            	j--;
            }
        }
    	
    	for(int i = 0;i < diff.size();i++){
    		System.out.println(diff.get(i)); 
    		if(diff.get(i).indexOf("仓位") != -1) {
    			System.out.println("spec:"+diff.get(i)); 
    		}
        }
    	
    	
    	
//    	String filepath1 = System.getProperty("user.dir")+"\\div.area2.1.html";  
//    	String filepath2 = System.getProperty("user.dir")+"\\div.area2.2.html"; 
//    	
//    	List<String> lispP1 = getListPFromFile(filepath1);
//    	List<String> lispP2 = getListPFromFile(filepath2);
//    	
//    	if(lispP1.size() >= lispP2.size()) {
//    		System.out.println("lispP1.size() is err");  
//    	}
//    	for(int j = 0;j < lispP1.size();j++){
//    		System.out.println("p1:"+lispP1.get(j)); 
//    		System.out.println("p2:"+lispP2.get(j)); 
//            if(!lispP1.get(j).equals(lispP2.get(j)) ) {
//            	diff.add(lispP2.get(j));
//            	lispP2.remove(j);
//            	j--;
//            }
//        }
//    	for(int i = 0;i < diff.size();i++){
//    		System.out.println(diff.get(i)); 
//    		if(diff.get(i).indexOf("仓位") != -1) {
//    			System.out.println("spec:"+diff.get(i)); 
//    		}
//        }
    	
        
    }
    public static List<String> getListPFromUrl(String url) throws Exception{   	
    	
    	List<String> listP = new ArrayList<String>();
        /**HtmlUnit请求web页面*/  
    	WebClient wc = new WebClient();  
    	wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
    	wc.getOptions().setCssEnabled(false); //禁用css支持  
    	wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
    	wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
    	HtmlPage page = wc.getPage(url);  
    	String pageXml = page.asXml(); //以xml的形式获取响应文本  
    	//System.out.println(pageXml.toString());  
        Document doc = Jsoup.parse(pageXml, "UTF-8");   
      
        //System.out.println(doc.toString());  
        
        Element blogtext = doc.select("div#sina_keyword_ad_area2").get(0);
        Elements p = blogtext.select("p");
        //System.out.println(titleLinks.toString());  
        System.out.println("p size:"+Integer.toString(p.size())); 
        
        for(int j = 0;j < p.size();j++){
            listP.add(p.get(j).text());
        }
        return listP;
    }
    public static List<String> getListPFromFile(String filePath) throws Exception{   	
    	
    	File pageXml = new File(filePath);
    	List<String> listP = new ArrayList<String>();
        /**jsoup解析文档*/  
        Document doc = Jsoup.parse(pageXml, "UTF-8");   
      
        //System.out.println(doc.toString());  
        
        Element blogtext = doc.select("div#sina_keyword_ad_area2").get(0);
        Elements p = blogtext.select("p");
        //System.out.println(titleLinks.toString());  
        System.out.println(Integer.toString(p.size())); 
        
        for(int j = 0;j < p.size();j++){
            listP.add(p.get(j).text());
        }
        return listP;
    }
}
