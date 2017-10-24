package org.scada_lts.permissions;

import com.serotonin.mango.view.View;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.dataSource.DataSourceVO;

public class EntityPermission<T> implements IEntityPermision {
	
	T entity;
	int type;
	int id;
	
	EntityPermission(T object) {
		this.entity = object;
		this.type = getType();
		this.id = getId();
	}
	

	@Override
	public int getType() {
		if(entity.getClass().equals(DataSourceVO.class)) {
			return this.DATA_SOURCE;
		} else if(entity.getClass().equals(DataPointVO.class)) {
			return this.DATA_POINT;
		} else if(entity.getClass().equals(View.class)) {
			return this.GRAPHICAL_VIEW;
		} else if(entity.getClass().equals(WatchList.class)) {
			return this.WATCHLIST;
		} else {
			return this.UNKNOWN;
		}
	}


	@Override
	public int getId() {
		if(type == this.DATA_SOURCE) {
			DataSourceVO dataSource = (DataSourceVO) this.entity;
			return dataSource.getId();
		} else if (type == this.DATA_POINT) {
			DataPointVO dataPoint = (DataPointVO) this.entity;
			return dataPoint.getId();
		} else if (type == this.DATA_POINT) {
			WatchList watchList = (WatchList) this.entity;
			return watchList.getId();
		} else if (type == this.GRAPHICAL_VIEW) {
			View view = (View) this.entity;
			return view.getId();
		} else {
			return 0;
		}
	}
	
	

}
