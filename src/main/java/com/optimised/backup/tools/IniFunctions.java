package com.optimised.backup.tools;

import com.optimised.backup.data.entity.Engineer;
import com.optimised.backup.data.entity.Site;
import com.optimised.backup.data.service.SiteService;
import lombok.extern.log4j.Log4j2;
import org.ini4j.Wini;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Log4j2
@Component
public class IniFunctions {

    final
    SiteService siteService;

    public IniFunctions(SiteService siteService) {
        this.siteService = siteService;
    }

    public void SetSiteInfo(String wn3000IniPath, SiteService siteService) {
        try {
            Wini ini = new Wini(new File(wn3000IniPath));
            int noOfSites = ini.get("SiteList", "TotalSites", int.class);
            for (int n = 1; n < noOfSites + 1; n++) {

                Integer siteNo = Conversions.tryParseInt(ini.get("SiteList", "Site" + n));
                String siteSection = "Site" + siteNo;
                Site site = new Site();
                site.setSiteNumber(siteNo);
                site.setDirectory(ini.get(siteSection, "Directory"));
                site.setInternet(Conversions.tryParseInt(ini.get(siteSection, "Internet")));
                site.setIDCode(ini.get(siteSection, "IDCode"));
                site.setIpAddr(ini.get(siteSection, "IPAddr"));
                site.setName(ini.get(siteSection, "Name"));
                site.setNetwork(Conversions.tryParseInt(ini.get(siteSection, "Network")));
                site.setPort(Conversions.tryParseInt(ini.get(siteSection, "Port")));
                site.setRemote(Conversions.tryParseInt(ini.get(siteSection, "Remote")));
                site.setTelephone(ini.get(siteSection, "Telephone"));
                site.setBacNet(Conversions.tryParseInt(ini.get(siteSection,"BacNet")));
                site.setDefaultType(Conversions.tryParseInt(ini.get(siteSection,"DefaultType")));
                site.setExisting(true);
                Engineer engineer = new Engineer();
                Site siteOrg = siteService.findSiteBySiteNo(siteNo);
                if (siteOrg != null){
                    engineer.setId(siteOrg.getEngineer().getId());
                } else
                {
                    engineer.setId(1L);
                }
                site.setEngineer(engineer);
                siteService.saveSite(site);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
