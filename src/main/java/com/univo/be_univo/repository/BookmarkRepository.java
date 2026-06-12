package com.univo.be_univo.repository;

import com.univo.be_univo.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
}
