package org.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
public class Track {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String artist;

    public Track(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    @Setter
    @Getter
    @JsonIgnoreProperties({"tracks"})
    @ManyToMany()
    @JoinTable(name = "playlists_tracks",
            joinColumns = { @JoinColumn(name = "playlist_id") },
            inverseJoinColumns = { @JoinColumn(name = "track_id") })
    private Set<Playlist> playlists = new HashSet<>();

    @JsonIgnoreProperties({"tracks"})
    @ManyToMany(mappedBy = "tracks")
    private List<Tag> tags = new ArrayList<>();

    public Track() {
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    public List<Tag> getTags() {
        return tags;
    }
}
