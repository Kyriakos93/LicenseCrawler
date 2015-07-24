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

package com.kkyria16.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Downloader
 * 
 * This is a program designed in order to download single .zip files from a
 * certain download link. Designed for future integrated download feature of all
 * projects on GitHub of LicenseCrawler.
 * 
 * @author Kyriakos Kyriakou <kkyria16>
 * @date 24/07/2015
 * @version 1.0.0
 * @bugs No known bugs
 *
 */
public class Downloader {

	/**
	 * Download a single file.
	 * 
	 * Download and save a file with a specific filename to be saved from a
	 * certain url link that represents the actual file online.
	 * 
	 * @param fileName
	 * @param uri
	 * @throws IOException
	 * @returns void
	 */
	public static void downloadFile(String fileName, String uri)
			throws IOException {
		// The file that will be saved on the basic path
		URL link = new URL(uri); // The file that you want to download

		// Code to download
		InputStream in = new BufferedInputStream(link.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();

		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(response);
		System.out.println(response.length);
		fos.close();
		// End download code
	}

	/**
	 * Finds and returns the .zip file name from a url link.
	 * 
	 * @param uri
	 * @return String
	 */
	public String getFileName(String uri) {
		String filename = "", revfilename = "";

		if (uri.contains(".zip")) {
			// notify
			System.out.println("there is a file");

			// save filename reversed
			for (int i = uri.length() - 1; i > 0; i--) {
				char ch = uri.charAt(i);

				if (ch == '/')
					break;
				else
					revfilename += ch;
			}
			// reverse string
			for (int i = revfilename.length() - 1; i >= 0; i--) {
				if (revfilename.charAt(i) != '\0')
					filename += revfilename.charAt(i);
			}

		} else {
			// do nothing
			System.out.println("there is not a file");
		}

		return filename;
	}

}
