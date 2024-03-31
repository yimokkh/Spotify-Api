package org.example.test.repository;
import org.example.test.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findOneById(Integer id);
}
