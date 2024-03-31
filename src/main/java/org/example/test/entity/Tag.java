package org.example.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String track;

    private String text;

    public Tag(String text, String track) {
        this.text = text;
        this.track = track;
    }

    public Tag() {

    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTrackForTag() {
        return track;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTrackForTag(String trackName) {
        this.track = trackName;
    }
}