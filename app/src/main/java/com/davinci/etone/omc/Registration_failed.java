package com.davinci.etone.omc;

public class Registration_failed {
    String line;
    String error;

    public Registration_failed(String line, String error) {
        this.line = line;
        this.error = error;
    }

    public Registration_failed() {
        this.line = "";
        this.error = "";
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
