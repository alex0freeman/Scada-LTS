package org.scada_lts.permissions;

public interface IEntityPermision<T> {
	final static int UNKNOWN = 0;
	final static int DATA_SOURCE = 1;
	final static int DATA_POINT = 2;
	final static int GRAPHICAL_VIEW = 3;
	final static int WATCHLIST = 4;
	
	public int getType();
	
	public int getId();
}
