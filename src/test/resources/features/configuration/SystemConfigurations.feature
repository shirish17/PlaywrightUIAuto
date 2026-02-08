@smoke 
Feature: System configuration page - Tabs with defaults and update capabilities
  As a user
  I want to validate default state and update behavior of tabs under system configuration page
  So that configuration is correct and consitent across the application

@role_edit
  Scenario: Check initial state of the List(s) tab when the user opens it
    When the user navigates to "List(s)" tab
    Then the user should be on the "List(s)" tab
    Then the "List(s)" tab displays following contols with default values
      | type      | label           | expected        |
      | text      | List label      | List label      |
      | searchbox | Filter Items... | Filter Items... |
      | linkText  | Country Name    | Country Name    |

@role_create
  Scenario Outline: Add a new active country
    When the user adds a country named "<country>" and activates it
    Then the country "<country>" appears in the list

    Examples:
      | country |
      | country_DDMMYY     |
