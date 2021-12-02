package net.idk.golder06.lastlife;

import net.fabricmc.api.ModInitializer;
import net.idk.golder06.lastlife.command.*;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class LastLife implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> TransferLifeCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> StartCommand.register(dispatcher));
    }
}