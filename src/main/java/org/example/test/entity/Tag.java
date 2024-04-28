package org.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Tag {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Setter
    @Column(unique = true)
    private String text;

    public Tag(String text) {
        this.text = text;
    }

    public Tag() {

    }

    @Getter
    @JsonIgnoreProperties({"tags", "playlists"})

    @ManyToMany()
    @JoinTable(name = "tracks_tags",
            joinColumns = { @JoinColumn(name = "track_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Track> tracks = new HashSet<>();

}