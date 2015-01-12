package com.external.timepicker;

import java.text.SimpleDateFormat;

public class JudgeDate {

	/**
      * �ж��Ƿ�Ϊ�Ϸ�������ʱ���ַ�
      * @param str_input
      * @param str_input
      * @return boolean;���Ϊtrue,�����Ϊfalse
      */
	public static  boolean isDate(String str_input,String rDateFormat){
		if (!isNull(str_input)) {
	         SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
	         formatter.setLenient(false);
	         try {
	             formatter.format(formatter.parse(str_input));
	         } catch (Exception e) {
	             return false;
	         }
	         return true;
	     }
		return false;
	}
	public static boolean isNull(String str){
		if(str==null)
			return true;
		else
			return false;
	}
}