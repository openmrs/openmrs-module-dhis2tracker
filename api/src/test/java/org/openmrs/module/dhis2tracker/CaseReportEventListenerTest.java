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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.jms.MapMessage;

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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class, EncounterProcessor.class })
public class CaseReportEventListenerTest {
	
	@Mock
	private EncounterService es;
	
	@Mock
	private EncounterProcessor ep;
	
	private CaseReportEventListener listener = new CaseReportEventListener(null);
	
	@Before
	public void before() throws IOException {
		PowerMockito.mockStatic(Context.class);
		PowerMockito.mockStatic(EncounterProcessor.class);
		when(EncounterProcessor.newInstance()).thenReturn(ep);
		when(ep.process(any(Encounter.class))).thenReturn(true);
	}
	
	@Test
	public void processMessage_shouldProcessMessageIfTheEncounterIsOfTheCaseReportEncounterType() throws Exception {
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
	public void processMessage_shouldNotProcessMessageIfTheEncounterIsOfAnotherEncounterType() throws Exception {
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
