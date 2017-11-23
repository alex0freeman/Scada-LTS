/*
 * (c) 2017 Abil'I.T. http://abilit.eu/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.scada_lts.permissions;

import com.serotonin.mango.view.View;
import com.serotonin.mango.vo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.permissions.model.EntryDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Arkadiusz Parafiniuk arkadiusz.parafiniuk@gmail.com
 */
public class PermissionFilterView {

    private static final Log LOG = LogFactory.getLog(PermissionFilterView.class);

    private static String CLAZZ = "org.scada_lts.domain.View";
    private static Long CLAZZ_ID = 2L;

    private static PermissionFilterView instance = null;

    public static PermissionFilterView getInstance() {
        if(instance == null) {
            instance = new PermissionFilterView();
        }
        return instance;
    }

    private PermissionFilterView() {

    }

    public List<View> filter(List<View> toFilter, User user) {
        List<View> result = null;
        Set<Long> views = PermissionEvaluatorAcl.getInstance().filter(CLAZZ, CLAZZ_ID, user.getUsername(), user.getId(),user.getId()).stream().map(EntryDto::getId).collect(Collectors.toSet());

        result = toFilter.stream().filter(e -> views.contains(e.getId())).collect(Collectors.toList());
        return null;
    }

}
