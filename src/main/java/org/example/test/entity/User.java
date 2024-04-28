package org.example.test.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    public User() { }

    public User(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "user", orphanRemoval = true,
            cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    private List<Playlist> playlists = new ArrayList<>();

    public void addPlaylist(String playlistName) {
        Playlist playlist = new Playlist(playlistName, this);
        playlists.add(playlist);
    }

}

