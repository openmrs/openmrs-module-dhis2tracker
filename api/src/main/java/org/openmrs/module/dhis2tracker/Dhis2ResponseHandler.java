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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.dhis2tracker.model.Dhis2Response;

public class Dhis2ResponseHandler extends AbstractResponseHandler<Dhis2Response> {
	
	protected static final Log log = LogFactory.getLog(Dhis2ResponseHandler.class);
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public Dhis2Response handleEntity(HttpEntity httpEntity) throws IOException {
		log.debug("Unmarshalling the response contents");
		String json = EntityUtils.toString(httpEntity);
		//log.debug("Response Json: "+json);
		return mapper.readValue(json, Dhis2Response.class);
	}
	
}
