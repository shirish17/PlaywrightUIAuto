Feature: Home page features including company logo, main navigation links, bell icon and user icon.
  As an authenticated user
  I want to use home page to access core modules
  So that I can quickly access main navigation links, bell icon for messages and user icon to get user action
  functionality.

  #@smoke
  Scenario: Navigate to homepage via company logo
    Given the user is on any page
    Then the company logo "<companyName>" is visible on the top navigation bar
    When the user navigates to the homepage using the company logo
    Then the homepage displays the header "<destinationHeading>"

    Examples:
      | companyName | destinationHeading |
      | Sitero      | CTMS               |

  Scenario Outline: Ensure top navigation links lead to correct pages
    Then the "<link>" link is visible in the top navigation bar
    When the user navigates to "<link>" using the top navigation
    Then the page displays the header "<expectedHeader>"

    Examples:
      | link          | destinationHeading   |
      | Configuration | System Configuration |
      | Console       | Program List         |
