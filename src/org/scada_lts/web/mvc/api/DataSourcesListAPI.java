package org.scada_lts.web.mvc.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.mango.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.dataSource.DataSourceVO;

/**
 * Controller for data sources list
 * 
 * @author Arkadiusz Parafiniuk  
 * e-mail: arkadiusz.parafiniuk@gmail.com
 */

@Controller
public class DataSourcesListAPI {
	
	private static final Log LOG = LogFactory.getLog(DataSourcesListAPI.class);
	
	private DataSourceService dataSourceService = new DataSourceService();
	
	@RequestMapping(value = "/api/data_sources/getAll", method = RequestMethod.GET)
	public ResponseEntity<String> getAll(HttpServletRequest request) {
		LOG.info("/api/data_sources/getAll");
		
		try{
			User user = Common.getUser(request);
			
			if (user != null) {
				class DataSourceJSON implements Serializable{
					private String xid;
					private String name;
					private String dataSourceType;
					
					DataSourceJSON(String xid,String name) {
						this.setXid(xid);
						this.setName(name);
					}
					
					public void setXid(String xid) {
						this.xid = xid;
					}
					public void setName(String name) {
						this.name = name;
					}
					public String getXid() {
						return xid;
					}
					public String getName() {
						return name;
					}
				}
				
				List<DataSourceVO<?>> listDS = dataSourceService.getDataSources();
				
				List<DataSourceJSON> lst = new ArrayList<DataSourceJSON>();
				for (DataSourceVO ds:listDS) {
					DataSourceJSON dsJ = new DataSourceJSON(ds.getXid(), ds.getName());
					lst.add(dsJ);
				}
				
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(lst);
				
				return new ResponseEntity<String>(json,HttpStatus.OK);		
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
