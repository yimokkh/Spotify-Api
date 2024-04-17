package org.example.test.repository;
import org.example.test.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    @Query("SELECT DISTINCT p FROM Playlist p JOIN p.tracks t WHERE t.name LIKE %:name%")
    List<Playlist> findPlaylistsByTrackName(@Param("name") String name);
}
