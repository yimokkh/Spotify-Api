package org.example.test.service;

import org.example.test.aop.annotation.Logging;
import org.example.test.cache.EntityCache;
import org.example.test.entity.Track;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TrackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Logging
@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final EntityCache<Integer, Object> cacheMap;

    private static final String TRACK_NOT_FOUND_MESSAGE = "Track not found!";

    public TrackService(TrackRepository trackRepository, EntityCache<Integer, Object> cacheMap) {
        this.trackRepository = trackRepository;
        this.cacheMap = cacheMap;
    }

    public void postTrack(Track track) {
        try {
            Track savedTrack = trackRepository.save(track);
            Integer trackId = savedTrack.getId();
            cacheMap.put(trackId, savedTrack);
            ResponseEntity.ok(savedTrack);
        } catch (Exception e) {
            throw new BadRequestErrorException("Failed to create track: " + e.getMessage());
        }
    }

    public void deleteTrackById(Integer id) {
        Optional<Track> trackOptional = trackRepository.findById(id);
        if (trackOptional.isPresent()) {
            trackRepository.deleteById(id);
            updateCacheForAllTracks();
        } else {
            throw new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE);
        }
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
            throw new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE);
        }
    }

    public Optional<List<Track>> getAllTracks() {
        List<Track> trackList = trackRepository.findAll();
        if (!trackList.isEmpty()) {
            trackList.forEach(track -> cacheMap.put(track.getId(), track));
            return Optional.of(trackList);
        } else {
            throw new ResourceNotFoundException("No tracks has been created!");
        }
    }

    public Optional<Track> getTrackById(Integer id) {
        int hashCode = Objects.hash("track_by_id", id);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return Optional.ofNullable((Track) cachedData);
        } else {
            Optional<Track> trackOptional = Optional.ofNullable(trackRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE)));
            trackOptional.ifPresent(track -> cacheMap.put(hashCode, track));
            return trackOptional;
        }
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


