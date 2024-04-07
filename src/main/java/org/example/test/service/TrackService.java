package org.example.test.service;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.Track;
import org.example.test.repository.TrackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final EntityCache<Integer, Object> cacheMap;


    public TrackService(TrackRepository trackRepository, EntityCache<Integer, Object> cacheMap) {
        this.trackRepository = trackRepository;
        this.cacheMap = cacheMap;
    }

    public void postTrack(Track track) {
        Track savedTrack = trackRepository.save(track);
        Integer trackId = savedTrack.getId();
        cacheMap.put(trackId, savedTrack);
    }

    public void deleteTrackById(Integer id) {
        trackRepository.deleteById(id);
        updateCacheForAllTracks();
    }

    public ResponseEntity<Object> updateTrackNameById(Integer id, String newName) {
        Optional<Track> optionalTrack = trackRepository.findById(id);
        if (optionalTrack.isPresent()) {
            Track track = optionalTrack.get();
            track.setName(newName);
            trackRepository.save(track);
            updateCacheForTrackById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Optional<List<Track>> getAllTracks() {
        List<Track> trackList = trackRepository.findAll();
        if (!trackList.isEmpty()) {
            trackList.forEach(track -> cacheMap.put(track.getId(), track));
            return Optional.of(trackList);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Track> getTrackById(Integer id) {
        int hashCode = Objects.hash("track_by_id", id);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return Optional.ofNullable((Track) cachedData);
        } else {
            Optional<Track> trackOptional = trackRepository.findById(id);
            trackOptional.ifPresent(track -> cacheMap.put(hashCode, track));
            return trackOptional;
        }
    }

    public List<Track> findTracksByName(String name) {
        return trackRepository.findTracksByName(name);
    }

    private void updateCacheForAllTracks() {
        String cacheKey = "all_tracks";
        Integer hashCode = cacheKey.hashCode();
        List<Track> trackList = trackRepository.findAll();
        cacheMap.put(hashCode, trackList);
    }

    private void updateCacheForTrackById(Integer id) {
        int hashCode = Objects.hash("track_by_id", id);
        Optional<Track> trackOptional = trackRepository.findById(id);
        trackOptional.ifPresent(track -> cacheMap.put(hashCode, track));
    }
}


