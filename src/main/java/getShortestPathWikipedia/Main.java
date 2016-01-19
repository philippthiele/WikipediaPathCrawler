package getShortestPathWikipedia;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class Main {
	
	public static String start;
	public static String end = "https://de.wikipedia.org/wiki/Altgriechische_Sprache";
	public static int maxDepth = 4;
	public static ArrayList<String> linksDone = new ArrayList<String>();
	public static boolean useThreads = true;
	public static ArrayList<crawlThread> threadList = new ArrayList<crawlThread>();
	public static long startTime;
	
  public static void main(String[] args) throws Exception {
	  if(args.length == 1) {
		  start = args[0];
		  startTime = new Date().getTime();
		  while (true) {
			  File file = new File(System.getProperty("user.home") + "/Desktop/wikipedia.txt");
			  file.getParentFile().mkdirs();
			  PrintWriter printWriter = new PrintWriter(file);

			  crawler c = new crawler(start, 0, null, null, printWriter);
			  maxDepth++;
			  System.out.println("Found it in " +  maxDepth + " Rounds");
			  linksDone.clear();
			  int lastPrint = 0;
			  while (threadList.size() > 0) {
                  //System.out.println("#" + threadList.size() + " Round: " + maxDepth + " Active: " + java.lang.Thread.activeCount());
                  lastPrint = threadList.size();
			  }
			  threadList.clear();
		  }
	  }else{
		  System.out.println("Wrong amount of Parameters specified");
	  }
  }
 
}
