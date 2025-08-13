package com.poly.Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayUtil {

	public static String buildSignedUrl(Map<String, String> params, String secretKey) {
		List<String> fieldNames = new ArrayList<>(params.keySet());
		Collections.sort(fieldNames);

		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator<String> itr = fieldNames.iterator();

		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = params.get(fieldName);
			if (fieldValue != null && fieldValue.length() > 0) {
				hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
				query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8)).append('=')
						.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
				if (itr.hasNext()) {
					hashData.append('&');
					query.append('&');
				}
			}
		}

		String secureHash = hmacSHA512(secretKey, hashData.toString());
		query.append("&vnp_SecureHash=").append(secureHash);
		return query.toString();
	}

	public static String hmacSHA512(String key, String data) {
		try {
			Mac hmac512 = Mac.getInstance("HmacSHA512");
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
			hmac512.init(secretKeySpec);
			byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

			StringBuilder hash = new StringBuilder();
			for (byte b : bytes) {
				hash.append(String.format("%02x", b));
			}
			return hash.toString();
		} catch (Exception e) {
			return "";
		}
	}
}
