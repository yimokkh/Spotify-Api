package org.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Playlist {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    public Integer id;
    private String name;
    @JsonIgnoreProperties({"playlists"})
     @ManyToMany(mappedBy = "playlists")
     private List<Track> tracks = new ArrayList<>();


    @ManyToOne(cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public Playlist(String name){
        this.name = name;
    }

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
        user.addPlaylist(String.valueOf(this));
    }
    public Playlist(){
    }

    public void addTrack(Track track){
        this.tracks.add(track);
    }

    public void removeTrack(Track track){this.tracks.remove(track);}

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
