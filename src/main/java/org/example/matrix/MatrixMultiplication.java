package org.example.matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author Aleksandr Lozovoy
 * version 1.0
 */


public class MatrixMultiplication extends Thread {

    private int[][] firstMatrix;
    private int[][] secondMatrix;
    private int[][] resultMatrix;
    private int firstIndex;
    private int lastIndex;
    private int sumLength;

    private static final Logger log =Logger.getLogger(MatrixMultiplication.class.getName());

    /**
     *
     * @param firstMatrix first matrix
     * @param secondMatrix second matrix
     * @param resultMatrix result matrix
     * @param firstIndex first index
     * @param lastIndex  last index
     */
    public MatrixMultiplication(int[][] firstMatrix, int[][] secondMatrix, int[][] resultMatrix, int firstIndex, int lastIndex){
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;

        sumLength=secondMatrix.length;
    }

    /**Calculating a value in a single cell.
     *
     * @param row Number row.
     * @param col Number col.
     */
    private void valueCalc(final int row, final int col)
    {
        int sum = 0;
        for (int i = 0; i < sumLength; ++i)
            sum += firstMatrix[row][i] * secondMatrix[i][col];
        resultMatrix[row][col] = sum;
        log.log(Level.INFO, "Add value in a single cell");
    }

    @Override
    public void run()
    {   log.log(Level.INFO,"Thread name " + getName() + " started. Calculating cells from " + firstIndex + " to " + lastIndex + "...");
        int colCount = secondMatrix[0].length;
        for (int index = firstIndex; index < lastIndex; ++index)
            valueCalc(index / colCount, index % colCount);
        log.log(Level.INFO, "Thread " + getName() + " finished.");
    }

    /**
     *
     * @param firstMatrix first matrix
     * @param secondMatrix second matrix
     * @param threadCount  thread count
     * @return result
     */
    public static int[][] matrixMultiplication(int[][] firstMatrix, int[][] secondMatrix, int threadCount){
        int rowCount=firstMatrix.length;
        int colCount=secondMatrix[0].length;
        int[][] result = new int[rowCount][colCount];
        int firstIndex = 0;
        int cellsForThread = (rowCount * colCount) / threadCount;
        MatrixMultiplication[] matrixMultiplications = new MatrixMultiplication[threadCount];

        for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
            int lastIndex = firstIndex + cellsForThread;
            if (threadIndex == 0) {
                lastIndex = rowCount * colCount;
            }
            matrixMultiplications[threadIndex] = new MatrixMultiplication(firstMatrix, secondMatrix, result, firstIndex, lastIndex);
            matrixMultiplications[threadIndex].start();
            firstIndex = lastIndex;
        }

        try {
            for (MatrixMultiplication matrixMultiplication2 : matrixMultiplications)
                matrixMultiplication2.join();
        }
        catch (InterruptedException e) {
            log.log(Level.WARNING, "thread not completed");
            e.printStackTrace();
        }
        return result;
    }

    /** Output of the matrix to a file.

     * @param recordWriter record file.
     * @param matrix return matrix.
     * @throws IOException
     */
    private static void printMatrix(final FileWriter recordWriter,
                                    final int[][] matrix) throws IOException
    {
        boolean hasNegative = false;
        int     maxValue    = 0;

        for (final int[] row : matrix) {
            for (final int element : row) {
                int temp = element;
                if (element < 0) {
                    hasNegative = true;
                    temp = -temp;
                }
                if (temp > maxValue)
                    maxValue = temp;
            }
        }

        int len = Integer.toString(maxValue).length() + 1;
        if (hasNegative)
            ++len;

        final String formatString = "%" + len + "d";


        for (final int[] row : matrix) {
            for (final int element : row)
                recordWriter.write(String.format(formatString, element));

            recordWriter.write("\n");
        }
    }

    /**
     * return 3 matrix.
     *
     * @param fileName     file name.
     * @param firstMatrix  first matrix.
     * @param secondMatrix second matrix.
     * @param resultMatrix result matrix.
     */
    public static void printAllMatrix(final String fileName,
                                       final int[][] firstMatrix,
                                       final int[][] secondMatrix,
                                       final int[][] resultMatrix)
    {
        try (final FileWriter recordWriter = new FileWriter(fileName, false)) {
            recordWriter.write("First matrix:\n");
            printMatrix(recordWriter, firstMatrix);

            recordWriter.write("\nSecond matrix:\n");
            printMatrix(recordWriter, secondMatrix);

            recordWriter.write("\nResult matrix:\n");
            printMatrix(recordWriter, resultMatrix);
            log.log(Level.INFO,"write all file");
        }
        catch (IOException e) {
            log.log(Level.WARNING, "error write");
            e.printStackTrace();
        }
    }



}
