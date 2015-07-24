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

import java.io.IOException;
import java.util.Scanner;

import com.kkyria16.crawler.LicenseCrawler;
import com.kkyria16.licenses.LicenseIdentifier;

/**
 * Executor
 * 
 * This class is the main interface for managing and running LicenseCrawler.
 * 
 * @author Kyriakos Kyriakou <kkyria16>
 * @date 24/07/2015
 * @version 1.1.0
 * @bugs No known bugs
 *
 */
public class Executor {

	@SuppressWarnings({ "static-access" })
	public static void main(String[] args) {
		String str = args.toString();
		Scanner cmd = new Scanner(str);
		LicenseCrawler crawler = new LicenseCrawler();

		if (args.length == 0)
			System.err
					.println("[!] Wrong commands or no commands given! Program will close.");
		try {

			LicenseIdentifier.loadLicensesIdentifiers();

			for (int i = 0; i < args.length; i++) {
				String command = args[i];

				if (command.compareTo("-c") == 0) {
					// crawl >stars_rated projects
					int stars_rated = Integer.parseInt(args[++i]);
					crawler.crawlUri(stars_rated);
					crawler.writeProjectsFile();
					crawler.writeDataFile();
				} else if (command.compareTo("-s") == 0) {
					// show all projects on screen
					crawler.printAllProjects();
				} else if (command.compareTo("-r") == 0) {
					// read/load primitiveness.lcdata
					crawler.loadPrimitiveData();
					System.out.println("File primitiveness.lcdata loaded.");
				} else if (command.compareTo("-a") == 0) {
					// show statistics on screen
					System.out.println("Loading Statistics..\n");
					crawler.showStatistics();
				} else if (command.compareTo("-v") == 0) {
					// filter valid ones and return it to screen and into a
					// valid_osp.lcv file
					System.out
							.println("Writting valid projects file (valid_osp.lcv)..");
					crawler.writeProjects("valid_osp.lcv",
							crawler.getValidProjects());
					System.out.println("Success!");
				} else if (command.compareTo("-l") == 0) {
					// print all licenses loaded into crawler
					LicenseIdentifier.printAllLicenses();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[!] Something went wrong, program will close.");
			e.printStackTrace();
		}
	}

}// end of Executor class
