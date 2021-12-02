package net.idk.golder06.lastlife.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HiddenBoogeyCommand {
    // public static final Identifier COMMAND_POWER_SOURCE = Apoli.identifier("command");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(literal("boogeychoose").requires(cs -> false)
                .then(argument("targets", EntityArgumentType.players())
                        .executes((command) -> {
                                    final ServerCommandSource source = command.getSource();
                                    final Collection<ServerPlayerEntity> allPlayers = EntityArgumentType.getPlayers(command, "targets");
                                    Random random = new Random();

                                    return 1;
                                }
                        )
                )
        );
    }

    private static void simpleSetBoogey(PlayerEntity player, String livesId) {
        String[] splitOrigin = livesId.split(":");
        Origin lives = OriginRegistry.get(new Identifier(splitOrigin[0], splitOrigin[1]));
        OriginLayer layer = OriginLayers.getLayer(new Identifier("lastlife", "lives"));
        OriginComponent component = ModComponents.ORIGIN.get(player);
        component.setOrigin(layer, lives);
    }
    /*
    // How tf do you even?-
    public List<Integer> getRandomElement(Collection<ServerPlayerEntity> list, int totalItems, Random rand) {
        Collection<ServerPlayerEntity> newList = ;
        for (int i = 0; i < totalItems; i++) {

            // take a random index between 0 to size
            // of given List
            int randomIndex = rand.nextInt(list.size());

            // add element in temporary list
            newList.add(list.get(randomIndex));

            // Remove selected element from original list
            list.remove(randomIndex);
        }
        return newList;
    }
     */
}
