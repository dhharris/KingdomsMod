package net.kingdomsmod.common;

import net.kingdomsmod.common.command.KingdomsCommand;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.stream.Collectors;

@Mod(KingdomsMod.MOD_ID)
public class KingdomsMod
{
    // The value here should match an entry in the META-INF/mods.toml file
    public static final String MOD_ID = "kingdoms";

    // Useful for getting players by UUID: server.getPlayerList().getPlayerByUUID();
    public static MinecraftServer server;
    public static ServerWorld world;
    private static KingdomsModWorldSavedData data = new KingdomsModWorldSavedData();


    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public KingdomsMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void addKingdom(Kingdom kingdom) {
        data.addKingdom(kingdom);
    }

    public static Kingdom[] getKingdoms() {
        return data.getKingdoms();
    }

    public static void collectTaxes(Kingdom kingdom, UUID playerUUID) {
        data.collectTaxes(kingdom, playerUUID);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("kingdoms", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // Add kingdoms command
        new KingdomsCommand(event.getCommandDispatcher());

        // Populate static server-related variables
        server = event.getServer();
        world = server.getWorld(DimensionType.OVERWORLD);
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        data.markDirty(); // Not sure if this is needed. More for safety reasons
    }
//    @SubscribeEvent
//    public void registerBlocks(RegistryEvent.Register<Block> event) {
//        event.getRegistry().register(new BlockHandler(event.getRegistry().getValue()));
//    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
//    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
//            // register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }
}
