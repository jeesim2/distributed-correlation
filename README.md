# distributed-correlation
This is a package provide distributed correlation compute.
When we have billions of rows across several machine there might difficulties to collect all data into a single machine.
At that time we can compute total correlation with several statistical summaries.

# Central vs Distributed

### given data
```
    double[][] testData = new double[][]{
            {1, 2, 4, 3, 0, 3, 95, 252},
            {3, 1, 1, 3, 23, 3, 9, 211},
            {3, 5, 8, 3, 1, 12, 19, 654},
            {8, 6, 4, 7, 7, 65, 9, 245},
            {1, 2, 3, 5, 44, 3, 9, 342},
            {2, 5, 9, 3, 22, 76, 9, 1},
            {1, 26, 7, 3, 7, 24, 9, 323}
    };
```

## Single machine calculation with Commons-Math
```
PearsonsCorrelation pc = new PearsonsCorrelation(testData);
RealMatrix rm = pc.getCorrelationMatrix();
rm.getData(); // <-- this will return correlation matrix
-----
the result is 8x8 matrix ( because we have 8 dimensions )
1.0,-0.16522172239891145,-0.1538941861588454,0.7511979080926176,-0.2499709732662804,0.5194481540200064,-0.30061225995022467,-0.010430482065829414,
-0.16522172239891145,1.0,0.42240432707652015,-0.14935598323733362,-0.3001115748582165,0.13538377906909307,-0.25189284700012293,0.0905155178297097,
-0.1538941861588454,0.42240432707652015,1.0,-0.32221816559634964,-0.35034572699423827,0.5068800296091045,-0.12435740322712624,0.08701383986665655,
0.7511979080926176,-0.14935598323733362,-0.32221816559634964,1.0,0.17973527053939675,0.36286519022218816,-0.27157052726627057,-0.04022795234533219,
-0.2499709732662804,-0.3001115748582165,-0.35034572699423827,0.17973527053939675,1.0,-0.08647345173262828,-0.4647483604970352,-0.30029929753314133,
0.5194481540200064,0.13538377906909307,0.5068800296091045,0.36286519022218816,-0.08647345173262828,1.0,-0.3627681799671043,-0.5443510243133955,
-0.30061225995022467,-0.25189284700012293,-0.12435740322712624,-0.27157052726627057,-0.4647483604970352,-0.3627681799671043,1.0,0.010606088380907614,
-0.010430482065829414,0.0905155178297097,0.08701383986665655,-0.04022795234533219,-0.30029929753314133,-0.5443510243133955,0.010606088380907614,1.0,

```

## Distributed calculation with this package
```
// simulate rows are distributed across three nodes

// first node
double[][] range1 = Arrays.copyOfRange(testData, 0, 3);
LocalStats localStats1 = new LocalStats(range1);

// second node
double[][] range2 = Arrays.copyOfRange(testData, 3, 5);
LocalStats localStats2 = new LocalStats(range2);

// third node
double[][] range3 = Arrays.copyOfRange(testData, 5, 7);
LocalStats localStats3 = new LocalStats(range3);


// calculate GlobalStats with localStats list.
GlobalStats globalStats = new GlobalStats(Arrays.asList(localStats1, localStats2, localStats3));
globalStats.correlationCoefficient; // <-- this will return correlation matrix

-----
the result is 8x8 matrix ( because we have 8 dimensions )

1.0,-0.1652217,-0.1538942,0.7511979,-0.249971,0.5194482,-0.3006123,-0.0104305,
-0.1652217,1.0,0.4224043,-0.149356,-0.3001116,0.1353838,-0.2518928,0.0905155,
-0.1538942,0.4224043,1.0,-0.3222182,-0.3503457,0.50688,-0.1243574,0.0870138,
0.7511979,-0.149356,-0.3222182,1.0,0.1797353,0.3628652,-0.2715705,-0.040228,
-0.249971,-0.3001116,-0.3503457,0.1797353,1.0,-0.0864735,-0.4647484,-0.3002993,
0.5194482,0.1353838,0.50688,0.3628652,-0.0864735,1.0,-0.3627682,-0.544351,
-0.3006123,-0.2518928,-0.1243574,-0.2715705,-0.4647484,-0.3627682,1.0,0.0106061,
-0.0104305,0.0905155,0.0870138,-0.040228,-0.3002993,-0.544351,0.0106061,1.0,
```
