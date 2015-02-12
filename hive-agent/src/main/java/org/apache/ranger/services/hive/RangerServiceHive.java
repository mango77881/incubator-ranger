/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ranger.services.hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ranger.plugin.model.RangerService;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.service.RangerBaseService;
import org.apache.ranger.plugin.service.ResourceLookupContext;
import org.apache.ranger.services.hive.client.HiveResourceMgr;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RangerServiceHive extends RangerBaseService {

	private static final Log LOG = LogFactory.getLog(RangerServiceHive.class);
	
	RangerService		service;
	RangerServiceDef	serviceDef;
	Map<String, String> configs;
	String			    serviceName;
	
	public RangerServiceHive() {
		super();
	}
	
	@Override
	public void init(RangerServiceDef serviceDef, RangerService service) {
		super.init(serviceDef, service);
		init();
	}

	@Override
	public HashMap<String,Object> validateConfig() throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerServiceHive.validateConfig Service: (" + service + " )");
		}
		if ( configs != null) {
			try  {
				ret = HiveResourceMgr.testConnection(service.getName(), service.getConfigs());
			} catch (Exception e) {
				LOG.error("<== RangerServiceHive.validateConfig Error:" + e);
				throw e;
			}
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerServiceHive.validateConfig Response : (" + ret + " )");
		}
		return ret;
	}

	@Override
	public List<String> lookupResource(ResourceLookupContext context) throws Exception {
		
		List<String> ret = new ArrayList<String>();
		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerServiceHive.lookupResource Context: (" + context + ")");
		}
		if (context != null) {
			try {
				ret  = HiveResourceMgr.getHiveResources(service.getName(),service.getConfigs(),context);
			} catch (Exception e) {
			  LOG.error( "<==RangerServiceHive.lookupResource Error : " + e);
			  throw e;
			}
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("<== RangerServiceHive.lookupResource Response: (" + ret + ")");
		}
		return ret;
	}
	
	public void init() {
		service		 = getService();
		serviceDef	 = getServiceDef();
		serviceName  = service.getName();
		configs 	 = service.getConfigs();
	}
	
}
