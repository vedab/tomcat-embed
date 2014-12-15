package com.trials.tomcatembed;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class Main {

  private final static int HTTP_PORT = 8080;

  public static void main(String[] args)
  throws LifecycleException, InterruptedException, ServletException {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(HTTP_PORT);

    Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());

    Tomcat.addServlet(ctx, "hello", new HttpServlet() {
    	
		private static final long serialVersionUID = 1L;

		protected void service(HttpServletRequest req, HttpServletResponse resp) 
	      throws ServletException, IOException {
	        Writer w = resp.getWriter();
	        w.write("Hello, World!");
	        w.flush();
	    }
    });
    ctx.addServletMapping("/*", "hello");

    Tomcat.addServlet(ctx, "tomcat", new HttpServlet() {
		private static final long serialVersionUID = 1L;

		protected void service(HttpServletRequest req, HttpServletResponse resp) 
	      throws ServletException, IOException {
	        Writer w = resp.getWriter();
	        w.write("Hello, from Tomcat Embeded server!");
	        w.flush();
	    }
    });
    ctx.addServletMapping("/tomcat", "tomcat");
    
    Tomcat.addServlet(ctx, "files", new HttpServlet() {
		private static final long serialVersionUID = 1L;

		protected void service(HttpServletRequest req, HttpServletResponse resp) 
	      throws ServletException, IOException {
	        Writer w = resp.getWriter();
	        w.write("Files accessible by Tomcat Embeded server are...");
	        
	        File f = new File(".");
	        if(f.exists() && (f.isDirectory() || f.isFile())){
	        	if(f.isFile()){
	        		w.write("\nDirectory: "+f.getAbsolutePath() +"/"+f.getName());
	        	}else{
	        		for(File subFile : f.listFiles()){
	        			String type = subFile.isDirectory()? "Dir " : "File";
	        			w.write( String.format("\n%s: %s%s%s",
	        					type, f.getAbsolutePath(), File.separator, subFile.getName()));
	        		}
	        	}
	        }
	        
	        w.flush();
	    }
    });
    ctx.addServletMapping("/files", "files");
    
    
    tomcat.start();
	
	System.out.printf("******* Tomcat server started at Port:%d *******", HTTP_PORT);
	
	System.out.printf("\n=====> Access: \n" +
			"\thttp://localhost:%d/\n" +
			"\thttp://localhost:%d/tomcat\n" +
			"\thttp://localhost:%d/files\n",
			HTTP_PORT, HTTP_PORT, HTTP_PORT);
			
    tomcat.getServer().await();
  }

}