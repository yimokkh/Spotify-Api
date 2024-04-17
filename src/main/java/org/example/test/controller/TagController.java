package org.example.test.controller;


import org.example.test.entity.Tag;
import org.example.test.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Integer id) {
        return tagService.getTagById(id);
    }

    @PostMapping()
    public void postTag(@RequestParam String text,
                        @RequestParam String track) {
        tagService.postTag(new Tag(text, track));
    }

    @DeleteMapping("/{id}")
    public void deleteTagById(@PathVariable Integer id) {
        tagService.deleteTagById(id);
    }

    @PatchMapping("/{id}")
    public void updateTagById(@PathVariable Integer id,
                              @RequestParam String newText) {
        tagService.updateTagById(id, newText);
    }
}
