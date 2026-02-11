@smoke 
Feature: System configuration page - this page has below features
  As a user with proper permission I can be able to perform:
  a. check default state and update behavior across tabs
  b. add new country, edit and delete an existing country  


@role_create
  Scenario Outline: User with create role able to add a new country with active status
    Given the user is on the country management page
    #When the user adds a country named "<country>" and activates it
    #Then the country "<country>" appears in the list

    Examples:
      | country |
      | Auto_CountryName     |
      

@role_delete
  Scenario Outline: User with delete permission, can delete an existing country
    Given the user is on the country management page
    #When the user deletes the country "<country>"
    #Then the country "<country>" should no longer exist
 
    Examples:
      | country |
      | Auto_CountryName |
