/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 University of Queensland
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package au.edu.uq.rcc.nimrod.optim;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class Properties {

	private HashMap<String, String> m_Values;

	public Properties() {
		m_Values = new HashMap<String, String>();
	}

	public Properties(Properties props) {
		this(props.m_Values);
	}

	public Properties(Map<String, String> props) {
		this();
		m_Values.putAll(props);
	}

	public void addAll(HashMap<String, String> props) {
		m_Values.putAll(props);
	}

	public void addAll(Properties props) {
		if(props == null) {
			return;
		}

		addAll(props.m_Values);
	}

	public void setProperty(String key, String value) {
		m_Values.put(key, value);
	}

	public String getProperty(String key) {
		return m_Values.get(key);
	}

	public void clear() {
		m_Values.clear();
	}

	public Map<String, String> getRawData() {
		return Collections.unmodifiableMap(m_Values);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(String key : m_Values.keySet()) {
			sb.append(key);
			sb.append(" ");
			sb.append(m_Values.get(key));
			sb.append("\n");
		}

		return sb.toString();
	}

	public java.util.Properties toJavaProperties() {
		java.util.Properties props = new java.util.Properties();
		
		for(String key : m_Values.keySet()) {
			props.put(key, m_Values.get(key));
		}

		return props;
	}
	
	public static Properties parseString(String s) throws ParseException {
		if(s == null || s.isEmpty()) {
			return new Properties();
		}

		String[] lines = s.trim().split("[\r\n]+");

		Map<String, String> cfg = new HashMap<String, String>();

		for(int i = 0; i < lines.length; ++i) {
			parseLine(cfg, lines[i], i);
		}

		return new Properties(cfg);
	}

	private static void parseLine(Map<String, String> map, String line, int i) throws ParseException {
		String[] tokens = line.trim().split("\\s+", 2);

		if(tokens.length != 2) {
			throw new ParseException(String.format("Invalid token count %d. Expected 2.", tokens.length), i);
		}

		map.put(tokens[0], tokens[1]);
	}
}
