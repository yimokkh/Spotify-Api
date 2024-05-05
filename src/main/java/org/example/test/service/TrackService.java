package org.example.test.service;

import org.example.test.aop.annotation.Logging;
import org.example.test.cache.EntityCache;
import org.example.test.entity.Tag;
import org.example.test.entity.Track;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TagRepository;
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
    private final TagRepository tagRepository;
    private final EntityCache<Integer, Object> cacheMap;

    private static final String TRACK_NOT_FOUND_MESSAGE = "Track not found!";

    private static final String TAG_NOT_FOUND_MESSAGE = "Tag not found!";

    public TrackService(TrackRepository trackRepository, EntityCache<Integer, Object> cacheMap,
                        TagRepository tagRepository) {
        this.trackRepository = trackRepository;
        this.cacheMap = cacheMap;
        this.tagRepository = tagRepository;
    }

    public Track postTrack(Track track) {
        try {
            Track savedTrack = trackRepository.save(track);
            Integer trackId = savedTrack.getId();
            cacheMap.put(trackId, savedTrack);
            ResponseEntity.ok(savedTrack);
            return savedTrack;
        } catch (Exception e) {
            throw new BadRequestErrorException("Failed to create track: " + e.getMessage());
        }
    }

    public void deleteTrackById(Integer id) {
        Optional<Track> optionalTrack = trackRepository.findById(id);
        if (optionalTrack.isPresent()) {
            Track track = optionalTrack.get();
            track.getTags().forEach(tag -> tag.getTracks().remove(track));
            track.getTags().clear();
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
        Optional<Track> trackOptional = trackRepository.findById(id);

        if (trackOptional.isEmpty()) {
            throw new ResourceNotFoundException("Трек с ID " + id + " не найден.");
        }

        trackOptional.ifPresent(track -> {
            int hashCode = Objects.hash("track_by_id", id);
            cacheMap.put(hashCode, track);
        });
        return trackOptional;
    }

    public Tag addTagToTrack(Integer trackId, Integer tagId) {
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE));
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException(TAG_NOT_FOUND_MESSAGE));

        List<Tag> trackTags = track.getTags();
        if (!trackTags.contains(tag)) {
            track.addTag(tag);
            tag.getTracks().add(track);
            trackRepository.save(track);
            updateCacheForTrackById(trackId);
            return tag;
        } else {
            throw new IllegalArgumentException("Tag is already in track!");
        }
    }

    public void removeTagFromTrack(Integer trackId, Integer tagId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException(TRACK_NOT_FOUND_MESSAGE));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(TAG_NOT_FOUND_MESSAGE));

        if (track.getTags().contains(tag)) {
            track.removeTag(tag);
            tag.getTracks().remove(track);
            trackRepository.save(track);
            updateCacheForTrackById(trackId);
        } else {
            throw new IllegalArgumentException("Track is not in the playlist!");
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


