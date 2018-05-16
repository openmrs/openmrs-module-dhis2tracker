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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.PERSON_ATTRIBUTE_TYPE_UUID;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptName;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.util.LocaleUtility;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class, Dhis2HttpClient.class, LocaleUtility.class })
public class EncounterProcessorTest {
	
	private static final Locale en = Locale.ENGLISH;
	
	@Mock
	private PersonService ps;
	
	@Mock
	private Dhis2HttpClient dhis2HttpClient;
	
	private EncounterProcessor processor = EncounterProcessor.newInstance();
	
	@Test
	public void process_shouldRegisterAndEnrollThePatientInAProgramInTracker() throws Exception {
		mockStatic(Context.class);
		mockStatic(Dhis2HttpClient.class);
		mockStatic(LocaleUtility.class);
		when(LocaleUtility.getLocalesInOrder()).thenReturn(Collections.singleton(en));
		final String expectedUid = "test-uid";
		Patient p = new Patient();
		Encounter e = new Encounter();
		e.setPatient(p);
		Concept triggerConcept = createTriggerConcept();
		Obs newHivCase = new Obs();
		newHivCase.setConcept(triggerConcept);
		newHivCase.setObsDatetime(new Date());
		newHivCase.setValueCoded(createConcept("New HIV Case"));
		Obs newDisease = new Obs();
		newDisease.setConcept(triggerConcept);
		newDisease.setValueCoded(createConcept("New HIV Disease"));
		newDisease.setObsDatetime(new Date());
		e.addObs(newHivCase);
		e.addObs(newDisease);
		List<TriggerEvent> events = new ArrayList<>();
		TriggerEvent newHivEvent = new TriggerEvent();
		//newHivEvent.setTrigger(newHivCase.getValueCoded().getDisplayString());
		newHivEvent.setPatientUid(expectedUid);
		newHivEvent.setDate(newHivCase.getObsDatetime());
		events.add(newHivEvent);
		TriggerEvent newDiseaseEvent = new TriggerEvent();
		//newDiseaseEvent.setTrigger(newDisease.getValueCoded().getDisplayString());
		newDiseaseEvent.setPatientUid(expectedUid);
		newDiseaseEvent.setDate(newDisease.getObsDatetime());
		events.add(newDiseaseEvent);
		when(Context.getPersonService()).thenReturn(ps);
		PersonAttributeType uidAttribType = new PersonAttributeType();
		uidAttribType.setUuid(PERSON_ATTRIBUTE_TYPE_UUID);
		when(ps.getPersonAttributeTypeByUuid(eq(PERSON_ATTRIBUTE_TYPE_UUID))).thenReturn(uidAttribType);
		when(Dhis2HttpClient.newInstance()).thenReturn(dhis2HttpClient);
		when(dhis2HttpClient.registerAndEnroll(eq(p), eq(e.getEncounterDatetime()))).thenReturn(expectedUid);
		when(dhis2HttpClient.sendEvents(argThat(new ListMatcher(events)))).thenReturn(true);
		
		assertNull(p.getAttribute(uidAttribType));
		assertTrue(processor.process(e));
		assertEquals(expectedUid, p.getAttribute(uidAttribType).getValue());
	}
	
	@Test
	public void process_shouldSendEventToTrackerIfThePatientIsAlreadyRegisteredInDhis2() throws Exception {
		mockStatic(Context.class);
		mockStatic(Dhis2HttpClient.class);
		mockStatic(LocaleUtility.class);
		when(LocaleUtility.getLocalesInOrder()).thenReturn(Collections.singleton(en));
		final String expectedUid = "test-uid";
		PersonAttributeType uidAttribType = new PersonAttributeType();
		uidAttribType.setUuid(PERSON_ATTRIBUTE_TYPE_UUID);
		when(Context.getPersonService()).thenReturn(ps);
		when(ps.getPersonAttributeTypeByUuid(eq(PERSON_ATTRIBUTE_TYPE_UUID))).thenReturn(uidAttribType);
		Patient p = new Patient();
		p.addAttribute(new PersonAttribute(uidAttribType, expectedUid));
		Encounter e = new Encounter();
		e.setPatient(p);
		Obs newHivCase = new Obs();
		newHivCase.setConcept(createTriggerConcept());
		newHivCase.setObsDatetime(new Date());
		newHivCase.setValueCoded(createConcept("New HIV Case"));
		e.addObs(newHivCase);
		//Add an obs with a different trigger concept to ensure it is ignored
		Obs nonTriggerConcept = new Obs();
		Concept c = createTriggerConcept();
		c.getConceptMappings().iterator().next().getConceptReferenceTerm().setCode("Other code");
		nonTriggerConcept.setConcept(c);
		nonTriggerConcept.setObsDatetime(new Date());
		nonTriggerConcept.setValueCoded(createConcept("Other Trigger"));
		e.addObs(nonTriggerConcept);
		List<TriggerEvent> events = new ArrayList<>();
		TriggerEvent newHivEvent = new TriggerEvent();
		//newHivEvent.setTrigger(newHivCase.getValueCoded().getDisplayString());
		newHivEvent.setPatientUid(expectedUid);
		newHivEvent.setDate(newHivCase.getObsDatetime());
		events.add(newHivEvent);
		when(Dhis2HttpClient.newInstance()).thenReturn(dhis2HttpClient);
		when(dhis2HttpClient.sendEvents(argThat(new ListMatcher(events)))).thenReturn(true);
		
		assertNotNull(p.getAttribute(uidAttribType));
		assertTrue(processor.process(e));
	}
	
	private Concept createTriggerConcept() {
		return createConcept("Trigger");
	}
	
	private Concept createConcept(String conceptName) {
		Concept c = new Concept();
		c.addName(new ConceptName(conceptName, en));
		ConceptSource source = new ConceptSource();
		source.setHl7Code(Dhis2TrackerConstants.TRIGGER_CONCEPT_SOURCE);
		ConceptReferenceTerm term = new ConceptReferenceTerm(source, Dhis2TrackerConstants.TRIGGER_CONCEPT_CODE, null);
		c.addConceptMapping(new ConceptMap(term, new ConceptMapType()));
		return c;
	}
	
}
