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
package essi.lod.data.source;

import java.util.Vector;


import essi.lod.entity.esip.Person;
import essi.lod.exception.SourceNotReadyException;

/**
 * Interface for specifying sources of ESIP people
 * @author Tom Narock
 */
public interface PersonDataSourceEsip extends DataSource {
	
	/**
	 * Method to retrieve the set of people from the data source
	 * @return a Vector of People instances
	 */
	public Vector <Person> getPeople( String xmlFile ) throws SourceNotReadyException;
	
}
