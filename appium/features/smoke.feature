Feature: MOB_Feature

	@MOB-TC-59 @JREQ-MOB-5 @AIO-FOLDER-Esmorga_App/E2E_Tests/Event_Manager_MVP
	Scenario: TC - Event manager MVP - Check event list and event details for a certain event
		Given I am not authenticated
		When Opening the app
		Then I should be able to explore the list of events
		And I should be able to check event details

	@MOB-TC-67 @JREQ-MOB-53 @AIO-FOLDER-Esmorga_App/E2E_Tests/Event_Manager_Enhanced
	Scenario: TC - Event Manager Enhanced - Join an event and check it in my events
		Given I am authenticated
		When Opening the app
		Then I should be able to join an event from details screen
		And I should be able to see the event joined in My Events screen

	@MOB-TC-68 @JREQ-MOB-53 @AIO-FOLDER-Esmorga_App/E2E_Tests/Event_Manager_Enhanced
	Scenario: TC - Event Manager Enhanced - Check my events and unsubscribe from an event
		Given I am authenticated
		And I have joined an event
		When opening My Events
		Then I should be able to check the event joined and unsubscribe from it
		And I should no longer see the event in My Events list

	@MOB-TC-169 @JREQ-MOB-31 @JREQ-MOB-123 @AIO-FOLDER-Esmorga_App/E2E_Tests/User_Management
	Scenario:  TC - Forgot password - E2E - forgot password flow
	This test covers the entire forgot password flow from requesting a code to updating the password.
		Given the POST Forgot Password API is available
		When a POST request is made to Forgot Password API
		Then well-formed success response with status code 204 returned
		And reset password email with correct format is received
		Given the PUT Password Update API is available
		And forgot password code via email is used
		When a PUT request is made to Password Update API
		Then well-formed success response with status code 204 returned
		Given the POST Login API is available
		And the user can now log in with the new password
		When a POST request is made to Login API
		Then well-formed success response with status code 200 returned
    
  Scenario: Open App and reach events list screen
    Given Opened app
    When tap on secondary button
    Then events list screen is shown
