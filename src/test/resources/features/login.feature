Feature: Authentication on The Internet Login Page
  In order to securely access the secure area
  As a user of the application
  I want to be able to log in and receive proper feedback

  Background:
    Given I open the login page

  # 1. Successful login with valid credentials
  Scenario: Successful login
    When I enter username "tomsmith" and password "SuperSecretPassword!"
    And I click on the login button
    Then I should see a success message containing "You logged into a secure area!"

  # 2. Unsuccessful login with invalid password
  Scenario: Invalid password
    When I enter username "tomsmith" and password "WrongPassword"
    And I click on the login button
    Then I should see an error message containing "Your password is invalid!"

  # 3. Unsuccessful login with invalid username
  Scenario: Invalid username
    When I enter username "wronguser" and password "SuperSecretPassword!"
    And I click on the login button
    Then I should see an error message containing "Your username is invalid!"

  # 4. Unsuccessful login with blank username
  Scenario: Blank username
    When I enter username "" and password "SuperSecretPassword!"
    And I click on the login button
    Then I should see an error message containing "Your username is invalid!"

  # 5. Unsuccessful login with blank password
  Scenario: Blank password
    When I enter username "tomsmith" and password ""
    And I click on the login button
    Then I should see an error message containing "Your password is invalid!"

  # 6. Unsuccessful login with both fields blank
  Scenario: Both credentials blank
    When I enter username "" and password ""
    And I click on the login button
    Then I should see an error message containing "Your username is invalid!"

  # 7. Error message disappears after navigating away
  Scenario: Error cleared on page refresh
    When I enter username "tomsmith" and password "WrongPassword"
    And I click on the login button
    Then I should see an error message
    When I refresh the page
    Then I should not see any error or success message

  # 8. Logout from secure area returns to login page
  Scenario: Logout returns to login page
    When I enter username "tomsmith" and password "SuperSecretPassword!"
    And I click on the login button
    Then I click on the logout button
    Then I should be back on the login page with message containing "You logged out of the secure area!"

  # 9. Case sensitivity of username
  Scenario: Username is case-sensitive
    When I enter username "TomSmith" and password "SuperSecretPassword!"
    And I click on the login button
    Then I should see an error message containing "Your username is invalid!"

  # 10. Leading/trailing whitespace is trimmed
  Scenario: Credentials with extra whitespace
    When I enter username "  tomsmith  " and password "  SuperSecretPassword!  "
    And I click on the login button
    Then I should see a success message containing "You logged into a secure area!"
