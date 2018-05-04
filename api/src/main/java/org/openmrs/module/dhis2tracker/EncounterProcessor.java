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

public class EncounterProcessor {

    protected static final Log log = LogFactory.getLog(EncounterProcessor.class);

    private EncounterProcessor() {
    }

    public static EncounterProcessor newInstance() {
        return new EncounterProcessor();
    }

    /**
     * Processes the specified encounter
     *
     * @param encounter the encounter to process
     * @return true if the encounter was processed otherwise false
     */
    public boolean process(Encounter encounter) {
        log.debug("Processing encounter");
        //Check if patient has a dhis2 identifier
        //If the don't register and enroll
        //Otherwise enroll or post event
        return true;
    }

}
