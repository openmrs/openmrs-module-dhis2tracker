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

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static org.junit.Assert.assertEquals;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.CONTENT_TYPE_JSON;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.CONTENT_TYPE_XML;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.DATE_FORMATTER;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.HEADER_ACCEPT;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.HEADER_CONTENT_TYPE;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.PARAMS_ID_SCHEMES;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.GlobalProperty;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.OpenmrsClassLoader;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class Dhis2HttpClientTest extends BaseModuleContextSensitiveTest {
	
	private static final Integer DHIS2_PORT = getAvailablePort();
	
	private static final String SUCCESS_RESPONSE_REGISTER = "success_response_register.json";
	
	private static final String SUCCESS_RESPONSE_EVENT = "success_response_event.json";
	
	private static final String FAILURE_RESPONSE_REGISTER = "failure_response_register.json";
	
	private static final String FAILURE_RESPONSE_EVENT = "failure_response_event.json";
	
	private Dhis2HttpClient dhis2HttpClient = Dhis2HttpClient.newInstance();
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(DHIS2_PORT);
	
	public static Integer getAvailablePort() {
		
		for (int i = 1024; i < 49151; i++) {
			try {
				new ServerSocket(i).close();
				return i;
			}
			catch (IOException e) {
				//Port is not available for use
			}
		}
		
		//Really! No port is available?
		throw new APIException("No available port found");
	}
	
	public static void setDhis2Port(Integer port) {
		AdministrationService as = Context.getAdministrationService();
		GlobalProperty gp = as.getGlobalPropertyObject(Dhis2TrackerConstants.GP_URL);
		gp.setPropertyValue(StringUtils.replaceOnce(gp.getPropertyValue(), "{{PORT}}", port.toString()));
		as.saveGlobalProperty(gp);
	}
	
	public static String getResponse(boolean isRegistration, boolean success) throws IOException {
		String filename;
		if (isRegistration) {
			filename = success ? SUCCESS_RESPONSE_REGISTER : FAILURE_RESPONSE_REGISTER;
		} else {
			filename = success ? SUCCESS_RESPONSE_EVENT : FAILURE_RESPONSE_EVENT;
		}
		return IOUtils.toString(OpenmrsClassLoader.getInstance().getResourceAsStream(filename));
	}
	
	public static void createPostStub(String resource, boolean isRegistration, boolean withSuccessResponse)
	    throws IOException {
		
		final int sc = withSuccessResponse ? HttpStatus.SC_OK : HttpStatus.SC_INTERNAL_SERVER_ERROR;
		final String contentType = isRegistration ? CONTENT_TYPE_JSON : CONTENT_TYPE_XML;
		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/" + resource + "?" + PARAMS_ID_SCHEMES))
		        .withHeader(HEADER_ACCEPT, containing(CONTENT_TYPE_JSON))
		        .withHeader(HEADER_CONTENT_TYPE, containing(contentType))
		        //.withRequestBody(containing(""))
		        .withBasicAuth("fake user", "fake password")
		        .willReturn(WireMock.aResponse().withStatus(sc).withHeader("Content-Type", CONTENT_TYPE_JSON)
		                .withBody(getResponse(isRegistration, withSuccessResponse))));
	}
	
	@Test
	public void registerAndEnroll_shouldRegisterAndEnrollThePatientWithDhis2Tracker() throws Exception {
		executeDataSet("moduleTestData-initial.xml");
		final String expectedUid = "z2v7tDgvurD";
		Patient p = new Patient();
		p.addIdentifier(new PatientIdentifier("201", null, null));
		p.addName(new PersonName("Horacio", "Tom", "Hornblower"));
		p.setGender("F");
		p.setBirthdate(DATE_FORMATTER.parse("1980-04-20"));
		Date incidenceDate = DATE_FORMATTER.parse("2018-04-21");
		setDhis2Port(DHIS2_PORT);
		createPostStub(Dhis2HttpClient.RESOURCE_TRACKED_ENTITY_INSTANCES, true, true);
		Encounter e = new Encounter();
		Location location = Context.getLocationService().getLocation(200);
		e.setPatient(p);
		e.setEncounterDatetime(incidenceDate);
		e.setLocation(location);
		String json = Dhis2Utils.buildRegisterAndEnrollContent(e);
		String uid = dhis2HttpClient.registerAndEnroll(json);
		assertEquals(expectedUid, uid);
	}
	
	@Test
	@Ignore
	public void sendEvents_shouldSendEventsToDhis2Tracker() throws Exception {
		executeDataSet("moduleTestData-initial.xml");
		final String patientUid = "z2v7tDgvurD";
		
		Date encDate = DATE_FORMATTER.parse("2018-04-20");
		List<TriggerEvent> events = new ArrayList<>();
		TriggerEvent newHivEvent = new TriggerEvent();
		newHivEvent.setPatientUid(patientUid);
		newHivEvent.setDate(encDate);
		events.add(newHivEvent);
		TriggerEvent newDiseaseEvent = new TriggerEvent();
		newDiseaseEvent.setPatientUid(patientUid);
		newDiseaseEvent.setDate(encDate);
		events.add(newDiseaseEvent);
		setDhis2Port(DHIS2_PORT);
		createPostStub(Dhis2HttpClient.RESOURCE_EVENTS, false, true);
		
		//assertTrue(dhis2HttpClient.sendEvents(events));
	}
	
}
