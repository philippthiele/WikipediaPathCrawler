package getShortestPathWikipedia;

import java.io.PrintWriter;


public class crawlThread implements Runnable{

	String link;
	int depth;
	crawler lastCrawler;
	PrintWriter pw;
	
	public crawlThread(String link, int depth, crawler lastCrawler, PrintWriter pw){
		this.link = link;
		this.depth = depth;
		this.lastCrawler = lastCrawler;
		this.pw = pw;
	}

	public void run() {
		crawler c = new crawler(link, depth, lastCrawler, this, pw);
		removeMe();
	}
	
	public synchronized void removeMe(){
		if(Main.threadList.contains(this))
			Main.threadList.remove(this);
	}
	
	public synchronized boolean isInList(String item){
		if(!Main.linksDone.contains(item)){
			Main.linksDone.add(item);
			return false;
		}
		return true;		
	}
	
	public void sleep(int mSec){
		try {
			Thread.sleep(mSec);
		} catch (InterruptedException e) {
			sleep(mSec);
		}
	}

}
