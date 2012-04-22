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
package org.esipfed.data;


import java.io.FileReader;
import java.util.Vector;
import org.esipfed.Cluster;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/** An XML Parser for the ESIP People RDF/XML
 * 
 * @author Tom Narock
 * 
 */
public class ClusterParserESIP extends DefaultHandler {
 
    StringBuffer accumulator = new StringBuffer();
    Vector <Cluster> clusters = new Vector <Cluster> ();
    String clusterID = "";
    
     /** Parse the file and return the information in a Person Vector
      * 
	  * @param  file  the file to parse
	  * @return Vector <Person>
	  */
    public Vector <Cluster> parse ( String file ) throws Exception {
    	XMLReader xr = XMLReaderFactory.createXMLReader();
        ClusterParserESIP handler = new ClusterParserESIP();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        FileReader r = new FileReader(file);
        xr.parse(new InputSource(r));
        return handler.clusters;
    }

    public ClusterParserESIP() { super(); }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////

     /** Method which tells the XML parser what to do at the start of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  */
    public void startElement (String uri, String name,
		      String qName, Attributes atts) {
    	
    	accumulator.setLength(0); // Ready to accumulate new text
    	if ( name.equals("Description") ) { this.clusterID = atts.getValue("rdf:about"); }
    	
    }
    
     /** Method which tells the XML parser what to do at the end of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  * @param  qName  the fully qualified name of the element
	  */
    public void endElement (String uri, String name, String qName) {

      String d = accumulator.toString().trim();
      if ( name.equals("title") ) { 
    	  Cluster c = new Cluster ( d, this.clusterID);
    	  clusters.add(c);
      }
	  
    }
    
     /** Method for parsing the character data of the XML file
	  *
	  */
    public void characters (char ch[], int start, int length) { accumulator.append(ch, start, length); }

}
