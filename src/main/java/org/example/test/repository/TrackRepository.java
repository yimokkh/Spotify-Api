package org.example.test.repository;
import org.example.test.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Integer> {

    @Query("SELECT t FROM Track t WHERE t.name LIKE %:name%")
    List<Track> findTracksByName(@Param("name") String name);

}
