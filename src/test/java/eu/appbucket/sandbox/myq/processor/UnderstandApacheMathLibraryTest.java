package eu.appbucket.sandbox.myq.processor;
import eu.appbucket.sandbox.myq.record.Record;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

import java.util.*;

public class UnderstandApacheMathLibraryTest {

    private SimpleRegression regression;

    @Before
    public void setup() {
        regression = new SimpleRegression(true);
    }

    @Test
    @Ignore
    public void testRegression() {
        regression.addData(0,0);
        regression.addData(1, 1);
        System.out.println(regression.getSlopeStdErr()); // ???
        System.out.println(regression.getRSquare()); // R square
        System.out.println(regression.getMeanSquareError()); // ???
        System.out.println(regression.getRegressionSumSquares()); // ???
        System.out.println(regression.getSumSquaredErrors()); // ???
        System.out.println(regression.getTotalSumSquares()); // ???
    }

    @Test
    public void testRegressionWithRealData() {
        regression.addData(1410768337, 25);
        regression.addData(1410768461, 26);
        regression.addData(1410769032, 36);
        regression.addData(1410769626, 44);
        regression.addData(1410769697, 45);
        regression.addData(1410769708, 45);
        regression.addData(1410769711, 45);
        regression.addData(1410769824, 46);
        regression.addData(1410769858, 46);
        regression.addData(1410770225, 52);
        regression.addData(1410770531, 50);
        regression.addData(1410770883, 58);
        regression.addData(1410771027, 60);
        regression.addData(1410771364, 61);
        regression.addData(1410772306, 272);
        regression.addData(1410773742, 88);
        regression.addData(1410774516, 92);
        regression.addData(1410774760, 92);
        regression.addData(1410775099, 93);
        regression.addData(1410775856, 96);
        regression.addData(1410775984, 97);
        regression.addData(1410776757, 102);
        regression.addData(1410777156, 105);
        regression.addData(1410777180, 106);
        regression.addData(1410777430, 112);
        regression.addData(1410777634, 100);
        regression.addData(1410777837, 115);
        regression.addData(1410777881, 168);
        regression.addData(1410777912, 116);
        regression.addData(1410778418, 121);
        regression.addData(1410778520, 122);
        regression.addData(1410779608, 131);
        System.out.println("y = ax + b"); // ???
        System.out.println("a: " + regression.getSlope());
        System.out.println("b: " + regression.getIntercept());
    }

    // http://www.mathsisfun.com/data/standard-deviation.html
    @Test
    public void testVariances() {
        Variance variance = new Variance(false);
        double varianceValue = variance.evaluate(new double[]{206, 76, -224, 36, -94});
        System.out.println(varianceValue);
        Assert.assertEquals(21704d, varianceValue, 0d);
    }

    // http://www.mathsisfun.com/data/standard-deviation.html
    @Test
    public void testStandardDeviationOfDogs() {
        StandardDeviation standardDeviation = new StandardDeviation(false);
        double standardDeviationValue = standardDeviation.evaluate(new double[]{206, 76, -224, 36, -94});
        System.out.println(standardDeviationValue);
        Assert.assertEquals(147d, standardDeviationValue, 0.5d);
    }

    @Test
    public void testStandardDeviationOfSimpleNumber() {
        StandardDeviation standardDeviation = new StandardDeviation(false);
        System.out.println(standardDeviation.evaluate(new double[]{1,1,2}));
    }

    // http://www.mathsisfun.com/data/standard-deviation.html
    @Test
    public void testMeanDogs() {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.addValue(600d);
        stats.addValue(470d);
        stats.addValue(170d);
        stats.addValue(430d);
        stats.addValue(300d);
        System.out.println(stats.getMean());
        Assert.assertEquals(394d, stats.getMean(), 0d);
    }

    // http://www.mathsisfun.com/data/standard-deviation.html
    @Test
    public void testThresholdOfDogs() {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.addValue(600d);
        stats.addValue(470d);
        stats.addValue(170d);
        stats.addValue(430d);
        stats.addValue(300d);
        double mean = stats.getMean();
        StandardDeviation standardDeviation = new StandardDeviation(false);
        double standardDeviationValue = standardDeviation.evaluate(new double[]{206, 76, -224, 36, -94});
        Assert.assertFalse(valueFitsTheThreshold(600d, mean, standardDeviationValue));
        Assert.assertTrue(valueFitsTheThreshold(470d, mean, standardDeviationValue));
        Assert.assertFalse(valueFitsTheThreshold(170d, mean, standardDeviationValue));
        Assert.assertTrue(valueFitsTheThreshold(430d, mean, standardDeviationValue));
        Assert.assertTrue(valueFitsTheThreshold(300d, mean, standardDeviationValue));
    }

    private static boolean valueFitsTheThreshold(double valueToTest, double mean, double standardDeviation) {
        double minThreshold = mean - standardDeviation;
        double maxThreshold = mean + standardDeviation;
        if(valueToTest > minThreshold && valueToTest < maxThreshold) {
            return true;
        }
        return false;
    }

    @Test
    public void testDistanceBetweenRegressionLineAndPointOnTheLine() {
        Vector2D pointNotOnTheLine = new Vector2D(0,0);
        Set<Vector2D> observations = new HashSet<Vector2D>();
        observations.add(new Vector2D(0, 0));
        observations.add(new Vector2D(1, 1));
        double distance = calculateDistanceBetweenPointAndObservations(pointNotOnTheLine, observations);
        System.out.println("Distance: " + distance);
        Assert.assertEquals(0, distance, 0.01d);
    }

    @Test
    public void testDistanceBetweenRegressionLineAndPointWhereLineInterceptIsAvailable() {
        Vector2D pointNotOnTheLine = new Vector2D(0,1);
        Set<Vector2D> observations = new HashSet<Vector2D>();
        observations.add(new Vector2D(0, 0));
        observations.add(new Vector2D(1, 1));
        double distance = calculateDistanceBetweenPointAndObservations(pointNotOnTheLine, observations);
        System.out.println("Distance: " + distance);
        Assert.assertEquals(Math.sqrt(2) / 2, distance, 0.01d);
    }

    @Test
    public void testDistanceBetweenRegressionLineAndPointWhereLineInterceptIsNotAvailable() {
        Vector2D pointNotOnTheLine = new Vector2D(0,1);
        Set<Vector2D> observations = new HashSet<Vector2D>();
        observations.add(new Vector2D(1, 0));
        observations.add(new Vector2D(1, 1));
        double distance = calculateDistanceBetweenPointAndObservations(pointNotOnTheLine, observations);
        System.out.println("Distance: " + distance);
        Assert.assertEquals(1d, distance, 0.01d);
    }

    private double calculateDistanceBetweenPointAndObservations(Vector2D point, Set<Vector2D> observations) {
        double distance = 0;
        if(areAllObservationsOnVerticalLine(observations)) {
            Vector2D observation = observations.iterator().next();
            distance = Math.abs(observation.getX() - point.getX());
        } else {
            SimpleRegression regression = new SimpleRegression(true);
            for(Vector2D observation: observations) {
                regression.addData(observation.getX(), observation.getY());
            }
            Line line = buildLineFromRegression(regression);
            distance = line.distance(point);
        }
        return distance;
    }

    private boolean areAllObservationsOnVerticalLine(Set<Vector2D> observations) {
        double firstObservationX = observations.iterator().next().getX();
        for(Vector2D point: observations) {
            if(point.getX() != firstObservationX) {
                return false;
            }
        }
        return true;
    }

    private Line buildLineFromRegression(SimpleRegression regression) {
        Vector2D linePoint1 = new Vector2D(0, findYForXOnLine(0, regression.getSlope(), regression.getIntercept()));
        Vector2D linePoint2 = new Vector2D(1, findYForXOnLine(1, regression.getSlope(), regression.getIntercept()));
        Line line = new Line(linePoint1, linePoint2, 0);
        return line;
    }

    private double findYForXOnLine(double x, double slope, double intercept) {
        double y = intercept + slope * x;
        return y;
    }

    @Test
    public void testMarkingOfPointsAsInvalidBecauseAreAboveCalculatedThreshold() {
        // valid observations points
        Vector2D validRegressionPoint1 = new Vector2D(0,0);
        Vector2D validRegressionPoint2 = new Vector2D(1,1);
        Vector2D validRegressionPoint3 = new Vector2D(2,2);
        // point to measure the variation
        Vector2D validTestPoint1 = new Vector2D(0,1);
        Vector2D validTestPoint2 = new Vector2D(2,1);
        Vector2D validTestPoint3 = new Vector2D(2,3);
        Vector2D validTestPoint4 = new Vector2D(4,3);
        // point which should be out of threshold
        Vector2D invalidTestPoint1 = new Vector2D(0,3);
        Vector2D invalidTestPoint2 = new Vector2D(4,1);
        // calculate their distance from regression line
        Set<Vector2D> observations = new HashSet<Vector2D>();
        observations.add(validRegressionPoint1);
        observations.add(validRegressionPoint2);
        observations.add(validRegressionPoint3);
        double validTestPoint1Distance = calculateDistanceBetweenPointAndObservations(validTestPoint1, observations);
        double validTestPoint2Distance = calculateDistanceBetweenPointAndObservations(validTestPoint2, observations);
        double validTestPoint3Distance = calculateDistanceBetweenPointAndObservations(validTestPoint3, observations);
        Assert.assertEquals(Math.sqrt(2) / 2, validTestPoint1Distance, 0.01d);
        Assert.assertEquals(Math.sqrt(2) / 2, validTestPoint2Distance, 0.01d);
        Assert.assertEquals(Math.sqrt(2) / 2, validTestPoint3Distance, 0.01d);
        double invalidTestPoint1Distance = calculateDistanceBetweenPointAndObservations(invalidTestPoint1, observations);
        double invalidTestPoint2Distance = calculateDistanceBetweenPointAndObservations(invalidTestPoint2, observations);
        Assert.assertEquals(3 * Math.sqrt(2) / 2, invalidTestPoint1Distance, 0.01d);
        Assert.assertEquals(3 * Math.sqrt(2) / 2, invalidTestPoint2Distance, 0.01d);
        // calculate variation
        StandardDeviation standardDeviation = new StandardDeviation(false);
        double margin = standardDeviation.evaluate(new double[]{validTestPoint1Distance, validTestPoint2Distance, validTestPoint3Distance, invalidTestPoint1Distance, invalidTestPoint2Distance});
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.addValue(validTestPoint1Distance);
        stats.addValue(validTestPoint2Distance);
        stats.addValue(validTestPoint3Distance);
        stats.addValue(invalidTestPoint1Distance);
        stats.addValue(invalidTestPoint2Distance);
        double mean = stats.getMean();
        double maxMargin = mean + margin;
        double minMargin = mean - margin;
        System.out.println("min = " + minMargin);
        System.out.println("max = " + maxMargin);
        // decide which points are out of the standard variation threshold
        System.out.println(validTestPoint1Distance);
        Assert.assertTrue(validTestPoint1Distance > minMargin && validTestPoint1Distance < maxMargin);
        System.out.println(validTestPoint2Distance);
        Assert.assertTrue(validTestPoint2Distance > minMargin && validTestPoint2Distance < maxMargin);
        System.out.println(validTestPoint3Distance);
        Assert.assertTrue(validTestPoint3Distance > minMargin && validTestPoint3Distance < maxMargin);
        Assert.assertTrue(invalidTestPoint1Distance < minMargin || invalidTestPoint1Distance > maxMargin);
        Assert.assertTrue(invalidTestPoint2Distance < minMargin || invalidTestPoint2Distance > maxMargin);
    }

    private class Point {
        private double x;
        private double y;
        public void setX(double x) { this.x = x; }
        public void setY(double y) { this.y = y; }
        public double getX() { return x; }
        public double getY() { return y; }
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
    
    private class Store {
        private Set<Point> data = new LinkedHashSet<Point>();
        private void addData(double x, double y) {
            Point point = new Point();
            point.setX(x);
            point.setY(y);
            data.add(point);
        }
        public Set<Point> getData() {
            return data;
        }
    }

    @Test
    public void testRealData() {
        Store store = new Store();
        store.addData(1410768337, 25);
        store.addData(1410768461, 26);
        store.addData(1410769032, 36);
        store.addData(1410769626, 44);
        store.addData(1410769697, 45);
        store.addData(1410769708, 45);
        store.addData(1410769711, 45);
        store.addData(1410769824, 46);
        store.addData(1410769858, 46);
        store.addData(1410770225, 52);
        store.addData(1410770531, 50);
        store.addData(1410770883, 58);
        store.addData(1410771027, 60);
        store.addData(1410771364, 61);
        store.addData(1410772306, 272);
        store.addData(1410773742, 88);
        store.addData(1410774516, 92);
        store.addData(1410774760, 92);
        store.addData(1410775099, 93);
        store.addData(1410775856, 96);
        store.addData(1410775984, 97);
        store.addData(1410776757, 102);
        store.addData(1410777156, 105);
        store.addData(1410777180, 106);
        store.addData(1410777430, 112);
        store.addData(1410777634, 100);
        store.addData(1410777837, 115);
        store.addData(1410777881, 168);
        store.addData(1410777912, 116);
        store.addData(1410778418, 121);
        store.addData(1410778520, 122);
        store.addData(1410779608, 131);
        SimpleRegression regression = new SimpleRegression(true);
        for(Point point: store.getData()) {
            regression.addData(point.getX(), point.getY());
        }
        DescriptiveStatistics stats = new DescriptiveStatistics();
        double [] distances = new double[store.getData().size()];
        int i = 0;
        for(Point point: store.getData()) {
            double distanceFromTheMean = findDistanceBetweenRegressionLineAndPoint(regression, point);
            stats.addValue(distanceFromTheMean);
            distances[i++] = distanceFromTheMean;
            System.out.println(point + ", point.distance =  " + distanceFromTheMean);
        }
        double mean = stats.getMean();
        System.out.println("distance.mean =  " + mean);
        StandardDeviation standardDeviation = new StandardDeviation(false);
        double deviation = standardDeviation.evaluate(distances);
        System.out.println("distance.deviation =  " + deviation);
        double margin = mean + deviation;
        System.out.println("distance.margin =  " + margin);
        for(Point point: store.getData()) {
            double distance = findDistanceBetweenRegressionLineAndPoint(regression, point);
            if(distance < margin) {
                System.out.println(point + " is Valid, point.distance = " + distance + ", distance.margin = " + margin);
            } else {
                System.out.println(point + " is Invalid, point.distance = " + distance + ", distance.margin = " + margin);
                Assert.assertTrue(point.getY() == 272 || point.getY() == 168);
            }
        }
    }

    private double findDistanceBetweenRegressionLineAndPoint(SimpleRegression regression, Point point) {
        double predictedY = regression.predict(point.getX());
        double distance = Math.abs(Math.abs(point.getY()) - Math.abs(predictedY));
        return distance;
    }


    private class Sample {
        private List<Record> records = new ArrayList<Record>();
        int i = 1;
        private void addRecord(long timeStamp, int servicedNumber) {
            Record record = new Record();
            record.setTimeStamp(timeStamp * 1000);
            record.setServedTicket(servicedNumber);
            record.setId(i++);
            records.add(record);
        }
        public List<Record> getRecords() {
            return records;
        }
    }

    @Test
    public void testCalculateRegressionForFilteredOutData() {
        Sample sample = new Sample();
        sample.addRecord(1410768337, 25);
        sample.addRecord(1410768461, 26);
        sample.addRecord(1410769032, 36);
        sample.addRecord(1410769626, 44);
        sample.addRecord(1410769697, 45);
        sample.addRecord(1410769708, 45);
        sample.addRecord(1410769711, 45);
        sample.addRecord(1410769824, 46);
        sample.addRecord(1410769858, 46);
        sample.addRecord(1410770225, 52);
        sample.addRecord(1410770531, 50);
        sample.addRecord(1410770883, 58);
        sample.addRecord(1410771027, 60);
        sample.addRecord(1410771364, 61);
        sample.addRecord(1410772334, 50);
        sample.addRecord(1410773742, 88);
        sample.addRecord(1410774516, 92);
        sample.addRecord(1410774760, 92);
        sample.addRecord(1410775099, 93);
        sample.addRecord(1410775856, 96);
        sample.addRecord(1410775984, 97);
        sample.addRecord(1410776757, 102);
        sample.addRecord(1410777156, 105);
        sample.addRecord(1410777180, 106);
        sample.addRecord(1410777430, 112);
        sample.addRecord(1410777634, 100);
        sample.addRecord(1410777837, 115);
        sample.addRecord(1410777881, 168);
        sample.addRecord(1410777912, 116);
        sample.addRecord(1410778418, 121);
        sample.addRecord(1410778520, 122);
        sample.addRecord(1410779608, 131);
        sample.addRecord(1410780069, 134);
        sample.addRecord(1410780523, 144);
        sample.addRecord(1410780554, 145);
        sample.addRecord(1410780752, 146);
        sample.addRecord(1410780824, 149);
        sample.addRecord(1410783252, 159);
        sample.addRecord(1410783625, 172);
        sample.addRecord(1410783695, 172);
        sample.addRecord(1410784422, 178);
        sample.addRecord(1410784432, 162);
        sample.addRecord(1410785910, 195);
        sample.addRecord(1410786081, 196);
        sample.addRecord(1410786710, 204);
        sample.addRecord(1410786806, 205);
        sample.addRecord(1410786864, 206);
        sample.addRecord(1410786920, 252);
        sample.addRecord(1410788000, 211);
        sample.addRecord(1410788736, 218);
        sample.addRecord(1410789960, 229);
        sample.addRecord(1410790145, 231);
        sample.addRecord(1410790630, 233);
        sample.addRecord(1410793777, 254);
        sample.addRecord(1410794530, 261);
        sample.addRecord(1410794576, 263);
        sample.addRecord(1410798064, 299);
        sample.addRecord(1410798307, 294);
        sample.addRecord(1410799129, 299);
        sample.addRecord(1410804578, 370);
        sample.addRecord(1410804599, 361);
        sample.addRecord(1410804617, 362);
        sample.addRecord(1410809081, 400);
        sample.addRecord(1410809138, 400);
        SimpleRegression regression = new SimpleRegression(true);
        for(Record point: sample.getRecords()) {
            regression.addData(point.getTimeStamp(), point.getServedTicket());
        }
        System.out.print("filtered slope = " + regression.getSlope() + ", intercept = " + regression.getIntercept());
    }

    @Test
    public void testCalculateRegressionForData() {
        Sample sample = new Sample();
        sample.addRecord(1410849840, 9);
        sample.addRecord(1410857340, 32);
        sample.addRecord(1410860700, 47);
        sample.addRecord(1410861000, 47);
        sample.addRecord(1410864480, 69);
        sample.addRecord(1410865920, 79);
        sample.addRecord(1410865980, 100);
        sample.addRecord(1410866940, 84);
        sample.addRecord(1410867780, 90);
        sample.addRecord(1410869640, 113);
        sample.addRecord(1410876540, 172);
        sample.addRecord(1410876600, 174);
        sample.addRecord(1410878940, 210);
        sample.addRecord(1410879000, 212);
        sample.addRecord(1410879120, 214);
        sample.addRecord(1410879720, 217);
        sample.addRecord(1410880260, 223);
        sample.addRecord(1410887040, 200);
        sample.addRecord(1410887040, 200);
        sample.addRecord(1410891960, 320);
        sample.addRecord(1410893640, 302);
        sample.addRecord(1410893640, 310);
        sample.addRecord(1410893700, 360);
        sample.addRecord(1410895440, 340);
        sample.addRecord(1410895500, 338);
        sample.addRecord(1410895500, 320);
        sample.addRecord(1410895560, 338);
        SimpleRegression regression = new SimpleRegression(true);
        for(Record point: sample.getRecords()) {
            regression.addData(point.getTimeStamp(), point.getServedTicket());
        }
        System.out.print("actual slope = " + regression.getSlope() + ", intercept = " + regression.getIntercept());
    }
}
