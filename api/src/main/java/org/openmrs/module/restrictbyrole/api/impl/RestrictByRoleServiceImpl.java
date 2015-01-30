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
package org.openmrs.module.restrictbyrole.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.Cohort;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.api.db.SerializedObjectDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.serializer.ReportingSerializer;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.UserRestrictionResult;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.openmrs.module.restrictbyrole.api.db.RestrictByRoleDAO;
import org.openmrs.serialization.SerializationException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * It is a default implementation of {@link RestrictByRoleService}.
 */
public class RestrictByRoleServiceImpl extends BaseOpenmrsService implements RestrictByRoleService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private RestrictByRoleDAO dao;
	private SerializedObjectDAO sodao;
	
	public RestrictByRoleDAO getDao() {
		return dao;
	}

	public void setDao(RestrictByRoleDAO dao) {
		this.dao = dao;
	}	
	
	public SerializedObjectDAO getSodao() {
		return sodao;
	}

	public void setSodao(SerializedObjectDAO sodao) {
		this.sodao = sodao;
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#createRoleRestriction(RoleRestriction)
	 */
	public void createRoleRestriction(RoleRestriction roleRestriction) {
		getDao().createRoleRestriction(roleRestriction);
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#deleteRoleRestriction(RoleRestriction)
	 */
	public void deleteRoleRestriction(RoleRestriction roleRestriction) {
		getDao().deleteRoleRestriction(roleRestriction);
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getRoleRestriction(Integer)
	 */
	public RoleRestriction getRoleRestriction(Integer id) {
		return getDao().getRoleRestriction(id);
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getRoleRestrictions()
	 */
	public List<RoleRestriction> getRoleRestrictions() {
		return getDao().getRoleRestrictions();
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getRoleRestrictions(Role)
	 */
	public List<RoleRestriction> getRoleRestrictions(Role role) {
		return getDao().getRoleRestrictions(role, true);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getActiveRoleRestrictions(Role)
	 */
	public List<RoleRestriction> getActiveRoleRestrictions(Role role) {
		return getDao().getRoleRestrictions(role, false);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#updateRoleRestriction(RoleRestriction)
	 */
	public void updateRoleRestriction(RoleRestriction roleRestriction) {
		getDao().updateRoleRestriction(roleRestriction);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#doesCurrentUserHavePermission(Patient)
	 */
	public boolean doesCurrentUserHavePermission(Patient patient) {
		return doesCurrentUserHavePermission(patient.getPatientId());
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#doesCurrentUserHavePermission(Integer)
	 */
	public boolean doesCurrentUserHavePermission(Integer patientId) {
		UserRestrictionResult urr = getCurrentUserRestrictionResult();
		if(!urr.isRestricted())
			return true;
		else
			return urr.getCohort().contains(patientId);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getUserRestrictions(User,boolean)
	 */
	public Set<RoleRestriction> getUserRestrictions(User user, boolean includeRetired) {
		if (user == null)
			return null;
		Set<Role> roles = user.getAllRoles();
		Set<RoleRestriction> ret = new HashSet<RoleRestriction>();
		for (Role role : roles){
			if(includeRetired)
				ret.addAll(getRoleRestrictions(role));
			else
				ret.addAll(getActiveRoleRestrictions(role));
		}			
		log.debug("current user has " + ret.size() + " restrictions");
		return ret;
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getCurrentUserRestrictions(boolean)
	 */
	public Set<RoleRestriction> getCurrentUserRestrictions(boolean includeRetired) {
		if (!Context.isAuthenticated())
			return null;
		User currentUser = Context.getAuthenticatedUser();
		return getUserRestrictions(currentUser, includeRetired);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getAllSerializedObjects()
	 */
	public List<SerializedObject> getAllSerializedObjects(){
		return sodao.getAllSerializedObjects(CohortDefinition.class, false);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getUserRestrictionResult(User)
	 */
	public UserRestrictionResult getUserRestrictionResult(User user){
		
		// This method can take a long time to execute, so the result is cached
		try {
			return cachedUserRestrictionResult.get(user);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getCurrentUserRestrictionResult()
	 */
	public UserRestrictionResult getCurrentUserRestrictionResult() {
		if (!Context.isAuthenticated())
			return null;
		User currentUser = Context.getAuthenticatedUser();
		return getUserRestrictionResult(currentUser);
	}
	
	/**
	 * This is the method that performs the evaluation of user restrictions and 
	 * generate the restricted patient set for the user. This method is accessed 
	 * through a cache in order to improve performance
	 * @param user User to be evaluated
	 * @return UserRestrictionResult associated to the user
	 */
	private UserRestrictionResult generateUserRestrictiontResult(User user){
		Set<RoleRestriction> restrictions = getUserRestrictions(user, false);
		if (restrictions == null || restrictions.size() == 0)
			return new UserRestrictionResult(user, false);
		
		Cohort ret = null;
		for (RoleRestriction restriction : restrictions) {
					
			try {

				SerializedObject so = restriction.getSerializedObject();
				CohortDefinition c = Context.getSerializationService().deserialize(so.getSerializedData(), CohortDefinition.class, ReportingSerializer.class);

				CohortDefinitionService pq = Context.getService(CohortDefinitionService.class);
				EvaluatedCohort result = pq.evaluate(c, null);
				
				ret = ret == null ? result : Cohort.intersect(ret, result);
				
			} catch (SerializationException e) {
				e.printStackTrace();
			} catch (EvaluationException e) {
				e.printStackTrace();
			}
			
		}
				
		return new UserRestrictionResult(user, true, ret);		
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getSerializedObject(Integer)
	 */
	public SerializedObject getSerializedObject(Integer id) {
		return sodao.getSerializedObject(id);
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#getSerializedObjectByUuid(String)
	 */
	public SerializedObject getSerializedObjectByUuid(String uuid) {
		return sodao.getSerializedObjectByUuid(uuid);
	}

	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#retireRestrictionsWithCohortDefinition(CohortDefinition)
	 */
	@Override
	public void retireRestrictionsWithCohortDefinition(CohortDefinition cohort) {
		dao.retireRestrictionsWithCohortDefinition(cohort);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyrole.api.RestrictByRoleService#invalidateGetUserRestrictedPatientSetCache();
	 */
	public void invalidateGetUserRestrictedPatientSetCache(){
		this.cachedUserRestrictionResult.invalidateAll();
	}
	
	private LoadingCache<User, UserRestrictionResult> cachedUserRestrictionResult = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build(
					new CacheLoader<User, UserRestrictionResult>() {
						public UserRestrictionResult load (User user){
							return generateUserRestrictiontResult(user);
						}
					});
	
}