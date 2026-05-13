package org.redhat.rhdh.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerResourceTest {

    private static final String BASE_PATH = "/api/v1/customers";

    @Test
    @Order(1)
    void testCreateCustomer() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@example.com",
                    "phone": "555-1234"
                }
                """)
        .when()
            .post(BASE_PATH)
        .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("firstName", equalTo("John"))
            .body("lastName", equalTo("Doe"))
            .body("email", equalTo("john.doe@example.com"))
            .body("id", notNullValue());
    }

    @Test
    @Order(2)
    void testCreateCustomerMissingFields() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "firstName": "",
                    "lastName": ""
                }
                """)
        .when()
            .post(BASE_PATH)
        .then()
            .statusCode(400);
    }

    @Test
    @Order(3)
    void testCreateDuplicateEmail() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "firstName": "Jane",
                    "lastName": "Doe",
                    "email": "john.doe@example.com",
                    "phone": "555-5678"
                }
                """)
        .when()
            .post(BASE_PATH)
        .then()
            .statusCode(409);
    }

    @Test
    @Order(4)
    void testGetCustomer() {
        given()
        .when()
            .get(BASE_PATH + "/1")
        .then()
            .statusCode(200)
            .body("firstName", equalTo("John"))
            .body("email", equalTo("john.doe@example.com"));
    }

    @Test
    @Order(5)
    void testGetCustomerNotFound() {
        given()
        .when()
            .get(BASE_PATH + "/999")
        .then()
            .statusCode(404)
            .body("error", equalTo("Not Found"));
    }

    @Test
    @Order(6)
    void testListCustomers() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 20)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(200)
            .body("total", greaterThanOrEqualTo(1))
            .body("page", equalTo(0))
            .body("size", equalTo(20));
    }

    @Test
    @Order(7)
    void testUpdateCustomer() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "firstName": "John",
                    "lastName": "Smith",
                    "email": "john.smith@example.com",
                    "phone": "555-9999"
                }
                """)
        .when()
            .put(BASE_PATH + "/1")
        .then()
            .statusCode(200)
            .body("lastName", equalTo("Smith"))
            .body("email", equalTo("john.smith@example.com"));
    }

    @Test
    @Order(8)
    void testUpdateCustomerNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "firstName": "Ghost",
                    "lastName": "User",
                    "email": "ghost@example.com"
                }
                """)
        .when()
            .put(BASE_PATH + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    @Order(9)
    void testDeleteCustomer() {
        given()
        .when()
            .delete(BASE_PATH + "/1")
        .then()
            .statusCode(204);
    }

    @Test
    @Order(10)
    void testDeleteCustomerNotFound() {
        given()
        .when()
            .delete(BASE_PATH + "/999")
        .then()
            .statusCode(404);
    }
}
