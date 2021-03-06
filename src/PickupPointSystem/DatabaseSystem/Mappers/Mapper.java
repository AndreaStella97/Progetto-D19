package PickupPointSystem.DatabaseSystem.Mappers;

import java.io.IOException;

/**
 * @author Andrea Stella
 * @version 1.0
 */

public interface Mapper {

    /**
     * This method returns some kind of object given a key.
     * @param oid
     * @return Object
     */

    public Object get(String oid) throws IOException;
}
