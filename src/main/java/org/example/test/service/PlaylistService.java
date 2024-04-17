package org.example.test.service;

import org.example.test.aop.annotation.Logging;
import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.Track;
import org.example.test.entity.User;
import org.example.test.exeption.ResourceNotFoundException;
import org.example.test.repository.PlaylistRepository;
import org.example.test.repository.TrackRepository;
import org.example.test.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Logging
@Service
public class PlaylistService {

    private static final String PLAYLIST_NOT_FOUND_MESSAGE = "Playlist not found!";

    private static final String TRACK_NOT_FOUND_MESSAGE = "Track not found!";

    private final TrackRepository trackRepository;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final EntityCache<Integer, Object> cacheMap;

    public PlaylistService(PlaylistRepository playlistRepository, TrackRepository trackRepository, UserRepository userRepository, EntityCache<Integer, Object> cacheMap) {
        this.playlistRepository = playlistRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.cacheMap = cacheMap;
    }

    public List<Playlist> findPlaylistsByTrackName(String name) {
        return playlistRepository.findPlaylistsByTrackName(name);
    }

    public void deletePlaylistById(Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            playlist.getTracks().forEach(track -> track.getPlaylists().remove(playlist));
            playlist.getTracks().clear();
            playlistRepository.deleteById(id);
            updateCacheForAllPlaylists();
        } else {
            throw new ResourceNotFoundException(PLAYLIST_NOT_FOUND_MESSAGE);
        }
    }

    public ResponseEntity<Object> updatePlaylistNameById(Integer id, String newName) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            playlist.setName(newName);
            playlistRepository.save(playlist);
            updateCacheForPlaylistById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException(PLAYLIST_NOT_FOUND_MESSAGE);
        }
    }

    public void addTrackToPlaylist(Integer playlistId, Integer trackId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND_MESSAGE));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE));

        playlist.addTrack(track);
        track.getPlaylists().add(playlist);
        playlistRepository.save(playlist);
        updateCacheForPlaylistById(playlistId);
    }

    public void removeTrackFromPlaylist(Integer playlistId, Integer trackId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND_MESSAGE));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE));

        if (playlist.getTracks().contains(track)) {
            playlist.removeTrack(track);
            track.getPlaylists().remove(playlist);
            playlistRepository.save(playlist);
            updateCacheForPlaylistById(playlistId);
        } else {
            throw new IllegalArgumentException("Track is not in the playlist!");
        }
    }

    public Optional<List<Playlist>> getAllPlaylists() {
        List<Playlist> playlistList = playlistRepository.findAll();
        if (!playlistList.isEmpty()) {
            playlistList.forEach(playlist -> cacheMap.put(playlist.getId(), playlist));
            return Optional.of(playlistList);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Playlist> getPlaylistById(Integer playlistId) {
        int hashCode = Objects.hash("playlist_by_id", playlistId);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return Optional.of((Playlist) cachedData);
        } else {
            Optional<Playlist> optionalPlaylist = Optional.ofNullable(playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new ResourceNotFoundException(PLAYLIST_NOT_FOUND_MESSAGE)));
            optionalPlaylist.ifPresent(playlist -> cacheMap.put(hashCode, playlist));
            return optionalPlaylist;
        }
    }

    public void postPlaylist(Integer userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Playlist playlist = new Playlist(name);
        user.getPlaylists().add(playlist);
        playlist.setUser(user);
        playlistRepository.save(playlist);
        updateCacheForAllPlaylists();
    }

    private void updateCacheForAllPlaylists() {
        String cacheKey = "all_playlists";
        Integer hashCode = cacheKey.hashCode();
        List<Playlist> playlistList = playlistRepository.findAll();
        if (!playlistList.isEmpty()) {
            playlistList.forEach(playlist -> cacheMap.put(playlist.getId(), playlist));
        }
        cacheMap.put(hashCode, playlistList);
    }

    private void updateCacheForPlaylistById(Integer id) {
        int hashCode = Objects.hash("playlist_by_id", id);
        Optional<Playlist> playlistOptional = playlistRepository.findById(id);
        playlistOptional.ifPresent(playlist -> cacheMap.put(hashCode, playlist));
    }
}


