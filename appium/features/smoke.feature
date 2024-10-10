Feature: MOB_Feature

	Scenario: TC - Event manager MVP - Check event list and event details for a certain event
		Given opened app
		Then wellcome screen is shown
		When tap on secondary button
		Then events list screen is shown
		When tap on event
		Then event details screen is shown

   	Scenario: TC - Login MVP - Login
		Given opened app
		Then wellcome screen is shown
		When tap on primary button
		Then login screen is shown
		When write esmorga.test.04@yopmail.com on field email
		And write Password!4 on field password
		And tap on primary button
		Then events list screen is shown