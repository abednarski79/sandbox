package eu.appbucket.sandbox.myq.record;

import java.util.Date;

/**
 * Created by adambednarski on 09/06/2015.
 */
public class Record {

    private Date date;
    private int id;
    private int userTicket;
    private int servedTicket;
    private Flag flag = Flag.VALID;
    boolean valid = true;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public int getUserTicket() {
        return userTicket;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        if(flag != Flag.VALID) {
            valid = false;
        }
        this.flag = flag;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserTicket(int userTicket) {
        this.userTicket = userTicket;
    }

    public void setServedTicket(int servedTicket) {
        this.servedTicket = servedTicket;
    }

    public int getServedTicket() {
        return servedTicket;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return this.date.getTime() / 1000;
    }
    public void setTimeStamp(long timeStamp) {
        this.setDate(new Date(timeStamp));
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", date=" + date +
                ", servedTicket=" + servedTicket +
                ", flag=" +flag +
                '}';
    }
}
