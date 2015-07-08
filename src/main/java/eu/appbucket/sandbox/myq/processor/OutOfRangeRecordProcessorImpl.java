package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutOfRangeRecordProcessorImpl implements RecordProcessor {

    private Set<Record> recordsCache;
    private Record currentRecord;
    private RecordProcessor nextRecordProcessor;
    private long timeBase;
    private List<Record> recordsToBeMarked;
    double timeToServedTicketMedian;

    public OutOfRangeRecordProcessorImpl(long timeBase) {
        recordsCache = new HashSet<Record>();
        this.timeBase=timeBase;
    }

    @Override
    public void setSuccessor(RecordProcessor successor) {
        nextRecordProcessor = successor;
    }


    @Override
    public void processRecords(List<Record> recordsToBeMarked) {
        this.recordsToBeMarked = recordsToBeMarked;
        calculateTimeToServedTicketMedian();
        List<Record> markedRecords = this.markRecords(recordsToBeMarked);
        if(nextRecordProcessor != null) {
            nextRecordProcessor.processRecords(markedRecords);
        }
    }

    private void calculateTimeToServedTicketMedian() {
        Median median = new Median();
        double [] deltas = new double[recordsToBeMarked.size()];
        int i = 0;
        double timeDurationFromTimeBaseToRecordTimestamp;
        for(Record record: recordsToBeMarked) {
            timeDurationFromTimeBaseToRecordTimestamp = record.getTimeStamp() - timeBase;
            deltas[i++] = timeDurationFromTimeBaseToRecordTimestamp / record.getServedTicket();
        }
        timeToServedTicketMedian = median.evaluate(deltas);
    }

    private List<Record> markRecords(List<Record> records) {
        for(Record record: records) {
            if(!record.isValid())
                continue;
            currentRecord = record;
            markCurrentRecord();
        }
        return records;
    }

    private void markCurrentRecord() {
        if(isRecordValueOutOfRange()) {
            currentRecord.setFlag(Flag.DELTA_VALUE_NOT_IN_RANGE);
        } else {
            currentRecord.setFlag(Flag.VALID);
        }
    }

    private boolean isRecordValueOutOfRange() {
        double maxCutOffDelta = timeToServedTicketMedian * 1.5;
        double minCutOffDelta = timeToServedTicketMedian / 1.5;
        double deltaDurationToServedTicket = Math.abs(currentRecord.getTimeStamp() - timeBase) / currentRecord.getServedTicket();
        if(deltaDurationToServedTicket > maxCutOffDelta ||
                deltaDurationToServedTicket < minCutOffDelta) {
            return true;
        }
        return false;
    }
}
