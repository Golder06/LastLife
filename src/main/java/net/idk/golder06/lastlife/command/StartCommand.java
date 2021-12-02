package net.idk.golder06.lastlife.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.CommandFunctionArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;

import java.util.Collection;
import java.util.Iterator;

import static net.minecraft.server.command.CommandManager.literal;

public class StartCommand {
    // public static final Identifier COMMAND_POWER_SOURCE = Apoli.identifier("command");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(literal("startsession").requires(cs -> cs.hasPermissionLevel(2))
                .executes((command) -> {
                            final ServerCommandSource source = command.getSource();
                            Collection<CommandFunction> function = CommandFunctionArgumentType.getFunctions(command, "lastlife:startsession");
                            return executeFunction(source, function);
                            // randomNumbersTitle(source, (Collection<ServerPlayerEntity>) source.getPlayer(), TitleS2CPacket::new);
                        }
                )
        );
    }

    /*
    private static int executeTitle(ServerCommandSource source, Collection<ServerPlayerEntity> targets, String title, Function<Text, Packet<?>> constructor) throws CommandSyntaxException {
        Text newTitle = new LiteralText(title);

        for (ServerPlayerEntity serverPlayerEntity : targets) {
            serverPlayerEntity.networkHandler.sendPacket(constructor.apply(Texts.parse(source, newTitle, serverPlayerEntity, 0)));
        }

        return targets.size();
    }
    */

    private static int executeFunction(ServerCommandSource serverCommandSource, Collection<CommandFunction> collection) {
        int i = 0;

        CommandFunction commandFunction;
        for (Iterator<CommandFunction> var3 = collection.iterator(); var3.hasNext(); i += serverCommandSource.getServer().getCommandFunctionManager().execute(commandFunction, serverCommandSource.withSilent().withMaxLevel(2))) {
            commandFunction = var3.next();
        }
        return i;
    }

    /*
    private static int scheduleCommand(ServerCommandSource source, Pair<Identifier, Either<CommandFunction, Tag<CommandFunction>>> function, int time, boolean replace) throws CommandSyntaxException {
        long l = source.getWorld().getTime() + (long)time;
        Identifier identifier = function.getFirst();
        Timer<MinecraftServer> timer = source.getServer().getSaveProperties().getMainWorldProperties().getScheduledEvents();
        ((Either)function.getSecond()).ifLeft((functionx) -> {
            String string = identifier.toString();
            if (replace) {
                timer.remove(string);
            }

            timer.setEvent(string, l, new FunctionTimerCallback(identifier));
            source.sendFeedback(new TranslatableText("commands.schedule.created.function", identifier, time, l), true);
        }).ifRight((tag) -> {
            String string = "#" + identifier;
            if (replace) {
                timer.remove(string);
            }

            timer.setEvent(string, l, new FunctionTagTimerCallback(identifier));
            source.sendFeedback(new TranslatableText("commands.schedule.created.tag", identifier, time, l), true);
        });
        return Math.floorMod(l, 2147483647);
    }
    */

}
