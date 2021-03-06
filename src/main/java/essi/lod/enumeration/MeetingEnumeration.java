/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package essi.lod.enumeration;

/**
 * Enumeration of types of AGU meetings
 * @author Eric Rozell 
 */
public enum MeetingEnumeration 
{
	FALL 
	{
		public String toString()
		{
			return "FM";
		}
	},
	JOINT
	{
		public String toString()
		{
			return "JM";
		}
	}, 
	SPRING 
	{
		public String toString()
		{
			return "SM";
		}
	}, 
	AMERICAS
	{
		public String toString()
		{
			return "MA";
		}
	},
	GEOPHYSICS
	{
		public String toString()
		{
			return "WG";
		}
	}
}
