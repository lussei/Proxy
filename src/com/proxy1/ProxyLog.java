package com.proxy1;

public class ProxyLog {
    String url;
    int result;
    double time;
    String contentType;
    int length;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return  "Result: "  + url + " | Result " + result  + " | Time(s): " + time + " | content type:"  + contentType + " | length:" + length;

    }
}
