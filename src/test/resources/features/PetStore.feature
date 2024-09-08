Feature: Pet Store API

  Scenario: Get All Pets by Status
    Given User requests pets with status "available"
    When User gets the list of pets
    Then User should receive a valid response with status code 200
    And User should be able to extract the first pet name

  Scenario: Create New Pet
    Given User wants to create a new pet with name "New Pet" and status "available"
    When User sends a request to add the pet
    Then User should receive a valid response with status code 200
    And User should see that the created pet name matches "New Pet"

  Scenario: Get Random Pet
    Given User has a pet with id 2
    When User requests the pet details
    Then User should receive a valid response with status code 200
    And User should be able to extract the pet name
