package ru.common;

public class Domain
{
    private String name;

    public Domain(String domainName)
    {
        this.name = domainName;
    }

    public String getDomainName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return String.format("Domain-%s", name);
    }
}
