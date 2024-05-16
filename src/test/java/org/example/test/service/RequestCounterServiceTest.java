package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {RequestCounterService.class})
@ExtendWith(SpringExtension.class)
class RequestCounterServiceTest {
  @Autowired
  private RequestCounterService requestCounterService;

  /**
   * Method under test: {@link RequestCounterService#requestIncrement()}
   */
  @Test
  void testRequestIncrement() {
    //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

    // Arrange
    requestCounterService = new RequestCounterService();

    // Act
    requestCounterService.requestIncrement();

    // Assert
    assertEquals(0, requestCounterService.getRequestCount());
  }

  /**
   * Method under test: {@link RequestCounterService#getRequestCount()}
   */
  @Test
  void testGetRequestCount() {
    // Arrange, Act and Assert
    assertEquals(-1, requestCounterService.getRequestCount());
  }
}
