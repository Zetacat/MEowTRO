package meowtro;

import meowtro.metro_system.*;

public interface EntityManager {
    public void build(Station head, Station tail, Line line); 
    public void destroy(Railway railway); 
}
