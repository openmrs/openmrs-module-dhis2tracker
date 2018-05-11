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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, Dhis2HttpClient.class})
public class EncounterProcessorTest {

    @Mock
    private PersonService ps;

    @Mock
    private Dhis2HttpClient dhis2HttpClient;

    private EncounterProcessor processor = EncounterProcessor.newInstance();

    @Test
    public void process_shouldRegisterAndEnrollThePatientInAProgramInTracker() {
        mockStatic(Context.class);
        mockStatic(Dhis2HttpClient.class);
        final String expectedUid = "test-uid";
        Patient p = new Patient();
        Encounter e = new Encounter();
        e.setPatient(p);
        when(Context.getPersonService()).thenReturn(ps);
        PersonAttributeType uidAttribType = new PersonAttributeType();
        uidAttribType.setUuid(DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID);
        when(ps.getPersonAttributeTypeByUuid(eq(DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID))).thenReturn(uidAttribType);
        when(Dhis2HttpClient.newInstance()).thenReturn(dhis2HttpClient);
        when(dhis2HttpClient.registerAndEnrollInProgramInTracker(eq(p))).thenReturn(expectedUid);

        assertNull(p.getAttribute(uidAttribType));
        assertTrue(processor.process(e));
        assertEquals(expectedUid, p.getAttribute(uidAttribType).getValue());
    }

    @Test
    public void process_shouldSendEventToTrackerIfThePatientIsAlreadyRegisteredInDhis2() {
        mockStatic(Context.class);
        mockStatic(Dhis2HttpClient.class);
        when(Context.getPersonService()).thenReturn(ps);
        PersonAttributeType uidAttribType = new PersonAttributeType();
        uidAttribType.setUuid(DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID);
        when(ps.getPersonAttributeTypeByUuid(eq(DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID))).thenReturn(uidAttribType);
        Patient p = new Patient();
        p.addAttribute(new PersonAttribute(uidAttribType, "test-uid"));
        Encounter e = new Encounter();
        e.setPatient(p);
        when(Dhis2HttpClient.newInstance()).thenReturn(dhis2HttpClient);
        when(dhis2HttpClient.sendEventToTracker(eq(p))).thenReturn(true);

        assertNotNull(p.getAttribute(uidAttribType));
        assertTrue(processor.process(e));
    }

}
