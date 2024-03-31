package org.example.test.service;

import org.example.test.entity.Tag;
import org.example.test.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    TagRepository tagRepository;

    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    public Tag getTagById(Integer id){
        return tagRepository.findOneById(id);
    }
    public void postTag(Tag tag){
        tagRepository.save(tag);
    }

    public void deleteTagById(Integer id){
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
