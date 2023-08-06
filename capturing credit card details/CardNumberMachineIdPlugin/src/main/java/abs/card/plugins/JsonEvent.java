package abs.card.plugins;

import java.util.ArrayList;

public class JsonEvent {

	  private String type;
	  private String eventId;
	  private String eventName;
	  EventPayload EventPayloadObject;


	 // Getter Methods 

	  public String getType() {
	    return type;
	  }

	  public String getEventId() {
	    return eventId;
	  }

	  public String getEventName() {
	    return eventName;
	  }

	  public EventPayload getEventPayload() {
	    return EventPayloadObject;
	  }

	 // Setter Methods 

	  public void setType( String type ) {
	    this.type = type;
	  }

	  public void setEventId( String eventId ) {
	    this.eventId = eventId;
	  }

	  public void setEventName( String eventName ) {
	    this.eventName = eventName;
	  }

	  public void setEventPayload( EventPayload eventPayloadObject ) {
	    this.EventPayloadObject = eventPayloadObject;
	  }
	}
	 class EventPayload {
	  ArrayList<Object> elements = new ArrayList<Object>();
	  private String width;


	 // Getter Methods 

	  public String getWidth() {
	    return width;
	  }

	 // Setter Methods 

	  public void setWidth( String width ) {
	    this.width = width;
	  }
	}