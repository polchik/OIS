package ru.common;

import javafx.util.Pair;

import java.util.ArrayList;

public class Shared
{
    public static boolean isApsCanBeConnected(AccessPoint ap1, AccessPoint ap2)
    {
        if (ap1.isInput() == ap2.isInput())
        {
            return false;
        }

        ArrayList<StandardProperty> stdProps1 = new ArrayList<>(ap1.getStandardProperties());
        ArrayList<StandardProperty> stdProps2 = new ArrayList<>(ap2.getStandardProperties());

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

    public static boolean isBlocksCanBeConnected(Block block1, Block block2)
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


    public static ArrayList<Pair<Block, Block>> getAvailableChains(Block block, ArrayList<Block> blocks)
    {
        ArrayList<Pair<Block, Block>> availableChains = new ArrayList<>();

        for (Block value : blocks)
        {
            boolean isCanBeConnected = isBlocksCanBeConnected(block, value);
            if (isCanBeConnected)
                availableChains.add(new Pair<Block, Block>(block, value));
        }

        return availableChains;
    }

    public static ArrayList<ArrayList<Block>> buildConnectedBlocksV3(ArrayList<Pair<Block, Block>> availableChains, Block startBlock)
    {
        ArrayList<ArrayList<Block>> result = new ArrayList<>();
        ArrayList<Block> arb = new ArrayList<>();
        arb.add(startBlock);
        result.add(arb);

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
                            continue;

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
}
