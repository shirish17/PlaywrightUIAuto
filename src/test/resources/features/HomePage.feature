Feature:Home page features including company logo, main navigation links, bell icon and user icon.
	As an authenticated user
	I want to use home page to access core modules
	So that I can quickly access main navigation links, bell icon for messages and user icon to get user action functionality.
	
Scenario:Logo is visible on top left and click action navigates user to home page
Then the company logo with "<companyName>" be visible
When I click on the "<companyName>" logo
Then I should land on the page containing header "<destinationHeading>"

Examples:
| companyName       |destinationHeading|
|Sitero|CTMS |

Scenario Outline::Verify main link and navigation

Then the "<link"> link should be visible
When I click the "<link>" link
Then I should land on the "<destinationHeading>" page
Examples:
|link|destinationHeading|
|Configuration|System Configuration|
|Console|Program List|
