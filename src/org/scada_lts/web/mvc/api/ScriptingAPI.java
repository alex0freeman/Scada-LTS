package org.scada_lts.web.mvc.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.dao.ScriptDAO;
import org.scada_lts.service.ScriptingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.web.dwr.DwrResponseI18n;

import br.org.scadabr.db.dao.ScriptDao;
import br.org.scadabr.vo.scripting.ContextualizedScriptVO;
import br.org.scadabr.vo.scripting.ScriptVO;
import br.org.scadabr.vo.scripting.ScriptVO.Type;

/**
* Controller for scripting
* 
* @author Arkadiusz Parafiniuk  
* email: arkadiusz.parafiniuk@gmail.com
*/

@Controller
public class ScriptingAPI {
	
	private static final Log LOG = LogFactory.getLog(ScriptingAPI.class);
	
	@Resource
	ScriptingService scriptingService;
	
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
				
				List<ScriptVO<?>> scripts = scriptingService.getScripts();
				
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
		LOG.info("/api/scripting/{id} id:" + id);
		
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
				
				ContextualizedScriptVO script = (ContextualizedScriptVO) scriptingService.getScript(id);
				
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
	
	@RequestMapping(value = "/api/scripting/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteScript(@PathVariable("id") int id, HttpServletRequest request) {
		LOG.info("/api/scripting/{id} id:" + id);
		try {
			User user = Common.getUser(request);
			if(user != null){
				scriptingService.deleteScript(id);
				return new ResponseEntity<String>(HttpStatus.OK);	
			} else {
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/api/scripting", method = RequestMethod.POST)
	public ResponseEntity<String> createScript(
			@RequestHeader("xid") String xid, 
			@RequestHeader("name") String name, 
			@RequestHeader("script") String script, 
			@RequestBody() List<IntValuePair> pointsOnContext, 
			@RequestBody() List<IntValuePair> objectsOnContext, 
			HttpServletRequest request) {
		LOG.info("/api/scripting/createScript");
		try {
			User user = Common.getUser(request);
			if(user != null){
				scriptingService.insertScript(Common.NEW_ID, xid, name, script, pointsOnContext, objectsOnContext);
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/api/scripting/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateScript(
			@PathVariable("id") int id,
			@PathVariable("xid") String xid, 
			@PathVariable("name") String name, 
			@PathVariable("script") String script, 
			@PathVariable("pointsOnContext") List<IntValuePair> pointsOnContext, 
			@PathVariable("objectsOnContext") List<IntValuePair> objectsOnContext,
			HttpServletRequest request) {
		LOG.info("/api/scripting/updateScript {id} id:" + id);
		try {
				User user = Common.getUser(request);
				if(user != null){
					scriptingService.updateScript(id, "1324234", "aaa", "scriptscriptscript", null, null);
					return new ResponseEntity<String>(HttpStatus.OK);
				} else {
					return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
				}
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
