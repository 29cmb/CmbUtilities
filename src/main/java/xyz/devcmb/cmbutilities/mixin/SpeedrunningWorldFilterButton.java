package xyz.devcmb.cmbutilities.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.devcmb.cmbutilities.CmbUtilities;
import xyz.devcmb.cmbutilities.client.util.ClientOptionManager;
import xyz.devcmb.cmbutilities.client.util.ClientOptions;

@Mixin(SelectWorldScreen.class)
public abstract class SpeedrunningWorldFilterButton extends Screen {
    @Unique
    private TexturedButtonWidget buttonWidget;

    @Unique
    private final Identifier check = Identifier.of("cmbutilities", "textures/gui/check.png");
    @Unique
    private final Identifier x = Identifier.of("cmbutilities", "textures/gui/x.png");

    protected SpeedrunningWorldFilterButton(Text title) {
        super(title);
    }

    @Unique
    private void refreshWorldList() {
        assert this.client != null;
        this.init(this.client, this.width, this.height);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        if(buttonWidget != null) {
            this.remove(buttonWidget);
        }

        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        SelectWorldScreen screen = (SelectWorldScreen) (Object) this;

        boolean checkExists = resourceManager.getResource(check).isPresent();
        boolean xExists = resourceManager.getResource(x).isPresent();

        CmbUtilities.LOGGER.info("Check texture exists: " + checkExists);
        CmbUtilities.LOGGER.info("X texture exists: " + xExists);

        for (var widget : screen.children()) {
            if (widget instanceof TextFieldWidget textFieldWidget) {
                try {
                    ButtonTextures textures;

                    if((boolean) ClientOptionManager.getOption(ClientOptions.WORLD_FILTER_ENABLED)) {
                        textures = new ButtonTextures(check,check,check,check);
                    } else {
                        textures = new ButtonTextures(x, x, x, x);
                    }

                    buttonWidget = new TexturedButtonWidget(
                        textFieldWidget.getX() - (textFieldWidget.getHeight() + 10),
                        textFieldWidget.getY(),
                        textFieldWidget.getHeight(),
                        textFieldWidget.getHeight(),
                            textures,
                        button -> {
                            ClientOptionManager.setOption(
                                    ClientOptions.WORLD_FILTER_ENABLED,
                                    !(boolean) ClientOptionManager.getOption(ClientOptions.WORLD_FILTER_ENABLED)
                            );

                            ButtonTextures textures2;

                            if((boolean) ClientOptionManager.getOption(ClientOptions.WORLD_FILTER_ENABLED)) {
                                textures2 = new ButtonTextures(check, check, check, check);
                            } else {
                                textures2 = new ButtonTextures(x, x, x, x);
                            }

                            TexturedButtonWidget newButtonWidget = new TexturedButtonWidget(
                                buttonWidget.getX(),
                                buttonWidget.getY(),
                                buttonWidget.getWidth(),
                                buttonWidget.getHeight(),
                                textures2,
                                (b) -> buttonWidget.onPress(),
                                buttonWidget.getMessage()
                            );

                            this.remove(buttonWidget);
                            this.addDrawableChild(newButtonWidget);
                            buttonWidget = newButtonWidget;

                            refreshWorldList();
                        },
                        Text.of(Text.translatable("gui.cmbutilities.world_filter"))
                    );

                    this.addDrawableChild(buttonWidget);
                } catch (Exception e) {
                    CmbUtilities.LOGGER.warning("Failed to add button to screen: " + e.getMessage());
                }

                break;
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(buttonWidget == null) {
            return;
        }

        RenderSystem.setShaderTexture(0, x);
        RenderSystem.setShaderTexture(0, check);

        buttonWidget.render(context, mouseX, mouseY, delta);
        if (buttonWidget.isHovered()) {
            context.drawTooltip(textRenderer, Text.translatable("gui.cmbutilities.world_filter"), mouseX, mouseY);
        }
    }
}