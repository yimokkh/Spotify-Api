package org.example.test.dto;

public class ExternalApiRequest {

    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ExternalApiRequest(String name, String type){
        this.name = name;
        this.type = type;
    }
}
