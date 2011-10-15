package org.agu.essi;

/**
 * Container class for AGU Meeting information
 * @author Eric Rozell
 */
public class Meeting {
	private String _name;
	private String _id;
	
	/**
	 * Construct Meeting instance from name
	 * @param name name of meeting
	 */
	public Meeting(String name)
	{
		_name = name;
	}
	
	/**
	 * Construct Meeting instance from name
	 * @param name name of meeting
	 * @param id ID for meeting
	 */
	public Meeting(String name, String id)
	{
		_name = name;
		_id = id;
	}
	
	/**
	 * Method to get meeting name
	 * @return name of meeting
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method to get meeting ID
	 * @return ID of meeting
	 */	
	public String getId()
	{
		return _id != null ? _id.toUpperCase() : _id;
	}
	
	/**
	 * Equality method for Meeting class
	 * @param o an Object
	 * @return true if meeting has the same name, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Meeting && ((Meeting)o).getName().equals(this._name))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
