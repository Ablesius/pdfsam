/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 10/dic/2014
 * Copyright 2013-2014 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.ui;

import static org.pdfsam.support.RequireUtils.requireNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Map;

import javax.inject.Named;

import org.pdfsam.i18n.DefaultI18nContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jr.ob.JSON;

/**
 * Implementation of the workspace service where data is stored and loaded in json format
 * 
 * @author Andrea Vacondio
 *
 */
@Named
class JsonWorkspaceService implements WorkspaceService {
    private static final Logger LOG = LoggerFactory.getLogger(JsonWorkspaceService.class);

    public void saveWorkspace(Map<String, Map<String, String>> data, File destination) {
        requireNotNull(destination, "Destination file cannot be null");
        LOG.debug(DefaultI18nContext.getInstance().i18n("Saving workspace data to {0}", destination.getAbsolutePath()));
        try {
            JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT).without(JSON.Feature.WRITE_NULL_PROPERTIES)
                    .write(data, destination);
            LOG.info(DefaultI18nContext.getInstance().i18n("Workspace saved"));
        } catch (Exception e) {
            // make it unchecked
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, String>> loadWorkspace(File workspace) {
        requireNotNull(workspace, "Workspace file cannot be null");
        Map<String, Map<String, String>> data = Collections.emptyMap();
        try (FileInputStream stream = new FileInputStream(workspace)) {
            data = (Map) JSON.std.mapFrom(stream);
        } catch (Exception e) {
            // make it unchecked
            throw new RuntimeException(e);
        }
        return data;
    }

}
