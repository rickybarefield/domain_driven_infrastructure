package com.appagility.domaindriveninfrastructure.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GoldenAmi {

    private String id;

    public static GoldenAmi BASE = new GoldenAmi("ami-0122335235123525");
    public static GoldenAmi MONGO = new GoldenAmi("ami-689394723490823");
}
