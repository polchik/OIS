package ru.common;

import java.util.ArrayList;

public class Block
{
    private ArrayList<AccessPoint> accessPoints = null;
    private String name = null;

    public Block(String name)
    {
        accessPoints = new ArrayList<>();
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<AccessPoint> getAccessPoints()
    {
        return this.accessPoints;
    }

    public void addAccessPoint(AccessPoint ac)
    {
        this.accessPoints.add(ac);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < accessPoints.size(); ++i)
        {
            sb.append("AccessPoint-");
            sb.append(accessPoints.get(i).getName());

            if (i != (accessPoints.size() - 1))
                sb.append(" ");
        }

        return String.format("Block-%s (%s)", name, sb.toString());
    }
}
