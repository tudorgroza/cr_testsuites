package au.org.garvan.phenomics.cr.testsuite.common.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommonUtil {

	public static final String PATH_PATTERN = "%PATH%/";

	public static String checkAndRelocateFolder(String mainFolder, String location, String pattern) {
		if (location != null) {
			if (location.startsWith(pattern)) {
				String tmp = location.substring(pattern.length());
				return mainFolder + tmp;
			} else {
				return location;
			}
		}
		
		return null;
	}

	public static String readFile(String file) {
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while (br.ready()) {
				buffer.append(br.readLine()).append("\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static String readFileUTF8(String file) {
		StringBuffer buffer = new StringBuffer();
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			byte b[] = new byte[1];

			while (dis.available() > 0) {
				dis.read(b);
				String s = new String(b, "UTF-8");
				buffer.append(s);
			}
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static String formatTime(double totalTime) {
		if (totalTime > 60) {
			double mins = totalTime / 60;
			int secs = (int) totalTime % 60;

			if (mins > 60) {
				int hors = (int) (mins / 60);
				mins = mins % 60;
				int m = (int) mins;

				if (secs == 0) {
					if (m == 0) {
						return Integer.toString(hors) + "h";
					} else {
						return Integer.toString(hors) + "h"
								+ Integer.toString(m) + "m";
					}
				} else {
					if (m == 0) {
						return Integer.toString(hors) + "h"
								+ Integer.toString(secs) + "s";
					} else {
						return Integer.toString(hors) + "h"
								+ Integer.toString(m) + "m"
								+ Integer.toString(secs) + "s";
					}
				}
			} else {
				int m = (int) mins;
				if (secs == 0) {
					return Integer.toString(m) + "m";
				} else {
					return Integer.toString(m) + "m" + Integer.toString(secs)
							+ "s";
				}
			}
		} else {
			int t = (int) totalTime;
			return Integer.toString(t) + "s";
		}
	}
}
