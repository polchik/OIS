package ru.lab;


import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

class Domain
{
    private String name;

    Domain(String domainName)
    {
        this.name = domainName;
    }

    String getDomainName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return String.format("Domain-%s", name);
    }
}

class StandardProperty
{
    private Domain assignedDomain = null;
    private String name = null;
    StandardProperty(Domain domain, String name)
    {
        this.assignedDomain = domain;
        this.name = name;
    }

    Domain getAssignedDomain()
    {
        return this.assignedDomain;
    }

    String getStandartPropertyName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return String.format("StandardProperty-%s (%s)", name, assignedDomain.toString());
    }
}

class AccessPoint
{
    private boolean isInput = false;
    private ArrayList<StandardProperty> properties = null;
    private String name = null;

    AccessPoint(boolean isInput, String name)
    {
        this.name = name;
        this.isInput = isInput;
        this.properties = new ArrayList<>();
    }

    boolean isInput()
    {
        return this.isInput;
    }

    void addStandardsProperty(StandardProperty property)
    {
        this.properties.add(property);
    }

    ArrayList<StandardProperty> getStandartProperties()
    {
        return this.properties;
    }

    String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        String propertyString = Main.buildStringFromArray(properties, ", ");
        return String.format("AccessPoint(%s)-%s (%s)", isInput ? "INPUT" : "OUTPUT", name, propertyString);
    }
}

class Block
{
    private ArrayList<AccessPoint> accessPoints = null;
    private String name = null;

    Block(String name)
    {
        accessPoints = new ArrayList<>();
        this.name = name;
    }

    ArrayList<AccessPoint> getAccessPoints()
    {
        return this.accessPoints;
    }

    void addAccessPoint(AccessPoint ac)
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



public class Main
{

    static String buildStringFromArray(ArrayList<?> arr, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < arr.size(); ++i)
        {
            sb.append(arr.get(i).toString());

            if (i != (arr.size() - 1))
                sb.append(separator);
        }

        return sb.toString();
    }

    static  boolean isApsCanBeConnected(AccessPoint ap1, AccessPoint ap2)
    {
        if (ap1.isInput() == ap2.isInput())
        {
            return false;
        }

        ArrayList<StandardProperty> stdProps1 = new ArrayList<>(ap1.getStandartProperties());
        ArrayList<StandardProperty> stdProps2 = new ArrayList<>(ap2.getStandartProperties());

        for(int i = stdProps1.size() - 1; i >= 0; --i)
        {
            for(int j = stdProps2.size() - 1; j >= 0; --j)
            {
                StandardProperty stdProp1 = stdProps1.get(i);
                StandardProperty stdProp2 = stdProps2.get(j);

                if (stdProp1.getAssignedDomain() == stdProp2.getAssignedDomain())
                {
                    stdProps1.remove(i);
                    stdProps2.remove(j);
                    break;
                }

            }
        }

        return ((stdProps1.size() == 0) && (stdProps2.size() == 0));

    }

    static boolean isBlocksCanBeConnected(Block block1, Block block2)
    {
        boolean result = false;

        ArrayList<AccessPoint> aps1 = new ArrayList<>(block1.getAccessPoints());
        ArrayList<AccessPoint> aps2 = new ArrayList<>(block2.getAccessPoints());

        for(int i = aps1.size() - 1; i >= 0; --i)
        {
            if(aps1.get(i).isInput())
                continue;

            for(int j = aps2.size() - 1; j >= 0; --j)
            {
                AccessPoint ap1 = aps1.get(i);
                AccessPoint ap2 = aps2.get(j);

                boolean res = isApsCanBeConnected(ap1, ap2);
                if (res)
                    return true;
            }
        }

        return false;
    }


    static ArrayList<ArrayList<Block>> buildChain(ArrayList<Block> blocks, ArrayList<Block> availbleBlocks)
    {
        ArrayList<ArrayList<Block>> results = new ArrayList<>();

        int currentBlock = blocks.size() - 1;
        Block bl = blocks.get(currentBlock);

        boolean flag = true;

        for(int i = 0; i < availbleBlocks.size(); ++i)
        {
            if (isBlocksCanBeConnected(bl, availbleBlocks.get(i)))
            {
                //System.out.println(String.format("Blocks can be connected: %s =>> %s", bl.toString(), availbleBlocks.get(i).toString()));
                if (blocks.indexOf(availbleBlocks.get(i)) != -1)
                    continue;

                ArrayList<Block> blocksCopy = new ArrayList<>(blocks);
                ArrayList<Block> avalableBlocksNew = new ArrayList<>(availbleBlocks);
                blocksCopy.add(avalableBlocksNew.remove(i));

                ArrayList<ArrayList<Block>> res = buildChain(blocksCopy, avalableBlocksNew);
                results.addAll(res);

                flag = res.size() == 0 && flag;
            }
        }

        if (flag)
            results.add(blocks);
        return results;

    }

    static ArrayList<ArrayList<Block>> buildConnectedBlocks(ArrayList<Block> blocks)
    {
        ArrayList<ArrayList<Block>> result = new ArrayList<>();

        for(int i = 0; i < blocks.size(); ++i)
        {
            System.out.println(String.format("Calculating: %f%%", (double)i/blocks.size()*100));

            ArrayList<Block> copyBlock = new ArrayList<>(blocks);
            Block currentBlock = copyBlock.remove(i);
            ArrayList<Block> bbb = new ArrayList<>();
            bbb.add(currentBlock);

            ArrayList<ArrayList<Block>> res = buildChain(bbb, copyBlock);
            result.addAll(res);
        }
        return result;
    }


    static ArrayList<ArrayList<Block>> buildConnectedBlocksv2(ArrayList<Block> blocks)
    {
        ArrayList<ArrayList<Block>> result = new ArrayList<>();

        ArrayList<Pair<Block, Block>> availableChains = new ArrayList<>();

        for(int i = 0; i < blocks.size(); ++i)
        {
            for(int j = 0; j < blocks.size(); ++j)
            {
                if (i == j)
                    continue;

                boolean isCanBeConnected = isBlocksCanBeConnected(blocks.get(i), blocks.get(j));
                if (isCanBeConnected)
                    availableChains.add(new Pair<Block,Block>(blocks.get(i), blocks.get(j)));

            }
        }

        System.out.println("\n\nAvailable block chains: ");
        for(Pair<Block, Block> blc : availableChains)
            System.out.println(String.format("Chain %s => %s", blc.getKey(), blc.getValue()));

        // avaialbe chains

        for(Block b : blocks)
        {
            ArrayList<Block> arb = new ArrayList<>();
            arb.add(b);
            result.add(arb);
        }

        System.out.println("Running algo...");
        boolean flag = true;
        while (flag)
        {
            flag = false;

            ArrayList<ArrayList<Block>> resultsCopy = result;
            result = new ArrayList<>();

            for(ArrayList<Block> blc : resultsCopy)
            {
                Block lastBlock = blc.get(blc.size() - 1);
                boolean is_inserted_one = false;
                for(Pair<Block, Block> pr : availableChains)
                {
                    if (lastBlock == pr.getKey())
                    {
                        // Skip infinite list
                        if (blc.indexOf(pr.getValue()) != -1)
                        {
                            System.out.println("Skip");
                            continue;
                        }

                        ArrayList<Block> blcCopy = new ArrayList<>(blc);
                        blcCopy.add(pr.getValue());
                        result.add(blcCopy);
                        flag = true;
                        is_inserted_one = true;
                    }
                }

                if ((blc.size() != 1) && !is_inserted_one)
                    result.add(blc);
            }
        }

        return result;

    }



    public static void main(String[] args)
    {
        int domainsCount = 5;
        int propertiesCount = 10;
        int accessPointsCount = 15;
        int blockApsHigh = 4;
        int blockApsLow = 1;

        Random rnd = new Random();

        System.out.println("Available Domains: ");
        ArrayList<Domain> domains = new ArrayList<>();
        for (int i = 0; i < domainsCount; ++i)
        {
            Domain d = new Domain(String.valueOf(i));
            domains.add(d);

            System.out.println(d.toString());
        }


        System.out.println("\nAvailable properties:");
        ArrayList<StandardProperty> properties = new ArrayList<>();
        for (int i = 0; i < propertiesCount; ++i)
        {
            int selectedDomain = rnd.nextInt(1000) % domains.size();
            StandardProperty stdProp = new StandardProperty(domains.get(selectedDomain), String.valueOf(i));
            properties.add(stdProp);

            System.out.println(stdProp.toString());
        }

        System.out.println("\nAvailable access points:");
        ArrayList<AccessPoint> aps = new ArrayList<>();
        for (int i = 0; i < accessPointsCount; ++i)
        {
            AccessPoint ap = new AccessPoint(rnd.nextBoolean(), String.valueOf(i));

            int apPropCount = rnd.nextInt(1) + 2;

            HashSet<Integer> hs = new HashSet<>();
            while(hs.size() != apPropCount) hs.add(rnd.nextInt(1000) % properties.size());

            for (int j : hs)
                ap.addStandardsProperty(properties.get(j));

            aps.add(ap);

            System.out.println(ap.toString());
        }

        System.out.println("\nAvailable blocks:");
        ArrayList<Block> blocks = new ArrayList<>();
        ArrayList<AccessPoint> pointsCopy = new ArrayList<>(aps);
        int blockNumber = 0;
        while(pointsCopy.size() != 0)
        {
            int poinstCount = rnd.nextInt(blockApsHigh - blockApsLow) + blockApsLow;
            Block block = new Block(String.valueOf(blockNumber++));
            for (int i = 0; i < poinstCount; ++i)
            {
                if (pointsCopy.size() == 0)
                    break;

                int randAp = (rnd.nextInt(pointsCopy.size() / 2 + 1) + pointsCopy.size() / 2 + 1) % pointsCopy.size();
                block.addAccessPoint(pointsCopy.remove(randAp));
            }

            blocks.add(block);
            System.out.println(block.toString());
        }

        ArrayList<ArrayList<Block>> results = buildConnectedBlocksv2(blocks);

        System.out.println("\n\nGenerated blocks chains: ");
        for (ArrayList<Block> result : results)
        {
            if (result.size() == 1)
                continue;

            String resultString = buildStringFromArray(result, " ==>> ");
            System.out.println(resultString);
        }

    }
}
