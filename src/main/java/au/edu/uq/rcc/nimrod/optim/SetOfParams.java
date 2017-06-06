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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class SetOfParams extends AbstractList<OptimParameter> {

	private final OptimParameter[] m_Params;
	
	public SetOfParams(List<OptimParameter> params) {
		if(params == null) {
			throw new IllegalArgumentException();
		}
		
		m_Params = new OptimParameter[params.size()];
		for(int i = 0; i < m_Params.length; ++i) {
			m_Params[i] = params.get(i);
		}
	}

	public SetOfParams(OptimParameter[] params) {
		if(params == null) {
			throw new IllegalArgumentException();
		}

		m_Params = Arrays.copyOf(params, params.length);
	}

	@Override
	public int size() {
		return m_Params.length;
	}

	@Override
	public OptimParameter get(int i) {
		if(i >= m_Params.length || i < 0) {
			throw new IllegalArgumentException();
		}
		
		return m_Params[i];
	}
	
	public static SetOfParams fromString(String paramString) throws ParseException {
		HashSet<String> nameList = new HashSet<String>();

		List<OptimParameter> params = new ArrayList<OptimParameter>();

		String[] lines = paramString.split("[;\n]");
		for(String line : lines) {
			line = line.trim();
			if(line.isEmpty()) {
				continue;
			}

			OptimParameter param = null;
			try {

				param = OptimParameter.fromString(line);
			} catch(ParseException e) {
				throw new IllegalArgumentException(e);
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException(e);
			}

			int oldLen = nameList.size();
			nameList.add(param.name);
			if(oldLen == nameList.size()) {
				throw new IllegalArgumentException("Duplicate parameter name");
			}

			params.add(param);
		}
		
		return new SetOfParams(params);
	}
}
