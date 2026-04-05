package de.d151l.moreserverlists.mixin;

import de.d151l.moreserverlists.MoreServerListsModClient;
import de.d151l.moreserverlists.screen.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    private Screen parent;

    private final int arrowWidth = 20;
    private final int listNameWidth = 150;
    private final int spaceBetweenListNameAndArrow = 3;
    private final int spaceBetweenWindowAndBar = 5;

    protected MultiplayerScreenMixin(final Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo info) {

        final int screenWidth = this.width;
        final int center = screenWidth / 2;
        //final int leftCenter = center / 2;

        final int barWidth = this.arrowWidth + this.spaceBetweenListNameAndArrow + this.arrowWidth + this.spaceBetweenListNameAndArrow + this.listNameWidth;
        final int centerBar = barWidth / 2;

        //int start = leftCenter - centerBar;

        final Button arrowLeft = Button.builder(Component.nullToEmpty("«"), (buttonWidget) -> {
            MoreServerListsModClient.getInstance().getServerListHandler().previousServerList();

            Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(parent));
        }).width(this.arrowWidth).pos(this.spaceBetweenWindowAndBar, this.spaceBetweenWindowAndBar).build();

        final Button arrowRight = Button.builder(Component.nullToEmpty("»"), (buttonWidget) -> {
            MoreServerListsModClient.getInstance().getServerListHandler().nextServerList();

            Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(parent));
        }).width(this.arrowWidth).pos(arrowLeft.getX() + arrowLeft.getWidth() + this.spaceBetweenListNameAndArrow, arrowLeft.getY()).build();

        final String currentServerListName = MoreServerListsModClient.getInstance().getServerListHandler().getCurrentServerListName();
        final int serverListIndex = MoreServerListsModClient.getInstance().getServerListHandler().getServerListIndex();
        final int maximumServerListIndex = MoreServerListsModClient.getInstance().getServerListHandler().getMaximumServerListIndex();

        final Button listName = Button.builder(Component.nullToEmpty(currentServerListName + " (" + serverListIndex + "/" + maximumServerListIndex + ")"), (buttonWidget) -> {

            final ConfigScreen configScreen = new ConfigScreen(parent);
            Minecraft.getInstance().setScreen(configScreen);
        }).width(this.listNameWidth).pos(arrowRight.getX() + arrowRight.getWidth() + this.spaceBetweenListNameAndArrow, arrowRight.getY()).build();

        this.addRenderableWidget(arrowLeft);
        this.addRenderableWidget(arrowRight);
        this.addRenderableWidget(listName);
    }
}