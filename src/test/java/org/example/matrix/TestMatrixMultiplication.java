package org.example.matrix;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.assertNotNull;


public class TestMatrixMultiplication {

    @Test
    public void TestRandomAddMatrix(){
        int[][] matrix=new int[3][3];
        randomMatrix(matrix);
        assertNotNull(matrix);
    }

    @Test
    public void TestThead(){
        int[][] firstMatrix=new int[3][3];
        int[][] secondMatrix=new int[3][3];
        int[][] resultMatrix=new int[3][3];
        int firstIndex=0;
        int lastIndex=9;
        randomMatrix(firstMatrix);
        randomMatrix(secondMatrix);
        MatrixMultiplication matrixMultiplication=new MatrixMultiplication(firstMatrix,secondMatrix,resultMatrix,
                firstIndex,lastIndex);
        int[][] resultMatrix1=matrixMultiplication.matrixMultiplication(firstMatrix,secondMatrix,Runtime.getRuntime().availableProcessors());
        assertNotNull(resultMatrix1);

        matrixMultiplication.printAllMatrix("MatrixMultiplication.txt", firstMatrix, secondMatrix, resultMatrix1);
    }

    private static void randomMatrix(int[][] matrix)
    {
        final Random random = new Random();

        for (int row = 0; row < matrix.length; ++row)
            for (int col = 0; col < matrix[row].length; ++col)
                matrix[row][col] = random.nextInt(100);
    }

}
