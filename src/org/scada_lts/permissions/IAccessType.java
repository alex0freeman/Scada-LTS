package org.scada_lts.permissions;

/** 
 * Access type interface 
 * 
 * @author Arkadiusz Parafiniuk    email: arkadiusz.parafiniuk@gmail.com
 * 
 */

public interface IAccessType {
	final static int NONE = 0;
	final static int READ = 1;
	final static int WRITE = 2;
	final static int ADMIN = 3;	
}
