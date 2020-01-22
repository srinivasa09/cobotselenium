package com.peddle.digital.cobot.Util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;;

/**
 * 
 * @authors Srinivasa Reddy Challa, Raj Kumar
 * 
 * Util Class to work with Base64 Encoded Strings
 *
 */
public class Base64Utils {
	
	final static Logger logger = Logger.getLogger(Base64Utils.class);
	
	/**
	 * Method to decode the Base64 encoded string
	 * @param base64EncodedStr : base64 encoded string
	 * @return decoded Base64 encoded string
	 */

	public static String decode(String base64EncodedStr)
	{
		byte[] decoded = Base64.decodeBase64(base64EncodedStr);
		String decodedStr="";
		try
		{
			 decodedStr = new String(decoded, "UTF-8");
		}catch(Exception e)
		{
			logger.error("failed to decode Base64 String", e);
         e.printStackTrace();
		}

		return decodedStr;
	}
	
}
