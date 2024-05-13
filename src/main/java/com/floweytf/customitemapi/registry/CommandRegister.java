package com.floweytf.customitemapi.registry;

import com.floweytf.customitemapi.helpers.tag.CompoundTagBuilder;
import com.floweytf.customitemapi.ModMain;
import com.floweytf.customitemapi.helpers.ItemStackStateManager;
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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;

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

    public static ItemStack makeItem(ResourceLocation id, Optional<String> variant, int count, Optional<CompoundTag> dataTag) {
        final var extraTag = dataTag.orElseGet(CompoundTag::new);
        final var item = CustomItemRegistryImpl.getInstance().get(CraftNamespacedKey.fromMinecraft(id));
        if (item == null) {
            throw new IllegalArgumentException("unknown item id");
        }

        if(variant.isPresent() && !item.variants().contains(variant.get())) {
            throw new IllegalArgumentException("illegal variant state");
        }

        CompoundTag itemTag = CompoundTagBuilder.of()
            .put("id", BuiltInRegistries.ITEM.getKey(CraftMagicNumbers.getItem(item.baseItem())).toString())
            .put("Count", (byte) count)
            .put("tag", CompoundTagBuilder.of(
                ItemStackStateManager.ROOT_TAG_KEY, CompoundTagBuilder.of()
                    .put(ItemStackStateManager.KEY_BUILTIN_ID, variant.map(u -> item.key() + "@" + u).orElse(item.key().toString()))
                    .put(ItemStackStateManager.KEY_SAVE_DATA, extraTag)
                    .get()
            )).get();

        return ItemStack.of(itemTag);
    }

    private static int doGive(CommandContext<CommandSourceStack> context, boolean hasCount, boolean hasTag, boolean hasVariant) throws CommandSyntaxException {
        try {
            CompoundTag tag = hasTag ? CompoundTagArgument.getCompoundTag(context, "tag") : new CompoundTag();
            int count = hasCount ? IntegerArgumentType.getInteger(context, "count") : 1;
            ResourceLocation id = ResourceLocationArgument.getId(context, "id");
            Player player = EntityArgument.getPlayer(context, "player");

            final var stack = makeItem(id, hasVariant ? Optional.of(StringArgumentType.getString(context, "variant")) : Optional.empty(), count, Optional.of(tag));

            player.addItem(stack);

            return 1;
        } catch (Throwable e) {
            context.getSource().sendFailure(Component.literal("Failed to give custom item to player!").withStyle(s -> s.withHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(e.getMessage()))
            )));
            ModMain.LOGGER.info(e);
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
                        arg("variant", StringArgumentType.string(),
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