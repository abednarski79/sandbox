package eu.appbucket.sandbox.myq.estimator;

import eu.appbucket.sandbox.myq.record.Record;

import java.util.List;

/**
 * Created by adambednarski on 08/06/2015.
 */
public class VotingBasedEstimator {

    public Integer estimateCurrentServedNumber(List<Record> records) {
        Record lastRecord = records.get(records.size() - 1);
        return lastRecord.getServedTicket();
    }
}
