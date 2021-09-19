Feature: Make some coffee

  Scenario: make coffee
    Given I order coffee that cost 50
    When I pay 75
    Then I get back 25

  Scenario: make coffee with exact cost
    Given I order coffee that cost 50
    When I pay 50
    Then I get back 0

  Scenario: make coffee with not enough money
    Given I order coffee that cost 100
    When I pay 50
    Then I get back 50

  Scenario: make coffee with not enough ingredients
    Given I order coffee that cost 50
    When I pay 75
    And I don't have enough ingredients
    Then I get back 75