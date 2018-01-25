package com.ebp.owat.lib.datastructure.matrix;

import java.util.Iterator;

public abstract class MatrixIterator<T> implements Iterator<T> {
	protected long curRow = 0;
	protected long curCol = 0;
	
	/**
	 * Determines if we are on the
	 * @return
	 */
	public boolean onNewRow(){
		return this.hasNext() && curCol <= 0;
	}
}