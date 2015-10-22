package cn.fox.utils;

import java.io.File;
import java.io.FilenameFilter;
/*
 * Filter with file names with ignoring case.
 */
public class FileNameFilter implements FilenameFilter {
	private String s;
	
	public FileNameFilter(String s) {
		this.s = s.toUpperCase();
	}

	@Override
	public boolean accept(File dir, String name) {
		String newName = name.toUpperCase();
		
		if(newName.indexOf(s) != -1)
			return true;

		return false;
	}

}
