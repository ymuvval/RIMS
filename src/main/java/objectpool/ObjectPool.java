package objectpool;

import java.util.*;

public abstract class ObjectPool<T> {
	
	long deadTime;
	 
    Hashtable<T, Long> inUse, notInUse;
 
    protected ObjectPool()
    {
        deadTime = 10000; // 10 seconds
        inUse = new Hashtable<T, Long>();
        notInUse = new Hashtable<T, Long>();
    }
 
    public abstract T create();
 
    public abstract boolean validate(T o);
 
    public abstract void dead(T o);
 
    synchronized T giveMeObject() {
    	long now = System.currentTimeMillis();
        T t;
        if (notInUse.size() > 0) {
            Enumeration<T> e = notInUse.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                if ((now - notInUse.get(t)) > deadTime) {
                    // object has dead
                    notInUse.remove(t);
                    dead(t);
                    t = null;
                }
                else {
                    if (validate(t)) {
                        notInUse.remove(t);
                        inUse.put(t, now);
                        return (t);
                    }
                    else {
                        // object failed validation
                        notInUse.remove(t);
                        dead(t);
                        t = null;
                    }
                }
            }
        }
        // no objects available, create a new one
        t = create();
        inUse.put(t, now);
        return (t);
    }
    synchronized void releaseObject(T t)
    {
        inUse.remove(t);
        notInUse.put(t, System.currentTimeMillis());
    }

}
