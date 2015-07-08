package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class DuplicateRecordProcessorImplTest {

    private DuplicatedRecordProcessorImpl marker;

    @Before
    public void setup() {
        marker = new DuplicatedRecordProcessorImpl();
    }

    @Test
    public void singleRecordIsValid() {
        List<Record> inputSample = buildSingleRecordSample();
        List<Record> markedSample = marker.markRecords(inputSample);
        Assert.assertEquals(Flag.VALID, markedSample.get(0).getFlag());
    }

    private List<Record> buildSingleRecordSample() {
        List<Record> sample = new ArrayList<Record>();
        Record record = new Record();
        sample.add(record);
        return sample;
    }

    @Test
    public void uniqueRecordsAreValid() {
        List<Record> inputSample = buildUniqueRecordsSample();
        List<Record> markedSample = marker.markRecords(inputSample);
        Assert.assertEquals("Record 1", Flag.VALID, markedSample.get(0).getFlag());
        Assert.assertEquals("Record 2", Flag.VALID, markedSample.get(1).getFlag());
    }

    private List<Record> buildUniqueRecordsSample() {
        List<Record> sample = new ArrayList<Record>();
        Record uniqueRecord1 = new Record();
        uniqueRecord1.setServedTicket(10);
        uniqueRecord1.setUserTicket(1);
        Record uniqueRecord2 = new Record();
        uniqueRecord2.setServedTicket(20);
        uniqueRecord2.setUserTicket(2);
        sample.add(uniqueRecord1);
        sample.add(uniqueRecord2);
        return sample;
    }

    @Test
    public void duplicatedRecordsAreInvalid() {
        List<Record> inputSample = buildDuplicatedRecordsSample();
        List<Record> markedSample = marker.markRecords(inputSample);
        Assert.assertEquals("Original Record 1 expected to be valid", Flag.VALID, markedSample.get(0).getFlag());
        Assert.assertEquals("Duplicated Record 2 expected to be invalid", Flag.DUPLICATE_ENTRY, markedSample.get(1).getFlag());
    }

    private List<Record> buildDuplicatedRecordsSample() {
        List<Record> sample = new ArrayList<Record>();
        Record duplicateRecord = new Record();
        duplicateRecord.setServedTicket(10);
        duplicateRecord.setUserTicket(1);
        sample.add(duplicateRecord);
        duplicateRecord = new Record();
        duplicateRecord.setServedTicket(10);
        duplicateRecord.setUserTicket(1);
        sample.add(duplicateRecord);
        return sample;
    }
}
