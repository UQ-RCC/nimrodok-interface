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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAlgorithm {

	public enum State {
		STOPPED,
		RUNNING,
		WAITING_FOR_BATCH,
		FINISHED
	}

	public final Configuration config;
	public final INimrodReceiver receiver;
	public final ArrayOfPoints startingPoints;

	protected State m_State;
	protected Batch m_CurrentBatch;
	protected final List<PointContainer> m_ParetoFront;

	protected BaseAlgorithm(ArrayOfPoints startingPoints, Configuration config, INimrodReceiver logger) throws NimrodOKException {
		if(config == null) {
			throw new IllegalArgumentException("config");
		}

		if(logger == null) {
			throw new IllegalArgumentException("logger");
		}

		if(startingPoints == null) {
			throw new IllegalArgumentException("startingPoints");
		}

		this.receiver = logger;
		this.config = config;
		this.startingPoints = startingPoints;

		if(config.objectiveCount() < 1) {
			throw new NimrodOKException("Objective count must be >= 1");
		}

		if(!config.convergenceSettings().parsingValid) {
			throw new NimrodOKException("Convergence Settings must pass");
		}

		m_State = State.STOPPED;
		m_CurrentBatch = null;
		m_ParetoFront = new ArrayList<PointContainer>();
		logger.logf(this, "Resolved properties: %s", config.customProperties().toString());
	}

	
	protected BaseAlgorithm(PointContainer startingPoint, Configuration config, INimrodReceiver logger) throws NimrodOKException {
		this(new ArrayOfPoints(startingPoint.point, 1), config, logger);
	}

	public abstract void fire() throws NimrodOKException;

	public void cleanup() {
		
	}

	/**
	 * Get the state of the optimisation.
	 *
	 * @return The current state of the optimisation.
	 */
	public State getState() {
		return m_State;
	}

	public Batch getCurrentBatch() {
		return m_CurrentBatch;
	}

	public List<PointContainer> getResults() {
		return Collections.unmodifiableList(m_ParetoFront);
	}

	protected Batch createBatch(List<PointContainer> points) {
		return createBatch(points.toArray(new PointContainer[points.size()]));
	}

	protected Batch createBatch(PointContainer[] points) {
		return new Batch(this, points);
	}
}
