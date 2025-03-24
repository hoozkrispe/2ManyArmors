package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class DownloadTask extends LongRunningTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("mco.download.preparing");
    private final long realmId;
    private final int slot;
    private final Screen lastScreen;
    private final String downloadName;

    public DownloadTask(long pWorldId, int pSlot, String pDownloadName, Screen pLastScreen) {
        this.realmId = pWorldId;
        this.slot = pSlot;
        this.lastScreen = pLastScreen;
        this.downloadName = pDownloadName;
    }

    @Override
    public void run() {
        RealmsClient realmsclient = RealmsClient.create();
        int i = 0;

        while (i < 25) {
            try {
                if (this.aborted()) {
                    return;
                }

                WorldDownload worlddownload = realmsclient.requestDownloadInfo(this.realmId, this.slot);
                pause(1L);
                if (this.aborted()) {
                    return;
                }

                setScreen(new RealmsDownloadLatestWorldScreen(this.lastScreen, worlddownload, this.downloadName, p_90325_ -> {
                }));
                return;
            } catch (RetryCallException retrycallexception) {
                if (this.aborted()) {
                    return;
                }

                pause((long)retrycallexception.delaySeconds);
                i++;
            } catch (RealmsServiceException realmsserviceexception) {
                if (this.aborted()) {
                    return;
                }

                LOGGER.error("Couldn't download world data", (Throwable)realmsserviceexception);
                setScreen(new RealmsGenericErrorScreen(realmsserviceexception, this.lastScreen));
                return;
            } catch (Exception exception) {
                if (this.aborted()) {
                    return;
                }

                LOGGER.error("Couldn't download world data", (Throwable)exception);
                this.error(exception);
                return;
            }
        }
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }
}