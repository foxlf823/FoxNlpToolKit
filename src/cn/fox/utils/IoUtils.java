package cn.fox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class IoUtils {
	/*
	 * Delete all the files and directories in "dir".
	 */
	public static void clearDirectory(File dir) {  
	
	    File[] files = dir.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        if (files[i].isFile()) {  
	            files[i].delete();  
	        } 
	        else {  
	        	clearDirectory(files[i]);  
	        	files[i].delete();
	        }  
	    }  
	    
	}  
	
	public static String readAFileAll(File file, String charset) {
		String contentString = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), charset);
	    	StringBuilder sb = new StringBuilder();
	    	int ch = -1;
			while ((ch = isr.read()) != -1) {
				sb.append((char)ch);
			}    
			isr.close();
			contentString = new String(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentString;
	}
	
	// copy a directory to a destination directory
	public static void copyDir(File src, File des) {
		if(!des.exists())
			des.mkdir();
		for(File f:src.listFiles()) {
			if(f.isDirectory())
				copyDir(f, new File(des.getAbsolutePath()+"/"+f.getName()));
			else {
				copyFile(f, new File(des.getAbsolutePath()+"/"+f.getName()));
			}
		}
	}
	
	public static void copyFile(File src, File des)  { 
		try {
	        FileInputStream fin = new FileInputStream(src); 
	        FileOutputStream fout = new FileOutputStream(des); 
	        int bytesRead; 
	        byte[] buf = new byte[1024];   
	        while ((bytesRead = fin.read(buf)) != -1) { 
	            fout.write(buf, 0, bytesRead); 
	        } 
	        fout.flush(); 
	        fout.close(); 
	        fin.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
}
