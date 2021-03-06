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
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TransferLifeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("givelife")
                .then(argument("target", EntityArgumentType.player())
                        .executes((command) -> {
                            final ServerCommandSource source = command.getSource();
                            final PlayerEntity sender = source.getPlayer();
                            final PlayerEntity target = EntityArgumentType.getPlayer(command, "target");
                            if (sender == target) {
                                command.getSource().sendError(new LiteralText("You can't give lives to yourself.."));
                                return 0;
                            } else if (simpleHasLife(target, "lastlife:lifes/6")) {
                                command.getSource().sendError(new TranslatableText("commands.lastlife.givelife.error.too_many_lives", target.getDisplayName()));
                                return 0;
                            } else if (simpleHasLife(sender, "lastlife:lifes/1")) {
                                command.getSource().sendError(new LiteralText("You don't have enough lives to transfer."));
                                return 0;
                            } else if (simpleHasLife(target, "lastlife:lifes/death")) {
                                command.getSource().sendError(new TranslatableText("commands.lastlife.givelife.error.dead_target", target.getDisplayName()));
                                return 0;
                            } else if (simpleHasLife(sender, "lastlife:lifes/death")) {
                                command.getSource().sendError(new TranslatableText("commands.lastlife.givelife.error.dead_sender", target.getDisplayName()));
                                return 0;
                            /*
                            } else if (simpleHasLife(sender, "lastlife:lifes/randomizer")) {
                                command.getSource().sendError(new LiteralText("The game hasn't started yet. Please wait."));
                                return 0;
                            */
                            } else {
                                giveLife(sender, target);
                                command.getSource().sendFeedback(new TranslatableText("commands.lastlife.givelife.success", target.getDisplayName()), true);
                                return 1;
                            }
                        })
                )
        );
    }

    private static void giveLife(PlayerEntity sender, PlayerEntity target) {
        Origin senderLives = simpleGetLives(sender);
        Origin targetLives = simpleGetLives(target);
        String senderLivesId = senderLives.getIdentifier().toString();
        String targetLivesId = targetLives.getIdentifier().toString();

        switch (senderLivesId) {
            case "lastlife:lifes/2" -> simpleSetLives(sender, "lastlife:lifes/1");
            case "lastlife:lifes/3" -> simpleSetLives(sender, "lastlife:lifes/2");
            case "lastlife:lifes/4" -> simpleSetLives(sender, "lastlife:lifes/3");
            case "lastlife:lifes/5" -> simpleSetLives(sender, "lastlife:lifes/4");
            case "lastlife:lifes/6" -> simpleSetLives(sender, "lastlife:lifes/5");
        }
        switch (targetLivesId) {
            case "lastlife:lifes/1" -> simpleSetLives(target, "lastlife:lifes/2");
            case "lastlife:lifes/2" -> simpleSetLives(target, "lastlife:lifes/3");
            case "lastlife:lifes/3" -> simpleSetLives(target, "lastlife:lifes/4");
            case "lastlife:lifes/4" -> simpleSetLives(target, "lastlife:lifes/5");
            case "lastlife:lifes/5" -> simpleSetLives(target, "lastlife:lifes/6");
        }
    }

    private static Origin simpleGetLives(PlayerEntity player) {
        OriginComponent component = ModComponents.ORIGIN.get(player);
        OriginLayer layer = OriginLayers.getLayer(new Identifier("lastlife", "lives"));

        return component.getOrigin(layer);
    }

    private static boolean simpleHasLife(PlayerEntity player, String originId) {
        String[] splitOrigin = originId.split(":");
        OriginLayer layer = OriginLayers.getLayer(new Identifier("lastlife", "lives"));
        Origin origin = OriginRegistry.get(new Identifier(splitOrigin[0], splitOrigin[1]));
        return hasOrigin(player, layer, origin);
    }

    private static boolean hasOrigin(PlayerEntity player, OriginLayer layer, Origin origin) {
        OriginComponent component = ModComponents.ORIGIN.get(player);
        return component.hasOrigin(layer) && component.getOrigin(layer).equals(origin);
    }

    private static void simpleSetLives(PlayerEntity player, String livesId) {
        String[] splitOrigin = livesId.split(":");
        Origin lives = OriginRegistry.get(new Identifier(splitOrigin[0], splitOrigin[1]));
        OriginLayer layer = OriginLayers.getLayer(new Identifier("lastlife", "lives"));
        OriginComponent component = ModComponents.ORIGIN.get(player);
        component.setOrigin(layer, lives);
        OriginComponent.sync(player);
        boolean hadOriginBefore = component.hadOriginBefore();
        OriginComponent.partialOnChosen(player, hadOriginBefore, lives);
    }
}
