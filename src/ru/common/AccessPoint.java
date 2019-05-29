package ru.common;

import java.util.ArrayList;

public class AccessPoint
{
    private boolean isInput = false;
    private ArrayList<StandardProperty> properties = null;
    private String name = null;

    public AccessPoint(boolean isInput, String name)
    {
        this.name = name;
        this.isInput = isInput;
        this.properties = new ArrayList<>();
    }

    public boolean isInput()
    {
        return this.isInput;
    }

    public void addStandardsProperty(StandardProperty property)
    {
        this.properties.add(property);
    }

    public ArrayList<StandardProperty> getStandardProperties()
    {
        return this.properties;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return String.format("AccessPoint(%s)-%s (%s)", isInput ? "INPUT" : "OUTPUT", name);
    }
}
