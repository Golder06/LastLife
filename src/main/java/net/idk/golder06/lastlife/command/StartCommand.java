package net.idk.golder06.lastlife.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.Scheduler;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StartCommand {
    public static final Identifier COMMAND_POWER_SOURCE = Apoli.identifier("command");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(literal("start").requires(cs -> cs.hasPermissionLevel(2))
                .then(literal("session")
                        .executes((command) -> {
                                    final ServerCommandSource source = command.getSource();
                                    Scheduler scheduler = new Scheduler();

                                    return 1;
                                }
                        )
                )
                .then(literal("game"))
                .executes((command) -> {
                            final ServerCommandSource source = command.getSource();
                            return 1;
                        }
                )
        );
    }

    private static int executeTitle(ServerCommandSource source, Collection<ServerPlayerEntity> targets, String title, String titleType, Function<Text, Packet<?>> constructor) throws CommandSyntaxException {
        Text newTitle = new LiteralText(title);

        for (ServerPlayerEntity serverPlayerEntity : targets) {
            serverPlayerEntity.networkHandler.sendPacket(constructor.apply(Texts.parse(source, newTitle, serverPlayerEntity, 0)));
        }

        return targets.size();
    }
}
