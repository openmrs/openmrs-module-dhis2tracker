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

import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.CONTENT_TYPE_JSON;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.CONTENT_TYPE_XML;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.HEADER_ACCEPT;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.HEADER_AUTH;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.HEADER_CONTENT_TYPE;
import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.SUB_PATH_API;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.module.dhis2tracker.model.Dhis2Response;
import org.openmrs.module.dhis2tracker.model.ImportSummary;

public class Dhis2HttpClient {
	
	protected static final Log log = LogFactory.getLog(Dhis2HttpClient.class);
	
	public static final String RESOURCE_REGISTER_AND_ENROLL = "trackedEntityInstances";
	
	public static final String RESOURCE_EVENT = "events";
	
	private Dhis2HttpClient() {
	}
	
	public static Dhis2HttpClient newInstance() {
		return new Dhis2HttpClient();
	}
	
	/**
	 * Registers the specified patient in DHIS2 and enrolls them in the program
	 *
	 * @param patient the patient ro register and enroll
	 * @param incidentDate The date of occurrence of the incidence
	 * @return the generated UID of the patient in DHIS2
	 */
	public String registerAndEnroll(Patient patient, Date incidentDate) throws IOException {
		Object data = Dhis2Utils.buildRegisterAndEnrollContent(patient, incidentDate);
		Dhis2Response response = post(RESOURCE_REGISTER_AND_ENROLL, data, true);
		if (HttpStatus.SC_OK != response.getHttpStatusCode()
		        || !ImportSummary.STATUS_SUCCESS.equals(response.getResponse().getStatus())) {
			throw new APIException("Registration of patient with id: " + patient.getId() + " was not successful");
		}
		log.debug("Extracting generated the UID of the registered patient");
		
		return response.getResponse().getImportSummaries().get(0).getReference();
	}
	
	/**
	 * Sends the specified events to DHIS tracker
	 *
	 * @param events the events to send
	 * @return true if the event are successfully sent otherwise false;
	 */
	public boolean sendEvents(List<TriggerEvent> events) {
		
		return false;
	}
	
	public Dhis2Response post(String resource, Object data, boolean isRegistration) throws IOException {
		log.debug("Posting data to DHIS2");
		
		String url = Dhis2Utils.getUrl();
		String username = Dhis2Utils.getUsername();
		String password = Dhis2Utils.getPassword();
		url = url.endsWith("/") ? url : url + "/";
		HttpPost post = new HttpPost(url + SUB_PATH_API + resource);
		post.addHeader(HEADER_ACCEPT, CONTENT_TYPE_JSON);
		post.addHeader(HEADER_CONTENT_TYPE, isRegistration ? CONTENT_TYPE_JSON : CONTENT_TYPE_XML);
		String authToken = Base64.encodeBase64String((username + ":" + password).getBytes());
		post.addHeader(HEADER_AUTH, "Basic " + authToken);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			return httpclient.execute(post, new Dhis2ResponseHandler());
		}
		catch (IOException e) {
			log.error("An error occurred while submitting data to dhi2tracker", e);
			throw e;
		}
		finally {
			try {
				httpclient.close();
			}
			catch (IOException e) {
				log.error("An error occurred while closing http client", e);
			}
		}
	}
	
}
