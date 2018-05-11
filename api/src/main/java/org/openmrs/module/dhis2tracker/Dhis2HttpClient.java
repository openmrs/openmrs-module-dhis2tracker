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

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openmrs.Patient;

public class Dhis2HttpClient {
	
	protected static final Log log = LogFactory.getLog(Dhis2HttpClient.class);
	
	private static final String DHIS2_URL = "http://localhost/";
	
	private Dhis2HttpClient() {
	}
	
	public static Dhis2HttpClient newInstance() {
		return new Dhis2HttpClient();
	}
	
	/**
	 * Registers the specified patient in DHIS2 and enrolls them in the program
	 *
	 * @param patient the patient ro register and enroll
	 * @return the generated UID of the patient in DHIS2
	 */
	public String registerAndEnroll(Patient patient) {
		return null;
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
	
	public boolean post(String resource, Object data) {
		log.debug("Posting data to DHIS2");
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(DHIS2_URL + resource);
			CloseableHttpResponse response = httpclient.execute(post, null, null);
			try {
				log.debug("Processing response...");
			}
			finally {
				response.close();
			}
		}
		catch (Exception e) {
			log.error("An error occurred while submitting data to dhi2tracker", e);
		}
		finally {
			try {
				httpclient.close();
			}
			catch (IOException e) {
				log.error("An error occurred while closing http client", e);
			}
		}
		
		return false;
	}
}
