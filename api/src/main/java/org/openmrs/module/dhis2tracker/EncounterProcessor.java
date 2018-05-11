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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;

import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID;

public class EncounterProcessor {

    protected static final Log log = LogFactory.getLog(EncounterProcessor.class);

    private Dhis2HttpClient dhis2HttpClient;

    private EncounterProcessor() {
    }

    public static EncounterProcessor newInstance() {
        return new EncounterProcessor();
    }

    /**
     * Processes the specified encounter
     *
     * @param encounter the encounter to process
     * @return true if the encounter was processed successfully otherwise false
     */
    public boolean process(Encounter encounter) {
        log.debug("Processing encounter");
        PersonService ps = Context.getPersonService();
        PersonAttributeType uidAttributeType = ps.getPersonAttributeTypeByUuid(DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID);
        if (uidAttributeType == null) {
            throw new APIException("Cannot find person attribute type for dhis2 uid");
        }

        if (dhis2HttpClient == null) {
            dhis2HttpClient = Dhis2HttpClient.newInstance();
        }

        Patient patient = encounter.getPatient();
        PersonAttribute pAttrib = patient.getAttribute(uidAttributeType);
        if (pAttrib == null) {
            String patientUid = dhis2HttpClient.registerAndEnrollInProgramInTracker(patient);
            patient.addAttribute(new PersonAttribute(uidAttributeType, patientUid));
            ps.savePerson(patient);
            return true;
        } else {
            return dhis2HttpClient.sendEventToTracker(patient);
        }
    }

}
