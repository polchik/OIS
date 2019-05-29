package ru.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.common.AccessPoint;
import ru.common.Block;
import ru.common.Domain;
import ru.common.StandardProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Parser
{
    public static void write_test_data(TestData testData, String filename) throws IOException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String gg = gson.toJson(testData);
        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(gg);
        fileWriter.close();
    }

    public static TestData read_test_data(String filename) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        Gson gson = new Gson();
        return gson.fromJson(bufferedReader, TestData.class);
    }

    public static TestData generate_test_data(int domainsCount, int propertiesCount, int accessPointsCount, int blockApsLow, int blockApsHigh)
    {
        TestData testData = new TestData();

        Random rnd = new Random();

        for (int i = 0; i < domainsCount; ++i)
            testData.domains.add(new Domain(String.valueOf(i)));


        for (int i = 0; i < propertiesCount; ++i)
        {
            int selectedDomain = rnd.nextInt(1000) %  testData.domains.size();
            StandardProperty stdProp = new StandardProperty( testData.domains.get(selectedDomain), String.valueOf(i));
            testData.standardProperties.add(stdProp);
        }

        for (int i = 0; i < accessPointsCount; ++i)
        {
            AccessPoint ap = new AccessPoint(rnd.nextBoolean(), String.valueOf(i));

            int apPropCount = rnd.nextInt(1) + 2;

            HashSet<Integer> hs = new HashSet<>();
            while(hs.size() != apPropCount) hs.add(rnd.nextInt(1000) % testData.standardProperties.size());

            for (int j : hs)
                ap.addStandardsProperty(testData.standardProperties.get(j));

            testData.accessPoints.add(ap);
        }

        ArrayList<AccessPoint> pointsCopy = new ArrayList<>(testData.accessPoints);
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

            testData.blocks.add(block);
        }

        return testData;
    }

}
