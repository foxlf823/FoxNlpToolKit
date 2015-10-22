package cn.fox.utils;


public class ExeCaller {
	public static void call(String callString) {
		Runtime rn = Runtime.getRuntime();
		Process p = null;
		try {
			p = rn.exec(callString);
			p.waitFor();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
