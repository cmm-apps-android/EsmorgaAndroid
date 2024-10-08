Feature: Smoke

  Scenario: Open App and reach events list screen
    Given Opened app
    When tap on secondary button
    Then events list screen is shown
