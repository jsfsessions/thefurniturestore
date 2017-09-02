package com.util.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Status {

    /* Usage in java code:

    String newStatus = Status.ACTIVE;
    or
    walmartProduct.setStatus(Status.DAMAGED);
     */
    public final static String EMPTY = null;
    public final static String SCHEDULED = "SCHEDULED";
    public final static String ACTIVE = "ACTIVE";
    public final static String SOLD = "SOLD";
    public final static String AGING = "AGING";
    public final static String ASSEMBLE = "ASSEMBLE";
    public final static String PARTIAL = "PARTIAL";
    public final static String DAMAGED = "DAMAGED";
    public final static String SHRINKAGE = "SHRINKAGE";

    private final Map<String, Object> values;   // Status values used in JSF drop-downs

    public Status() {
        values = new LinkedHashMap<>();
        values.put("Make a selection", "");
        values.put("Scheduled", SCHEDULED);     // label and value for <f:selectItems />
        values.put("Active", ACTIVE);
        values.put("Sold", SOLD);
        values.put("Aging", AGING);
        values.put("Assemble", ASSEMBLE);
        values.put("Partial", PARTIAL);
        values.put("Damaged", DAMAGED);
        values.put("Shrinkage", SHRINKAGE);
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
