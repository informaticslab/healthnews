package gov.cdc.irdu.healthnews.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Sep 13, 2011
 *
 */
public class SessionID implements Serializable
{
    private String type;
    private String realm;
    private String unique;
    private Date created;
    
    public SessionID() { }
    
    public SessionID(String type, String realm, String unique) {
        this.type = type;
        this.realm = realm;
        this.unique = unique;
        this.created = new Date();
    }
    
    public SessionID(String realm, String unique) {
        this("ANON", realm, unique);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof SessionID))
            return false;
        
        SessionID sid = (SessionID) other;
        return type.equals(sid.type) && 
               realm.equals(sid.realm) && 
               unique.equals(sid.unique) && 
               created.equals(sid.created);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = type.hashCode() + realm.hashCode() + unique.hashCode() + created.hashCode();
        return hash;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * @return the realm
     */
    public String getRealm()
    {
        return this.realm;
    }

    /**
     * @return the unique
     */
    public String getUnique()
    {
        return this.unique;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @param realm the realm to set
     */
    public void setRealm(String realm)
    {
        this.realm = realm;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(String unique)
    {
        this.unique = unique;
    }

    /**
     * @return the created
     */
    public Date getCreated()
    {
        return this.created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created)
    {
        this.created = created;
    }
    
}
