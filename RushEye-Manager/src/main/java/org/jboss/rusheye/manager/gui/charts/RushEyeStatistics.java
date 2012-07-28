/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.rusheye.manager.gui.charts;

import java.util.*;
import org.jboss.rusheye.suite.ResultConclusion;

/**
 *
 * @author hcube
 */
public class RushEyeStatistics {

    private Map<ResultConclusion, Integer> map;

    public RushEyeStatistics() {
        map = new EnumMap<ResultConclusion, Integer>(ResultConclusion.class);
        for (ResultConclusion rc : ResultConclusion.values()) {
            map.put(rc, new Integer(0));
        }
    }

    public synchronized void setValue(ResultConclusion key, Integer value) {
        map.put(key, value);
    }

    public synchronized void addValue(ResultConclusion key, Integer incr) {
        Integer val = map.get(key);
        map.put(key, val + incr);
    }

    public synchronized Integer getValue(ResultConclusion rc) {
        return map.get(rc);
    }

    public synchronized List<Integer> getValues() {
        ArrayList<Integer> result = new ArrayList<Integer>();

        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            result.add((Integer) it.next());
        }

        return result;
    }

    public synchronized int calculateSum() {
        int result = 0;
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            result += (Integer) it.next();
        }

        return result;
    }

    public String toString() {
        String result = "";
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            result += "\t" + ((Integer) it.next());
        }
        return result;
    }
}
