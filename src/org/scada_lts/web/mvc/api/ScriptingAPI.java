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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;

import br.org.scadabr.db.dao.ScriptDao;
import br.org.scadabr.vo.scripting.ContextualizedScriptVO;
import br.org.scadabr.vo.scripting.ScriptVO;
import br.org.scadabr.vo.scripting.ScriptVO.Type;

/**
* Controller for data sources list
* 
* @author Arkadiusz Parafiniuk  
* email: arkadiusz.parafiniuk@gmail.com
*/

@Controller
public class ScriptingAPI {
	
	private static final Log LOG = LogFactory.getLog(ScriptingAPI.class);
	
	@RequestMapping(value = "/api/scripting", method = RequestMethod.GET)
	public ResponseEntity<String> getAll(HttpServletRequest request) {
		LOG.info("/api/scripting");
		
		try{
			User user = Common.getUser(request);
			
			if(user != null){
				
				class ScriptsListJSON implements Serializable{
					private int id;
					private String name;
					
					ScriptsListJSON(){
					}
					
					ScriptsListJSON(int id, String name){
						this.setId(id);
						this.setName(name);
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}

					public int getId() {
						return id;
					}

					public void setId(int id) {
						this.id = id;
					}
					
				}
				
				List<ScriptVO<?>> scripts = new ScriptDao().getScripts();
				
				List<ScriptsListJSON> scriptsJSON = new ArrayList<ScriptsListJSON> ();
				for(ScriptVO script : scripts){
					ScriptsListJSON scriptJSON = new ScriptsListJSON(script.getId(), script.getName());
					scriptsJSON.add(scriptJSON);
				}
				
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(scriptsJSON);
				
				return new ResponseEntity<String>(json,HttpStatus.OK);	
			} else {
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/api/scripting/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getScript(@PathVariable("id") int id, HttpServletRequest request) {
		LOG.info("/api/scripting/{xid} id:" + id);
		
		try {
			User user = Common.getUser(request);
			
			if(user != null){
				
				class ScriptJSON implements Serializable{
					private int id;
					private String xid;
					private String name;
					private Type type;
					private String script;
					private List<IntValuePair> pointsOnContext;
					
					ScriptJSON(int id, String xid, String name, Type type, String script, List<IntValuePair> pointsOnContext) {
						this.id = id;
						this.xid = xid;
						this.name = name;
						this.type = type;
						this.script = script;
						this.pointsOnContext = pointsOnContext;
					}
					
					public int getId() {
						return id;
					}
					public void setId(int id) {
						this.id = id;
					}
					public String getName() {
						return name;
					}
					public void setName(String name) {
						this.name = name;
					}
					public Type getType() {
						return type;
					}
					public void setType(Type type) {
						this.type = type;
					}

					public String getScript() {
						return script;
					}

					public void setScript(String script) {
						this.script = script;
					}

					public List<IntValuePair> getPointsOnContext() {
						return pointsOnContext;
					}

					public void setPointsOnContext(List<IntValuePair> pointsOnContext) {
						this.pointsOnContext = pointsOnContext;
					}

					public String getXid() {
						return xid;
					}

					public void setXid(String xid) {
						this.xid = xid;
					}
					
				}
				
				ContextualizedScriptVO script = (ContextualizedScriptVO) new ScriptDao().getScript(id);
				
				ScriptJSON scriptJSON = new ScriptJSON(id, script.getXid(), script.getName(), script.getType(), script.getScript(), script.getPointsOnContext());
				
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(scriptJSON);
				
				return new ResponseEntity<String>(json,HttpStatus.OK);	
			} else {
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
