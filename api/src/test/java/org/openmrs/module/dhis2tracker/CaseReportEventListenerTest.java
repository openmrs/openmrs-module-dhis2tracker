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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;

import javax.jms.MapMessage;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class, EncounterProcessor.class })
public class CaseReportEventListenerTest {
	
	@Mock
	private AdministrationService as;
	
	@Mock
	private EncounterService es;
	
	@Mock
	private EncounterProcessor ep;
	
	@Mock
	private Concept caseReportConcept;
	
	private CaseReportEventListener listener = new CaseReportEventListener(null);
	
	private EncounterType caseReportEncounterType;
	
	@Before
	public void before() throws IOException {
		PowerMockito.mockStatic(Context.class);
		PowerMockito.mockStatic(EncounterProcessor.class);
		when(EncounterProcessor.newInstance()).thenReturn(ep);
		when(ep.process(any(Encounter.class))).thenReturn(true);
		when(Context.getAdministrationService()).thenReturn(as);
		when(Context.getEncounterService()).thenReturn(es);
		final String conceptAndEncounterTypeName = "Public health Case report";
		when(as.getGlobalProperty(eq(Dhis2TrackerConstants.GP_CR_ENCOUNTER_TYPE_NAME)))
		        .thenReturn(conceptAndEncounterTypeName);
		when(caseReportConcept.getName()).thenReturn(new ConceptName(conceptAndEncounterTypeName, Locale.ENGLISH));
		caseReportEncounterType = new EncounterType();
		caseReportEncounterType.setName(conceptAndEncounterTypeName);
		when(es.getEncounterType(eq(conceptAndEncounterTypeName))).thenReturn(caseReportEncounterType);
	}
	
	@Test
	public void processMessage_shouldProcessMessageIfTheEncounterIsOfTheCaseReportEncounterType() throws Exception {
		Encounter enc = new Encounter();
		enc.setEncounterType(caseReportEncounterType);
		when(es.getEncounterByUuid(eq(enc.getUuid()))).thenReturn(enc);
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
		when(es.getEncounterByUuid(eq(enc.getUuid()))).thenReturn(enc);
		
		MapMessage message = new ActiveMQMapMessage();
		message.setString("uuid", enc.getUuid());
		Assert.assertFalse(listener.processMessage(message));
	}
	
}
