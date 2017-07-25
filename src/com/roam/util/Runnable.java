package com.roam.util;

/**
 * Created by barry on 2017/7/20.
 */
public class Runnable {
	public static void main(String[] args) {
		ChecksumUtil cc = new ChecksumUtil();
		System.out.println(cc.doChecksumUns(args[0]));
	}
}