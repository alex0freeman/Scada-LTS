package org.scada_lts.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.dao.ScriptDAO;
import org.springframework.stereotype.Service;

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
	

}
