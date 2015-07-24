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

package com.kkyria16.crawler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkyria16.crawler.Project;
import com.kkyria16.licenses.LicenseIdentifier;
import com.kkyria16.util.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * LicenseCrawler
 * 
 * This program developed for a research of Software Engineering and Internet
 * Technologies Laboratory (SEIT Lab) at University Of Cyprus. It was designed
 * and developed by Kyriakos Kyriakou <kkyria16> under an internship research
 * program. Here are listing some contact information about author:
 * 
 * @Website: http://www.cs.ucy.ac.cy/~kkyria16
 * @email: kyriakosb4a@yahoo.gr, kkyria16@cs.ucy.ac.cy
 * @Lab Official Website: http://www.cs.ucy.ac.cy/seit/
 * 
 *      It’s a crawler that indicates basic information for projects belong to
 *      GitHub users, filtered by their star rating. The basic information are:
 *      Project Name, Author, Stars Rated, Language Written, License(s) and
 *      Directory Link of each project. LicenseCrawler supports a bigger project
 *      for a future license suggesting website.
 * 
 * @GitHub Repository: https://github.com/Kyriakos93/LicenseCrawler
 * 
 * @author Kyriakos Kyriakou <kkyria16>
 * @date 24/07/2015
 * @version 1.1.0
 * @bugs No known bugs
 *
 */
public class LicenseCrawler {
	// site root directory in order to crawl
	private static final String ROOT_DIRECTORY = "http://github.com/";
	// api root directory
	private static final String ROOT_API_DIR = "https://api.github.com/";
	// general api accepting
	private static final String ACCEPT1 = "application/vnd.github.v3+json";
	// license accepting
	private static final String ACCEPT2 = "application/vnd.github.drax-preview+json";

	static boolean must_compare = true;
	// list containing links for debug
	static ArrayList<String> lk = new ArrayList<String>();
	// list of all projects collected
	static ArrayList<Project> projects = new ArrayList<Project>();

	/**
	 * Gets a path of the file that license exist. There are 3 possible files on
	 * GitHub: LICENSE, COPYING, README. This path is basic an online link to
	 * that file. You MUST give the directory of the project as a parameter to
	 * be able to do this.
	 * 
	 * @param s_uri
	 * @return String
	 */
	public static String getLicensePath(String s_uri) {

		// get useful information from the search page
		Document doc = null;
		try {
			doc = Jsoup.connect(s_uri).timeout(0).get();
		} catch (IOException e) {
			// if there are not any other pages
			System.out
					.println("[!]Crawl stopped for a reason at getLicensePath().");
		}

		// Elements slinks = doc.select("span.lang");
		Elements slinks = doc.select("a.js-directory-link").select("a[href]");
		// .select("li.repo-list-item").select("a[href]");
		String st = slinks.select("a[href]").text();

		String hr = "";

		for (Element el : slinks) {
			String ns = el.toString();

			if (ns.contains("LICENSE") || ns.contains("License")
					|| ns.contains("license") || ns.contains("LICENCE")
					|| ns.contains("Licence") || ns.contains("licence")) {
				// TODO LICENSES !!!
				hr = getHref(ns);
				break;
				// System.out.println(el);
			} else if (ns.contains("COPYING") || ns.contains("Copying")
					|| ns.contains("copying")) {
				hr = getHref(ns);
				break;
			} else if (ns.contains("README") || ns.contains("Readme")
					|| ns.contains("readme")) {
				hr = getHref(ns);
				break;
			}
		}

		return hr;
	}

	/**
	 * Gets/Concatenate the hyperlink of the license file given and returns it.
	 * 
	 * @param codeline
	 * @return String
	 */
	public static String getHref(String codeline) {
		String href = "https://github.com";
		boolean found = false;

		for (int i = 0; i < codeline.length(); i++) {
			if (codeline.charAt(i) == '"' && found == true) {
				found = false;
				break;
			}
			if (found) {
				href += codeline.charAt(i);
			}
			if (codeline.charAt(i) == '"') {
				found = true;
			}
		}

		return href;
	}

	/**
	 * Gets and returns the content text of LICENSE file link.
	 * 
	 * @param s_uri
	 * @return String
	 */
	public static String getLContent(String s_uri) {

		// get useful information from the search page
		Document doc = null;
		try {
			doc = Jsoup.connect(s_uri).timeout(0).get();
		} catch (IOException e) {
			// if there are not any other pages
			System.err
					.println("[!]An unknown issue appeared at getLContent().");
		}

		Elements slinks = doc.select("div.blob-wrapper");
		if (slinks.text().length() == 0)
			slinks = doc.select("div.file");

		return slinks.text();
	}

	/**
	 * Gets and returns the content text of README file link.
	 * 
	 * @param s_uri
	 * @return String
	 */
	public static String getRContent(String s_uri) {

		// get useful information from the search page
		Document doc = null;
		try {
			doc = Jsoup.connect(s_uri).timeout(0).get();
		} catch (IOException e) {
			// if there are not any other pages
			System.err
					.println("[!]An unknown issue appeared at getRContent().");
		}

		Elements slinks = doc.select("div.blob");
		if (slinks.text().length() == 0)
			slinks = doc.select("div.file");

		return slinks.text();
	}

	/**
	 * Gets and returns the content text of COPYING file link.
	 * 
	 * @param s_uri
	 * @return String
	 */
	public static String getCContent(String s_uri) {

		// get useful information from the search page
		Document doc = null;
		try {
			doc = Jsoup.connect(s_uri).timeout(0).get();
		} catch (IOException e) {
			// if there are not any other pages
			System.err
					.println("[!]An unknown issue appeared at getCContent().");
		}

		Elements slinks = doc.select("div.blob-wrapper");
		if (slinks.text().length() == 0)
			slinks = doc.select("div.file");

		return slinks.text();
	}

	/**
	 * This is ONLY for tests on crawlign and fetching information on DEBUG
	 * mode.
	 * 
	 * @return void
	 */
	public static void crawlTest() {

		String s_uri = "https://github.com/nwjs/nw.js/blob/nw13/LICENSE";
		// s_uri =
		// "https://github.com/necolas/normalize.css/blob/master/LICENSE.md";
		s_uri = "https://github.com/joyent/node/blob/master/LICENSE";

		// get useful information from the search page
		Document doc = null;
		try {
			doc = Jsoup.connect(s_uri).timeout(0).get();
		} catch (IOException e) {
			// if there are not any other pages
			System.out.println("[!]Crawl stopped for a reason.");
		}

		// Elements slinks = doc.select("span.lang");
		Elements slinks = doc.select("div.file");
		// .select("li.repo-list-item").select("a[href]");

		System.out.println(slinks.toString());
		System.out.println(slinks.text());

		System.out.println(LicenseIdentifier.findLicense(slinks.text()));
		System.out.println(LicenseIdentifier.findLicense(slinks.text())
				.length());

	}

	/**
	 * Gets the language of current project written by giving the directory link
	 * as a parameter for that project.
	 * 
	 * @param s_uri
	 * @return String
	 */
	public static String getLanguage(String s_uri) {

		// get useful information from the search page
		Document doc = null;
		try {
			doc = Jsoup.connect(s_uri).timeout(0).get();
		} catch (IOException e) {
			// if there is a particular problem
			System.out.println("[!]Crawl stopped for a reason.");
		}

		Elements langs = doc.select("span.lang");
		Scanner search = new Scanner(langs.text());

		String language = "";
		try {
			language = search.next();
		} catch (Exception ioex) {
			search.close();
			return "null";
		}

		if (language == null || language == "" || language == " ") {
			search.close();
			return "null";
		}
		search.close();
		return language;

	}

	/**
	 * Crawls GitHub Open-Source Projects.
	 *
	 * Moves through links and finds all projects sorted by minimum stars given
	 * and saves the actual project download package link into a list.
	 *
	 * @param uri
	 * @return void
	 * @throws IOException
	 */
	public static void crawlUri(int star_rate) throws IOException {
		int i = 1, m = 0;
		System.out.println("Crawling and collecting data for projects over "
				+ star_rate + " stars..");
		while (true) {// while there is a next page
			System.out.println("PAGE >> " + i);

			String s_uri = "https://github.com/search?utf8=%E2%9C%93&p=" + i
					+ "&q=stars%3A%3E" + star_rate
					+ "&type=Repositories&ref=advsearch&l=";

			// get useful information from the search page
			Document doc = null;
			try {
				doc = Jsoup.connect(s_uri).timeout(0).get();
			} catch (IOException e) {
				// if there are not any other pages
				System.out
						.println("[!]Crawl complete. There are NOT any other pages.");
				break;
			}

			Elements slinks = doc.select("ul.repo-list")
					.select("li.repo-list-item").select("a[href]");

			System.out.println("Projects : Loading ");

			// represent all info into a line of text
			String info = slinks.select("a").text();
			Scanner scan = new Scanner(info);

			boolean pass = true;

			while (scan.hasNext()) {
				String nextscan = scan.next();
				while (nextscan.contains("http://")
						|| nextscan.contains("https://")
						|| nextscan.contains("@")) {
					// skip all those critical situations
					if (scan.hasNext()) {
						pass = true;
						nextscan = scan.next();
					} else {
						pass = false;
						break;
					}

				}
				if (!pass) // go to next page if it's the last element
					break;
				projects.add(new Project());
				projects.get(m).setStars(makePureInt(nextscan));
				scan.next(); // forks will be ignored
				String projectname = scan.next();
				projects.get(m).setName(projectname);
				projects.get(m).setDirectory(ROOT_DIRECTORY + projectname);
				Document inf = Jsoup.connect(projects.get(m).directory)
						.timeout(0).get();
				projects.get(m)
						.setAuthor(inf.select("span.author-name").text());

				// NEW ADDITION FOR LANGUAGE
				projects.get(m).setLanguage(
						getLanguage(projects.get(m).getDirectory()));

				// NEW ADDITION FOR LICENSES
				String lc_path = getLicensePath(projects.get(m).getDirectory());
				String lcs = "", content = "";
				if (lc_path.contains("LICENSE") || lc_path.contains("License")
						|| lc_path.contains("license")
						|| lc_path.contains("LICENCE")
						|| lc_path.contains("Licence")
						|| lc_path.contains("licence")) {
					content = getLContent(lc_path);
				} else if (lc_path.contains("COPYING")
						|| lc_path.contains("Copying")
						|| lc_path.contains("copying")) {
					content = getCContent(lc_path);
				} else if (lc_path.contains("README")
						|| lc_path.contains("Readme")
						|| lc_path.contains("readme")) {
					content = getRContent(lc_path);
				}

				String licenses = LicenseIdentifier.findLicense(content);
				if (licenses.compareTo("") == 0 || licenses.compareTo(" ") == 0)
					projects.get(m).setLicense("null");
				else
					projects.get(m).setLicense(licenses);

				/*
				 * CODE IS BEING USED FOR COMMUNICATING WITH GITHUB API-------
				 * String proj_repos = getProjectRepos(projects.get(m));
				 * ArrayList<String> langlc = getLangNLicense(ROOT_API_DIR,
				 * ACCEPT1, ACCEPT2, proj_repos);
				 * 
				 * // ************** ADDITION if (langlc == null) { proj_repos =
				 * projects.get(m).getDirectory(); langlc =
				 * getLangNLicense(ROOT_API_DIR, ACCEPT1, ACCEPT2, proj_repos);
				 * // System.out.println(info); // !proj_repos =
				 * getProjectRepos(projects.get(m)); // langlc =
				 * getLangNLicense(ROOT_API_DIR, // ACCEPT1, ACCEPT2,
				 * proj_repos); } // ***************
				 * 
				 * // if 404 page not found exception will return null if
				 * (langlc != null) {
				 * projects.get(m).setLanguage(langlc.get(0));
				 * projects.get(m).setLicense(langlc.get(1)); } else {
				 * projects.get(m).setLanguage("null");
				 * projects.get(m).setLicense("null"); }
				 */

				System.out.print("██");
				m++;
			}// end of while loop
			System.out.println();
			i++;
		}

	}

	/**
	 * Gets project repository.
	 * 
	 * @param pr
	 * @return String
	 */
	public static String getProjectRepos(Project pr) {
		String str = pr.getName();

		String repos = pr.getAuthor();
		boolean found = false;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '/')
				found = true;
			if (found)
				repos += str.charAt(i);
		}

		return repos;
	}

	/**
	 * Communicate with GitHub REST API version 3 to get language and license
	 * info for a certain project given.
	 * 
	 * @param res
	 * @param accept1
	 * @param accept2
	 * @param project
	 * @return ArrayList<String>
	 */
	private static ArrayList<String> getLangNLicense(String res,
			String accept1, String accept2, String project) {
		ArrayList<String> info = new ArrayList<String>();

		// Connect with client
		Client client = Client.create();
		WebResource resource = client.resource(res);
		resource.accept(accept1);

		// Get response as String
		String string;

		try {
			string = resource.path("/repos/" + project).accept(accept2)
					.get(String.class);
		} catch (Exception e) {
			// if 404 not found code returned
			return null;
		}

		// language handling
		try {
			JSONObject jsn = new JSONObject(string);
			info.add(jsn.get("language").toString());
			// HERE System.out.println(info.get(0));

		} catch (Exception e) {
			// put an empty language entry if language info does not exist at
			// repository
			info.add("null");
		}

		// license handling
		try {
			JSONObject jsn = new JSONObject(string);
			JSONObject jsn_lc = new JSONObject(jsn.get("license").toString());
			info.add(jsn_lc.get("name").toString());

		} catch (Exception e) {
			// put an empty license entry if LICENSE file does not exist at
			// repository
			info.add("null");
		}

		return info;

	}

	/**
	 * Makes a pure integer number without any commas.
	 *
	 * @param str
	 * @return
	 */
	private static int makePureInt(String str) {
		String temp = "";

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ',')
				temp += str.charAt(i);
		}

		return Integer.parseInt(temp);
	}

	/**
	 * Crawl links from a starting one using recursion. This is an old general
	 * function for crawling links.
	 *
	 * @param URL
	 * @throws IOException
	 */
	public static void processPage(String URL) throws IOException {
		// check if the given URL is already in database
		must_compare = true;
		for (int i = 0; i < lk.size(); i++) {
			if (URL.compareTo(lk.get(i)) == 0)
				must_compare = false;
		}

		if (must_compare) {
			// store the URL to database to avoid parsing again
			lk.add(URL);

			// get useful information
			Document doc = Jsoup.connect("http://www.mit.edu/").get();

			if (doc.text().contains("research")) {
				System.out.println(URL);
			}

			// get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");

			for (Element link : questions) {
				if (link.attr("href").contains("mit.edu"))
					processPage(link.attr("abs:href"));
			}

		}
	}

	/**
	 * Print all projects in a formal and readable view on screen.
	 *
	 * @return void
	 */
	public static void printAllProjects() {
		System.out.println();
		System.out.println("Projects Found:\n");
		for (int i = 0; i < projects.size(); i++) {
			System.out.println(projects.get(i) + "\n");
		}
		System.out.println("\n");
	}

	/**
	 * Print all links that exists into lk list filled by crawler.
	 *
	 * @return void
	 */
	public static void printAllLinks() {
		System.out.println();
		System.out.println("Links that list contains: ");
		for (int i = 0; i < lk.size(); i++)
			System.out.println((i + 1) + " -> " + lk.get(i));
	}

	/**
	 * Writes all the projects data into a single file called
	 * open_source_projects.lcl
	 *
	 * @return void
	 * @throws IOException
	 */
	public static void writeProjectsFile() throws IOException {
		FileWriter fw = new FileWriter(new File("open_source_projects.lcl"));
		fw.write("\t\t\t\t\t\t\tOPEN SOURCE PROJECTS\t\t\t\t\t\t\t\n");

		for (int i = 0; i < projects.size(); i++) {
			fw.write(projects.get(i) + "\n\n");
		}
		fw.close();
	}

	/**
	 * Writes all the projects data into a single file called
	 * primitiveness.lcdata
	 *
	 * @return void
	 * @throws IOException
	 */
	public static void writeDataFile() throws IOException {
		FileWriter fw = new FileWriter(new File("primitiveness.lcdata"));
		for (int i = 0; i < projects.size(); i++) {
			fw.write(projects.get(i).getName() + "\n"
					+ projects.get(i).getAuthor() + "\n"
					+ projects.get(i).getStars() + "\n"
					+ projects.get(i).getLanguage() + "\n"
					+ projects.get(i).getLicense() + "\n"
					+ projects.get(i).getDirectory() + "\n");
		}
		fw.close();
	}

	/**
	 * Load Primitive Data into the crawler.
	 * 
	 * With that function you can load any data written on your own or collected
	 * before with the crawler (a previous state). The default file is being
	 * called primitiveness.lcdata and is been loaded from the default directory
	 * of the program. Note that at the beginning the default projects list of
	 * the crawler is being deleted.
	 *
	 * @return void
	 * @throws IOException
	 */
	public static void loadPrimitiveData() throws IOException {
		projects.removeAll(projects); // remove all projects if exist in the
										// list
		int i = 0;

		Scanner reader = new Scanner(new File("primitiveness.lcdata"));
		while (reader.hasNext()) {
			projects.add(new Project());
			projects.get(i).setName(reader.nextLine());
			projects.get(i).setAuthor(reader.nextLine());
			projects.get(i).setStars(makePureInt(reader.nextLine()));
			projects.get(i).setLanguage(reader.nextLine());
			projects.get(i).setLicense(reader.nextLine());
			projects.get(i).setDirectory(reader.nextLine());
			i++;
		}
		reader.close();
	}

	/**
	 * Find all the valid projects that are containing ALL the info needed for
	 * them and returns them into a list of projects.
	 * 
	 * @return ArrayList<Project>
	 */
	public static ArrayList<Project> getValidProjects() {
		ArrayList<Project> valid = new ArrayList<Project>();

		for (int i = 0; i < projects.size(); i++) {
			if (projects.get(i).getLanguage().compareTo("null") == 0
					|| projects.get(i).getLicense().compareTo("null") == 0) {
				// do nothing and skip
			} else {
				valid.add(projects.get(i));
			}
		}

		return valid;
	}

	/**
	 * Prints all projects from the list given as a parameter.
	 * 
	 * @param list
	 * @return void
	 */
	public static void printProjectList(ArrayList<Project> list) {
		System.out.println("Valid Projects Found for use: \n\n");

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			System.out.println("\n");
		}
	}

	/**
	 * Writes a projects file declared by the filename given and stores in it
	 * all the projects given in the list as a parameter.
	 * 
	 * @param filename
	 * @param list
	 * @throws IOException
	 */
	public static void writeProjects(String filename, ArrayList<Project> list)
			throws IOException {
		FileWriter fw = new FileWriter(new File(filename));
		fw.write("\t\t\t\t\t\t\tOPEN SOURCE PROJECTS\t\t\t\t\t\t\t\n");

		for (int i = 0; i < list.size(); i++) {
			fw.write(list.get(i) + "\n\n");
		}
		fw.close();
	}

	/**
	 * Shows on screen basic statistics for licenses and valid projects found.
	 * 
	 * @return void
	 */
	public static void showStatistics() {

		for (int i = 0; i < projects.size(); i++) {
			for (int j = 0; j < LicenseIdentifier.lics.size(); j++) {
				if (projects.get(i).getLicense()
						.contains(LicenseIdentifier.lics.get(j).getTag())) {
					LicenseIdentifier.lics.get(j).count++;
				}
			}
		}

		System.out.println("Licenses Found: \n");
		for (int i = 0; i < LicenseIdentifier.lics.size(); i++) {
			System.out.println(i + "\t|\t"
					+ LicenseIdentifier.lics.get(i).getTag() + ":\t"
					+ LicenseIdentifier.lics.get(i).count);
		}

		// statistics to be calculated
		int all_licenses = LicenseIdentifier.getCounterSum();
		double p = (100.0 / all_licenses);
		double temp;

		System.out.println("\nPercentage Used per License:\n");
		for (int i = 0; i < LicenseIdentifier.lics.size(); i++) {
			temp = (p * LicenseIdentifier.lics.get(i).count);
			System.out.println(i + "\t|\t"
					+ LicenseIdentifier.lics.get(i).getTag() + ":\t" + temp
					+ "%");
		}
		int validproj = 0, allproj = 0;
		for (int i = 0; i < projects.size(); i++) {
			if (projects.get(i).getLanguage().compareTo("null") != 0
					&& projects.get(i).getLicense().compareTo("null") != 0) {
				validproj++;
			}
			allproj++;
		}
		System.out.println("\nProjects found: " + validproj
				+ " valid projects/" + allproj + "\n");

		LicenseIdentifier.reinitializeCounters();
	}

}// end of LicenseCrawler class
