package net.idk.golder06.lastlife.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;

public class StartCommand {
    // public static final Identifier COMMAND_POWER_SOURCE = Apoli.identifier("command");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("startsession")
                .executes((command) -> {
                    final ServerCommandSource source = command.getSource();
                    final PlayerEntity sender = source.getPlayer();
                    PowerType<?> power = PowerTypeRegistry.get(new Identifier("lastlife", "grant/startsession"));
                    grantPower(sender, power);
                    return 1;
                })
        );
    }

    private static void grantPower(LivingEntity entity, PowerType<?> power) {
        Identifier source = new Identifier("lastlife", "sessionstarting");
        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        boolean success = component.addPower(power, source);
        if (success) {
            component.sync();
        }
    }
}
