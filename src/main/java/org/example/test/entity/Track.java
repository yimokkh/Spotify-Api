package org.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Track {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer id;

    private String name;

    private String artist;

    public Track(String name, String artist){
        this.name = name;
        this.artist = artist;
    }

    @JsonIgnoreProperties({"tracks"})
    @ManyToMany(/*cascade = CascadeType.REMOVE*/)
    @JoinTable(name = "playlists_tracks",
            joinColumns = { @JoinColumn(name = "playlist_id") },
            inverseJoinColumns = { @JoinColumn(name = "track_id") })
    private Set<Playlist> playlists = new HashSet<>();

    public Track(){

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }


}
