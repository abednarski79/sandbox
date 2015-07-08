package eu.appbucket.sandbox.myq;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import eu.appbucket.sandbox.myq.record.Flag;
import eu.appbucket.sandbox.myq.record.Record;
import junit.framework.Assert;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * @author ashraf_sarhan
 */
public class CsvFileReader {

    //CSV file header
    private static final String[] RAW_FILE_HEADER_MAPPING = {"id","created","user_ticket","served_ticket"};
    private static final String[] MARKED_FILE_HEADER_MAPPING = {"id","created","user_ticket","served_ticket","manually_marked_to_ignore"};

    //Student attributes
    private static final String UPDATE_ID = "id";
    private static final String UPDATE_CREATED = "created";
    private static final String UPDATE_USER_TICKET_NUMBER = "user_ticket";
    private static final String UPDATE_SERVED_TICKET_NUMBER = "served_ticket";
    private static final String MANUALLY_MARKED_TO_IGNORE = "manually_marked_to_ignore";
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    public static List<Record> readRecordsFromCsvFileLocatedInTestResourcesFolder(String fileNameInTestResourcesFolder) {
        ClassLoader classLoader = CsvFileReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileNameInTestResourcesFolder).getFile());
        return readRawCsvDataFromFile(file);
    }

    public static List<Record> readRawCsvDataFromFile(File file) {

        //Create a new list of student to be filled by CSV file data
        List<Record> entries = new ArrayList();

        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(RAW_FILE_HEADER_MAPPING);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {

            //initialize FileReader object
            fileReader = new FileReader(file);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord cvsRecord = csvRecords.get(i);
                //Create a new student object and fill his data
                Record record = new Record();
                record.setId(Integer.parseInt(cvsRecord.get(UPDATE_ID)));
                record.setServedTicket(Integer.parseInt(cvsRecord.get(UPDATE_SERVED_TICKET_NUMBER)));
                record.setDate(df.parse(cvsRecord.get(UPDATE_CREATED)));
                record.setUserTicket(Integer.parseInt(cvsRecord.get(UPDATE_USER_TICKET_NUMBER)));
                entries.add(record);
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        return entries;
    }

    public static List<Record> readRecordsAndFlagsFromCsvFileLocatedInTestResourcesFolder(String fileNameInTestResourcesFolder) {
        ClassLoader classLoader = CsvFileReader.class.getClassLoader();
        File file = new File(classLoader.getResource(fileNameInTestResourcesFolder).getFile());
        if(!file.exists()) {
            System.out.println("File: " + file.getAbsolutePath() + " doesn't exists.");
        }
        return readMarkedCsvDataFromFile(file);
    }

    public static List<Record> readMarkedCsvDataFromFile(File file) {

        //Create a new list of student to be filled by CSV file data
        List<Record> entries = new ArrayList();

        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(MARKED_FILE_HEADER_MAPPING);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {

            //initialize FileReader object
            fileReader = new FileReader(file);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord cvsRecord = csvRecords.get(i);
                //Create a new student object and fill his data
                Record record = new Record();
                record.setId(Integer.parseInt(cvsRecord.get(UPDATE_ID)));
                record.setServedTicket(Integer.parseInt(cvsRecord.get(UPDATE_SERVED_TICKET_NUMBER)));
                record.setDate(df.parse(cvsRecord.get(UPDATE_CREATED)));
                record.setFlag(convertToFlag(cvsRecord.get(MANUALLY_MARKED_TO_IGNORE)));
                record.setUserTicket(Integer.parseInt(cvsRecord.get(UPDATE_USER_TICKET_NUMBER)));
                entries.add(record);
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        return entries;
    }

    private static Flag convertToFlag(String markerText) {
        if (markerText.startsWith("Y-R")) {
            return Flag.DUPLICATE_ENTRY;
        } else if (markerText.startsWith("Y-T")) {
            return Flag.UNEXPECTED_VALUE;
        } else if (markerText.startsWith("Y-H")) {
            return Flag.SERVED_TICKET_HIGHER_THEN_USER_TICKET;
        } else if (markerText.startsWith("Y-O")) {
            return Flag.DELTA_VALUE_NOT_IN_RANGE;
        }
        return Flag.VALID;
    }

    @Test
    public void dateConverter() throws Exception{
        String dateInUTC = "2014-09-15 07:00:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse(dateInUTC);
        NumberFormat formatter = new DecimalFormat("#0");
        long expectedTimeStamp = 1412664927;
        long actualTimeStamp = date.getTime() / 1000;
        System.out.println(actualTimeStamp);
        //Assert.assertEquals("Expected " + formatter.format(expectedTimeStamp) + " but found " + formatter.format(actualTimeStamp), expectedTimeStamp, actualTimeStamp, 0);
    }
}