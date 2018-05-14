package com.darichey.minesweeper;

import java.util.Arrays;

public final class Matrix {

    public static Matrix eliminate(Matrix matrix) {
        double[][] mat = arrayCopy(matrix.values);

        int row = 0;
        int col = 0;
        while ((row < mat.length) && (col < mat[row].length)) {
            int maxRow = row;

            for (int currentRow = row + 1; currentRow < mat.length; ++currentRow) {
                if (Math.abs(mat[currentRow][col]) > Math.abs(mat[maxRow][col])) {
                    maxRow = currentRow;
                }
            }

            if (mat[maxRow][col] != 0) {
                if (row != maxRow) {
                    double[] temp = mat[row];
                    mat[row] = mat[maxRow];
                    mat[maxRow] = temp;
                }

                double currentValue = mat[row][col];
                mat[row] = multiply(mat[row], 1 / currentValue);

                for (int iterRow = row + 1; iterRow < mat.length; ++iterRow) {
                    double mulVal = -mat[iterRow][col];
                    if (mulVal != 0) {
                        mat[row] = multiply(mat[row], mulVal);
                        mat[iterRow] = add(mat[iterRow], mat[row]);
                        mat[row] = multiply(mat[row], 1 / mulVal);
                    }
                }
                row++;
            }
            col++;
        }

        return new Matrix(mat);
    }

    private static double[] multiply(double[] row, double value) {
        for (int i = 0; i < row.length; i++) {
            row[i] *= value;
        }
        return row;
    }

    private static double[] add(double[] row, double[] toAdd) {
        for (int i = 0; i < row.length; i++) {
            row[i] += toAdd[i];
        }
        return row;
    }

    private static double[][] arrayCopy(double[][] toCopy) {
        double[][] result = new double[toCopy.length][];
        for (int i = 0; i < toCopy.length; i++) {
            result[i] = Arrays.copyOf(toCopy[i], toCopy[i].length);
        }
        return result;
    }

    private final double[][] values;

    public Matrix(double[][] values) {
        this.values = values;
    }

    public int getWidth() {
        return values[0].length;
    }

    public int getHeight() {
        return values.length;
    }

    public double get(int y, int x) {
        return values[y][x];
    }

    public double[][] getValues() {
        return arrayCopy(values);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] aMat : values) {
            for (double anAMat : aMat) {
                s.append((int) (anAMat + 0.0)).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}
