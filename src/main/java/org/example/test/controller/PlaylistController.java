package org.example.test.controller;

import org.example.test.entity.Playlist;
import org.example.test.exception.ServerException;
import org.example.test.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping()
    public Optional<List<Playlist>> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/error")
    public void throwError() {
        throw new ServerException("Internal Server Error occurred");
    }

    @GetMapping("/{id}")
    public Optional<Playlist> getPlaylistById(@PathVariable Integer id) {
        return playlistService.getPlaylistById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Playlist>> searchPlaylistsByTrackName(@RequestParam String trackName) {
        List<Playlist> playlists = playlistService.findPlaylistsByTrackName(trackName);
        return ResponseEntity.ok(playlists);
    }

    @PostMapping()
    public void postPlaylist(@RequestParam String name, @RequestParam Integer userId) {
        playlistService.postPlaylist(userId, name);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylistById(@PathVariable Integer id) {
        playlistService.deletePlaylistById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePlaylistNameById(@PathVariable Integer id,
                                                 @RequestParam String newName) {
        return playlistService.updatePlaylistNameById(id, newName);
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public void addTrackToPlaylist(@PathVariable Integer playlistId, @PathVariable Integer trackId) {
        playlistService.addTrackToPlaylist(playlistId, trackId);
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public void removeTrackFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer trackId) {
        playlistService.removeTrackFromPlaylist(playlistId, trackId);
    }

}
