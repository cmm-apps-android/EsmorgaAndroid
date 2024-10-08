import { Given, When, Then } from '@wdio/cucumber-framework';
import { expect, $ } from '@wdio/globals'

import LoginPage from '../pageobjects/login.page.js';
import SecurePage from '../pageobjects/secure.page.js';

const pages = {
    login: LoginPage
}

Given('Opened app', async () => {
    await browser.pause(5000);
});

When('tap on secondary button', async () => {
//    const el1 = mob.findElement("xpath://androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.Button");
    const secondaryButton = await $('//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.Button'); // Cambia esto por el selector correcto
    await secondaryButton.click();
});

Then('events list screen is shown', async () => {
    const eventsListScreen = await $('//android.widget.TextView[@text="Event list"]'); // Cambia esto por el selector correcto
    await expect(eventsListScreen).toBeDisplayed();
});


