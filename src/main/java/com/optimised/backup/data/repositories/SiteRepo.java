package com.optimised.backup.data.repositories;

import com.optimised.backup.data.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SiteRepo extends JpaRepository<Site,Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Site SET existing = false")
    void setExistingFalse();

    @Transactional
    @Modifying
    @Query("DELETE FROM Site WHERE existing=false ")
    void deleteByExistingFalse();

    @Query("select c from Site c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Site> search(@Param("searchTerm") String searchTerm);

    @Query("select c from Site c " +
            "where c.engineer.id = 1")
    List<Site> findAllCheckedOutSites();

    @Query("select c from Site c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.name) like lower(concat('%', :searchTerm, '%'))" +
            " and c.engineer.id = 1")
    List<Site> searchUnCheckedOutSites(@Param("searchTerm") String searchTerm);


    Site findFirstBySiteNumber(Integer siteNo);


}
