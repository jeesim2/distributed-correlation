package com.jeesim2.distributedcorrelation;

import java.util.List;

public class GlobalStats {

    List<LocalStats> localStats;

    public int featureCount;
    public long totalEntryCount;
    public double[] featureSums;
    public double[] featureVariances;

    public double[][] featureMultiplySums;
    public double[][] covariances;
    public double[][] correlationCoefficient;

    public GlobalStats(List<LocalStats> localStats) {
        this.localStats = localStats;
        this.featureCount = localStats.get(0).featureCount;
        this.featureSums = new double[featureCount];
        this.featureVariances = new double[featureCount];
        this.featureMultiplySums = new double[featureCount][featureCount];
        this.covariances = new double[featureCount][featureCount];
        this.correlationCoefficient = new double[featureCount][featureCount];
        calculate();
    }

    private void calculate() {
        calculateTotalEntryCount();
        calculateFeatureSums();
        calculateFeatureMultiplySums();
        calculateCovariances();
        calculateVariance();
        calculateCorrelationCoefficient();
    }


    private void calculateCorrelationCoefficient() {
        for (int featureA = 0; featureA < featureCount; featureA++) {
            for (int featureB = 0; featureB < featureCount; featureB++) {
                correlationCoefficient[featureA][featureB] = Math.round(covariances[featureA][featureB] / (Math.sqrt(featureVariances[featureA]) * Math.sqrt(featureVariances[featureB])) * 10000000D) / 10000000D;
            }
        }
    }

    private void calculateVariance() {
        for (int i = 0; i < featureCount; i++) {
            double numerator = 0;
            for (LocalStats nodeSummary : localStats) {
                double nodeFeatureAvg = nodeSummary.sums[i] / nodeSummary.rowCount;
                double globalFeatureAvg = featureSums[i] / totalEntryCount;
                double varianceContribution = nodeSummary.rowCount * (nodeSummary.variances[i] + Math.pow(nodeFeatureAvg - globalFeatureAvg, 2));
                numerator += varianceContribution;
            }
            featureVariances[i] = numerator / totalEntryCount;
        }
    }

    private void calculateCovariances() {
        for (int featureA = 0; featureA < featureCount; featureA++) {
            for (int featureB = 0; featureB < featureCount; featureB++) {
                double featureAavg = featureSums[featureA] / totalEntryCount;
                double featureBavg = featureSums[featureB] / totalEntryCount;
                double featureMultiplyAvg = featureMultiplySums[featureA][featureB] / totalEntryCount;
                covariances[featureA][featureB] = featureMultiplyAvg - (featureAavg * featureBavg);
            }
        }
    }

    private void calculateFeatureMultiplySums() {
        for (LocalStats nodeSummary : localStats) {
            for (int featureA = 0; featureA < featureCount; featureA++) {
                for (int featureB = 0; featureB < featureCount; featureB++) {
                    featureMultiplySums[featureA][featureB] += nodeSummary.multiplySums[featureA][featureB];
                }
            }
        }
    }

    private void calculateTotalEntryCount() {
        for (LocalStats localStats : this.localStats) {
            totalEntryCount += localStats.rowCount;
        }
    }

    private void calculateFeatureSums() {
        for (LocalStats localStats : this.localStats) {
            for (int i = 0; i < localStats.sums.length; i++) {
                featureSums[i] += localStats.sums[i];
            }
        }
    }
}
