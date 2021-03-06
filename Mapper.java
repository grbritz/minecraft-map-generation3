import net.morbz.minecraft.blocks.*;
import net.morbz.minecraft.blocks.states.Facing4State;
import net.morbz.minecraft.level.FlatGenerator;
import net.morbz.minecraft.level.GameType;
import net.morbz.minecraft.level.IGenerator;
import net.morbz.minecraft.level.Level;
import net.morbz.minecraft.world.DefaultLayers;
import net.morbz.minecraft.world.World;

import java.awt.geom.Point2D;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jordansoltman on 12/22/15.
 */
public class Mapper {

    public static void main(String[] args) {

        Properties propList = new Properties(
                new Point2D.Double(-122.468627, 47.394935),
                new Point2D.Double(-122.446226, 47.382558),
                1
        );

//        ArrayList<String> apiKeys = new ArrayList<>();
//        apiKeys.add("AIzaSyD2qrOqFQ2bJ-jTlr6ZVxwOrgPjHhdOZOY");
//        ElevationFetcher fetcher = new ElevationFetcher(propList, apiKeys);
//
//
//        List<List<Double>> terrainData = fetcher.fetchElevations();

//        WriterReader.write2DArray(terrainData, "test1");


        List<List<Double>> terrainData = WriterReader.read2DArray("test1");




        // Create the base layers of the generated world.
// We set the bottom layer of the world to be bedrock and the 20 layers above to be melon
// blocks.
        DefaultLayers layers = new DefaultLayers();
        layers.setLayer(0, Material.BEDROCK);
       layers.setLayers(1, 2, Material.WATER);

// Create the internal Minecraft world generator.
// We use a flat generator. We do this to make sure that the whole world will be paved
// with melons and not just the part we generated.
        IGenerator generator = new FlatGenerator(layers);

// Create the level configuration.
// We set the mode to creative creative mode and name our world. We also set the spawn point
// in the middle of our glass structure.
        Level level = new Level("MelonWorld", generator);
        level.setGameType(GameType.CREATIVE);
        level.setSpawnPoint(50, 0, 50);

// Now we create the world. This is where we can set our own blocks.
        World world = new World(level, layers);

// Create a huge structure of glass that has an area of 100x100 blocks and is 50 blocks
// height. On top of the glass structure we put a layer of grass.
        for(int y = 0; y < terrainData.size(); y++) {
            List<Double> elevationList = terrainData.get(y);
            for(int x = 0; x < terrainData.get(0).size(); x++) {

                int height = elevationList.get(x).intValue() + 2;

                if(height < 4) {
                    for(int h = height - 1; h < 2; h++) {
                        world.setBlock(x, h, y, SimpleBlock.WATER);
                    }
                }
                world.setBlock(x, height, y, SimpleBlock.GRASS);




//                // Set glass
//                for(int y = 0; y < 50; y++) {
//                    world.setBlock(x, y, z, SimpleBlock.GLASS);
//                }
//
//                // Set grass
//                world.setBlock(x, 50, z, SimpleBlock.GRASS);
//                world.setBlock(x, 51, z, SaplingBlock.OAK_SAPLING);
            }
        }

// Now we create the door. It consists of 2 blocks, that's why we can't use a SimpleBlock
// here.
//        world.setBlock(50, 51, 50, DoorBlock.makeLower(DoorBlock.DoorMaterial.OAK, Facing4State.EAST, false));
//        world.setBlock(50, 52, 50, DoorBlock.makeUpper(DoorBlock.DoorMaterial.OAK, DoorBlock.HingeSide.LEFT));

// Everything's set up so we're going to save the world.
        try{
            world.save();
        } catch (Exception e)
        {

        }

    }
}
