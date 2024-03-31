package org.example.test.service;

import org.example.test.entity.Playlist;
import org.example.test.entity.Track;
import org.example.test.entity.User;
import org.example.test.repository.PlaylistRepository;
import org.example.test.repository.TrackRepository;
import org.example.test.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private static final String PLAYLIST_NOT_FOUND = "Playlist not found";
    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    public PlaylistService(PlaylistRepository playlistRepository, TrackRepository trackRepository, UserRepository userRepository){
        this.playlistRepository = playlistRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    public List<Playlist> getAllPlaylists(){
        return playlistRepository.findAll();
    }

    public void deletePlaylistById(Integer id) {
        playlistRepository.deleteById(id);
    }

    public ResponseEntity<Object> updatePlaylistNameById(Integer id, String newName) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            playlist.setName(newName);
            playlistRepository.save(playlist);
        } else {
            return ResponseEntity.notFound().build();
        }
        return null;
    }

    public void addTrackToPlaylist(Integer playlistId, Integer trackId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new IllegalArgumentException(PLAYLIST_NOT_FOUND));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new IllegalArgumentException("Track not found"));

        playlist.addTrack(track);
        track.getPlaylists().add(playlist);
        playlistRepository.save(playlist);
    }

    public void removeTrackFromPlaylist(Integer playlistId, Integer trackId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException(PLAYLIST_NOT_FOUND));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        if (playlist.getTracks().contains(track)) {
            playlist.removeTrack(track);
            track.getPlaylists().remove(playlist);
            playlistRepository.save(playlist);
        } else {
            throw new IllegalArgumentException("Track is not in the playlist");
        }
    }


    public List<Track> getTracksByPlaylistId(Integer playlistId) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            return playlist.getTracks();
        } else {
            throw new IllegalArgumentException(PLAYLIST_NOT_FOUND);
        }
    }

    public void postPlaylist(Integer userId, String name){
        User user = userRepository.findById(userId)
                .orElseThrow();
        Playlist playlist = new Playlist(name);
        user.getPlaylists().add(playlist);
        playlist.setUser(user);
        playlistRepository.save(playlist);
    }

}
