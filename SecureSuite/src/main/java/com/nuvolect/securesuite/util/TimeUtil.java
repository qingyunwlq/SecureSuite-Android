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

package com.nuvolect.securesuite.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

	private static SimpleDateFormat formatMDYM = new SimpleDateFormat("MMM d, yyyy  K:mm a", Locale.US);
	private static SimpleDateFormat formatHrMinSec = new SimpleDateFormat("K:mm:ss a", Locale.US);
	/**
	 * Return time as a string in a user friendly format
	 * @param t
	 * @return string
	 */
	static public String friendlyTimeMDYM(long t){

		formatMDYM.setTimeZone(TimeZone.getDefault());
		return formatMDYM.format( t );
	}
	/**
	 * Return time as a string in a user friendly format
	 * @param t
	 * @return string
	 */
	static public String friendlyTimeHrMinSec(long t){

		formatHrMinSec.setTimeZone(TimeZone.getDefault());
		return formatHrMinSec.format( t );
	}

}
