package eu.appbucket.sandbox.myq.processor;

import eu.appbucket.sandbox.myq.CsvFileReader;
import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;

public class RealDataMarkerChainImplTest {

    private RecordProcessor processor;

    @Before
    public void setup() {
        RecordProcessor duplicatedRecordProcessor = new DuplicatedRecordProcessorImpl();
        RecordProcessor outOfRangeRecordProcessor = new OutOfRangeRecordProcessorImpl(1412665200);
        //RecordProcessor outOfTrendRecordProcessor = new OutOfTrendRecordProcessorImpl();
        duplicatedRecordProcessor.setSuccessor(outOfRangeRecordProcessor);
        //outOfRangeRecordProcessor.setSuccessor(/*outOfTrendRecordProcessor*/);
        processor = duplicatedRecordProcessor;
    }

    @Test
    public void realDataSep19Test() {
        List<Record> unmarkedRecords =
                CsvFileReader.readRecordsFromCsvFileLocatedInTestResourcesFolder("marked-sample_15-sep-2015.csv");
        processor.processRecords(unmarkedRecords);
        List<Record> actualMarkedRecords = unmarkedRecords;
        List<Record> expectedMarkedRecords =
                CsvFileReader.readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder("marked-sample_15-sep-2015.csv");
        int i = 0;
        for(Record record: expectedMarkedRecords) {
            if(record.isValid()) {
                i++;
            }
        }
        System.out.println("Number of valid records = " + i);
        assertActualFlagsMatchExpectedForAllRecords(expectedMarkedRecords, actualMarkedRecords);
    }

    private void assertActualFlagsMatchExpectedForAllRecords(List<Record> expectedMarkedRecords, List<Record> actualMarkedRecords) {
        Map<Integer, Record> expectedMarkedRecordsMap = convertListToMap(expectedMarkedRecords);
        Record expectedRecord;
        for(Record actualRecord: actualMarkedRecords) {
            expectedRecord = expectedMarkedRecordsMap.get(actualRecord.getId());
            assertRecordFlagsMatch(expectedRecord, actualRecord);
        }
    }

    private Map<Integer, Record> convertListToMap(List<Record> listOfRecords) {
        Map<Integer, Record> mapOfRecords = new HashMap<Integer, Record>();
        for(Record record: listOfRecords) {
            mapOfRecords.put(record.getId(), record);
        }
        return mapOfRecords;
    }

    private void assertRecordFlagsMatch(Record expectedRecord, Record actualRecord) {
        Assert.assertEquals("Flags don't match for record: " +
                expectedRecord, expectedRecord.getFlag(), actualRecord.getFlag());
    }

    @Test
    public void testRealDataPrinter() {
        List<Record> expectedMarkedRecords =
                CsvFileReader.readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder("marked-sample_07-oct-2015_csv.csv");
        Set<Flag> acceptedFlags = new HashSet<Flag>();
        acceptedFlags.add(Flag.VALID);
        acceptedFlags.add(Flag.UNEXPECTED_VALUE);
        acceptedFlags.add(Flag.DELTA_VALUE_NOT_IN_RANGE);
        //acceptedFlags.add(Flag.DUPLICATE_ENTRY);
        System.out.println("accepted flags: " + acceptedFlags);
        int i = 0;
        System.out.println("===============================");
        for(Record record: expectedMarkedRecords) {
            if(acceptedFlags.contains(record.getFlag())) {
                printTimestampAndServedTicketNumberToSample(record);
                i++;
            }
        }
        System.out.println("===============================");
        System.out.println("ID");
        for(Record record: expectedMarkedRecords) {
            if(acceptedFlags.contains(record.getFlag())) {
                printExcelIdColumn(record);
            }
        }
        System.out.println("===============================");
        System.out.println("TIMESTAMP");
        for(Record record: expectedMarkedRecords) {
            if(acceptedFlags.contains(record.getFlag())) {
                printExcelTimeStampColumn(record);
            }
        }
        System.out.println("===============================");
        System.out.println("SERVED TICKET");
        for(Record record: expectedMarkedRecords) {
            if(acceptedFlags.contains(record.getFlag())) {
                printExcelValueColumn(record);
            }
        }
        System.out.println("===============================");
        System.out.println("USER TICKET");
        for(Record record: expectedMarkedRecords) {
            if(acceptedFlags.contains(record.getFlag())) {
                printExcelUserTicketColumn(record);
            }
        }
        System.out.println("total="+i);
    }

    private void printTimestampAndServedTicketNumberToSample(Record record) {
        DecimalFormat formatter = new DecimalFormat("#0");
        double timestampInMilliseconds = record.getTimeStamp();
        double epochTimestamp = timestampInMilliseconds / 1000;
        System.out.println("sample.addRecord("+formatter.format(epochTimestamp)+", "+record.getServedTicket()+");");
    }

    private void printExcelTimeStampColumn(Record record) {
        DecimalFormat formatter = new DecimalFormat("#0");
        double timestampInMilliseconds = record.getTimeStamp();
        double epochTimestamp = timestampInMilliseconds / 1000;
        System.out.println(formatter.format(epochTimestamp));
    }

    private void printExcelValueColumn(Record record) {
        System.out.println(record.getServedTicket() + "");
    }

    private void printExcelUserTicketColumn(Record record) {
        System.out.println(record.getUserTicket() + "");
    }

    private void printExcelIdColumn(Record record) {
        System.out.println(record.getId() + "");
    }

    @Test
    public void realDataOct07Test() {
        String fileName = "marked-sample_07-oct-2015_csv.csv";
        List<Record> unmarkedRecords =
                CsvFileReader.readRecordsFromCsvFileLocatedInTestResourcesFolder(fileName);
        processor.processRecords(unmarkedRecords);
        List<Record> actualMarkedRecords = unmarkedRecords;
        List<Record> expectedMarkedRecords =
                CsvFileReader.readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder(fileName);
        int i = 0;
        for(Record record: expectedMarkedRecords) {
            if(record.isValid()) {
                i++;
            }
        }
        System.out.println("Number of valid records = " + i);
        assertActualFlagsMatchExpectedForAllRecords(expectedMarkedRecords, actualMarkedRecords);
    }

    @Test
    public void realDataSep16Test() {
        RecordProcessor duplicatedRecordProcessor = new DuplicatedRecordProcessorImpl();
        RecordProcessor outOfRangeRecordProcessor = new OutOfRangeRecordProcessorImpl(1410850800);
        //RecordProcessor outOfTrendRecordProcessor = new OutOfTrendRecordProcessorImpl();
        duplicatedRecordProcessor.setSuccessor(outOfRangeRecordProcessor);
        //outOfRangeRecordProcessor.setSuccessor(/*outOfTrendRecordProcessor*/);
        processor = duplicatedRecordProcessor;
        List<Record> unmarkedRecords =
                CsvFileReader.readRecordsFromCsvFileLocatedInTestResourcesFolder("marked-sample_16-sep-2015_csv.csv");
        processor.processRecords(unmarkedRecords);
        List<Record> actualMarkedRecords = unmarkedRecords;
        List<Record> expectedMarkedRecords =
                CsvFileReader.readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder("marked-sample_16-sep-2015_csv.csv");
        for(Record record: actualMarkedRecords) {
            if(record.getFlag() == Flag.VALID)
                //System.out.println(record.getId() + "\t" + record.getTimeStamp() + "\t" + record.getServedTicket()/* + "\t" + record.getFlag()*/);
                System.out.println("sample.addRecord(" + record.getTimeStamp() + ", " + record.getServedTicket() + ");");
        }
        /*int i = 0;
        for(Record record: expectedMarkedRecords) {
            if(record.isValid()) {
                i++;
            }
        }
        System.out.println("Number of valid records = " + i);
        assertActualFlagsMatchExpectedForAllRecords(expectedMarkedRecords, actualMarkedRecords);*/
    }

    @Test
    public void realDataOct20Test() {
        RecordProcessor duplicatedRecordProcessor = new DuplicatedRecordProcessorImpl();
        RecordProcessor outOfRangeRecordProcessor = new OutOfRangeRecordProcessorImpl(1413788400);
        //RecordProcessor outOfTrendRecordProcessor = new OutOfTrendRecordProcessorImpl();
        duplicatedRecordProcessor.setSuccessor(outOfRangeRecordProcessor);
        //outOfRangeRecordProcessor.setSuccessor(/*outOfTrendRecordProcessor*/);
        processor = duplicatedRecordProcessor;
        List<Record> unmarkedRecords =
                CsvFileReader.readRecordsFromCsvFileLocatedInTestResourcesFolder("marked-sample_20-oct-2015_csv.csv");
        processor.processRecords(unmarkedRecords);
        List<Record> actualMarkedRecords = unmarkedRecords;
        List<Record> expectedMarkedRecords =
                CsvFileReader.readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder("marked-sample_20-oct-2015_csv.csv");
        for(Record record: actualMarkedRecords) {
            if(record.getFlag() == Flag.VALID)
                //System.out.println(record.getId() + "\t" + record.getTimeStamp() + "\t" + record.getServedTicket() /*+ "\t" + record.getFlag()*/);
                System.out.println("sample.addRecord(" + record.getTimeStamp() + ", " + record.getServedTicket() + ");");
        }
    }

    @Test
    public void realDataSep15v2Test() {
        RecordProcessor duplicatedRecordProcessor = new DuplicatedRecordProcessorImpl();
        RecordProcessor outOfRangeRecordProcessor = new OutOfRangeRecordProcessorImpl(1410764400);
        //RecordProcessor outOfTrendRecordProcessor = new OutOfTrendRecordProcessorImpl();
        duplicatedRecordProcessor.setSuccessor(outOfRangeRecordProcessor);
        //outOfRangeRecordProcessor.setSuccessor(/*outOfTrendRecordProcessor*/);
        processor = duplicatedRecordProcessor;
        List<Record> unmarkedRecords =
                CsvFileReader.readRecordsFromCsvFileLocatedInTestResourcesFolder("marked-sample_15-sep-2015_csv.csv");
        processor.processRecords(unmarkedRecords);
        List<Record> actualMarkedRecords = unmarkedRecords;
        List<Record> expectedMarkedRecords =
                CsvFileReader.readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder("marked-sample_15-sep-2015_csv.csv");
        for(Record record: actualMarkedRecords) {
            if(record.getFlag() == Flag.VALID)
                //System.out.println(record.getId() + "\t" + record.getTimeStamp() + "\t" + record.getServedTicket() /*+ "\t" + record.getFlag()*/);
                System.out.println("sample.addRecord(" + record.getTimeStamp() + ", " + record.getServedTicket() + ");");
        }
    }
}
