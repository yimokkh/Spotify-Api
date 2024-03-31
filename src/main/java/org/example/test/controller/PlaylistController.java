package org.example.test.controller;

import org.example.test.entity.Playlist;
import org.example.test.entity.Track;
import org.example.test.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;


    public PlaylistController(PlaylistService playlistService){
        this.playlistService = playlistService;
    }

    @GetMapping()
    public List<Playlist> getAllPlaylists(){
        return playlistService.getAllPlaylists();
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
    public void updatePlaylistNameById(@PathVariable Integer id,
                                       @RequestParam String newName) {
        playlistService.updatePlaylistNameById(id, newName);
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public void addTrackToPlaylist(@PathVariable Integer playlistId, @PathVariable Integer trackId) {
        playlistService.addTrackToPlaylist(playlistId, trackId);
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public void removeTrackFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer trackId){
        playlistService.removeTrackFromPlaylist(playlistId, trackId);
    }

    @GetMapping("/{playlistId}/tracks")
    public List<Track> getTracksByPlaylistId(@PathVariable Integer playlistId) {
        return playlistService.getTracksByPlaylistId(playlistId);
    }

}
