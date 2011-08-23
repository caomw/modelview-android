package org.jtb.modelview.util;

import java.util.Calendar;

public class Helper {
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit){
	    	return bytes + " B";
	    }
	    final int exp = (int) (Math.log(bytes) / Math.log(unit));
	    final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String createDatestring(long timestamp){
	    final Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timestamp);
	    StringBuilder sb = new StringBuilder();
	    
	    sb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	    sb.append("/");
	    sb.append(String.valueOf(cal.get(Calendar.MONTH)+1));
	    sb.append("/");
	    sb.append(String.valueOf(cal.get(Calendar.YEAR)));
	    sb.append(" ");
	    sb.append(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
	    sb.append(":");
	    sb.append(String.valueOf(cal.get(Calendar.MINUTE)));
	    sb.append(":");
	    sb.append(String.valueOf(cal.get(Calendar.SECOND)));
	    
	    return sb.toString();
	}

	
}
