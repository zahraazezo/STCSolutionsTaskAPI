package steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

public class PetStoreSteps {
    private Response response;
    private String petName;

    // TODO:Move this to a helper class
    private RequestSpecification createRequestSpec(String path, String body, String queryParamKey, String queryParamValue) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .log(LogDetail.ALL);

        if (body != null) {
            builder.setContentType("application/json")
                    .setBody(body);
        }

        if (queryParamKey != null && queryParamValue != null) {
            builder.addQueryParam(queryParamKey, queryParamValue);
        }

        if (path != null) {
            builder.addPathParam("petId", path);
        }

        return builder.build();
    }

    @Given("User has a pet with id {int}")
    public void user_has_a_pet_with_id(int petId) {
        //TODO:Move to properties file
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        RequestSpecification requestSpec = createRequestSpec(String.valueOf(petId), null, null, null);

        response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get("/pet/{petId}")
                .then()
                .log().all() // Log response details
                .extract().response();

        // Print additional details
        System.out.println("GET Pet Details API Request:");
        System.out.println("Request URL: " + RestAssured.baseURI + "/pet/" + petId);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("User requests the pet details")
    public void user_requests_the_pet_details() {
    }

    @Then("User should receive a valid response with status code {int}")
    public void user_should_receive_a_valid_response_with_status_code(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, "Status code should match");
    }

    @Then("User should be able to extract the pet name")
    public void user_should_be_able_to_extract_the_pet_name() {
        petName = response.jsonPath().getString("name");
        System.out.println("Pet Name: " + petName);
        Assert.assertNotNull(petName, "Pet name should not be null");
    }

    @Given("User requests pets with status {string}")
    public void user_requests_pets_with_status(String status) {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        RequestSpecification requestSpec = createRequestSpec(null, null, "status", status);

        response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all() // Log response details
                .extract().response();

        // Print additional details
        System.out.println("GET FindByStatus API Request:");
        System.out.println("Request URL: " + RestAssured.baseURI + "/pet/findByStatus?status=" + status);
        System.out.println("Request Query Parameters: status=" + status);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("User gets the list of pets")
    public void user_gets_the_list_of_pets() {
    }

    @Then("User should be able to extract the first pet name")
    public void user_should_be_able_to_extract_the_first_pet_name() {
        petName = response.jsonPath().getString("[0].name");
        System.out.println("First Pet Name: " + petName);
        Assert.assertNotNull(petName, "First pet name should not be null");
    }

    @Given("User wants to create a new pet with name {string} and status {string}")
    public void user_wants_to_create_a_new_pet_with_name_and_status(String name, String status) {
        //TODO:Move to properties file
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        String requestBody = "{"
                + "\"id\": 1001,"
                + "\"name\": \"" + name + "\","
                + "\"status\": \"" + status + "\""
                + "}";

        RequestSpecification requestSpec = createRequestSpec(null, requestBody, null, null);

        response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .post("/pet")
                .then()
                .log().all() // Log response details
                .extract().response();

        // Print additional details
        System.out.println("POST Add Pet API Request:");
        System.out.println("Request URL: " + RestAssured.baseURI + "/pet");
        System.out.println("Request Body: " + requestBody);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("User sends a request to add the pet")
    public void user_sends_a_request_to_add_the_pet() {
    }

    @Then("User should see that the created pet name matches {string}")
    public void user_should_see_that_the_created_pet_name_matches(String expectedName) {
        String createdPetName = response.jsonPath().getString("name");
        System.out.println("Created Pet Name: " + createdPetName);
        Assert.assertEquals(createdPetName, expectedName, "Pet name should match the requested name");
    }
}
