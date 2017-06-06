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

import junit.framework.Assert;
import org.junit.Test;

public class ParserTest {

	public static final String ZDT4_PARAMS
			= "parameter x1 float range from 0.0 to 1.0\n"
			+ "parameter x2 float range from -5.0 to 5.0\n"
			+ "parameter x3 float range from -5.0 to 5.0\n"
			+ "parameter x4 float range from -5.0 to 5.0\n"
			+ "parameter x5 float range from -5.0 to 5.0\n"
			+ "parameter x6 float range from -5.0 to 5.0\n"
			+ "parameter x7 float range from -5.0 to 5.0\n"
			+ "parameter x8 float range from -5.0 to 5.0\n"
			+ "parameter x9 float range from -5.0 to 5.0\n"
			+ "parameter x10 float range from -5.0 to 5.0";

	@Test
	public void parseParamRegex() throws Exception {
		OptimParameter param = OptimParameter.fromString("parameter cl1 float range from 35       to 65 step\t\t5;");

		Assert.assertEquals("cl1", param.name);
		Assert.assertEquals(35.0, param.min, 0.00001);
		Assert.assertEquals(65.0, param.max, 0.00001);
		Assert.assertEquals(5.0, param.step, 0.00001);
	}

	@Test
	public void parseSetOfParams() throws Exception {
		SetOfParams sop = SetOfParams.fromString(ZDT4_PARAMS);
	}
}
