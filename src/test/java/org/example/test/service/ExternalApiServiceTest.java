package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.test.dto.ExternalApiRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ExternalApiService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ExternalApiServiceTest {
  @Autowired
  private ExternalApiService externalApiService;

  @MockBean
  private ObjectMapper objectMapper;

  /**
   * Method under test: {@link ExternalApiService#updateToken()}
   */
  @Test
  void testUpdateToken() {
    // Arrange, Act and Assert
    assertEquals("hello", externalApiService.updateToken());
  }

  /**
   * Method under test:
   * {@link ExternalApiService#getArtistByName(ExternalApiRequest)}
   */
  @Test
  void testGetArtistByName() {
    // Arrange, Act and Assert
    assertNull(externalApiService.getArtistByName(new ExternalApiRequest("Name")));
    assertNull(externalApiService.getArtistByName(new ExternalApiRequest("U://U@[9U]:{UU?U#U")));
    assertNull(externalApiService.getArtistByName(mock(ExternalApiRequest.class)));
  }

  /**
   * Method under test:
   * {@link ExternalApiService#getTrackByName(ExternalApiRequest)}
   */
  @Test
  void testGetTrackByName() {
    // Arrange, Act and Assert
    assertNull(externalApiService.getTrackByName(new ExternalApiRequest("Name")));
    assertNull(externalApiService.getTrackByName(mock(ExternalApiRequest.class)));
  }
}
