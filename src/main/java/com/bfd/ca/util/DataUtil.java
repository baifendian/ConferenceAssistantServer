package com.bfd.ca.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

@SuppressWarnings("all")
public class DataUtil {

	private static final Log LOG = LogFactory.getLog(DataUtil.class);

	private static final byte[] _hexChars = "0123456789abcdef".getBytes();

	public static String calcMD5(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return toHexString(md.digest(data.getBytes()));
		} catch (Exception x) {
			LOG.warn("calc MD5 error, msg:" + x.getMessage());
		}
		return "";
	}

	public static String toHexString(byte[] b) {
		StringBuffer s = new StringBuffer(2 * b.length);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			s.append((char) _hexChars[v >> 4]);
			s.append((char) _hexChars[v & 0xf]);
		}
		return s.toString();
	}

	public static String decode(String data) {
		byte[] bt = data.getBytes();
		byte[] btres = new byte[bt.length];
		int i = 0, j = 0;
		for (i = 0, j = 0; j < bt.length; i++) {
			if ((bt[j] & 0xFC) == 0xC0) {
				btres[i] = (byte) (((bt[j] & 0x03) << 6) | (bt[j + 1] & 0x3F));
				j += 2;
			} else {
				btres[i] = bt[j++];
			}
		}
		try {
			return new String(btres, 0, i, "utf8");
		} catch (UnsupportedEncodingException e) {
			LOG.info("decode data exception, data: " + data + "\n error msg:" + e.getMessage());
		}
		return data;
	}
	
	/**
	 * @author ruining.he
	 * 
	 * params desc:
	 * hashSeed,any string what is not null or empty
	 * partitionSize,if you have 5 partitions,partitionSize is 5
	 * 
	 * return
	 * if something throng with your input,return -1
	 * otherwise,partitionSize is N,return 0 to (N-1)
	 * */
	public static int hash(String hashSeed,int partitionSize){
		if(hashSeed == null || hashSeed.trim().equals("")){
			LOG.error("Error details:hashSeed is null or empty");
			return -1;
		}	
		if(partitionSize<1){
			LOG.error("Error details:partitionSize must greater than 1,your input is " + partitionSize);
			return -1;
		}
		try {
			return (int)(Integer.parseInt(calcMD5(hashSeed.toLowerCase()).substring(0, 4),16) % partitionSize);
		} catch (Exception e) {
			LOG.error("Error details:hashSeed is " + hashSeed + ",partitionSize is " + partitionSize,e);
			return -1;
		}
	}
	
	public static void main(String[] args) {
		
	}
}