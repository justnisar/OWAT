package com.ebp.owat.app.runner;

import com.ebp.owat.lib.datastructure.matrix.Hash.HashedScramblingMatrix;
import com.ebp.owat.lib.datastructure.matrix.Matrix;
import com.ebp.owat.lib.datastructure.matrix.Scrambler;
import com.ebp.owat.lib.datastructure.set.LongLinkedList;
import com.ebp.owat.lib.datastructure.value.BitValue;
import com.ebp.owat.lib.datastructure.value.ByteValue;
import com.ebp.owat.lib.datastructure.value.NodeMode;
import com.ebp.owat.lib.datastructure.value.Value;
import com.ebp.owat.lib.utils.rand.OwatRandGenerator;
import com.ebp.owat.lib.utils.scramble.MoveValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class RunnerUtilities<N extends Value, M extends Matrix<N> & Scrambler, R extends OwatRandGenerator> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerUtilities.class);
	
	public RunnerUtilities(){
	
	}
	
	
	public LongLinkedList<Byte> readDataIn(InputStream dataInput) throws IOException {
		LongLinkedList<Byte> output = new LongLinkedList<>();
		
		try{
			byte curByte = (byte)dataInput.read();
			
			while (curByte != -1){
				output.addLast(curByte);
				curByte = (byte)dataInput.read();
			}
		}finally {
			if(dataInput != null){
				dataInput.close();
			}
		}
		return output;
	}
	
	private void fillMatrixWithData(M emptyMatrix, LongLinkedList values, long height, long width){
		if(height < 1 || width < 1){
			emptyMatrix.grow(values);
		}else{
			emptyMatrix.grow(height, width, values);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private M getBitMatrix(LongLinkedList<Byte> data, long height, long width){
		M matrix = (M) new HashedScramblingMatrix<BitValue>();
		
		matrix.setDefaultValue((N) new BitValue(false, false));
		
		Iterator<Byte> it = data.destructiveIterator();
		LongLinkedList<BitValue> bitValues = new LongLinkedList<>();
		
		while (it.hasNext()){
			byte curByte = it.next();
			bitValues.addAll(BitValue.fromByte(curByte, true));
		}
		this.fillMatrixWithData(matrix, bitValues, height, width);
		
		return matrix;
	}
	
	@SuppressWarnings("unchecked")
	private M getByteMatrix(LongLinkedList<Byte> data, long height, long width){
		M matrix = (M) new HashedScramblingMatrix<ByteValue>();
		
		matrix.setDefaultValue((N) new ByteValue((byte)0, false));
		
		Iterator<Byte> it = data.destructiveIterator();
		LongLinkedList<ByteValue> byteValues = new LongLinkedList<>();
		
		while (it.hasNext()){
			byte curByte = it.next();
			byteValues.addLast(new ByteValue(curByte,true));
		}
		
		this.fillMatrixWithData(matrix, byteValues, height, width);
		
		return matrix;
	}
	
	public M getMatrix(LongLinkedList<Byte> data, NodeMode nodeType, long height, long width){
		switch (nodeType){
			case BIT:
				LOGGER.debug("Matrix set to Bit nodes.");
				return this.getBitMatrix(data, height, width);
			case BYTE:
				LOGGER.debug("Matrix set to Bit nodes.");
				return this.getByteMatrix(data, height, width);
			default:
				throw new IllegalStateException();
		}
	}
	
	private List<N> getListOfValues(long numValues, OwatRandGenerator rand, NodeMode nodeType){
		LongLinkedList<N> output = new LongLinkedList<>();
		
		if(nodeType == NodeMode.BIT){
			for(long i = 0; i < numValues; i++){
				output.add((N) new BitValue(rand.nextBool(), false));
			}
		}else if(nodeType == NodeMode.BYTE){
			for(long i = 0; i < numValues; i++){
				output.add((N) new ByteValue(rand.nextByte(), false));
			}
		}
		
		return output;
	}
	
	private void addRandRowOrCol(M matrix, OwatRandGenerator rand, NodeMode nodeType){
		if(rand.nextBool()){
			matrix.addRow();
			matrix.replaceRow(matrix.getNumRows() -1, this.getListOfValues(matrix.getNumCols(), rand, nodeType));
		}else{
			matrix.addCol();
			matrix.replaceCol(matrix.getNumCols() -1, this.getListOfValues(matrix.getNumRows(), rand, nodeType));
		}
	}
	
	public void padMatrix(M matrix, OwatRandGenerator rand, NodeMode nodeType){
		//TODO:: fill last bits of end row if need be
		
		while (matrix.getNumCols() < MoveValidator.MIN_SIZE_FOR_SCRAMBLING){
			matrix.addCol();
			matrix.replaceCol(matrix.getNumCols() -1, this.getListOfValues(matrix.getNumRows(), rand, nodeType));
		}
		while (matrix.getNumRows() < MoveValidator.MIN_SIZE_FOR_SCRAMBLING){
			matrix.addRow();
			matrix.replaceRow(matrix.getNumRows() -1, this.getListOfValues(matrix.getNumCols(), rand, nodeType));
		}
		
		long numRowsColsToGenerate = matrix.size();
		LOGGER.debug("Adding a total of {} rows and columns of dummy data to the matrix.", numRowsColsToGenerate);
		for(long i = 0; i < numRowsColsToGenerate; i++){
			addRandRowOrCol(matrix, rand, nodeType);
		}
		
		//ensure that bit marices can be serialized properly
		if(nodeType == NodeMode.BIT){
			while (matrix.size() % 8 != 0){
				addRandRowOrCol(matrix, rand, nodeType);
			}
		}
	}
	
	public long determineMinStepsToTake(M matrix, OwatRandGenerator rand){
		//TODO::make this calculation smarter. Logrithmical?
		long min = matrix.size() * 2L;
		long max = min * 2L;
		return rand.nextLong(min, max);
	}
	public long determineMaxStepsToTake(M matrix, OwatRandGenerator rand, long minNumScrambleSteps){
		//TODO::make this calculation smarter. Logrithmical?
		long tempMin = minNumScrambleSteps + matrix.size();
		long tempMax = tempMin * 2L;
		return rand.nextLong(tempMin, tempMax);
	}
	
	public byte[] getMatrixAsBytes(M matrix, NodeMode nodeType){
		byte[] bytes;
		if(nodeType == NodeMode.BIT){
			Iterator<BitValue> bitIt = (Iterator<BitValue>) matrix.iterator();
			
			LongLinkedList<BitValue> tempBits;
			LongLinkedList<Byte> tempBytes = new LongLinkedList<>();
			
			while (bitIt.hasNext()){
				tempBits = new LongLinkedList<>();
				for(short i = 0; i < 8; i++){
					tempBits.addLast(bitIt.next());
				}
				tempBytes.addLast(BitValue.toByte(tempBits));
			}
			
			bytes = new byte[tempBytes.size()];
			Iterator<Byte> it = tempBytes.destructiveIterator();
			for(int i = 0; i < bytes.length; i++){
				bytes[i] = it.next();
			}
		}else if(nodeType == NodeMode.BYTE){
			bytes = new byte[(int)matrix.size()];
			int i = 0;
			Iterator<ByteValue> it = (Iterator<ByteValue>) matrix.iterator();
			while (it.hasNext()){
				bytes[i] = it.next().getValue();
				i++;
			}
		}else{
			throw new IllegalStateException();
		}
		return bytes;
	}
}