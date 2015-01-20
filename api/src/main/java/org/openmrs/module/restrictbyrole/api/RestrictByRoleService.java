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
package org.openmrs.module.restrictbyrole.api;

import java.util.List;
import java.util.Set;

import org.openmrs.Cohort;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(RestrictByRoleService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface RestrictByRoleService extends OpenmrsService {
	
	/**
	 * Add a RoleRestrictiction to the database
	 * @param rolePermission
	 */
	public void createRoleRestriction(RoleRestriction rolePermission);
	
	/**
	 * Update an existing RoleRestriction
	 * @param rolePermission
	 */
	public void updateRoleRestriction(RoleRestriction rolePermission);
	
	/**
	 * Delete a RoleRestriction
	 * @param rolePermission
	 */
	public void deleteRoleRestriction(RoleRestriction rolePermission);
	
	/**
	 * Get RoleRestriction by Id
	 * @param id Id for the RoleRestriction
	 * @return The corresponding RoleRestriction
	 */
	public RoleRestriction getRoleRestriction(Integer id);
	
	/**
	 * Get the list of all existing RoleRestrictions
	 * @return List of RoleRestrictions
	 */
	public List<RoleRestriction> getRoleRestrictions();
	
	/**
	 * Get the list of RoleRestrictions associated to a role (including retired)
	 * @param role Role to check the RoleRestrictions for
	 * @return List of RoleRestrictions
	 */
	public List<RoleRestriction> getRoleRestrictions(Role role);
	
	/**
	 * Get the list of active RoleRestrictions associated to a role
	 * @param role Role to check the RoleRestrictions for
	 * @return List of RoleRestrictions
	 */
	public List<RoleRestriction> getActiveRoleRestrictions(Role role);
	
	/**
	 * Check if the current user has permission to view/edit the patient provided as parameter
	 * @param patient Patient to check permission for
	 * @return If current user has permission or not
	 */
	public boolean doesCurrentUserHavePermission(Patient patient);
	
	/**
	 * Check if the current user has permission to view/edit the patient provided as parameter
	 * @param patientId If of the patient to check permission for
	 * @return If current user has permission or not
	 */
	public boolean doesCurrentUserHavePermission(Integer patientId);

	/**
	 * Get the list of RoleRestrictions associated to the current user (including retired)
	 * @return List of RoleRestrictions
	 */
	public Set<RoleRestriction> getCurrentUserRestrictions();
	
	/**
	 * Get the list of active RoleRestrictions associated to the current user
	 * @return List of active RoleRestrictions
	 */
	public Set<RoleRestriction> getCurrentUserActiveRestrictions();
	
	/**
	 * Get the cohort (list of ID's) that current user has permission for
	 * @return Cohort associated to current user
	 */
	public Cohort getCurrentUserRestrictedPatientSet();
	
	/**
	 * Get all SerializedObjects
	 * @return List of SerializedObjects
	 */
	public List<SerializedObject> getAllSerializedObjects();
	
	/**
	 * Get a SerializedObject by Id
	 * @param id Id of the SerializedObject
	 * @return SerializedObject
	 */
	public SerializedObject getSerializedObject(Integer id);
	
	/**
	 * Get a SerializedObject by uuid
	 * @param uuid Uuid of the SerializedObject
	 * @return SerializedObject
	 */
	public SerializedObject getSerializedObjectByUuid(String uuid);
	
	/**
	 * Retire all restrictions that contains the cohort definition
	 * @param cohort The cohort definition
	 */
	public void retireRestrictionsWithCohortDefinition(CohortDefinition cohort);
}