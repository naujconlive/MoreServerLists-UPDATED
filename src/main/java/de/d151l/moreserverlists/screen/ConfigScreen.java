package de.d151l.moreserverlists.screen;

import de.d151l.moreserverlists.MoreServerListsModClient;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private final Screen parent;

    public ConfigScreen(final Screen parent) {
        super(Component.nullToEmpty("MoreServerLists Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        final AtomicInteger startHeight = new AtomicInteger(60);
        final int center = this.width / 2;

        MoreServerListsModClient.getInstance().getServerListHandler().getServerLists().forEach((key, name) -> {
            final StringWidget textWidget = new StringWidget(center - 200, startHeight.get(), 200, 20, Component.nullToEmpty(key), this.font);

            final EditBox textFieldWidget = new EditBox(this.font, this.width / 2, startHeight.get(), 200, 20, Component.nullToEmpty(name));
            textFieldWidget.setMaxLength(24);
            textFieldWidget.setValue(name);

            if (!key.equals("servers")) {
                final Button removeButton = Button.builder(Component.nullToEmpty("Remove"), (buttonWidget) -> {
                    MoreServerListsModClient.getInstance().getServerListHandler().removeServerList(key);
                    Minecraft.getInstance().setScreen(new ConfigScreen(parent));
                }).width(50).pos(textFieldWidget.getX() + textFieldWidget.getWidth() + 5, textFieldWidget.getY()).build();
                this.addRenderableWidget(removeButton);
            }

            textFieldWidget.setResponder((string) -> {
                MoreServerListsModClient.getInstance().getServerListHandler().updateServerList(key, string);
            });

            this.addRenderableWidget(textWidget);
            this.addRenderableWidget(textFieldWidget);
            startHeight.set(startHeight.get() + 25);
        });


        final int addServerListHeight = startHeight.get() + 50;

        final EditBox addListKey = new EditBox(this.font, (this.width / 2) - 205, addServerListHeight, 200, 20, Component.nullToEmpty(""));
        addListKey.setMaxLength(8);
        addListKey.setHint(Component.nullToEmpty("Key"));

        final EditBox addListName = new EditBox(this.font, (this.width / 2), addListKey.getY(), 200, 20, Component.nullToEmpty(""));
        addListName.setMaxLength(24);
        addListName.setHint(Component.nullToEmpty("Name"));

        final Button addListButton = Button.builder(Component.nullToEmpty("Add"), (buttonWidget) -> {
            MoreServerListsModClient.getInstance().getServerListHandler().addServerList(addListKey.getValue(), addListName.getValue());
            Minecraft.getInstance().setScreen(new ConfigScreen(parent));
        }).width(50).pos(addListName.getX() + addListName.getWidth() + 5, addListName.getY()).build();

        this.addRenderableWidget(addListKey);
        this.addRenderableWidget(addListName);
        this.addRenderableWidget(addListButton);

        final Button doneButton = Button.builder(Component.nullToEmpty("Save & Done"), (buttonWidget) -> {
            MoreServerListsModClient.getInstance().getServerListHandler().saveConfig();
            Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(parent));
        }).width(160).pos(center - 80, this.height - 32).build();

        this.addRenderableWidget(doneButton);
    }
}
