package com.example.demo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class issue_dataholder {
    String iss_email, iss_issues, iss_roomnum;

    public issue_dataholder() {
    }

    public issue_dataholder(String iss_email, String iss_issues, String iss_roomnum) {
        this.iss_email = iss_email;
        this.iss_issues = iss_issues;
        this.iss_roomnum = iss_roomnum;
    }

    public String getIss_email() {
        return iss_email;
    }

    public void setIss_email(String iss_email) {
        this.iss_email = iss_email;
    }

    public String getIss_issues() {
        return iss_issues;
    }

    public void setIss_issues(String iss_issues) {
        this.iss_issues = iss_issues;
    }

    public String getIss_roomnum() {
        return iss_roomnum;
    }

    public void setIss_roomnum(String iss_roomnum) {
        this.iss_roomnum = iss_roomnum;
    }
}
