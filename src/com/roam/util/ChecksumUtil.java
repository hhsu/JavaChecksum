package com.roam.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.roam.util.model.CheckSumBinModel;
import com.roam.util.model.CheckSumUnsModel;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by barry on 2017/7/19.
 */
public class ChecksumUtil {

	/**
	 * @param filename UNS file name
	 * @return checksum value of whole Uns and checksum value of each Bin inside of the Uns file
	 * @throws IOException
	 */
	public JsonNode doChecksumUns(String filename) {
		byte[] bytes = new byte[0];
		try {
			// get bytes from UNS file
			bytes = Files.readAllBytes(Paths.get(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<CheckSumBinModel> binFileList = new ArrayList<>();

		// name of UNS file
		String unsName = new String(bytes, 0, 10).trim();

		byte[] binFiles = {bytes[18]};
		int binFilesCount = calcHexArray(binFiles);

		int binStartFrom = 128;
		List<Integer> binValues = new ArrayList<>();
		for (int i = 0; i < binFilesCount; i++) {
			// get begin position and file length for each Bin file
			byte[] positions = {bytes[binStartFrom + 3], bytes[binStartFrom + 2], bytes[binStartFrom + 1], bytes[binStartFrom]};
			byte[] files = {bytes[binStartFrom + 7], bytes[binStartFrom + 6], bytes[binStartFrom + 5], bytes[binStartFrom + 4]};
			int beginPosition = calcHexArray(positions);
			int fileLength = calcHexArray(files);

			// name of each Bin file
			String binFileName = new String(bytes, beginPosition + 11, 5).trim();

			Checksum checksumBinCRC = new CRC32();

			//checksum by CRC32 for Bin file
			checksumBinCRC.update(bytes, beginPosition, fileLength);
			binStartFrom += 32;

			//calulate CRC checksum for each Bin file
			long checksumBinValue = checksumBinCRC.getValue();
			String expectedBinChecksum = Long.toHexString(checksumBinValue).toUpperCase();

			byte[] binByte = hexStringToByteArray(expectedBinChecksum);
			int binByteValue = calcHexArray(binByte);
			binValues.add(binByteValue);

			CheckSumBinModel csbm = new CheckSumBinModel(binFileName, expectedBinChecksum);

			binFileList.add(csbm);
		}

		//calulate overall CRC checksum of UNS with XOR checksum of each Bin file
		int XOR = 0;
		if (binValues != null && binValues.size() > 0) {
			for (Integer binVal : binValues) {
				XOR ^= binVal;
			}
		}

		//convert the value of XOR for overall UNS
		String expectedUnsChecksum = Long.toHexString(XOR).toUpperCase();

		CheckSumUnsModel checkSumModel = new CheckSumUnsModel(unsName, expectedUnsChecksum, binFileList);

		return checkSumModel.getJsonFormat(checkSumModel);
	}

	// calculate hex byte array
	private static int calcHexArray(byte[] bytesArr) {
		BigInteger bi = new BigInteger(bytesArr);
		int res = Integer.valueOf(bi.toString());
		return res;
	}

	//convert hex string to byte array
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
