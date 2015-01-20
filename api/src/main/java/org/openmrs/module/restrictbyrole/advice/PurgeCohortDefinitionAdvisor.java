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
