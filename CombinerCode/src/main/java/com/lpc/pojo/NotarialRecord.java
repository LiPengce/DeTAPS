package com.lpc.pojo;

import java.util.Date;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-13
 */

public class NotarialRecord {
    private String companyName;
    private String crimeType;
    private String crimeDescription;
    private Date timestamp;

    public NotarialRecord() {}

    public NotarialRecord(String companyName, String crimeType, String crimeDescription, Date timestamp) {
        this.companyName = companyName;
        this.crimeType = crimeType;
        this.crimeDescription = crimeDescription;
        this.timestamp = timestamp;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getCrimeDescription() {
        return crimeDescription;
    }

    public void setCrimeDescription(String crimeDescription) {
        this.crimeDescription = crimeDescription;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
