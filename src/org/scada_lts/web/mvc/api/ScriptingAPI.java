package org.scada_lts.web.mvc.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;

import br.org.scadabr.db.dao.ScriptDao;
import br.org.scadabr.vo.scripting.ScriptVO;

/**
* Controller for data sources list
* 
* @author Arkadiusz Parafiniuk  
* email: arkadiusz.parafiniuk@gmail.com
*/

@Controller
public class ScriptingAPI {
	
	private static final Log LOG = LogFactory.getLog(ScriptingAPI.class);
	
	@RequestMapping(value = "/api/scripting/getAll", method = RequestMethod.GET)
	public ResponseEntity<String> getAll(HttpServletRequest request) {
		LOG.info("/api/scripting/getAll");
		
		try{
			User user = Common.getUser(request);
			
			if(user != null){
				
				class ScriptingJSON implements Serializable{
					private String xid;
					private String name;
					
					ScriptingJSON(String xid,String name){
						this.setXid(xid);
						this.setName(name);
					}

					public String getXid() {
						return xid;
					}

					public void setXid(String xid) {
						this.xid = xid;
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}
					
				}
				
				List<ScriptVO<?>> scripts = new ScriptDao().getScripts();
				
				List<ScriptingJSON> scriptsJSON = new ArrayList<ScriptingJSON> ();
				for(ScriptVO script : scripts){
					ScriptingJSON scriptJSON = new ScriptingJSON(script.getXid(), script.getName());
					scriptsJSON.add(scriptJSON);
				}
				
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(scriptsJSON);
				
				return new ResponseEntity<String>(json,HttpStatus.OK);	
			}
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		
	}
}
