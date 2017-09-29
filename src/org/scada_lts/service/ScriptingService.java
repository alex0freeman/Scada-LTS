package org.scada_lts.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.dao.ScriptDAO;
import org.springframework.stereotype.Service;

import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;

import br.org.scadabr.db.dao.ScriptDao;
import br.org.scadabr.rt.scripting.ScriptRT;
import br.org.scadabr.vo.scripting.ContextualizedScriptVO;
import br.org.scadabr.vo.scripting.ScriptVO;

/**
* Service for Scripting
* 
* @author Arkadiusz Parafiniuk  
* email: arkadiusz.parafiniuk@gmail.com
*/

@Service
public class ScriptingService {
	
	private static final Log LOG = LogFactory.getLog(ScriptingService.class);
	
	@Resource
	ScriptDAO scriptDAO;
	
	public List<ScriptVO<?>> getScripts(){
		return scriptDAO.getScripts();
	}
	
	public ScriptVO<?> getScript(int id){
		return scriptDAO.getScript(id);
	}
	
	public void deleteScript(int id){
		scriptDAO.delete(id);
	}
	
	
	
	public void insertScript(int id, String xid, String name,
		String script, List<IntValuePair> pointsOnContext,
		List<IntValuePair> objectsOnContext){
		
		ContextualizedScriptVO contextualizedScriptVO = new ContextualizedScriptVO();
		contextualizedScriptVO.setId(id);
		contextualizedScriptVO.setXid(xid);
		contextualizedScriptVO.setName(name);
		contextualizedScriptVO.setScript(script);
		contextualizedScriptVO.setPointsOnContext(pointsOnContext);
		contextualizedScriptVO.setObjectsOnContext(objectsOnContext);
		contextualizedScriptVO.setUserId(Common.getUser().getId());
		
		try{
			scriptDAO.insert(contextualizedScriptVO);
		} catch (Exception e) {
			LOG.warn("Script save error");
		}
	}
	
	public void updateScript(int id, String xid, String name,
		String script, List<IntValuePair> pointsOnContext,
		List<IntValuePair> objectsOnContext){
		
		ContextualizedScriptVO contextualizedScriptVO = new ContextualizedScriptVO();
		contextualizedScriptVO.setId(id);
		contextualizedScriptVO.setXid(xid);
		contextualizedScriptVO.setName(name);
		contextualizedScriptVO.setScript(script);
		contextualizedScriptVO.setPointsOnContext(pointsOnContext);
		contextualizedScriptVO.setObjectsOnContext(objectsOnContext);
		contextualizedScriptVO.setUserId(Common.getUser().getId());
		
		try{
			scriptDAO.update(contextualizedScriptVO);;
		} catch (Exception e) {
			LOG.warn("Script save error");
		}
	}
	
	public void executeScript(int id){
		ScriptVO<?> script = scriptDAO.getScript(id);

		try {
			if (script != null) {
				ScriptRT rt = script.createScriptRT();
				rt.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
