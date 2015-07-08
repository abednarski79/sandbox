package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Record;

import java.util.List;

public interface RecordProcessor {

    void processRecords(List<Record> recordsToBeMarked);
    void setSuccessor(RecordProcessor successor);
}
