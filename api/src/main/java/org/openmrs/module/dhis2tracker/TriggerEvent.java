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

import org.openmrs.Obs;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;

import java.util.Date;

import static org.openmrs.module.dhis2tracker.Dhis2TrackerConstants.DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID;

public class TriggerEvent {

    private String patientUid;

    private String trigger;

    private String locationUid = "";

    private Date date;

    public TriggerEvent() {
    }

    public TriggerEvent(Obs obs) {
        PersonService ps = Context.getPersonService();
        PersonAttributeType uidAttributeType = ps.getPersonAttributeTypeByUuid(DHIS2_UID_PERSON_ATTRIBUTE_TYPE_UUID);
        if (uidAttributeType == null) {
            throw new APIException("Cannot find person attribute type for dhis2 uid");
        }
        patientUid = obs.getPerson().getAttribute(uidAttributeType).getValue();
        trigger = obs.getValueCoded().getDisplayString();
        date = obs.getObsDatetime();
        //locationUid = obs.getLocation();
    }

    public String getPatientUid() {
        return patientUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getLocationUid() {
        return locationUid;
    }

    public void setLocationUid(String locationUid) {
        this.locationUid = locationUid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TriggerEvent that = (TriggerEvent) o;

        if (!patientUid.equals(that.patientUid)) {
            return false;
        }
        if (!trigger.equals(that.trigger)) {
            return false;
        }
        if (!locationUid.equals(that.locationUid)) {
            return false;
        }

        return date.equals(that.date);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = patientUid != null ? patientUid.hashCode() : 0;
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        result = 31 * result + (locationUid != null ? locationUid.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
