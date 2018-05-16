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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhis2tracker.model.Attribute;
import org.openmrs.module.dhis2tracker.model.Enrollment;
import org.openmrs.module.dhis2tracker.model.RegisterAndEnroll;

public class Dhis2Utils {
	
	private static final String trackedEntityType = "";
	
	private static final String orgUnit = "";
	
	private static final String program = "";
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String buildRegisterAndEnrollContent(Patient patient, Date date) throws IOException {
		List<Attribute> attributes = new ArrayList<>();
		String incidentDate = Dhis2TrackerConstants.DATE_FORMATTER.format(date);
		Enrollment enrollment = new Enrollment(orgUnit, program, incidentDate);
		RegisterAndEnroll ene = new RegisterAndEnroll(trackedEntityType, orgUnit, attributes, enrollment);
		return mapper.writeValueAsString(ene);
	}
	
	public static String getUrl() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_URL);
	}
	
	public static String getUsername() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_USERNAME);
	}
	
	public static String getPassword() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_PASSWORD);
	}
	
	public static String getTrackedEntityTypeUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_TRACKED_ENTITY_TYPE_UID);
	}
	
	public static String getprogramUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_PROGRAM_UID);
	}
	
	public static String getNewHivCaseProgramStateUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_PROGRAM_STATE_UID);
	}
	
	public static String getFirstnameUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_ATTRIB_FIRSTNAME_UID);
	}
	
	public static String getMiddlenameUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_ATTRIB_MIDDLENAME_UID);
	}
	
	public static String getLastnameAttributeUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_ATTRIB_LASTNAME_UID);
	}
	
	public static String getGenderAttributeUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_ATTRIB_GENDER_UID);
	}
	
	public static String getBirthdateUID() {
		return getGlobalProperty(Dhis2TrackerConstants.GP_ATTRIB_BIRTHDATE_UID);
	}
	
	/**
	 * Convenience method that gets the value of the specified global property name
	 *
	 * @param propertyName the global property name
	 * @return the global property value
	 */
	private static String getGlobalProperty(String propertyName) {
		return Context.getAdministrationService().getGlobalProperty(propertyName);
	}
	
}
