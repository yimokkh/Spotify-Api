package org.example.test.controller;

import org.example.test.entity.Playlist;
import org.example.test.entity.Tag;
import org.example.test.entity.Track;
import org.example.test.exception.ServerException;
import org.example.test.service.PlaylistService;
import org.example.test.service.RequestCounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.1.106:5173/", "*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE},
        allowedHeaders = {"Authorization", "Content-Type"})
@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    private final RequestCounterService counterService;

    public PlaylistController(PlaylistService playlistService,RequestCounterService counterService ) {
        this.playlistService = playlistService;
        this.counterService = counterService;
    }

    @GetMapping()
    public Optional<List<Playlist>> getAllPlaylists() {
        counterService.requestIncrement();
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/error")
    public void throwError() {
        counterService.requestIncrement();
        throw new ServerException("Internal Server Error occurred");
    }

    @GetMapping("/{id}")
    public Optional<Playlist> getPlaylistById(@PathVariable Integer id) {
        counterService.requestIncrement();
        return playlistService.getPlaylistById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Playlist>> searchPlaylistsByTrackName(@RequestParam String trackName) {
        counterService.requestIncrement();
        List<Playlist> playlists = playlistService.findPlaylistsByTrackName(trackName);
        return ResponseEntity.ok(playlists);
    }

    @PostMapping()
    public Playlist postPlaylist(@RequestParam String name, @RequestParam Integer userId) {
        counterService.requestIncrement();
        return playlistService.postPlaylist(userId, name);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylistById(@PathVariable Integer id) {
        counterService.requestIncrement();
        playlistService.deletePlaylistById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePlaylistNameById(@PathVariable Integer id,
                                                 @RequestParam String newName) {
        counterService.requestIncrement();
        return playlistService.updatePlaylistNameById(id, newName);
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public Track addTrackToPlaylist(@PathVariable Integer playlistId, @PathVariable Integer trackId) {
        counterService.requestIncrement();
        return playlistService.addTrackToPlaylist(playlistId, trackId);
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public void removeTrackFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer trackId) {
        counterService.requestIncrement();
        playlistService.removeTrackFromPlaylist(playlistId, trackId);
    }

}
