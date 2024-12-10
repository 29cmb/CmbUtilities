package xyz.devcmb.cmbutilities.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.devcmb.cmbutilities.client.util.ClientOptionManager;
import xyz.devcmb.cmbutilities.client.util.ClientOptions;

import java.util.Locale;

@Mixin(WorldListWidget.class)
abstract public class WorldFilter extends AlwaysSelectedEntryListWidget<WorldListWidget.Entry> {
    public WorldFilter(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @Inject(method = "shouldShow", at = @At("HEAD"), cancellable = true)
    private void shouldShow(String search, LevelSummary summary, CallbackInfoReturnable<Boolean> cir) {
        if((boolean) ClientOptionManager.getOption(ClientOptions.WORLD_FILTER_ENABLED)) {
            cir.setReturnValue((summary.getDisplayName().toLowerCase(Locale.ROOT).contains(search) || summary.getName().toLowerCase(Locale.ROOT).contains(search)) && !(summary.getName().contains("New World") || summary.getName().contains("Speedrun")));
        }
    }
}
