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
package org.openmrs.module.restrictbyrole.advice;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.definition.service.SerializedDefinitionService;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * Advisor that intercepts the following methods: 
 * {@link org.openmrs.module.reporting.definition.service.SerializedDefinitionService#purgeDefinition(String)}
 */
public class PurgeCohortDefinitionAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor{

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return (SerializedDefinitionService.class.isAssignableFrom(targetClass) && 
				method.getName().equals("purgeDefinition"));
	}

	public Advice getAdvice() {
		return new PurgeCohortDefinitionAdvice();
	}
	
	private class PurgeCohortDefinitionAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			// Identify definition
			for(Object arg : invocation.getArguments()){
				if(CohortDefinition.class.isAssignableFrom(arg.getClass())){
					RestrictByRoleService rbrService = Context.getService(RestrictByRoleService.class);
					rbrService.retireRestrictionsWithCohortDefinition((CohortDefinition)arg);
				}
			}
			return invocation.proceed();
		}
		
	}
	
}
