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
package essi.lod.abstracts;

import java.util.Vector;


import essi.lod.entity.agu.Author;
import essi.lod.entity.agu.Keyword;
import essi.lod.entity.agu.Meeting;
import essi.lod.entity.agu.Section;
import essi.lod.entity.agu.Session;
import essi.lod.enumeration.AbstractEnumeration;

/**
 * Extension of Abstract for a basic constructor
 * @author Eric Rozell
 */
public class DefaultAbstract extends Abstract 
{
	// containers for abstract info
	private String _hour;
	private String _abstractId;
	private String _title;
	private String _abstract;
	private Vector<Author> _authors;
	private Vector<Keyword> _keywords;
	private Meeting _meeting;
	private Section _section;
	private Session _session;
	private AbstractEnumeration _type;	
	
	/**
	 * @constructor
	 */
	public DefaultAbstract()
	{
		_authors = new Vector<Author>();
		_keywords = new Vector<Keyword>();
	}
	
	public void setTitle(String t)
	{
		_title = t;
	}
	
	public void setAbstract(String a)
	{
		_abstract = a;
	}
	
	public void setId(String id)
	{
		_abstractId = id;
	}
	
	public void addAuthor(Author a)
	{
		_authors.add(a);
	}
	
	public void addAuthors(Vector<Author> a)
	{
		_authors.addAll(a);
	}
	
	public void addKeyword(Keyword k)
	{
		_keywords.add(k);
	}
	
	public void addKeywords(Vector<Keyword> k)
	{
		_keywords.addAll(k);
	}
	
	public void setSession(Session s)
	{
		_session = s;
	}
	
	public void setAbstractType(AbstractEnumeration at)
	{
		_type = at;
	}
	
	public void setHour(String h)
	{
		_hour = h;
	}
	
	/**
	 * @constructor
	 * @param title
	 * @param abstr
	 * @param id
	 * @param hour
	 * @param session
	 * @param authors
	 * @param keywords
	 */
	public DefaultAbstract(String title, String abstr, String id, String hour, Session session, Vector<Author> authors, Vector<Keyword> keywords)
	{
		_title = title;
		_abstract = abstr;
		_abstractId = id;
		_hour = hour;
		_session = session;
		_section = session.getSection();
		_meeting = _section.getMeeting();
		_authors = authors;
		_keywords = keywords;
	}
	
	@Override
	public String getTitle() 
	{
		return _title;
	}

	@Override
	public Meeting getMeeting() 
	{
		return _meeting;
	}

	@Override
	public String getId() 
	{
		return _abstractId;
	}

	@Override
	public Session getSession() 
	{
		return _session;
	}

	@Override
	public String getAbstract() 
	{
		return _abstract;
	}

	@Override
	public AbstractEnumeration getAbstractType() 
	{
		return _type;
	}

	@Override
	public Vector<Keyword> getKeywords() 
	{
		return _keywords;
	}

	@Override
	public Vector<Author> getAuthors() 
	{
		return _authors;
	}

	@Override
	public Section getSection() 
	{
		return _section;
	}

	@Override
	public String getHour() 
	{
		return _hour;
	}
}
