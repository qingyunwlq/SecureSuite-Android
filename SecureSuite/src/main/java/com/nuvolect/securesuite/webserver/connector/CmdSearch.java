/*
 * Copyright (c) 2017. Nuvolect LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Contact legal@nuvolect.com for a less restrictive commercial license if you would like to use the
 * software without the GPLv3 restrictions.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not,
 * see <http://www.gnu.org/licenses/>.
 *
 */

package com.nuvolect.securesuite.webserver.connector;//

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nuvolect.securesuite.main.CConst;
import com.nuvolect.securesuite.util.LogUtil;
import com.nuvolect.securesuite.util.Omni;
import com.nuvolect.securesuite.util.OmniFile;
import com.nuvolect.securesuite.util.OmniUtil;
import com.nuvolect.securesuite.webserver.connector.base.ConnectorJsonCommand;

import java.io.InputStream;
import java.util.Map;


/**
 * search
 *
 * Return a list of files and folders list, that match the search string. arguments:
 *
 * cmd : search
 * q : search string,
 * Response:
 *
 * files : (Array) array of objects - files and folders list,
 * that match the search string.
 *
 * GET '/servlet/connector' {_=1459337627027, q=rose, cmd=search, target=l0_L3N0b3JhZ2UvZW11bGF0ZWQvMA}
 */
public class CmdSearch extends ConnectorJsonCommand {

    public static boolean DEBUG = LogUtil.DEBUG;

    @Override
    public InputStream go(@NonNull Map<String, String> params) {
        String url = params.get("url");
        /**
         * //TODO offer elFinder documentation
         * Target is one of:
         * 1. A hash for a directory or volume
         * 2. Empty: meaning to search all volumes
         */
        String target = params.get("target");
        String SearchStr = params.get("q");

        if (DEBUG) {
            LogUtil.log(LogUtil.LogType.CMD_SEARCH, "Target: " +
                    target + ", search: " + SearchStr);
        }

        /**
         * Files contains a list of search results
         */
        JsonArray files = new JsonArray();

        if (target.isEmpty()) {
            for (String volumeId: Omni.getActiveVolumeIds()) {
                // Search all volumes
                OmniFile targetFile = new OmniFile( volumeId, CConst.ROOT);
                searchTarget( SearchStr, targetFile, files, url);
            }
            
        } else {
            // Search a directory
            OmniFile targetFile = OmniUtil.getFileFromHash(target);
            searchTarget( SearchStr, targetFile, files, url);
        }

        JsonObject wrapper = new JsonObject();
        wrapper.add("files", files);

        if (DEBUG) {
            LogUtil.log(LogUtil.LogType.CMD_SEARCH, wrapper.toString());
        }

        return getInputStream(wrapper);
    }

    /**
     * Recursively search the target folder or volume for target files containing
     * the search string.
     * 
     * @param SearchStr
     * @param targetFile
     * @param files
     * @param url
     */
    private void searchTarget(
            String SearchStr, OmniFile targetFile, JsonArray files, String url) {

        if (DEBUG) {
            LogUtil.log(LogUtil.LogType.CMD_SEARCH, "Search target: "
                    + targetFile.getPath());
        }

        if (targetFile.getName().contains( SearchStr)) {
            files.add(targetFile.getFileObject(url));
        }
        
        for (OmniFile file: targetFile.listFiles()) {
            if (file.isDirectory()) {
                /**
                 * Recurse on each directory
                 */
                searchTarget( SearchStr, file, files, url);
            } else {
                if (file.getName().contains(SearchStr)) {
                    files.add(file.getFileObject(url));
                }
            }
        }
    }
}
