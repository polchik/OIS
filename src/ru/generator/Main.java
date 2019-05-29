package ru.generator;

import ru.common.AccessPoint;
import ru.common.Block;
import ru.common.Domain;
import ru.common.StandardProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(args));

        int domainsCount = 0;
        int propertiesCount = 0;
        int accessPointsCount = 0;
        int blockApsHigh = 0;
        int blockApsLow = 0;

        while (parameters.size() > 1)
        {
            String key = parameters.remove(0);
            Integer value =  Integer.valueOf(parameters.remove(0));

            switch (key)
            {
                case "-d":
                    domainsCount = value;
                    break;
                case "-p":
                    propertiesCount = value;
                    break;
                case "-ap":
                    accessPointsCount = value;
                    break;
                case "-apl":
                    blockApsLow = value;
                    break;
                case "-aph":
                    blockApsHigh = value;
                    break;
                default:
                    System.out.println("No such option: " + key);
                    System.exit(1);
            }
        }


        if ((parameters.size() != 0) || (domainsCount == 0) || (propertiesCount == 0) || (accessPointsCount == 0) || (blockApsHigh == 0) || (blockApsLow == 0))
        {
            System.out.println("Parameter error");
            System.exit(1);
        }

        TestData testData = Parser.generate_test_data(domainsCount, propertiesCount, accessPointsCount, blockApsLow, blockApsHigh);

        try
        {
            Parser.write_test_data(testData, "dataset.json");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}