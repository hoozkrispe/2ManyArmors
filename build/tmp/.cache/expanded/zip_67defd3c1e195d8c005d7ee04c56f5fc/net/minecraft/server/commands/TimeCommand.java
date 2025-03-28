package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class TimeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register(
            Commands.literal("time")
                .requires(p_139076_ -> p_139076_.hasPermission(2))
                .then(
                    Commands.literal("set")
                        .then(Commands.literal("day").executes(p_139101_ -> setTime(p_139101_.getSource(), 1000)))
                        .then(Commands.literal("noon").executes(p_139099_ -> setTime(p_139099_.getSource(), 6000)))
                        .then(Commands.literal("night").executes(p_139097_ -> setTime(p_139097_.getSource(), 13000)))
                        .then(Commands.literal("midnight").executes(p_139095_ -> setTime(p_139095_.getSource(), 18000)))
                        .then(
                            Commands.argument("time", TimeArgument.time())
                                .executes(p_139093_ -> setTime(p_139093_.getSource(), IntegerArgumentType.getInteger(p_139093_, "time")))
                        )
                )
                .then(
                    Commands.literal("add")
                        .then(
                            Commands.argument("time", TimeArgument.time())
                                .executes(p_139091_ -> addTime(p_139091_.getSource(), IntegerArgumentType.getInteger(p_139091_, "time")))
                        )
                )
                .then(
                    Commands.literal("query")
                        .then(Commands.literal("daytime").executes(p_139086_ -> queryTime(p_139086_.getSource(), getDayTime(p_139086_.getSource().getLevel()))))
                        .then(
                            Commands.literal("gametime")
                                .executes(p_326351_ -> queryTime(p_326351_.getSource(), (int)(p_326351_.getSource().getLevel().getGameTime() % 2147483647L)))
                        )
                        .then(
                            Commands.literal("day")
                                .executes(
                                    p_326352_ -> queryTime(p_326352_.getSource(), (int)(p_326352_.getSource().getLevel().getDayTime() / 24000L % 2147483647L))
                                )
                        )
                )
        );
    }

    private static int getDayTime(ServerLevel pLevel) {
        return (int)(pLevel.getDayTime() % 24000L);
    }

    private static int queryTime(CommandSourceStack pSource, int pTime) {
        pSource.sendSuccess(() -> Component.translatable("commands.time.query", pTime), false);
        return pTime;
    }

    public static int setTime(CommandSourceStack pSource, int pTime) {
        for (ServerLevel serverlevel : pSource.getServer().getAllLevels()) {
            serverlevel.setDayTime((long)pTime);
        }

        pSource.sendSuccess(() -> Component.translatable("commands.time.set", pTime), true);
        return getDayTime(pSource.getLevel());
    }

    public static int addTime(CommandSourceStack pSource, int pAmount) {
        for (ServerLevel serverlevel : pSource.getServer().getAllLevels()) {
            serverlevel.setDayTime(serverlevel.getDayTime() + (long)pAmount);
        }

        int i = getDayTime(pSource.getLevel());
        pSource.sendSuccess(() -> Component.translatable("commands.time.set", i), true);
        return i;
    }
}