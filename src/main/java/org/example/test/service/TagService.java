package org.example.test.service;

import org.example.test.aop.annotation.Logging;
import org.example.test.cache.EntityCache;
import org.example.test.entity.Tag;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Logging
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final EntityCache<Integer, Object> cacheMap;
    private final TrackService trackService;

    private static final String TAG_NOT_FOUND_MESSAGE = "Tag not found!";

    private static final String TAG_ALREADY_EXISTS_MESSAGE = "Tag already exists!";

    public TagService(TagRepository tagRepository, EntityCache<Integer, Object> cacheMap,
                      TrackService trackService) {
        this.tagRepository = tagRepository;
        this.cacheMap = cacheMap;
        this.trackService = trackService;
    }

    public void postTag(Tag tag) {
        try {
            Tag savedTag = tagRepository.save(tag);
            Integer tagId = savedTag.getId();
            cacheMap.put(tagId, savedTag);
            ResponseEntity.ok(savedTag);
        } catch (Exception e) {
            throw new BadRequestErrorException(TAG_ALREADY_EXISTS_MESSAGE);
        }
    }

    public void deleteTagById(Integer id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            tag.getTracks().forEach(track -> trackService.deleteTrackById(track.id));
            tag.getTracks().clear();
            tagRepository.deleteById(id);
            updateCacheForAllTags();
        } else {
            throw new ResourceNotFoundException(TAG_NOT_FOUND_MESSAGE);
        }
    }

    public void updateTagById(Integer id, String newText) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            tag.setText(newText);
            tagRepository.save(tag);
            updateCacheForTagById(id);
            ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException(TAG_NOT_FOUND_MESSAGE);
        }
    }

    public Optional<List<Tag>> getAllTags() {
        List<Tag> tagList = tagRepository.findAll();
        if (!tagList.isEmpty()) {
            tagList.forEach(tag -> cacheMap.put(tag.getId(), tag));
            return Optional.of(tagList);
        } else {
            throw new ResourceNotFoundException("No tag has been created!");
        }
    }

    public Optional<Tag> getTagById(Integer id) {
        int hashCode = Objects.hash("tag_by_id", id);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return Optional.ofNullable((Tag) cachedData);
        } else {
            Optional<Tag> tagOptional = Optional.ofNullable(tagRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(TAG_NOT_FOUND_MESSAGE)));
            tagOptional.ifPresent(tag -> cacheMap.put(hashCode, tag));
            return tagOptional;
        }
    }

    private void updateCacheForAllTags() {
        String cacheKey = "all_tags";
        Integer hashCode = cacheKey.hashCode();
        List<Tag> tagList = tagRepository.findAll();
        cacheMap.put(hashCode, tagList);
    }

    private void updateCacheForTagById(Integer id) {
        int hashCode = Objects.hash("tag_by_id", id);
        Optional<Tag> tagOptional = tagRepository.findById(id);
        tagOptional.ifPresent(tag -> cacheMap.put(hashCode, tag));
    }
}
