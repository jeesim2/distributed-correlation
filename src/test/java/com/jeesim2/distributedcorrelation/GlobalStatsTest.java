package com.jeesim2.distributedcorrelation;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

import java.util.Arrays;

public class GlobalStatsTest {


    double[][] testData = new double[][]{
            {1, 2, 4, 3, 0, 3, 95, 252},
            {3, 1, 1, 3, 23, 3, 9, 211},
            {3, 5, 8, 3, 1, 12, 19, 654},
            {8, 6, 4, 7, 7, 65, 9, 245},
            {1, 2, 3, 5, 44, 3, 9, 342},
            {2, 5, 9, 3, 22, 76, 9, 1},
            {1, 26, 7, 3, 7, 24, 9, 323}
    };


    @Test
    public void test() {

        // correlation by apache commons math
        double[][] corr1 = getCorrelationMatrixByMath3();
        for (double[] x : corr1) {
            for (double y : x) {
                System.out.print(y + ",");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();

        double[][] corr2 = getCorrelationMatrixByDistributedCompute();
        for (double[] x : corr2) {
            for (double y : x) {
                System.out.print(y + ",");
            }
            System.out.println();
        }
    }

    private double[][] getCorrelationMatrixByMath3() {
        PearsonsCorrelation pc = new PearsonsCorrelation(testData);
        RealMatrix rm = pc.getCorrelationMatrix();
        return rm.getData();
    }

    private double[][] getCorrelationMatrixByDistributedCompute() {

        // let's devide all rows into three parts
        double[][] range1 = Arrays.copyOfRange(testData, 0, 3);
        LocalStats localStats1 = new LocalStats(range1);

        double[][] range2 = Arrays.copyOfRange(testData, 3, 5);
        LocalStats localStats2 = new LocalStats(range2);

        double[][] range3 = Arrays.copyOfRange(testData, 5, 7);
        LocalStats localStats3 = new LocalStats(range3);

        // calculate GlobalStats with localStats list.
        GlobalStats globalStats = new GlobalStats(Arrays.asList(localStats1, localStats2, localStats3));
        return globalStats.correlationCoefficient;
    }
}
