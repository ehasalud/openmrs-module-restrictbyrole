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
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.db.SerializedObject;
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
     
public void createRoleRestriction(RoleRestriction rolePermission);
	
	public void updateRoleRestriction(RoleRestriction rolePermission);
	
	public void deleteRoleRestriction(RoleRestriction rolePermission);
	
	public RoleRestriction getRoleRestriction(Integer id);
	
	public List<RoleRestriction> getRoleRestrictions();
	
	public boolean doesCurrentUserHavePermission(Patient patient);
	
	public boolean doesCurrentUserHavePermission(Integer patientId);

	public Set<RoleRestriction> getCurrentUserRestrictions();
	
	public Cohort getCurrentUserRestrictedPatientSet();
	
	public List<SerializedObject> getAllSerializedObjects();
	
	public SerializedObject getSerializedObject(Integer id);
	
	public SerializedObject getSerializedObjectByUuid(String uuid);
}