package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;

import java.util.*;

public class DuplicatedRecordProcessorImpl implements RecordProcessor {

    private Set<Record> recordsCache;
    private Record currentRecord;
    private RecordProcessor nextRecordProcessor;

    public DuplicatedRecordProcessorImpl() {
        recordsCache = new HashSet<Record>();
    }

    @Override
    public void setSuccessor(RecordProcessor successor) {
        nextRecordProcessor = successor;
    }


    @Override
    public void processRecords(List<Record> recordsToBeMarked) {
        List<Record> markedRecords = this.markRecords(recordsToBeMarked);
        if(nextRecordProcessor != null) {
            nextRecordProcessor.processRecords(markedRecords);
        }
    }

    protected List<Record> markRecords(List<Record> records) {
        for(Record record: records) {
            if(!record.isValid())
                continue;
            currentRecord = record;
            markCurrentRecord();
            storeCurrentRecord();
        }
        return records;
    }

    private void storeCurrentRecord() {
        recordsCache.add(currentRecord);
    }

    private void markCurrentRecord() {
        if(isDuplicate()) {
            currentRecord.setFlag(Flag.DUPLICATE_ENTRY);
        } else {
            currentRecord.setFlag(Flag.VALID);
        }
    }

    private boolean isDuplicate() {
        for(Record cachedRecords: recordsCache) {
            if(currentRecord.getUserTicket() == cachedRecords.getUserTicket() &&
                    currentRecord.getServedTicket() == cachedRecords.getServedTicket()) {
                return true;
            }
        }
        return false;
    }
}
