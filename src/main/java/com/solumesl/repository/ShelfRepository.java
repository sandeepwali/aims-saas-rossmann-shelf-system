package com.solumesl.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solumesl.entity.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
	@Query(value = "SELECT * FROM shelf WHERE id IN (SELECT shelf_id from shelfitem WHERE articleno = ?1) ", nativeQuery = true)
	List<Shelf> findAllByArticleno(String articleno);

	List<Shelf> findByStoreAndModuleAndShelfAndRowno(String store,String module, String shelf, Integer rowno);
}
