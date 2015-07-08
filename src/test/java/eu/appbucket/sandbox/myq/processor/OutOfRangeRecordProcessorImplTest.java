package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OutOfRangeRecordProcessorImplTest {

    private OutOfRangeRecordProcessorImpl processor;

    @Before
    public void setup() {
        processor = new OutOfRangeRecordProcessorImpl(0);
    }

    @Test
    public void test() {
        List<Record> recordsToBeMarked = new ArrayList<Record>();
        Record record1 = new Record();
        record1.setDate(new Date(100));
        record1.setServedTicket(1);
        recordsToBeMarked.add(record1);
        Record record2 = new Record();
        record2.setDate(new Date(200));
        record2.setServedTicket(2);
        recordsToBeMarked.add(record2);
        Record record3 = new Record();
        record3.setDate(new Date(300));
        record3.setServedTicket(3);
        recordsToBeMarked.add(record3);
        Record record4 = new Record();
        record4.setDate(new Date(800));
        record4.setServedTicket(4);
        recordsToBeMarked.add(record4);
        processor.processRecords(recordsToBeMarked);
        Assert.assertEquals("Record 1", Flag.VALID, recordsToBeMarked.get(0).getFlag());
        Assert.assertEquals("Record 2", Flag.VALID, recordsToBeMarked.get(1).getFlag());
        Assert.assertEquals("Record 3", Flag.VALID, recordsToBeMarked.get(2).getFlag());
        Assert.assertEquals("Record 4", Flag.DELTA_VALUE_NOT_IN_RANGE, recordsToBeMarked.get(3).getFlag());
    }
}
