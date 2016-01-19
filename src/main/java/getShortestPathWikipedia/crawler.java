package getShortestPathWikipedia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;


public class crawler {
	
	String link = "";
	ArrayList<String> linkListe = new ArrayList<String>();
	int depth = 0;
	crawler lastCrawler;
	crawlThread myThread;
	int tryCount = 0;
	PrintWriter pw;
	
	
	public crawler(String start, int depth, crawler lastCrawler, crawlThread myThread, PrintWriter pw){
		link = start;
		this.depth = depth;
		this.depth++;
		this.lastCrawler = lastCrawler;
		this.myThread = myThread;
		this.pw = pw;
		if(this.depth <= Main.maxDepth)
		{
			crawl();
		}
	}
	
	public void crawl(){
	URL url;
	try {
		url = new URL(link);
		URLConnection urlConnection = url.openConnection();
		  
		  BufferedReader in = new BufferedReader( new InputStreamReader(
				  urlConnection.getInputStream()));
				  new ParserDelegator().parse(in, callback, false);
				  
				  int ThreadCount = 0;
				  int count = 0;
				  for(Iterator<String> i = linkListe.iterator(); i.hasNext(); ) {
					    String item = i.next();
					    count++;
					    if(myThread == null || !myThread.isInList(item))
					    {
					    	
					    	if(depth == 1 && Main.useThreads){
					    		crawlThread c = new crawlThread(item, depth, this, pw);
					    		Main.threadList.add(c);
					    		Thread t = new Thread(c);
					    		t.start();
					    		ThreadCount++;
					    		System.out.println("new thread");
					    	}
					    	else{
					    		crawler c = new crawler(item, depth, this, myThread, pw);
					    	}
					    }
					}
	} 
	catch (FileNotFoundException e){
		System.out.println("Website not available: " + link);
		Main.linksDone.add(link);
	}
	catch (IOException e) {
		e.printStackTrace();
		if(myThread != null)
			myThread.sleep(1000);
		tryCount++;
		if(tryCount < 2)
			crawl();
	}
	  
	}
	
	public void printLink(){
		
			System.out.println("#" + this.depth + ": " + link);
			pw.println("#" + this.depth + ": " + link);
			if(lastCrawler != null){
			lastCrawler.printLink();			
		}
			else{
				System.out.println("Time needed: " + (new Date().getTime() - Main.startTime));
				pw.println("Time needed: " + (new Date().getTime() - Main.startTime) + "ms");
				pw.close();
				System.exit(0);
			}
	}
	
	HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback(){
		
		  public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos){
			  if(t == HTML.Tag.A){
				  String link = (String) a.getAttribute(HTML.Attribute.HREF);
				  if(link != null && link.length() > 5){
					  if(link.substring(0, 6).equals("/wiki/")&& !link.contains(":") &&(link.length() < 13 || !link.substring(0, 13).equals("/wiki/Spezial"))){
						  link = "https://de.wikipedia.org" + link;
						  System.out.println("Scanned: " + link);
						  linkListe.add(link);
						  if(link.equals(Main.end)){
							  System.out.println("Target: " + Main.end);
							  pw.println("Target: " + Main.end);
							  printLink();
							  
						  }
					  }
				  }
			  }
		  }
		  
	  };
}
