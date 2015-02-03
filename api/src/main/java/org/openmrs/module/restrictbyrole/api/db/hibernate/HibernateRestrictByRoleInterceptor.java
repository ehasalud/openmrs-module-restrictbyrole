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
package org.openmrs.module.restrictbyrole.api.db.hibernate;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @see org.hibernate.EmptyInterceptor
 *
 */
public class HibernateRestrictByRoleInterceptor extends EmptyInterceptor implements ApplicationContextAware, Serializable {

	private static final long serialVersionUID = 1L;
	private static HibernateRestrictByRoleInterceptor instance;
	private ApplicationContext context;
	
	private Class[] targetClasses = {Patient.class, User.class};

	public static HibernateRestrictByRoleInterceptor getInstance() {
		if (instance == null) {
			instance = new HibernateRestrictByRoleInterceptor();
		}
		return instance;
	}
	
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, 
			String[] propertyNames, org.hibernate.type.Type[] types){
		return processEntity(entity);
	}
	
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, 
			String[] propertyNames, org.hibernate.type.Type[] types){
		processEntity(entity);
	}
	
	@Override
	public boolean onFlushDirty (Object entity, Serializable id, Object[] state, 
			Object[] previousState, String[] propertyNames, Type[] types){
		return processEntity(entity);
	}
	
	private boolean processEntity(Object entity){
		for (Class type : targetClasses){
			if (entity.getClass().isAssignableFrom(type)){
				Context.getService(RestrictByRoleService.class)
					.invalidateGetUserRestrictedPatientSetCache();
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

}
