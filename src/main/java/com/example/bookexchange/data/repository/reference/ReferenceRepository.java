package com.example.bookexchange.data.repository.reference;

import com.example.bookexchange.data.model.reference.Reference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferenceRepository extends CrudRepository<Reference, Long>, PagingAndSortingRepository<Reference, Long>, QueryByExampleExecutor<Reference> {

    @Query("select r from Reference r where lower(r.code)= ?1")
    Optional<Reference> findByCode(String code);

    @Query("select r from Reference r where r.parent is null and lower(r.code)= ?1")
    Optional<Reference> findParentByCode(String code);

}
