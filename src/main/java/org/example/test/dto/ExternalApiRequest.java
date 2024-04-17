package org.example.test.dto;

public class ExternalApiRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExternalApiRequest(String name) {
        this.name = name;
    }
}
