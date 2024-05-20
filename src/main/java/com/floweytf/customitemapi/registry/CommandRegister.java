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
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.regex.Pattern;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CommandRegister {
    private static final Pattern ID_PARSE = Pattern.compile("([a-z0-9_.-]+:)?[a-z0-9_./-]+(\\[.*])?");
    private static final SimpleCommandExceptionType GIVECUSTOM_PARSE_FAIL = new SimpleCommandExceptionType(
        Component.literal("Failed to parse item identifier, expected <resource-location> or <resource-location>[<variant>]")
    );

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

    private static int doGive(CommandContext<CommandSourceStack> context, boolean hasCount, boolean hasTag) {
        try {
            CompoundTag tag = hasTag ? CompoundTagArgument.getCompoundTag(context, "tag") : new CompoundTag();
            int count = hasCount ? IntegerArgumentType.getInteger(context, "count") : 1;
            final var fullId = StringArgumentType.getString(context, "id");

            // parsing magic
            if (!ID_PARSE.matcher(fullId).matches()) {
                throw GIVECUSTOM_PARSE_FAIL.create();
            }

            final var idParts = fullId.split("\\[");
            ItemStack stack;

            if (idParts.length == 2) {
                stack = CustomItemAPIMain.makeItem(
                    ResourceLocation.tryParse(idParts[0]),
                    idParts[1].substring(0, idParts[1].length() - 1),
                    count,
                    Optional.of(tag)
                );
            } else {
                stack = CustomItemAPIMain.makeItem(
                    ResourceLocation.tryParse(idParts[0]),
                    count,
                    Optional.of(tag)
                );
            }

            Player player = EntityArgument.getPlayer(context, "player");

            player.addItem(stack);

            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(
                Component.literal("Failed to give custom item to player!")
                    .withStyle(s -> s.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(e.getMessage()))
                    ))
            );
            CustomItemAPIMain.LOGGER.error("Failed to give custom item", e);
            return -1;
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> giveCustomCommand() {
        return lit(
            "givecustom",
            arg("player", EntityArgument.player(),
                arg("id", StringArgumentType.string(),
                    c -> doGive(c, false, false),
                    arg("count", IntegerArgumentType.integer(1),
                        c -> doGive(c, true, false),
                        arg("tag", CompoundTagArgument.compoundTag(),
                            c -> doGive(c, true, true)
                        )
                    )
                ).suggests((context, builder) -> SharedSuggestionProvider.suggest(CustomItemRegistryImpl.getInstance().getGiveCompletion(), builder))
            )
        );
    }

    public static void register(CommandDispatcher<CommandSourceStack> sender) {
        sender.register(giveCustomCommand());
    }
}