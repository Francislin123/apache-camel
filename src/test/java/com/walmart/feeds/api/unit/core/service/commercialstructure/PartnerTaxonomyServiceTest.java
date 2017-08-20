package com.walmart.feeds.api.unit.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by vn0y942 on 08/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PartnerTaxonomyServiceTest {

    @Mock
    private PartnerTaxonomyService partnerTaxonomyService;
    @Autowired
    private PartnerTaxonomyRepository partnerTaxonomyRepository;
    @Test
    public void fileReadingTest(){
//        StringBuilder sb = new StringBuilder();
//        sb.append(";;;\n");
//        sb.append(";;;\n");
//        sb.append("Código Google;Taxonomia Google;Taxonomia Walmart;\n");
//        sb.append("8526;Veículos e peças > Peças e acessórios de veículos > Eletrônicos para veículos  ");
//        MockMultipartFile commercialStructureFile = new MockMultipartFile("file", sb.toString().getBytes());
//        List<PartnerTaxonomyEntity> commercialStructureEntityList = partnerTaxonomyService.
//                readCVSFile(commercialStructureFile, any(String.class), any(String.class));
//        assertFalse(commercialStructureEntityList.isEmpty());
//        assertEquals("8526", commercialStructureEntityList.get(0).getPartnerPathId());
    }
}
