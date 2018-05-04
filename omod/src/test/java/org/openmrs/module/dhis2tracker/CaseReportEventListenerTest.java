/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhis2tracker;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhis2tracker.api.Dhis2TrackerConstants;
import org.openmrs.module.dhis2tracker.web.Dhis2HttpClient;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.MapMessage;
import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, Dhis2HttpClient.class})
public class CaseReportEventListenerTest {

    @Mock
    private EncounterService es;

    @Mock
    private Dhis2HttpClient client;

    private CaseReportEventListener listener = new CaseReportEventListener(null);

    @Before
    public void before() throws IOException {
        PowerMockito.mockStatic(Context.class);
        PowerMockito.mockStatic(Dhis2HttpClient.class);
        when(client.post(null, null)).thenReturn(true);
        when(Dhis2HttpClient.newInstance()).thenReturn(client);
    }

    @Test
    public void processMessage_shouldSubmitCaseReportDataToTheHealthExchange() throws Exception {
        EncounterType encType = new EncounterType();
        encType.setName(Dhis2TrackerConstants.LOINC_CODE_CASE_REPORT);
        Encounter enc = new Encounter();
        enc.setEncounterType(encType);
        when(es.getEncounterByUuid(Matchers.eq(enc.getUuid()))).thenReturn(enc);
        when(Context.getEncounterService()).thenReturn(es);

        MapMessage message = new ActiveMQMapMessage();
        message.setString("uuid", enc.getUuid());
        Assert.assertTrue(listener.processMessage(message));
    }

    @Test
    public void processMessage_shouldNotSubmitCaseReportDataForOtherEncounterTypes() throws Exception {
        EncounterType encType = new EncounterType();
        encType.setName("Some encounter type");
        Encounter enc = new Encounter();
        enc.setEncounterType(encType);
        when(es.getEncounterByUuid(Matchers.eq(enc.getUuid()))).thenReturn(enc);
        when(Context.getEncounterService()).thenReturn(es);

        MapMessage message = new ActiveMQMapMessage();
        message.setString("uuid", enc.getUuid());
        Assert.assertFalse(listener.processMessage(message));
    }

}
