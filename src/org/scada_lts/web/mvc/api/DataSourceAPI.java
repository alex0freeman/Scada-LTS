package org.scada_lts.web.mvc.api;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.mango.service.DataSourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO.Type;

@Controller
public class DataSourceAPI {
	
	private static final Log LOG = LogFactory.getLog(DataSourceAPI.class);
	
	private DataSourceService dataSourceService = new DataSourceService();
	
	@RequestMapping(value = "/api/data_source/getProperties/{xid}", method = RequestMethod.POST)
	public ResponseEntity<String> getProperties(@PathVariable("xid") String xid, HttpServletRequest request) {
		LOG.info("/api/source_properties/getProperties/{xid} id:"+xid);
		
		try {
			// check may use watch list
			User user = Common.getUser(request);
			if (user != null) {
				DataSourceVO dsvo = dataSourceService.getDataSource(xid);
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				
				class PropertiesSourceToJSON implements Serializable {
					
						private static final long serialVersionUID = 1L;
						
						private String name;
						private String xid;
						private Type type;
						private String typeKey;
						private int id;
						
						public PropertiesSourceToJSON(int id, String name, String xid, Type type, String typeKey) {
							
							this.id = id;
							this.name = name;
							this.xid = xid;
							this.type = type;
							this.typeKey = typeKey;
							
						}

						public String getName() {
							return name;
						}

						public void setName(String name) {
							this.name = name;
						}

						public String getXid() {
							return xid;
						}

						public void setXid(String xid) {
							this.xid = xid;
						}

						public Type getType() {
							return type;
						}

						public void setType(Type type) {
							this.type = type;
						}

						public String getTypeKey() {
							return typeKey;
						}

						public void setTypeKey(String typeKey) {
							this.typeKey = typeKey;
						}

						public int getId() {
							return id;
						}

						public void setId(int id) {
							this.id = id;
						}
						
				}
				
				ResourceBundle bundle = Common.getBundle();
		
				PropertiesSourceToJSON p = new PropertiesSourceToJSON( 
						dsvo.getId(),
						dsvo.getName(),
						dsvo.getXid(),
						dsvo.getType(),
						dsvo.getTypeKey()
						);
				
					json = mapper.writeValueAsString(p);
					
					return new ResponseEntity<String>(json,HttpStatus.OK);
				}
			
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
				
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	

}
