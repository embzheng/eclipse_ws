package com.example.webviewhtml3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlAnalyse {
	//private List<String> listLast;
	private List<String> diff;	
	
	public HtmlAnalyse() {
	//	listLast = new ArrayList<String>();
		diff = new ArrayList<String>();
	}
	
//public List<String> getDiff() throws Exception { 
//	List<String> lisCurrent = getListPFromHtmlunit();    	
//	if(lisCurrent.size() == 0) {
//		System.out.println("lisCurrent.size() is 0!"); 
//		return null;
//	}
//	if(listLast.size() == 0) {
//		System.out.println("listLast.size() is 0,begin!"); 
//		listLast = lisCurrent;
//		return null;
//	}    	
//	
//	if(listLast.size() >= lisCurrent.size()) {
//		System.out.println("listLast.size() is err:"+Integer.toString(listLast.size()));  
//		System.out.println("lisCurrent.size():"+Integer.toString(lisCurrent.size())); 
//		return null;
//	}
//	for(int j = 0;j < listLast.size();j++){
//		System.out.println("listLast:"+listLast.get(j)); 
//		System.out.println("lisCurrent:"+lisCurrent.get(j)); 
//      if(!listLast.get(j).equals(lisCurrent.get(j)) ) {
//      	diff.add(lisCurrent.get(j));
//      	lisCurrent.remove(j);
//      	j--;
//      }
//  }
//	listLast = lisCurrent;
//	return diff;  	
//}
//public List<String> getListPFromHtmlunit() throws Exception{ 
//	List<String> listP = new ArrayList<String>();
//  /**HtmlUnit请求web页面*/  
//	WebClient wc = new WebClient();  
//	wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
//	wc.getOptions().setCssEnabled(false); //禁用css支持  
//	wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
//	wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
//	HtmlPage page = wc.getPage("http://blog.sina.com.cn/s/blog_48874cec0102y0qm.html");  
//	String pageXml = page.asXml(); //以xml的形式获取响应文本  
//	//System.out.println(pageXml.toString());  
//  Document doc = Jsoup.parse(pageXml, "UTF-8");   
//
//  //System.out.println(doc.toString());          
//  listP = getListPFromDocument(doc);
//  return listP;
//}	
//	public void getBloglist() throws Exception {  
//        /**HtmlUnit请求web页面*/  
//        WebClient wc = new WebClient();  
//        wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
//        wc.getOptions().setCssEnabled(false); //禁用css支持  
//        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
//        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
//        HtmlPage page = wc.getPage("http://blog.sina.com.cn/s/articlelist_1216826604_0_1.html");  
//        String pageXml = page.asXml(); //以xml的形式获取响应文本  
//        //System.out.println(pageXml.toString());
//  
//        /**jsoup解析文档*/  
//        Document doc = Jsoup.parse(pageXml, "http://blog.sina.com.cn/");   
//      
//        //System.out.println(doc.toString());  
//        
//        Element articleList = doc.select("div.articleList").get(0);
//        Elements titleLinks = articleList.getElementsByClass("articleCell");
//        //System.out.println(titleLinks.toString());  
//        System.out.println(Integer.toString(titleLinks.size()));
//    
//        for(int j = 0;j < titleLinks.size();j++){
//            String title = titleLinks.get(j).select("span.atc_title").get(0).select("a").text();
//            System.out.println("title:" + title);
//            String uri = titleLinks.get(j).select("span.atc_title").get(0).select("a").attr("href");
//            System.out.println("uri:" + uri);
//       //   String desc = descLinks.get(j).select("span").text();
//            String time = titleLinks.get(j).select("span.atc_tm").text();
//            System.out.println("time:" + time);          
//        }
//  
//        System.out.println("Thank God!");  
//    }
//    public List<String> getDiff() throws Exception { 
//    	List<String> lisCurrent = getListPFromHtmlunit();    	
//    	if(lisCurrent.size() == 0) {
//    		System.out.println("lisCurrent.size() is 0!"); 
//    		return null;
//    	}
//    	if(listLast.size() == 0) {
//    		System.out.println("listLast.size() is 0,begin!"); 
//    		listLast = lisCurrent;
//    		return null;
//    	}    	
//    	
//    	if(listLast.size() >= lisCurrent.size()) {
//    		System.out.println("listLast.size() is err:"+Integer.toString(listLast.size()));  
//    		System.out.println("lisCurrent.size():"+Integer.toString(lisCurrent.size())); 
//    		return null;
//    	}
//    	for(int j = 0;j < listLast.size();j++){
//    		System.out.println("listLast:"+listLast.get(j)); 
//    		System.out.println("lisCurrent:"+lisCurrent.get(j)); 
//            if(!listLast.get(j).equals(lisCurrent.get(j)) ) {
//            	diff.add(lisCurrent.get(j));
//            	lisCurrent.remove(j);
//            	j--;
//            }
//        }
//    	listLast = lisCurrent;
//    	return diff;  	
//    }
//    public List<String> getListPFromHtmlunit() throws Exception{ 
//    	List<String> listP = new ArrayList<String>();
//        /**HtmlUnit请求web页面*/  
//    	WebClient wc = new WebClient();  
//    	wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
//    	wc.getOptions().setCssEnabled(false); //禁用css支持  
//    	wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
//    	wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
//    	HtmlPage page = wc.getPage("http://blog.sina.com.cn/s/blog_48874cec0102y0qm.html");  
//    	String pageXml = page.asXml(); //以xml的形式获取响应文本  
//    	//System.out.println(pageXml.toString());  
//        Document doc = Jsoup.parse(pageXml, "UTF-8");   
//      
//        //System.out.println(doc.toString());          
//        listP = getListPFromDocument(doc);
//        return listP;
//    }

	//默认next的是增量的，比较不同的时候删除next的不同项
    public List<String> getDiffFromList(List<String> listFron, List<String> listNext) throws Exception {     	
    	int listPsize = 0;
    	int i;
    	int offset = 0;
    	int offsetId = 0;
    	
    	
    	diff.clear();
    	
    	listPsize = listNext.size();
    	
    	for(int j = 0;j < listPsize;j++){
    		offsetId = j - offset;    		
    		
    		//System.out.println("p1:"+listFron.get(offsetId)); 
    		//System.out.println("p2:"+listNext.get(j)); 
            if(!listFron.get(offsetId).equals(listNext.get(j)) ) {
            	diff.add(listNext.get(j));
            	offset++;            	
            }
        } 
    	   	    	
    	return diff;
    }
    public List<String> getDiffFromFile(String filepath, String filepathNext) throws Exception {     	
    	List<String> lispP1 = getListPFromFile(filepath);
    	List<String> lispP2 = getListPFromFile(filepathNext);
    	return getDiffFromList(lispP1, lispP2);
	}
    
    public List<String> getDiffFromHtml(String html, String htmlNext) throws Exception {     	
    	List<String> lispP1 = getListPFromHtml(html);
    	List<String> lispP2 = getListPFromHtml(htmlNext);    	
    	  	    	
    	return getDiffFromList(lispP1, lispP2);
	}
    
    public List<String> getDiffFromHtmlByBr(String html, String htmlNext) throws Exception {      	
    	List<String> lispP1;
    	List<String> lispP2;  
    	lispP1 = Arrays.asList(html.split("<br>"));  
    	lispP2 = Arrays.asList(htmlNext.split("<br>")); 
    	
    	return getDiffFromList(lispP1, lispP2);
	}
    
    public String getHtmlByClass(String html, String divClass) throws Exception {  
    	Document doc = Jsoup.parse(html, "UTF-8"); 
    	String str = "div."+divClass;
    	Element p = doc.select(str).get(0);    	
    	return p.toString();
	}

    public List<String> getListPFromFile(String filePath) throws Exception{   	
    	List<String> listP = new ArrayList<String>();
    	File pageXml = new File(filePath);
        /**jsoup解析文档*/  
        Document doc = Jsoup.parse(pageXml, "UTF-8");         
        //System.out.println(doc.toString());          
        listP = getListPFromDocument(doc);
        return listP;
    }
    public List<String> getListPFromUrl(String url) throws Exception{   	
    	List<String> listP = new ArrayList<String>();
        /**jsoup解析文档*/  
    	Document doc = Jsoup.connect(url).get();      
        //System.out.println(doc.toString());          
        listP = getListPFromDocument(doc);
        return listP;
    }
    public List<String> getListPFromHtml(String html) throws Exception{   	
    	List<String> listP = new ArrayList<String>();
    	
        /**jsoup解析文档*/  
        Document doc = Jsoup.parse(html, "UTF-8");         
        //System.out.println(doc.toString());          
        listP = getListPFromDocument(doc);
        return listP;
    }
    
    public List<String> getListPFromDocument(Document doc) throws Exception{ 
    	List<String> listP = new ArrayList<String>();
        //Element blogtext = doc.select("div#sina_keyword_ad_area2").get(0);
        Elements p = doc.select("p");
        //System.out.println(titleLinks.toString());  
        //System.out.println(Integer.toString(p.size())); 
        
        for(int j = 0;j < p.size();j++){
            listP.add(p.get(j).text());
        }
        return listP;
    }
}
