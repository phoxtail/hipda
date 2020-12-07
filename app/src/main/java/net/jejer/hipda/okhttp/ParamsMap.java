package net.jejer.hipda.okhttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by GreenSkinMonster on 2016-12-04.
 */

public class ParamsMap {

    private final Map<String, List<String>> params = new HashMap<>();

    public void put(String key, String value) {
        if (!params.containsKey(key))
            params.put(key, new ArrayList<>());
        params.get(key).add(value);
    }


    public Set<Map.Entry<String, List<String>>> entrySet() {
        return params.entrySet();
    }

}
