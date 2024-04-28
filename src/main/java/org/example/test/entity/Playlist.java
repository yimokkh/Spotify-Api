package org.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Table(name = "playlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Playlist {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Integer id;
    @Getter
    private String name;
    @Getter
    @JsonIgnoreProperties({"playlists"})
     @ManyToMany(mappedBy = "playlists")
     private List<Track> tracks = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
        user.addPlaylist(String.valueOf(this));
    }

    public Playlist() {
    }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    public void removeTrack(Track track) {
        this.tracks.remove(track);
    }

}
