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

/**
 * Project
 * 
 * This is an object declaration and structure for storing and managing Projects
 * found at crawling phase. Here are being stores 6 basic information for each
 * project: project name, author, stars rated, language written, licenses has
 * and a direct project repository link from GitHub.
 * 
 * @author Kyriakos Kyriakou <kkyria16>
 * @date 24/07/2015
 * @version 1.0.0
 * @bugs No known bugs
 *
 */
public class Project {
	String name;
	String author;
	int stars;
	String language;
	String license;
	String directory;

	/*
	 * Constructor
	 * 
	 * Creates an empty basic Project object to store info later.
	 */
	Project() {
		name = "";
		author = "";
		stars = 0;
		language = "";
		license = "";
		directory = "";
	}

	/*
	 * Constructor
	 * 
	 * Creates a fully declared Project object, stored with the 6 info given.
	 */
	Project(String name, String author, int stars, String language,
			String license, String directory) {
		this.name = name;
		this.author = author;
		this.stars = stars;
		this.language = language;
		this.license = license;
		this.directory = directory;
	}

	/**
	 * Gets the name of current project.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the author of current project.
	 * 
	 * @return String
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the stars that current project is being rated.
	 * 
	 * @return int
	 */
	public int getStars() {
		return stars;
	}

	/**
	 * Gets the language, that current project is being written.
	 * 
	 * @return String
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets all licenses that current project is under protection.
	 * 
	 * @return String
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * Gets the GitHub directory link of current project.
	 * 
	 * @return String
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * Sets the name of current project.
	 * 
	 * @param name
	 * @return void
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the author of current project.
	 * 
	 * @param author
	 * @return void
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Sets the stars that current project is being rated.
	 * 
	 * @param stars
	 * @return void
	 */
	public void setStars(int stars) {
		this.stars = stars;
	}

	/**
	 * Sets the language that current project is being written.
	 * 
	 * @param language
	 * @return void
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Sets all licenses that current project is under protection.
	 * 
	 * @param license
	 * @return void
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * Sets the GitHub directory link of current project.
	 * 
	 * @param directory
	 * @return void
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Override
	public String toString() {

		return "Project: " + name + "\nAuthor: " + author + "\nStars: " + stars
				+ "\nLanguage: " + language + "\nLicense: " + license
				+ "\nDirectory: " + directory;
	}

}
