import { Given, When, Then } from '@wdio/cucumber-framework';
import { expect, $ } from '@wdio/globals'

import Wellcome from '../screenobjects/wellcome.screen.js';
import EventsList from '../screenobjects/eventsList.screen.js';
import EventDetails from '../screenobjects/eventDetails.screen.js';
import Login from '../screenobjects/login.screen.js';


const wellcome_screen = new Wellcome()
const events_list_screen = new EventsList()
const event_details_screen = new EventDetails()
const login_screen = new Login()

const screens = {
    wellcome: wellcome_screen,
    'events list': events_list_screen,
    'event details':event_details_screen,
    login:login_screen
}
const status={}
Given('opened app', async () => {
    await driver.activateApp('cmm.apps.esmorga');
    await browser.pause(1000);
});

When(/^tap on (.*)$/, async (where) => {
    const whereTapOn = screens[status.screen].get_where_tap_on(where)
    const findWhereTapOn = await $(whereTapOn);
    await findWhereTapOn.click();

});

When(/^write (.*) on field (.*)$/, async (text,where) => {
    const whereWriteOn = screens[status.screen].get_where_to_write(where)
    const findWriteOn = await $(whereWriteOn);
    await findWriteOn.addValue(text);
});

Then(/^(.*) screen is shown$/, async (screen) => {
    status.screen=screen
    const what_to_seek = screens[status.screen].get_what_to_seek()
    const find_what_to_seek = await $(what_to_seek)
    await expect(find_what_to_seek).toBeDisplayed();

});


