/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.restrictbyrole;

import org.openmrs.Cohort;
import org.openmrs.User;


public class UserRestrictionResult {

	private boolean restricted;
	private Cohort cohort;
	private User user;
	
	public UserRestrictionResult(User user, boolean restricted){
		this.user = user;
		this.restricted = restricted;
	}
	
	public UserRestrictionResult(User user, boolean restricted, Cohort cohort){
		this(user, restricted);
		this.cohort = cohort;
	}

	/**
	 * Check if this cohort is the result of a restriction or not
	 * @return True if the cohort is the result of a restriction
	 */
	public boolean isRestricted() {
		return restricted;
	}

	/**
	 * Set if this cohort is the result of a restriction or not
	 * @param restricted True if the cohort is the result of a restriction
	 */
	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}
	
	/**
	 * Get the cohort associated to this evaluation
	 * @return The cohort associated to the evaluation
	 */
	public Cohort getCohort() {
		return cohort;
	}

	/**
	 * Set the cohort associated to this evaluation
	 * @param cohort The cohort associated to the evaluation
	 */
	public void setCohort(Cohort cohort) {
		this.cohort = cohort;
	}

	/**
	 * Get the use associated to this evaluation
	 * @return The user associated to this evaluation
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Set the user associated to this evaluation
	 * @param user The User associated to this evaluation
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
