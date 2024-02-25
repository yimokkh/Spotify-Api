package org.example.test.dto;

public class ExternalApiResponse {

    public ExternalApiResponse(String name, Integer followers){
        this.name = name;
        this.followers = followers;
    }
    private String name;

    private Integer followers;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

}
