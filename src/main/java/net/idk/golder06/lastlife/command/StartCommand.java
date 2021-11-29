package net.idk.golder06.lastlife.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.Scheduler;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
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
                                    return 1; // randomNumbersTitle(source, (Collection<ServerPlayerEntity>) source.getPlayer(), TitleS2CPacket::new);
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

    private static int executeTitle(ServerCommandSource source, Collection<ServerPlayerEntity> targets, String title, Function<Text, Packet<?>> constructor) throws CommandSyntaxException {
        Text newTitle = new LiteralText(title);

        for (ServerPlayerEntity serverPlayerEntity : targets) {
            serverPlayerEntity.networkHandler.sendPacket(constructor.apply(Texts.parse(source, newTitle, serverPlayerEntity, 0)));
        }

        return targets.size();
    }

    private static void chooseLife(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Function<Text, Packet<?>> constructor) throws CommandSyntaxException {

    }

    private static int randomNumbersTitle(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Function<Text, Packet<?>> constructor) throws CommandSyntaxException {
        int i = 1;
        while (i <= 73)
            i++;
            String title = "test";
            executeTitle(source, targets, title, constructor);
        chooseLife(source, targets, constructor);
        return 1;
    }
}
