package org.example.test.service;

import org.example.test.entity.Track;
import org.example.test.repository.TrackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackService {

    private final TrackRepository trackRepository;


    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public void postTrack(Track track) {
        trackRepository.save(track);
    }

    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    public Optional<Track> getTrackById(Integer id) {
        return trackRepository.findById(id);
    }

    public void deleteTrackById(Integer id) {
        trackRepository.deleteById(id);
    }

    public ResponseEntity<Object> updateTrackNameById(Integer id, String newName) {
        Optional<Track> optionalTrack = trackRepository.findById(id);
        if (optionalTrack.isPresent()) {
            Track track = optionalTrack.get();
            track.setName(newName);
            trackRepository.save(track);
        } else {
            return ResponseEntity.notFound().build();
        }
        return null;
    }
}
