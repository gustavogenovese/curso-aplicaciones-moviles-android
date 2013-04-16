package com.gustavogenovese.mensajero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
	public static String inputStreamToString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (sb.length()>0){
				sb.append("\n");
			}
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
}
