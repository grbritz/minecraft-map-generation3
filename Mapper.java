import net.morbz.minecraft.blocks.DoorBlock;
import net.morbz.minecraft.blocks.Material;
import net.morbz.minecraft.blocks.SaplingBlock;
import net.morbz.minecraft.blocks.SimpleBlock;
import net.morbz.minecraft.blocks.states.Facing4State;
import net.morbz.minecraft.level.FlatGenerator;
import net.morbz.minecraft.level.GameType;
import net.morbz.minecraft.level.IGenerator;
import net.morbz.minecraft.level.Level;
import net.morbz.minecraft.world.DefaultLayers;
import net.morbz.minecraft.world.World;
import java.awt.Point;
import java.util.List;

/**
 * Created by jordansoltman on 12/22/15.
 */
public class Mapper {

    public static void main(String[] args) {

        TerrainFetcher fetcher = new TerrainFetcher();
        Point.Double topLeft = new Point.Double(-122.569313,47.521765);
        Point.Double botRight = new Point.Double(-122.352333,47.315945);

        System.out.println("test test");

        List<List<Double>> terrainData = fetchTerrain(topLeft, botRight);

        System.out.println(terrainData);


        // Create the base layers of the generated world.
// We set the bottom layer of the world to be bedrock and the 20 layers above to be melon
// blocks.
        DefaultLayers layers = new DefaultLayers();
        layers.setLayer(0, Material.BEDROCK);
        layers.setLayers(1, 20, Material.MELON_BLOCK);

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
        for(int x = 0; x < 100; x++) {
            for(int z = 0; z < 100; z++) {
                // Set glass
                for(int y = 0; y < 50; y++) {
                    world.setBlock(x, y, z, SimpleBlock.GLASS);
                }

                // Set grass
                world.setBlock(x, 50, z, SimpleBlock.GRASS);
                world.setBlock(x, 51, z, SaplingBlock.OAK_SAPLING);
            }
        }

// Now we create the door. It consists of 2 blocks, that's why we can't use a SimpleBlock
// here.
        world.setBlock(50, 51, 50, DoorBlock.makeLower(DoorBlock.DoorMaterial.OAK, Facing4State.EAST, false));
        world.setBlock(50, 52, 50, DoorBlock.makeUpper(DoorBlock.DoorMaterial.OAK, DoorBlock.HingeSide.LEFT));

// Everything's set up so we're going to save the world.
        try{
            world.save();
        } catch (Exception e)
        {

        }

    }
}
