package com.example.junk;

import java.io.Serializable;

public class Measure implements Serializable{
    private String uri;
    private String label;

    public Measure(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
