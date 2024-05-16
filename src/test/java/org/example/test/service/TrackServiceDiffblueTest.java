package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.Tag;
import org.example.test.entity.Track;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.repository.TagRepository;
import org.example.test.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TrackService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TrackServiceDiffblueTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private TrackRepository trackRepository;

    @Autowired
    private TrackService trackService;

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        // Act
        Track actualPostTrackResult = trackService.postTrack(track2);

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(trackRepository).save(isA(Track.class));
        assertSame(track, actualPostTrackResult);
    }

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.postTrack(track2));
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        when(track.getId()).thenReturn(1);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        // Act
        trackService.postTrack(track2);

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(track).addTag(isA(Tag.class));
        verify(track).getId();
        verify(track).setArtist(eq("Artist"));
        verify(track).setId(eq(1));
        verify(track).setName(eq("Name"));
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).save(isA(Track.class));
    }
}
