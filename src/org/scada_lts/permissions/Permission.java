package org.scada_lts.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scada_lts.mango.service.DataSourceService;

import com.serotonin.mango.view.View;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.dataSource.DataSourceVO;

/** 
 * Permission ACL
 * 
 * @author Arkadiusz Parafiniuk    email: arkadiusz.parafiniuk@gmail.com
 * 
 */

public class Permission implements IPermission {

	@Override
	public boolean hasObjectPermission(User user, IEntityPermision object, int accessType) {
		if(user.isAdmin()){
			return true;
		} else {
			switch (object.getType()) {
			case IEntityPermision.DATA_SOURCE: 
				if(user.getDataSourcePermissions().contains(object.getId()) || accessType<=IAccessType.NONE) {
					return true;
				} else {
					return false;
				}
			case IEntityPermision.DATA_POINT: ;
			if(user.getDataPointPermissions().contains(object.getId()) || accessType<=IAccessType.NONE) {
				return true;
			} else {
				return false;
			}
			case IEntityPermision.GRAPHICAL_VIEW: ;
			View view = (View) object;
			if(view.getUserAccess(user)>=accessType || accessType<=IAccessType.NONE) {
				return true;
			} else {
				return false;
			}
			case IEntityPermision.WATCHLIST: ;
			WatchList watchList = (WatchList) object;
			if(watchList.getUserAccess(user)>=accessType || accessType<=IAccessType.NONE) {
				return true;
			} else {
				return false;
			}
			default: ;
				break;
			}

		}
		return false;
	}

	@Override
	public Map<Long, IEntityPermision> getObjectsWithPermission(User user, IEntityPermision object) {
		switch (object.getType()) {
		case IEntityPermision.DATA_SOURCE: 
			Map<Long, IEntityPermision> mapDataSources = new HashMap<Long, IEntityPermision>();
			DataSourceService dss = null;
			List<DataSourceVO<?>> listDS = dss.getDataSources();
			for(DataSourceVO<?> ds : listDS) {
				if(user.getDataSourcePermissions().contains(ds.getId())) {
					mapDataSources.put((long) ds.getId(), ds);
				}
			}
			return mapDataSources;
		default: ;
			break;
		}
		return null;
	}

	@Override
	public boolean hasAdminPermission(User user) {
		return user.isAdmin();
	}

}
