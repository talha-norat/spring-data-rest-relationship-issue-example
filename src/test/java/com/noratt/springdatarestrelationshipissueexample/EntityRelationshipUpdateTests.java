package com.noratt.springdatarestrelationshipissueexample;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.noratt.springdatarestrelationshipissueexample.entity.School;
import com.noratt.springdatarestrelationshipissueexample.repository.SchoolRepository;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * @author noratt
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntityRelationshipUpdateTests {

  @LocalServerPort
  private int localPort;

  @Autowired
  private SchoolRepository schoolRepository;

  TestRestTemplate restTemplate = new TestRestTemplate();

  private final static String PARENT_WITH_NO_CHILDREN_PAYLOAD = """
      {
        "parentProp1": "X",
        "parentProp2": "Y",
        "children": []
      }
      """;

  private final static String PARENT_WITH_TWO_CHILDREN_PAYLOAD = """
      {
        "parentProp1": "X",
        "parentProp2": "Y",
        "children": [
          {
            "sequence": 1,
            "childProp1": "1C",
            "childProp2": true,
            "school": "http://localhost:${localPort}/schools/1"
          },
          {
            "sequence": 2,
            "childProp1": "2C",
            "childProp2": false,
            "school": "http://localhost:${localPort}/schools/2"
          }
        ]
      }
      """;

  private final static String SECOND_CHILD_SCHOOL_UPDATE_PAYLOAD = """
      {
        "parentProp1": "X",
        "parentProp2": "Y",
        "children": [
          {
            "sequence": 1,
            "childProp1": "1C",
            "childProp2": true,
            "school": "http://localhost:${localPort}/schools/1"
          },
          {
            "sequence": 2,
            "childProp1": "2C",
            "childProp2": false,
            "school": "http://localhost:${localPort}/schools/1"
          }
        ]
      }
      """;

  private final static String PARENT_WITH_FIRST_CHILD_REMOVED_PAYLOAD = """
      {
        "parentProp1": "X",
        "parentProp2": "Y",
        "children": [
           {
            "sequence": 2,
            "childProp1": "2C",
            "childProp2": false,
            "school": "http://localhost:${localPort}/schools/2"
          }
        ]
      }
      """;

  private final static String EXPECTED_RESPONSE_WITH_NO_CHILDREN = """
      {
        "parentProp1" : "X",
        "parentProp2" : "Y",
        "children" : [ ],
        "_links" : {
          "self" : {
            "href" : "http://localhost:${localPort}/parents/1"
          },
          "parent" : {
            "href" : "http://localhost:${localPort}/parents/1"
          }
        }
      }
      """;

  private final static String EXPECTED_RESPONSE_AFTER_ADDING_TWO_CHILDREN = """
      {
        "parentProp1" : "X",
        "parentProp2" : "Y",
        "children" : [ {
          "sequence" : 1,
          "childProp1" : "1C",
          "childProp2" : true,
          "_links" : {
            "school" : {
              "href" : "http://localhost:${localPort}/schools/1"
            }
          }
        }, {
          "sequence" : 2,
          "childProp1" : "2C",
          "childProp2" : false,
          "_links" : {
            "school" : {
              "href" : "http://localhost:${localPort}/schools/2"
            }
          }
        } ],
        "_links" : {
          "self" : {
            "href" : "http://localhost:${localPort}/parents/1"
          },
          "parent" : {
            "href" : "http://localhost:${localPort}/parents/1"
          }
        }
      }
      """;

  private final static String EXPECTED_RESPONSE_AFTER_UPDATING_SECOND_CHILD_SCHOOL = """
      {
        "parentProp1" : "X",
        "parentProp2" : "Y",
        "children" : [ {
          "sequence" : 1,
          "childProp1" : "1C",
          "childProp2" : true,
          "_links" : {
            "school" : {
              "href" : "http://localhost:${localPort}/schools/1"
            }
          }
        }, {
          "sequence" : 2,
          "childProp1" : "2C",
          "childProp2" : false,
          "_links" : {
            "school" : {
              "href" : "http://localhost:${localPort}/schools/1"
            }
          }
        } ],
        "_links" : {
          "self" : {
            "href" : "http://localhost:${localPort}/parents/1"
          },
          "parent" : {
            "href" : "http://localhost:${localPort}/parents/1"
          }
        }
      }
      """;

  private final static String EXPECTED_RESPONSE_AFTER_REMOVING_FIRST_CHILD = """
      {
        "parentProp1" : "X",
        "parentProp2" : "Y",
        "children" : [ {
          "sequence" : 2,
          "childProp1" : "2C",
          "childProp2" : false,
          "_links" : {
            "school" : {
              "href" : "http://localhost:${localPort}/schools/2"
            }
          }
        } ],
        "_links" : {
          "self" : {
            "href" : "http://localhost:${localPort}/parents/1"
          },
          "parent" : {
            "href" : "http://localhost:${localPort}/parents/1"
          }
        }
      }
      """;

  @Test
  @Order(1)
  void shouldCreateParentEntityWithoutAnyChildren_Using_POST() throws JSONException {
    initSchoolEntities();

    var serverAndPort = getServerAndPort();

    ResponseEntity<String> response = restTemplate.exchange(
        serverAndPort + "/parents",
        org.springframework.http.HttpMethod.POST,
        toHttpEntity(PARENT_WITH_NO_CHILDREN_PAYLOAD),
        String.class
    );

    JSONAssert.assertEquals(replaceLocalPort(EXPECTED_RESPONSE_WITH_NO_CHILDREN), response.getBody(), false);
  }

  @Test
  @Order(2)
  void shouldAddTwoChildren_Using_PUT() throws JSONException {
    performRequest(HttpMethod.PUT, PARENT_WITH_TWO_CHILDREN_PAYLOAD, EXPECTED_RESPONSE_AFTER_ADDING_TWO_CHILDREN);
  }

  @Test
  @Order(3)
  void shouldUpdateSchoolForSecondChild_Using_PUT() throws JSONException {
    performRequest(HttpMethod.PUT, SECOND_CHILD_SCHOOL_UPDATE_PAYLOAD, EXPECTED_RESPONSE_AFTER_UPDATING_SECOND_CHILD_SCHOOL);
  }

  @Test
  @Order(4)
  void shouldRemoveFirstChild_Using_PUT() throws JSONException {
    performRequest(HttpMethod.PUT, PARENT_WITH_FIRST_CHILD_REMOVED_PAYLOAD, EXPECTED_RESPONSE_AFTER_REMOVING_FIRST_CHILD);
  }

  @Test
  @Order(5)
  void shouldClearChildrenArray_Using_PUT() throws JSONException {
    performRequest(HttpMethod.PUT, PARENT_WITH_NO_CHILDREN_PAYLOAD, EXPECTED_RESPONSE_WITH_NO_CHILDREN);
  }

  @Test
  @Order(6)
  void shouldAddTwoChildren_Using_PATCH() throws JSONException {
    performRequest(HttpMethod.PATCH, PARENT_WITH_TWO_CHILDREN_PAYLOAD, EXPECTED_RESPONSE_AFTER_ADDING_TWO_CHILDREN);
  }

  @Test
  @Order(7)
  void shouldUpdateSchoolForSecondChild_Using_PATCH() throws JSONException {
    performRequest(HttpMethod.PATCH, SECOND_CHILD_SCHOOL_UPDATE_PAYLOAD, EXPECTED_RESPONSE_AFTER_UPDATING_SECOND_CHILD_SCHOOL);
  }

  @Test
  @Order(8)
  void shouldRemoveFirstChild_Using_PATCH() throws JSONException {
    performRequest(HttpMethod.PATCH, PARENT_WITH_FIRST_CHILD_REMOVED_PAYLOAD, EXPECTED_RESPONSE_AFTER_REMOVING_FIRST_CHILD);
  }

  @Test
  @Order(9)
  void shouldClearChildrenArray_Using_PATCH() throws JSONException {
    performRequest(HttpMethod.PATCH, PARENT_WITH_NO_CHILDREN_PAYLOAD, EXPECTED_RESPONSE_WITH_NO_CHILDREN);
  }

  private void performRequest(HttpMethod httpMethod, String requestPayload, String expectedResponse) throws JSONException {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    var serverAndPort = getServerAndPort();

    ResponseEntity<String> response = restTemplate.exchange(
        serverAndPort + "/parents/1",
        httpMethod,
        toHttpEntity(requestPayload),
        String.class
    );

    assertNotNull(response.getBody());
    JSONAssert.assertEquals(replaceLocalPort(expectedResponse), response.getBody(), false);
  }

  private String getServerAndPort() {
    return "http://localhost:" + localPort;
  }

  private HttpEntity<String> toHttpEntity(String jsonBody) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(replaceLocalPort(jsonBody), headers);
  }

  private String replaceLocalPort(String json) {
    return json.replace("${localPort}", String.valueOf(localPort));
  }

  private void initSchoolEntities() {
    if(schoolRepository.count() == 0) {
      School school1 = new School();
      school1.setId(1L);
      school1.setSchoolProp1("A");
      school1.setSchoolProp2("B");

      School school2 = new School();
      school2.setId(2L);
      school2.setSchoolProp1("C");
      school2.setSchoolProp2("D");

      schoolRepository.saveAll(List.of(school1, school2));
    }
  }
}
