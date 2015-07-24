/*
 * The MIT License (MIT)

	LicenseCrawler - Fetch info and licenses of all GitHub Projects.
    Copyright (C) 2015  Kyriakos Kyriakou <kkyria16>

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.

	Contact: kyriakosb4a@yahoo.gr, kkyria16@cs.ucy.ac.cy
	Website: www.cs.ucy.ac.cy/~kkyria16
 */
package com.kkyria16.licenses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * LicenseIdentifier
 * 
 * This basically is a library class for managing texts, analyse them and
 * figuring out what licenses are included in it. It also manages all licenses
 * that must be loaded from lic_identifiers.lcid file and all their samples.
 * After loading them, makes a list-library in order to identify the appropriate
 * licenses.
 * 
 * lic_identifiers.lcid file anatomy:
 * 
 * [license1_name] [license1_alternative_name] [sample_1] [sample_2] [sample_N]
 * ++/ [license2_name] [license2_alternative_name] [sample_1] [sample_2]
 * [sample_N] ++/
 * 
 * @author Kyriakos Kyriakou <kkyria16>
 * @date 24/07/2015
 * @version 1.0.0
 * @bugs No known bugs
 *
 */
public class LicenseIdentifier {

	public static ArrayList<License> lics = new ArrayList<License>();

	/**
	 * Loads all licenses information and samples from lic_identifiers.lcid file
	 * into a single list.
	 * 
	 * @return void
	 */
	public static void loadLicensesIdentifiers() {
		try {
			Scanner read = new Scanner(new File("lic_identifiers.lcid"));
			int i = 0;
			while (read.hasNext()) {
				lics.add(new License());
				lics.get(i).setName(read.nextLine());
				lics.get(i).setAlternative(read.nextLine());
				lics.get(i).setTag(read.nextLine());
				String str = read.nextLine();
				while (true) {
					if (!str.contains("++/")) {
						lics.get(i).samples.add(str);
						str = read.nextLine();
					} else
						break;
				}
				i++;
			}

			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("[!] File lic_identifiers.lcid not found!");
		}

	}

	/**
	 * Find all licenses from the text given as parameter and returns them in a
	 * line showing their tag name, separated with comma ','.
	 * 
	 * eg: MIT/X11, MPL-2.0, Apache-2.0
	 * 
	 * @param content
	 * @return String
	 */
	public static String findLicense(String content) {

		String response = "";

		for (int i = 0; i < lics.size(); i++) {
			// if (content.contains(lics.get(i).getName())) {
			if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					content, lics.get(i).getName())) {
				lics.get(i).namefound = true;
			}
			// if (content.contains(lics.get(i).getAlternative())) {
			if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(
					content, lics.get(i).getAlternative())) {
				lics.get(i).namefound = true;
			}
			for (int j = 0; j < lics.get(i).samples.size(); j++) {
				if (content.contains(lics.get(i).samples.get(j))) {
					lics.get(i).count++;
				}
			}// end of inner for loop
		}// end of outer for loop

		// here goes the check for which license it is
		boolean firstpass = true;
		for (int i = 0; i < lics.size(); i++) {
			if (lics.get(i).namefound) {
				if (firstpass) {
					response += lics.get(i).getTag();
					firstpass = false;
				} else
					response += ", " + lics.get(i).getTag();
			} else if (lics.get(i).count == lics.get(i).samples.size()) {
				if (firstpass) {
					response += lics.get(i).getTag();
					firstpass = false;
				} else
					response += ", " + lics.get(i).getTag();
			}

		}

		// re-initialize counters to zero
		reinitializeCounters();

		return response;
	}

	/**
	 * Re-initialises all appropriate counter and booleans into zero and false.
	 * 
	 * @return void
	 */
	public static void reinitializeCounters() {

		for (int i = 0; i < lics.size(); i++) {
			lics.get(i).namefound = false;
			lics.get(i).count = 0;
		}

	}

	/**
	 * Print all licenses in the list, on screen.
	 * 
	 * @return void
	 */
	public static void printAllLicenses() {
		// TODO Auto-generated method stub
		for (int i = 0; i < lics.size(); i++) {
			System.out.println(lics.get(i).toString() + "\n");
		}

	}

	/**
	 * Calculates and returns the sum of all counters.
	 * 
	 * @return int
	 */
	public static int getCounterSum() {
		int counters = 0;
		for (int i = 0; i < lics.size(); i++) {
			counters += lics.get(i).count;
		}

		return counters;
	}

}// end of LicenseIdentifier class
