package com.ebp.owat.lib.scrambler.move;

import com.ebp.owat.lib.dataStructure.matrix.Matrix;

/**
 * General things for each scramble move.
 *
 * Created by stewy on 3/23/17.
 */
public abstract class ScrambleMove {
	public enum Direction{
		FORWARD,
		BACKWARD
	}
	
	private final Matrix matrixToScramble;
	private final Direction direction;
	/**
	 * Constructor to set the matrix we are dealing with.
	 *
	 * @param matrixIn The matrix this move is dealing with.
	 */
	public ScrambleMove(Matrix matrixIn){
		this.matrixToScramble = matrixIn;
		this.direction = Direction.FORWARD;
	}
	
	/**
	 * Constructor to set the marix and the direction the move will take.
	 *
	 * @param matrixIn The matrix this move is dealing with.
	 * @param dirIn The direction this move is to do.
	 */
	public ScrambleMove(Matrix matrixIn, Direction dirIn){
		this.matrixToScramble = matrixIn;
		this.direction = dirIn;
	}
}
