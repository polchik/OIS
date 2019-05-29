package ru.common;

public class StandardProperty
{
    private Domain assignedDomain = null;
    private String name = null;
    public StandardProperty(Domain domain, String name)
    {
        this.assignedDomain = domain;
        this.name = name;
    }

    public Domain getAssignedDomain()
    {
        return this.assignedDomain;
    }

    public String getStandardPropertyName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return String.format("StandardProperty-%s (%s)", name, assignedDomain.toString());
    }
}
