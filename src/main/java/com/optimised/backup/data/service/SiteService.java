package com.optimised.backup.data.service;

import com.optimised.backup.data.entity.Site;
import com.optimised.backup.data.repositories.SiteRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    final private SiteRepo siteRepo;

    public SiteService(SiteRepo siteRepo) {
        this.siteRepo = siteRepo;
    }


    public List<Site> findAllSites(String stringFilter){
        if ((stringFilter == null || stringFilter.isEmpty())) {
            return siteRepo.findAll();
        } else {
            return siteRepo.search(stringFilter);
        }
    }

    public List<Site> findAllUnCheckedOutSites(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return siteRepo.findAllCheckedOutSites();
        } else {
            return siteRepo.searchUnCheckedOutSites(stringFilter);
        }
    }

    public void saveSite(Site site){
        Site siteDB = siteRepo.findFirstBySiteNumber(site.getSiteNumber());
        if (siteDB != null) {
            site.setId(siteDB.getId());
            if (site.getStoreNumber() == null){
                site.setStoreNumber((siteDB.getStoreNumber()));
            }
            if (site.getBackupTime() == null){
                site.setBackupTime(siteDB.getBackupTime());
            }
        }
        siteRepo.save(site);
    }

    public void setExistingFalse(){
        siteRepo.setExistingFalse();
    }

    public void deleteIfExisingFalse(){
        siteRepo.deleteByExistingFalse();
    }

    public Site findSiteBySiteNo(Integer siteNo){
        return siteRepo.findFirstBySiteNumber(siteNo);
    }
}
