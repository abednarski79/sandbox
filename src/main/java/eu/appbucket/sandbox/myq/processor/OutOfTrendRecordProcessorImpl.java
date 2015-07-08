package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.List;

public class OutOfTrendRecordProcessorImpl implements RecordProcessor {

    private List<Point> points = new ArrayList<Point>();
    private SimpleRegression regression = new SimpleRegression(true);
    private double distanceMean;
    private double distanceDeviation;
    private int MARGIN_100_PERCENT = 1;
    private int MARGIN_50_PERCENT = 2;
    private int MARGIN_25_PERCENT = 4;
    private RecordProcessor nextRecordProcessor;

    private class Point {
        private Record record;
        public Point(Record record) {
            this.record = record;
            x = record.getTimeStamp() / 1000;
            y = record.getServedTicket();
        }
        private double x;
        private double y;
        private double distanceToRegressionLine;
        public void setDistanceToRegressionLine(double distanceToRegressionLine) { this.distanceToRegressionLine = distanceToRegressionLine; }
        public double getDistanceToRegressionLine() { return distanceToRegressionLine; }
        public double getX() { return x; }
        public double getY() { return y; }
        public Record getRecord() { return record; }
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", flag=" + record.getFlag() +
                    '}';
        }
    }

    public void setSuccessor(RecordProcessor successor) {
        this.nextRecordProcessor = successor;
    }

    @Override
    public void processRecords(List<Record> recordsToBeMarked) {
        List<Record> markedRecords = this.markRecords(recordsToBeMarked);
        if(nextRecordProcessor != null) {
            nextRecordProcessor.processRecords(markedRecords);
        }
    }

    protected List<Record> markRecords(List<Record> records) {
        convertRecordsToPoints(records);
        calculateStandardRegression();
        calculatePointsDistancesToRegressionLine();
        calculateDistanceMean();
        calculatedDeviationOfDistances();
        setRecordsFlags();
        return records;
    }

    private void convertRecordsToPoints(List<Record> records) {
        for(Record record: records) {
            if(!record.isValid())
                continue;
            points.add(new Point(record));
        }
    }

    private void calculateStandardRegression() {
        for(Point point: points) {
            regression.addData(point.getX(), point.getY());
        }
    }

    private void calculatePointsDistancesToRegressionLine() {
        double distanceToRegressionLine;
        for(Point point: points) {
            distanceToRegressionLine = findDistanceBetweenRegressionLineAndPoint(point);
            point.setDistanceToRegressionLine(distanceToRegressionLine);
        }
    }

    private void calculateDistanceMean() {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        int i = 0;
        for(Point point: points) {
            stats.addValue(point.getDistanceToRegressionLine());
        }
        distanceMean = stats.getMean();
    }

    private double findDistanceBetweenRegressionLineAndPoint(Point point) {
        double predictedY = regression.predict(point.getX());
        double distance = Math.abs(Math.abs(point.getY()) - Math.abs(predictedY));
        return distance;
    }

    private void calculatedDeviationOfDistances() {
        StandardDeviation standardDeviation = new StandardDeviation(false);
        int i = 0;
        double[] distances = new double[points.size()];
        for(Point point: points) {
            distances[i++] = point.getDistanceToRegressionLine();
        }
        distanceDeviation = standardDeviation.evaluate(distances);
    }

    private void setRecordsFlags() {
        int marginDivider = MARGIN_50_PERCENT;
        double acceptableDistanceMargin = (distanceMean + distanceDeviation) / marginDivider;
        for(Point point: points) {
            if(point.getDistanceToRegressionLine() > acceptableDistanceMargin) {
                point.getRecord().setFlag(Flag.UNEXPECTED_VALUE);
            }
        }
    }
}
