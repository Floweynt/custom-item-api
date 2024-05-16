package com.floweytf.customitemapi.registry;

import com.floweytf.customitemapi.CustomItemAPIMain;
import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CommandRegister {
    @SafeVarargs
    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> arg(
        String key,
        ArgumentType<T> arg,
        ArgumentBuilder<CommandSourceStack, ?>... callbacks
    ) {
        final var inst = argument(key, arg);
        for (var callback : callbacks) {
            inst.then(callback);
        }
        return inst;
    }

    @SafeVarargs
    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> arg(
        String key,
        ArgumentType<T> arg,
        Command<CommandSourceStack> execute,
        ArgumentBuilder<CommandSourceStack, ?>... callbacks
    ) {
        final var inst = argument(key, arg);
        for (var callback : callbacks) {
            inst.then(callback);
        }
        inst.executes(execute);
        return inst;
    }

    @SafeVarargs
    public static LiteralArgumentBuilder<CommandSourceStack> lit(
        String key,
        ArgumentBuilder<CommandSourceStack, ?>... callbacks
    ) {
        final var inst = literal(key);
        for (var callback : callbacks) {
            inst.then(callback);
        }
        return inst;
    }

    @SafeVarargs
    public static LiteralArgumentBuilder<CommandSourceStack> lit(
        String key,
        Command<CommandSourceStack> execute,
        ArgumentBuilder<CommandSourceStack, ?>... callbacks
    ) {
        final var inst = literal(key);
        for (var callback : callbacks) {
            inst.then(callback);
        }
        inst.executes(execute);
        return inst;
    }

    private static int doGive(CommandContext<CommandSourceStack> context, boolean hasCount, boolean hasTag,
                              boolean hasVariant) {
        try {
            CompoundTag tag = hasTag ? CompoundTagArgument.getCompoundTag(context, "tag") : new CompoundTag();
            int count = hasCount ? IntegerArgumentType.getInteger(context, "count") : 1;
            ResourceLocation id = ResourceLocationArgument.getId(context, "id");
            Player player = EntityArgument.getPlayer(context, "player");

            final var stack = CustomItemAPIMain.makeItem(
                id,
                count,
                Optional.of(tag)
            );

            player.addItem(stack);

            return 1;
        } catch (Throwable e) {
            context.getSource().sendFailure(
                Component.literal("Failed to give custom item to player!")
                    .withStyle(s -> s.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(e.getMessage()))
                    ))
            );
            CustomItemAPIMain.LOGGER.info(e);
            return -1;
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> giveCustomCommand() {
        return lit(
            "givecustom",
            arg("player", EntityArgument.player(),
                arg("id", ResourceLocationArgument.id(),
                    c -> doGive(c, false, false, false),
                    arg("count", IntegerArgumentType.integer(1),
                        c -> doGive(c, true, false, false),
                        arg("tag", CompoundTagArgument.compoundTag(),
                            c -> doGive(c, true, true, false)
                        ),
                        arg("variantId", StringArgumentType.string(),
                            arg("tag", CompoundTagArgument.compoundTag(),
                                c -> doGive(c, true, true, true)
                            )
                        )
                    )
                ).suggests((context, builder) -> SharedSuggestionProvider.suggestResource(CustomItemRegistryImpl.getInstance().minecraftKeys(), builder))
            )
        );
    }

    public static void register(CommandDispatcher<CommandSourceStack> sender) {
        sender.register(giveCustomCommand());
    }
}