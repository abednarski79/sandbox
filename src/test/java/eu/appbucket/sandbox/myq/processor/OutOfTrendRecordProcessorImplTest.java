package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OutOfTrendRecordProcessorImplTest {

    private OutOfTrendRecordProcessorImpl marker;

    @Before
    public void setup() {
        marker = new OutOfTrendRecordProcessorImpl();
    }

    private class Sample {
        private List<Record> records = new ArrayList<Record>();
        int i = 1;
        private void addRecord(long timeStamp, int servicedNumber) {
            Record record = new Record();
            record.setTimeStamp(timeStamp);
            record.setServedTicket(servicedNumber);
            record.setId(i++);
            records.add(record);
        }
        public List<Record> getRecords() {
            return records;
        }
    }
    
    @Test
    public void testSampleOfData() {
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
        sample.addRecord(1410772306, 272);
        sample.addRecord(1410772334, 88);
        sample.addRecord(1410773742, 92);
        sample.addRecord(1410774516, 92);
        sample.addRecord(1410774760, 93);
        sample.addRecord(1410775099, 96);
        sample.addRecord(1410775856, 97);
        sample.addRecord(1410775984, 102);
        sample.addRecord(1410776757, 105);
        sample.addRecord(1410777156, 106);
        sample.addRecord(1410777180, 112);
        sample.addRecord(1410777430, 100);
        sample.addRecord(1410777634, 115);
        sample.addRecord(1410777837, 168);
        sample.addRecord(1410777881, 116);
        sample.addRecord(1410777912, 121);
        sample.addRecord(1410778418, 122);
        sample.addRecord(1410778520, 122);
        sample.addRecord(1410779608, 131);
        sample.addRecord(1410780069, 134);
        sample.addRecord(1410780523, 144);
        sample.addRecord(1410780554, 145);
        sample.addRecord(1410780752, 146);
        sample.addRecord(1410780806, 236);
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
        sample.addRecord(1410815541, 21);
        List<Record> records = sample.getRecords();
        marker.markRecords(records);
        for(Record record: records) {
            System.out.println(record);

            if(record.getTimeStamp() == 1410772306
                || record.getTimeStamp() == 1410777837
                || record.getTimeStamp() == 1410780806
                || record.getTimeStamp() == 1410786920
                || record.getTimeStamp() == 1410804578
                || record.getTimeStamp() == 1410815541) {
                Assert.assertEquals(record.toString() + ": " + record.getTimeStamp(),Flag.UNEXPECTED_VALUE, record.getFlag());
            }
        }
    }


}
