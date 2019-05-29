package ru.lab2;
import javafx.util.Pair;
import mpi.MPI;
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
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        String filename = "dataset.json";
        Integer count = 10;

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
        int i = me;
        int hc = 0;
        while(i < testData.blocks.size())
        {
            hc++;
            Shared.getAvailableChains(testData.blocks.get(i), testData.blocks);
            i += size;
        }
        long stopTime = System.nanoTime();

        StringBuilder sb = new StringBuilder();
        sb.append("Thread: ");
        sb.append(me);
        sb.append(" Handled blocks: ");
        sb.append(hc);
        sb.append("/");
        sb.append(testData.blocks.size());
        sb.append(" Took: ");
        sb.append(stopTime - startTime);
        System.out.println(sb.toString());
        MPI.Finalize();

    }
}