package com.jeesim2.distributedcorrelation;

public class LocalStats {

    final public int rowCount;
    final public int featureCount;

    final public double[] sums;
    final public double[] variances;
    final public double[][] multiplySums;

    public LocalStats(double[][] features) {
        this.rowCount = features.length;
        this.featureCount = features[0].length;
        this.sums = new double[featureCount];
        this.variances = new double[featureCount];
        this.multiplySums = new double[featureCount][featureCount];
        calculate(features);
    }

    private void calculate(double[][] features) {
        calculateSumOfEachFeatures(features);
        calculateVarianceOfEachFeature(features);
        calculateSumOfInterFeatureMultiply(features);
    }

    private void calculateSumOfEachFeatures(double[][] features) {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < featureCount; j++) {
                sums[j] += features[i][j];
            }
        }
    }

    private void calculateVarianceOfEachFeature(double[][] features) {
        for (int featureOffset = 0; featureOffset < featureCount; featureOffset++) {
            double distanceSquareSum = 0;
            for (int rowOffset = 0; rowOffset < rowCount; rowOffset++) {
                double entryFeatureVariable = features[rowOffset][featureOffset];
                distanceSquareSum += Math.pow(entryFeatureVariable - (sums[featureOffset] / rowCount), 2);
            }
            variances[featureOffset] = distanceSquareSum / rowCount;
        }
    }

    private void calculateSumOfInterFeatureMultiply(double[][] features) {
        for (int rowOffset = 0; rowOffset < rowCount; rowOffset++) {
            for (int i = 0; i < featureCount; i++) {
                for (int j = 0; j < featureCount; j++) {
                    multiplySums[i][j] += features[rowOffset][i] * features[rowOffset][j];
                }
            }
        }
    }
}
