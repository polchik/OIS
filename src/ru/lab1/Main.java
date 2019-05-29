package ru.lab1;


import javafx.util.Pair;
import ru.common.Block;
import ru.common.Shared;
import ru.generator.Parser;
import ru.generator.TestData;

import java.io.IOException;
import java.util.ArrayList;


public class Main
{

    public static void main(String[] args)
    {
        String filename = "dataset.json";
        Integer count = 100;

        TestData testData = null;
        try
        {
            testData = Parser.read_test_data(filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        long startTime = System.nanoTime();
        for (Block b : testData.blocks)
            Shared.getAvailableChains(b, testData.blocks);
        long stopTime = System.nanoTime();

        System.out.println(stopTime - startTime);

    }
}
