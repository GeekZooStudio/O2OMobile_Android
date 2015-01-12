package com.BeeFramework.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

	private static boolean isOutput = true;
	public static void output(String content) {
		if (isOutput) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分ss秒SSS");
			String time = sdf.format(new Date(System.currentTimeMillis()));
			System.out.println(content+"------>"+time);
		}
	}
	
}
