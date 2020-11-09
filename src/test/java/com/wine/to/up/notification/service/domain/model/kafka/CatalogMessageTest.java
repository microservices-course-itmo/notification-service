package com.wine.to.up.notification.service.domain.model.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class CatalogMessageTest {

    @Test
    public void testNoArgsConstructor() {
        CatalogMessage catalogMessage = new CatalogMessage();
        assertThat(catalogMessage.getTitle()).isEqualTo(null);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String title = "testTitle";
        Float price = 1.0F;
        CatalogMessage catalogMessage = new CatalogMessage(id, title, price);
        assertThat(catalogMessage.getId()).isEqualTo(id);
        assertThat(catalogMessage.getTitle()).isEqualTo(title);
        assertThat(catalogMessage.getPrice()).isEqualTo(price);
    }

    @Test
    public void testIdSetter() {
        CatalogMessage catalogMessage = new CatalogMessage();
        Long id = 1L;
        catalogMessage.setId(id);
        assertThat(catalogMessage.getId()).isEqualTo(id);
    }

    @Test
    public void testTitleSetter() {
        CatalogMessage catalogMessage = new CatalogMessage();
        String title = "testTitle";
        catalogMessage.setTitle(title);
        assertThat(catalogMessage.getTitle()).isEqualTo(title);
    }

    @Test
    public void testPriceSetter() {
        CatalogMessage catalogMessage = new CatalogMessage();
        Float price = 1.0F;
        catalogMessage.setPrice(price);
        assertThat(catalogMessage.getPrice()).isEqualTo(price);
    }

    @Test
    public void toStringTest() {
        Long id = 1L;
        String title = "testTitle";
        Float price = 1.0F;
        CatalogMessage catalogMessage = new CatalogMessage(id, title, price);
        assertThat(catalogMessage.toString()).isEqualTo("CatalogMessage(id=" + id + ", title=" + title + ", price=" + price + ")");
    }
}