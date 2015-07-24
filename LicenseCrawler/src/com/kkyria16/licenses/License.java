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

import java.util.ArrayList;

/**
 * License
 * 
 * This is an object declaration and structure for storing and managing Licenses
 * found in lic_identifiers.lcid file.
 * 
 * @author Kyriakos Kyriakou <kkyria16>
 * @date 24/07/2015
 * @version 1.0.0
 * @bugs No known bugs
 *
 */
public class License {

	String name;
	String alternative;
	String tag;
	ArrayList<String> samples;
	boolean namefound;
	public int count;

	/*
	 * Constructor
	 * 
	 * Creates an empty basic License object to store info later.
	 */
	License() {
		this.name = "";
		this.alternative = "";
		this.samples = new ArrayList<String>();
		this.namefound = false;
		this.count = 0;
	}

	public String toString() {
		return "Name: " + name + "\nAlternative: " + alternative + "\nTag: "
				+ tag + "\nSamples: " + samples.toString();

	}

	/**
	 * Gets the primary name of current license.
	 * 
	 * @return String
	 */
	String getName() {
		return name;
	}

	/**
	 * Sets the primary name of current license.
	 * 
	 * @param name
	 * @return void
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the secondary possible name of current license.
	 * 
	 * @return String
	 */
	String getAlternative() {
		return alternative;
	}

	/**
	 * Sets the secondary possible name of current license.
	 * 
	 * @param alternative
	 * @return void
	 */
	void setAlternative(String alternative) {
		this.alternative = alternative;
	}

	/**
	 * Gets a list of all the string-samples stored inside current license.
	 * 
	 * @return ArrayList<String>
	 */
	ArrayList<String> getSamples() {
		return samples;
	}

	/**
	 * Sets and stores a list of string-samples inside current license.
	 * 
	 * @param samples
	 * @return void
	 */
	void setSamples(ArrayList<String> samples) {
		this.samples = samples;
	}

	/**
	 * Get short-string tag name declared in current license.
	 * 
	 * @return String
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets a short-string tag name for current license.
	 * 
	 * @param tag
	 * @return void
	 */
	void setTag(String tag) {
		this.tag = tag;
	}

}// end of License object class
