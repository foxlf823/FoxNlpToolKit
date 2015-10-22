package cn.fox.utils;

/*
 * The edit distance between two strings.
 * All the functions are case-sensitive.
 * references:
 * 		http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
 */

public class EditDistance {
	public static int getOSA(String a, String b) {
		int lenStr1 = a.length();
		int lenStr2 = b.length();
		char[] str1 = a.toCharArray();
		char[] str2 = b.toCharArray();
		
	    int d[][] = new int[1+lenStr1][1+lenStr2];
	    int i, j; // represent the length of a, b
	    int cost; // represent we need one edit operation
	 
	    // b's length is 0
	    for(i=0;i<lenStr1;i++)
	        d[i][0] = i;
	    // a's length is 0
	    for(j=0;j<=lenStr2;j++) 
	        d[0][j] = j;
	 
	    for(i=1;i<=lenStr1;i++) {
	    	for(j=1;j<=lenStr2;j++) {
	    		if(str1[i-1] == str2[j-1]) cost = 0;
                else cost = 1;
	    		// d[i-1][j]+1 a deletion, d[i][j-1]+1 a insertion, d[i-1][j-1]+cost substitution    
	    		int min =  d[i-1][j]+1 > d[i][j-1]+1 ? d[i][j-1]+1:d[i-1][j]+1;
                min = min > d[i-1][j-1]+cost ? d[i-1][j-1]+cost:min;        
                if(i > 1 && j > 1 && str1[i-1] == str2[j-2] && str1[i-2] == str2[j-1]) {
                	min = min > d[i-2][j-2] + 1 ? d[i-2][j-2] + 1:min;
                }
	    		d[i][j] = min;       
                              
	    	}
	    }

	    return d[lenStr1][lenStr2];
	}
	
	
}
