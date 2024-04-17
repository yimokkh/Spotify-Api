package org.example.test.service;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Tag;
import org.example.test.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final EntityCache<Integer, Object> cacheMap;

    public TagService(TagRepository tagRepository, EntityCache<Integer, Object> cacheMap) {
        this.tagRepository = tagRepository;
        this.cacheMap = cacheMap;
    }

    public List<Tag> getAllTags() {
        int hashCode = Objects.hash(4 * 33);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return (List<Tag>) cachedData;
        } else {
            List<Tag> tagList = tagRepository.findAll();
            cacheMap.put(hashCode, tagList);
            return tagList;
        }
    }

    public Tag getTagById(Integer id) {
        int hashCode = Objects.hash("tag_by_id", id);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return (Tag) cachedData;
        } else {
            Tag tag = tagRepository.findOneById(id);
            if (tag != null) {
                cacheMap.put(hashCode, tag);
            }
            return tag;
        }
    }

    public void postTag(Tag tag) {
        tagRepository.save(tag);
    }

    public void deleteTagById(Integer id) {
        tagRepository.deleteById(id);
    }

    public ResponseEntity<Object> updateTagById(Integer id, String newText) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (((Optional<?>) optionalTag).isPresent()) {
            Tag tag = optionalTag.get();
            tag.setText(newText);
            tagRepository.save(tag);
        } else {
            return ResponseEntity.notFound().build();
        }
        return null;
    }

}
