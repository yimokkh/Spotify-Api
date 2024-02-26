package org.example.test.dto;

public class ExternalApiResponse {

    public ExternalApiResponse(String name, Integer followers, Integer popularity){
        this.name = name;
        this.followers = followers;
        this.popularity = popularity;
    }
    private String name;

    private Integer followers;

    private Integer popularity;

    public Integer getPopularity() {return popularity;}

    public void setPopularity(Integer popularity) {this.popularity = popularity;}

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
