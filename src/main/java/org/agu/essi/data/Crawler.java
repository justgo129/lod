package org.agu.essi.data;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Vector;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import org.agu.essi.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.match.MemoryMatcher;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.EntityMatcherRequiredException;
import org.agu.essi.util.exception.SourceNotReadyException;

/**
 * Web Crawler for AGU abstract data
 * @author Eric Rozell and Tom Narock
 */
public class Crawler implements DataSource 
{	
	static final Logger log = Logger.getLogger(org.agu.essi.data.Crawler.class);  

	// AGU Variables - location of, and access to, AGU Abstract Database
	private String aguBaseURL = "http://www.agu.org/";
	private String aguApplicationURL = "cgi-bin/SFgate/SFgate?application=";
	private String aguURLOptions = "listenv=table&multiple=1&range=0&fieldsel_1_name=&fieldsel_2_tie=and&" +
	  "fieldsel_2_name=sc&fieldsel_2_content=Informatics&maxhits=10000&desc=+&requestm=POST";
	private HashMap <String, String> aguDatabases;
	
	// directory to write output
	private String dataDir;
	
	// HTML parser
	private ParserDelegator parserDelegator = new ParserDelegator();
	
	// container for crawled abstracts
	private Vector<Abstract> _abstracts;

	// true if crawling has occurred
	boolean crawled;
	
	// holds the EntityMatcher for the data source
	private EntityMatcher matcher;
	
	// class constructor creates a HashMap of AGU meetings/data directory key/value pairs
	// AGU nomenclature - FM = Fall Meeting, JA = Joint Assembly, SM = Spring Meeting (changed to Joint Assembly in 2008)
	public Crawler ( String dir ) 
	{
		dataDir = dir;
		crawled = false;
		_abstracts = new Vector<Abstract>();
		aguDatabases = Utils.getAguDatabases();
		crawl();
	}
	
	public Crawler ()
	{
		_abstracts = new Vector<Abstract>();
		aguDatabases = Utils.getAguDatabases();
		crawled = false;
		crawl();
	}
	
	public void setEntityMatcher(EntityMatcher m)
	{
		matcher = m;
	}
	
	public EntityMatcher getEntityMatcher()
	{
		return matcher;
	}
	
	private void writeToRDFXML( ) throws EntityMatcherRequiredException
	{
		if (matcher == null) matcher = new MemoryMatcher();
		FileWrite fw = new FileWrite();
		for (int i = 0; i < _abstracts.size(); ++i)
		{
			Abstract abstr = _abstracts.get(i);
			abstr.setEntityMatcher(matcher);
			// replace spaces with _ for file name
			String title = abstr.getId();
			String meeting = abstr.getMeeting().getName();
			title = title.replaceAll("\\s+", "_");
			title = title.replaceAll("\\s", "_");
			meeting = meeting.replaceAll("\\s", "_");
			String file = dataDir + meeting + "_" + title + ".rdf";
			fw.newFile(file, abstr.toString("rdf/xml"));
		}
		fw.newFile(dataDir + "people.rdf", matcher.writeNewPeople("rdf/xml"));
		fw.newFile(dataDir + "organizations.rdf", matcher.writeNewOrganizations("rdf/xml"));
		fw.newFile(dataDir + "sessions.rdf", matcher.writeNewSessions("rdf/xml"));
		fw.newFile(dataDir + "sections.rdf", matcher.writeNewSections("rdf/xml"));
		fw.newFile(dataDir + "meetings.rdf", matcher.writeNewMeetings("rdf/xml"));
		fw.newFile(dataDir + "keywords.rdf", matcher.writeNewKeywords("rdf/xml"));
	}
	
	private void writeToXML ( ) throws EntityMatcherRequiredException 
	{	
		for (int i = 0; i < _abstracts.size(); ++i)
		{
			Abstract abstr = _abstracts.get(i);
			// replace spaces with _ for file name
			String title = abstr.getId();
			if (title.contains("Integrating"))
				System.out.println("here");
			String meeting = abstr.getMeeting().getName();
			title = title.replaceAll("\\s+", "_");
			title = title.replaceAll("\\s", "_");
			meeting = meeting.replaceAll("\\s", "_");
			String file = dataDir + meeting + "_" + title + ".xml";
			FileWrite fw = new FileWrite();
			fw.newFile(file, abstr.toString("xml"));
			
		}
	}

	// HTML parser - reads an AGU HTML page and extracts links to abstracts
    private ParserCallback parserCallback = new ParserCallback() 
    {
        public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) 
        {
        	if (tag == Tag.A) 
        	{
        		String address = (String) attribute.getAttribute(Attribute.HREF);
        		String line;
        		try 
        		{
        			URL u = new URL( aguBaseURL + address ); 
        			HttpURLConnection http = (HttpURLConnection) u.openConnection();
        			StringBuilder builder = new StringBuilder();
        			BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(),"UTF-8"));
        			while((line = reader.readLine()) != null) 
        			{ 
        				builder.append(line + " "); 
        			}
        			_abstracts.add(new Abstract(builder.toString()));
        		} 
        		catch ( Exception e ) 
        		{ 
        			System.err.println("File at " + address + " does not contain AGU abstract HTML.");
        		}
        	}
        }

        public void handleEndTag(Tag t, final int pos) {}

        public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) {}
        
        public void handleText(final char[] data, final int pos) {}
        
        public void handleComment(final char[] data, final int pos) {}

        public void handleError(final java.lang.String errMsg, final int pos) {}
    };
    
    // call the AGU interface and extract abstracts from all available meetings
	private void crawl ( ) 
	{    
		String url = null;
		Set <Map.Entry<String, String>> databases = aguDatabases.entrySet();
		for (Map.Entry<String, String> me : databases) 
		{
			try 
			{
				url = aguBaseURL + aguApplicationURL + me.getKey() + "&database=" + me.getValue() + "&" + aguURLOptions;
				URL u = new URL( url ); 
				ReadableByteChannel rbc = Channels.newChannel( u.openStream() );
				String localFilename = dataDir + me.getKey() + ".html";
				FileOutputStream fos = new FileOutputStream( localFilename );
				fos.getChannel().transferFrom(rbc, 0, 1 << 24);	      
				parserDelegator.parse(new FileReader( localFilename ), parserCallback, false);
				
				//delete HTML file when finished
				File file = new File(localFilename);
				file.delete();
			} 
			catch (Exception e) 
			{ 
				e.printStackTrace(); 
			} 
		}
		crawled = true;
	}
	
	public Vector<Abstract> getAbstracts() throws SourceNotReadyException 
	{
		if (!crawled) 
		{ 
			throw new SourceNotReadyException();
		}
		else
		{
			return _abstracts;
		}
	}

	public boolean ready() 
	{
		return crawled;
	}

	public static void main (String[] args)  
	{	
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("outputDirectory", true, "Directory in which to store the retrieved abstracts.");
	  	options.addOption("outputFormat",true,"Serialization format for the resulting data");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try 
	  	{
	  		cmd = parser.parse( options, args);
	  	} 
	  	catch ( Exception pe ) { 
	  		log.error("Error parsing command line options: " + pe.toString()); 
	  		pe.printStackTrace();
	  	}
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String format = null;
	  	
	  	// output directory
	    if ( !cmd.hasOption("outputDirectory") ) 
	    {
	    	error = true;
	  		log.error("--outputDirectory Not Set. Directory in which to store the retrieved abstracts.");
	  	}
	    
	    // output format
	  	if ( cmd.hasOption("outputFormat")) 
	  	{ 
	  		format = cmd.getOptionValue("outputFormat"); 
	  	} 
	    
	    if (!error)
	    {	
	    	// query AGU
	    	Crawler crawler = new Crawler ( cmd.getOptionValue("outputDirectory"));
    		try {
    			if (format != null && format.equals("rdf/xml")) 
    			{ 
	    			crawler.writeToRDFXML();
    			} 
    			else 
    			{ 
    				crawler.writeToXML(); 
    			}
    		}
    		catch (EntityMatcherRequiredException e) 
    		{
    			log.error("EntityMatcher not set. Required for RDF output formats.");
    			e.printStackTrace();
    		} 
	    }
	}
}