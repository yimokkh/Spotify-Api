package org.example.test.service;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.Track;
import org.example.test.entity.User;
import org.example.test.repository.PlaylistRepository;
import org.example.test.repository.TrackRepository;
import org.example.test.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlaylistService {

    private static final String PLAYLIST_NOT_FOUND = "Playlist not found";

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


    public void deletePlaylistById(Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            playlist.getTracks().forEach(track -> track.getPlaylists().remove(playlist));
            playlist.getTracks().clear();
            playlistRepository.deleteById(id);
            updateCacheForAllPlaylists();
        } else {
            throw new IllegalArgumentException(PLAYLIST_NOT_FOUND);
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
            return ResponseEntity.notFound().build();
        }
    }

    public void addTrackToPlaylist(Integer playlistId, Integer trackId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new IllegalArgumentException(PLAYLIST_NOT_FOUND));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new IllegalArgumentException("Track not found"));

        playlist.addTrack(track);
        track.getPlaylists().add(playlist);
        playlistRepository.save(playlist);
        updateCacheForPlaylistById(playlistId);
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
            updateCacheForPlaylistById(playlistId);
        } else {
            throw new IllegalArgumentException("Track is not in the playlist");
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
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
            optionalPlaylist.ifPresent(playlist -> cacheMap.put(hashCode, playlist));
            return optionalPlaylist;
        }
    }

    public void postPlaylist(Integer userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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


