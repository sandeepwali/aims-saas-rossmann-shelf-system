package com.solumesl.repository;


import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solumesl.entity.Shelfitem;

public interface ShelfitemRepository extends JpaRepository<Shelfitem, Long> {
	List<Shelfitem> findByShelfId(Long shelfId);

	List<Shelfitem> findAll(Sort orderno);

	List<Shelfitem> findByLength(String length);

	List<Shelfitem> findByArticleno(String articleno);
}
