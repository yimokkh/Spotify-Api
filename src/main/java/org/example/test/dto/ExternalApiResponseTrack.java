package org.example.test.dto;

public class ExternalApiResponseTrack {

    public ExternalApiResponseTrack(String artistName, String trackName) {
        this.artistName = artistName;
        this.trackName = trackName;
    }

    private String artistName;

    private String trackName;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
