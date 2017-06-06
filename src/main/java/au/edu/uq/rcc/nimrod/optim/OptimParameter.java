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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OptimParameter {

	public final String name;
	public final double min;
	public final double max;
	public final double step;

	public OptimParameter(String name, double min, double max, double step) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public OptimParameter(OptimParameter param) {
		if(param == null) {
			throw new IllegalArgumentException();
		}

		this.name = param.name;
		this.min = param.min;
		this.max = param.max;
		this.step = param.step;
	}

	void describeParameter() {
		System.out.println(toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("parameter %s range from %f to %f", name, min, max));

		if(step != 0) {
			sb.append(" step ");
			sb.append(step);
		}

		sb.append(';');
		return sb.toString();
	}

	private static final String PATTERN_STRING = "parameter\\s+(\\w+)\\s+float\\s+range\\s+from\\s+([^;\\s]+)\\s+to\\s+([^;\\s]+)(?:\\s+?step\\s+(\\w+))?\\s*(?:;)?";
	private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

	public static OptimParameter fromString(String s) throws ParseException {
		Matcher matcher = PATTERN.matcher(s);

		if(!matcher.matches()) {
			throw new ParseException("Invalid input", -1);
		}

		String paramName = matcher.group(1);

		double min = Double.parseDouble(matcher.group(2));
		double max = Double.parseDouble(matcher.group(3));

		double step = 0;
		String ss = matcher.group(4);
		if(ss != null) {
			step = Double.parseDouble(ss);
		}

		return new OptimParameter(paramName, min, max, step);
	}
}
