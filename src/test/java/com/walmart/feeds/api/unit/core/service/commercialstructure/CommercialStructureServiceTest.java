package com.walmart.feeds.api.unit.core.service.commercialstructure;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.resources.commercialstructure.service.CommercialStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by vn0y942 on 08/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommercialStructureServiceTest {

    @Mock
    private CommercialStructureService commercialStructureService;
    @Autowired
    private CommercialStructureRepository commercialStructureRepository;
    @Test
    public void fileReadingTest(){
//        StringBuilder sb = new StringBuilder();
//        sb.append(";;;\n");
//        sb.append(";;;\n");
//        sb.append("Código Google;Taxonomia Google;Taxonomia Walmart;\n");
//        sb.append("8526;Veículos e peças > Peças e acessórios de veículos > Eletrônicos para veículos  ");
//        MockMultipartFile commercialStructureFile = new MockMultipartFile("file", sb.toString().getBytes());
//        List<CommercialStructureEntity> commercialStructureEntityList = commercialStructureService.
//                readCVSFile(commercialStructureFile, any(String.class), any(String.class));
//        assertFalse(commercialStructureEntityList.isEmpty());
//        assertEquals("8526", commercialStructureEntityList.get(0).getStructurePartnerId());
    }
}
