/*
 * Copyright (c) 2017. Nuvolect LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.nuvolect.securesuite.license;

import android.content.Context;

import com.nuvolect.securesuite.util.DeviceInfo;

import java.util.HashSet;
import java.util.Set;

public class Whitelist {

	/**
	 * Check if a device is on the whitelist.  This is done using the a unique install ID
	 * in place of an email to avoid having to use the accounts permission.
	 * @param ctx
	 * @return
	 */
	public static String getWhiteListCredentials(Context ctx) {

		String uniqueDeviceId = DeviceInfo.getUniqueInstallId(ctx);

		if( developers.contains(uniqueDeviceId))
			return uniqueDeviceId;
		else
			return "";
	}

	/** Build the set of whitelist emails, all must be lower case */
	private static Set<String> developers = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
	{
        add("bfdcdf9d4013bd90"); // Matt's nexus 9
		add("c1e933593df14675"); // Matt's nexus 5
	}};
}
