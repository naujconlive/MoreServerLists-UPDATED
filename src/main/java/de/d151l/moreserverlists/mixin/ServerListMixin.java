package de.d151l.moreserverlists.mixin;

import de.d151l.moreserverlists.MoreServerListsModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerList.class)
public class ServerListMixin {

    public ServerListMixin(final Minecraft client) {

    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(stringValue = "servers.dat")
    )
    private String loadFileReplaceServerDatFileName(String variable) {
        return this.getServerListName();
    }

    @ModifyConstant(
            method = "save",
            constant = @Constant(stringValue = "servers.dat_old")
    )
    private String saveFileReplaceServerDatOldFileName(String variable) {
        return this.getServerListNameOld();
    }

    @ModifyConstant(
            method = "save",
            constant = @Constant(stringValue = "servers.dat")
    )
    private String saveFileReplaceServerDatFileName(String variable) {
        return this.getServerListName();
    }

    private String getServerListName() {
        final String string = MoreServerListsModClient.getInstance().getServerListHandler().getCurrentServerList() + ".dat";
        MoreServerListsModClient.LOGGER.info("Server list name: " + string);
        return string;
    }

    private String getServerListNameOld() {
        final String string = MoreServerListsModClient.getInstance().getServerListHandler().getCurrentServerList() + ".dat_old";
        MoreServerListsModClient.LOGGER.info("Server list name: " + string);
        return string;
    }
}