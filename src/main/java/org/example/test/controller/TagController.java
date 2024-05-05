package org.example.test.controller;

import org.example.test.entity.Tag;
import org.example.test.service.RequestCounterService;
import org.example.test.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173", "*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE},
        allowedHeaders = {"Authorization", "Content-Type"})
@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    private final RequestCounterService counterService;

    public TagController(TagService tagService, RequestCounterService counterService) {
        this.tagService = tagService;
        this.counterService = counterService;
    }

    @GetMapping()
    public Optional<List<Tag>> getAllTags() {
        counterService.requestIncrement();
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Optional<Tag> getTagById(@PathVariable Integer id) {
        counterService.requestIncrement();
        return tagService.getTagById(id);
    }

    @PostMapping()
    public Tag postTag(@RequestParam String text) {
        counterService.requestIncrement();
        return tagService.postTag(new Tag(text));
    }

    @DeleteMapping("/{id}")
    public void deleteTagById(@PathVariable Integer id) {
        counterService.requestIncrement();
        tagService.deleteTagById(id);
    }

    @PatchMapping("/{id}")
    public void updateTagById(@PathVariable Integer id,
                              @RequestParam String newText) {
        counterService.requestIncrement();
        tagService.updateTagById(id, newText);
    }
}
