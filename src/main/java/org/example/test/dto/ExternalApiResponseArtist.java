package org.example.test.dto;

public class ExternalApiResponseArtist {

    public ExternalApiResponseArtist(String name, Integer followers, Integer popularity, String reference){
        this.name = name;
        this.followers = followers;
        this.popularity = popularity;
        this.reference = reference;
    }

    private String reference;
    private String name;

    private Integer followers;

    private Integer popularity;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

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
