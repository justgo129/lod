package org.agu.essi.util;

import java.io.StringWriter;
import java.util.Vector;

import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;

public class EntityIdentifier {
	
	private static String meetingBaseId = Namespaces.esip + "Meeting_";
	private static String sectionBaseId = Namespaces.esip + "Section_";	
	private static String personBaseId = Namespaces.esip + "Person_";
	private static String organizationBaseId = Namespaces.esip + "Organization_";
	private static String sessionBaseId = Namespaces.esip + "Session_";
	private static String abstractBaseId = Namespaces.esip + "Abstract_";
	private static String keywordBaseId = Namespaces.esip + "Keyword_";
	private static Vector<Person> people = new Vector<Person>();
	private static Vector<Meeting> meetings = new Vector<Meeting>();
	private static Vector<String> organizations = new Vector<String>();
	private static Vector<Session> sessions = new Vector<Session>();
	private static Vector<Section> sections = new Vector<Section>();
	private static Vector<Keyword> keywords = new Vector<Keyword>();
	private static Integer abstractCount = 0;
	
	public static String getMeetingId(Meeting meeting)
	{
		if (meetings.contains(meeting))
		{
			int idx = meetings.indexOf(meeting);
			return meetingBaseId + (idx+1);
		}
		else
		{
			meetings.add(meeting);
			return meetingBaseId + meetings.size();
		}
	}

	public static String getSectionId(Section section)
	{
		if (sections.contains(section))
		{
			int idx = sections.indexOf(section);
			return sectionBaseId + (idx+1);
		}
		else
		{
			sections.add(section);
			return sectionBaseId + sections.size();
		}
	}	
	
	public static String getNextAbstractId()
	{
		return abstractBaseId + (++abstractCount);
	}
	
	public static String getPersonId(Person person)
	{
		if (people.contains(person))
		{
			int idx = people.indexOf(person);
			return personBaseId + (idx+1);
		}
		else
		{
			people.add(person);
			return personBaseId + people.size();
		}
	}
	
	public static String getSessionId(Session session)
	{
		if (sessions.contains(session))
		{
			int idx = sessions.indexOf(session);
			return sessionBaseId + (idx+1);
		}
		else
		{
			sessions.add(session);
			return sessionBaseId + sessions.size();
		}
	}
	
	public static String getOrganizationId(String org)
	{
		if (organizations.contains(org))
		{
			int idx = organizations.indexOf(org);
			return organizationBaseId + (idx+1);
		}
		else
		{
			organizations.add(org);
			return organizationBaseId + organizations.size();
		}
	}
	
	public static String getKeywordId(Keyword keyword)
	{
		if (keywords.contains(keyword))
		{
			int idx = keywords.indexOf(keyword);
			return keywordBaseId + (idx+1);
		}
		else
		{
			keywords.add(keyword);
			return keywordBaseId + keywords.size();
		}
	}
	
	public static String writePeople(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < people.size(); ++i)
			{
				Person p = people.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + personBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&foaf;Person\" />\n");
				sw.write("    <foaf:name rdf:datatype=\"&xsd;string\">" + p.getName() + "</foaf:name>\n");
				sw.write("    <foaf:mbox rdf:resource=\"mailto:" + p.getEmail() + "\" />\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public static String writeKeywords(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < keywords.size(); ++i)
			{
				Keyword k = keywords.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + keywordBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swrc;ResearchTopic\" />\n");
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + k.getId() + "</dc:identifier>\n");
				sw.write("    <dc:subject rdf:datatype=\"&xsd;string\">" + k.getName() + "</dc:subject>\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public static String writeOrganizations(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < organizations.size(); ++i)
			{
				String o = organizations.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + organizationBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&foaf;Organization\" />\n");
				sw.write("    <dc:description rdf:datatype=\"&xsd;string\">" + o + "</dc:description>\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}

	public static String writeSessions(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < sessions.size(); ++i)
			{
				Session s = sessions.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + sessionBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swc;SessionEvent\" />\n");
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + s.getId() + "</dc:identifier>\n");
				sw.write("    <swc:isSubEventOf rdf:resource=\"" + getSectionId(s.getSection()) + "\" />\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public static String writeSections(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < sections.size(); ++i)
			{
				Section s = sections.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + sectionBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swrc;Meeting\" />\n");
				sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + s.getName() + "</swrc:eventTitle>\n");
				sw.write("    <swc:isSubEventOf rdf:resource=\"" + getMeetingId(s.getMeeting()) + "\" />\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public static String writeMeetings(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < meetings.size(); ++i)
			{
				Meeting m = meetings.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + meetingBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swrc;Meeting\" />\n");
				sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + m.getName() + "</swrc:eventTitle>\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
}
